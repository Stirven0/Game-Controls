package com.aa.game_controls;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class GameButtonsView extends SurfaceView implements View.OnTouchListener {

    public interface ButtonsListener {
        void onButtonPressed(int buttonId);
        void onButtonReleased(int buttonId);
    }

    private class ButtonConfig {
        int id;
        float centerX, centerY;
        float normalRadius;
        float pressedRadius;
        int color;
        Drawable icon;
        String text;
        boolean isPressed = false;
        float currentRadius;

        ButtonConfig(int id, float normalRadius, float pressedRadius, int color, Drawable icon, String text) {
            this.id = id;
            this.normalRadius = normalRadius;
            this.pressedRadius = pressedRadius;
            this.currentRadius = normalRadius;
            this.color = color;
            this.icon = icon;
            this.text = text;
        }

        void setPosition(float centerX, float centerY) {
            this.centerX = centerX;
            this.centerY = centerY;
        }
    }

    private List<ButtonConfig> buttons;
    private ButtonsListener listener;
    private Paint paint;
    private Paint textPaint;
    private SparseArray<ValueAnimator> animators;

    // Constructores
    public GameButtonsView(Context context) {
        super(context);
        init(context, null);
    }

    public GameButtonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GameButtonsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOnTouchListener(this);
        if (context instanceof ButtonsListener) {
            listener = (ButtonsListener) context;
        }

        buttons = new ArrayList<>();
        animators = new SparseArray<>();
        
        paint = new Paint();
        paint.setAntiAlias(true);
        
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        // Configuración inicial desde atributos XML si existen
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GameButtonsView);
            // Puedes agregar atributos personalizados aquí si los necesitas
            a.recycle();
        }

        setZOrderOnTop(true);
        getHolder().setFormat(android.graphics.PixelFormat.TRANSLUCENT);
    }

    // Método para agregar botones programáticamente
    public void addButton(int buttonId, float normalRadius, float pressedRadius, int color, Drawable icon, String text) {
        ButtonConfig button = new ButtonConfig(buttonId, normalRadius, pressedRadius, color, icon, text);
        buttons.add(button);
        post(this::calculateButtonPositions);
    }

    // Método para agregar botón simple (sin icono)
    public void addButton(int buttonId, float normalRadius, float pressedRadius, int color, String text) {
        addButton(buttonId, normalRadius, pressedRadius, color, null, text);
    }

    public void setButtonsListener(ButtonsListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        boolean handled = false;

        for (ButtonConfig button : buttons) {
            float distance = (float) Math.sqrt(Math.pow(x - button.centerX, 2) + Math.pow(y - button.centerY, 2));
            
            if (distance <= button.currentRadius) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (!button.isPressed) {
                            button.isPressed = true;
                            startButtonAnimation(button, button.pressedRadius);
                            if (listener != null) {
                                listener.onButtonPressed(button.id);
                            }
                        }
                        handled = true;
                        break;
                        
                    case MotionEvent.ACTION_UP:
                        if (button.isPressed) {
                            button.isPressed = false;
                            startButtonAnimation(button, button.normalRadius);
                            if (listener != null) {
                                listener.onButtonReleased(button.id);
                            }
                        }
                        handled = true;
                        break;
                }
            } else {
                if (button.isPressed && (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)) {
                    button.isPressed = false;
                    startButtonAnimation(button, button.normalRadius);
                    if (listener != null) {
                        listener.onButtonReleased(button.id);
                    }
                }
            }
        }

        if (handled) {
            drawButtons();
        }
        
        return handled;
    }

    private void startButtonAnimation(ButtonConfig button, float targetRadius) {
        ValueAnimator animator = animators.get(button.id);
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        animator = ValueAnimator.ofFloat(button.currentRadius, targetRadius);
        animator.setDuration(150);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            button.currentRadius = (float) animation.getAnimatedValue();
            drawButtons();
        });
        animator.start();
        animators.put(button.id, animator);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateButtonPositions();
    }

    private void calculateButtonPositions() {
        if (buttons.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        
        // Calcular tamaño de texto basado en el tamaño de los botones
        float maxRadius = 0;
        for (ButtonConfig button : buttons) {
            if (button.normalRadius > maxRadius) {
                maxRadius = button.normalRadius;
            }
        }
        textPaint.setTextSize(maxRadius * 0.6f);

        // Distribuir botones en una cuadrícula flexible
        int totalButtons = buttons.size();
        int columns = (int) Math.ceil(Math.sqrt(totalButtons));
        int rows = (int) Math.ceil((float) totalButtons / columns);
        
        float cellWidth = width / (float) columns;
        float cellHeight = height / (float) rows;

        for (int i = 0; i < totalButtons; i++) {
            ButtonConfig button = buttons.get(i);
            int row = i / columns;
            int col = i % columns;
            
            float centerX = (col * cellWidth) + (cellWidth / 2);
            float centerY = (row * cellHeight) + (cellHeight / 2);
            
            button.setPosition(centerX, centerY);
        }

        drawButtons();
    }

    private void drawButtons() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            for (ButtonConfig button : buttons) {
                // Dibujar círculo del botón
                paint.setColor(button.color);
                paint.setAlpha(button.isPressed ? 200 : 150); // Cambiar alpha cuando está presionado
                canvas.drawCircle(button.centerX, button.centerY, button.currentRadius, paint);

                // Dibujar borde
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                canvas.drawCircle(button.centerX, button.centerY, button.currentRadius, paint);
                paint.setStyle(Paint.Style.FILL);

                // Dibujar icono o texto
                if (button.icon != null) {
                    drawIcon(canvas, button);
                } else if (button.text != null && !button.text.isEmpty()) {
                    drawText(canvas, button);
                }
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void drawIcon(Canvas canvas, ButtonConfig button) {
        if (button.icon != null) {
            int left = (int) (button.centerX - button.currentRadius * 0.6f);
            int top = (int) (button.centerY - button.currentRadius * 0.6f);
            int right = (int) (button.centerX + button.currentRadius * 0.6f);
            int bottom = (int) (button.centerY + button.currentRadius * 0.6f);
            button.icon.setBounds(left, top, right, bottom);
            button.icon.draw(canvas);
        }
    }

    private void drawText(Canvas canvas, ButtonConfig button) {
    if (button.text != null && !button.text.isEmpty()) {
        // Asegurarse de que el texto esté centrado correctamente
        textPaint.setTextAlign(Paint.Align.CENTER);
        
        // Método 1: Más preciso
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textY = button.centerY - (fm.ascent + fm.descent) / 2;
        
        // Método 2: Alternativo (también funciona bien)
        // float textY = button.centerY + (Math.abs(fm.ascent) - fm.descent) / 2;
        
        canvas.drawText(button.text, button.centerX, textY, textPaint);
        
        // DEBUG: Descomenta estas líneas para ver las guías de centrado
        /*
        Paint debugPaint = new Paint();
        debugPaint.setColor(Color.RED);
        debugPaint.setStrokeWidth(2);
        // Línea horizontal central
        canvas.drawLine(button.centerX - button.currentRadius, button.centerY, 
                       button.centerX + button.currentRadius, button.centerY, debugPaint);
        // Línea vertical central  
        canvas.drawLine(button.centerX, button.centerY - button.currentRadius,
                       button.centerX, button.centerY + button.currentRadius, debugPaint);
        */
    }
}

    // Métodos utilitarios para cargar desde recursos
    public void addButtonFromResources(int buttonId, float normalRadius, float pressedRadius, int colorRes, int iconRes, String text) {
        Drawable icon = iconRes != 0 ? getResources().getDrawable(iconRes, null) : null;
        int color = getResources().getColor(colorRes, null);
        addButton(buttonId, normalRadius, pressedRadius, color, icon, text);
    }

    // Limpiar todos los botones
    public void clearButtons() {
        buttons.clear();
        for (int i = 0; i < animators.size(); i++) {
            ValueAnimator animator = animators.valueAt(i);
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
        }
        animators.clear();
        drawButtons();
    }

    // Obtener información de un botón específico
    public boolean isButtonPressed(int buttonId) {
        for (ButtonConfig button : buttons) {
            if (button.id == buttonId) {
                return button.isPressed;
            }
        }
        return false;
    }
}