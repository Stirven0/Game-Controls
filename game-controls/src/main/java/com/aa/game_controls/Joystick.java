package com.aa.game_controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

public class Joystick extends SurfaceView implements View.OnTouchListener {
    private float centerX, centerY, baseRadius, hatRadius;
    private float joystickX, joystickY;
    private JoystickListener joystickCallback;

    // Ratios configurables para base y hat
    private float baseRatio = 0.5f;  // Por defecto: base ocupa 50% del tamaño mínimo
    private float hatRatio = 0.2f;   // Por defecto: hat ocupa 20% del tamaño mínimo
    private float maxHatRatio = 0.4f; // Límite máximo para el hat (40% del tamaño mínimo)

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }

    private void setupDimensions() {
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        joystickX = centerX;
        joystickY = centerY;

        // Calcular radios basados en los ratios configurados
        float minDimension = Math.min(getWidth(), getHeight());
        baseRadius = minDimension * baseRatio;
        hatRadius = minDimension * hatRatio;

        // Asegurar que el hat no sea más grande que la base
        if (hatRadius >= baseRadius) {
            hatRadius = baseRadius * 0.8f; // 80% del tamaño de la base como máximo
        }
    }

    private void drawJoystick(float newX, float newY) {
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            Paint paint = new Paint();

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            // Dibujar base del joystick
            paint.setColor(Color.GRAY);
            paint.setAlpha(100); // Hacerlo semi-transparente
            canvas.drawCircle(centerX, centerY, baseRadius, paint);

            // Dibujar "hat" del joystick
            paint.setColor(Color.BLACK);
            paint.setAlpha(200);
            canvas.drawCircle(newX, newY, hatRadius, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    // Constructores para instanciación desde XML y programática
    public Joystick(Context context) {
        super(context);
        init(context);
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOnTouchListener(this);

        // Configurar el callback
        if(context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }

        setZOrderMediaOverlay(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setFocusable(true);
        setFocusableInTouchMode(true);

        post(() -> {
            setupDimensions();
            drawJoystick(centerX, centerY);
        });
    }

    // MÉTODOS PARA CONFIGURAR TAMAÑOS ===============================

    /**
     * Establece el tamaño de la base como porcentaje del tamaño mínimo del joystick
     * @param ratio Valor entre 0.1 y 1.0 (10% a 100%)
     */
    public void setBaseSizeRatio(float ratio) {
        this.baseRatio = Math.max(0.1f, Math.min(1.0f, ratio));
        updateDimensions();
    }

    /**
     * Establece el tamaño del hat como porcentaje del tamaño mínimo del joystick
     * @param ratio Valor entre 0.1 y 0.4 (10% a 40%)
     */
    public void setHatSizeRatio(float ratio) {
        this.hatRatio = Math.max(0.1f, Math.min(maxHatRatio, ratio));
        updateDimensions();
    }

    /**
     * Establece ambos tamaños a la vez
     * @param baseRatio Tamaño de la base (0.1 a 1.0)
     * @param hatRatio Tamaño del hat (0.1 a 0.4)
     */
    public void setJoystickSizeRatios(float baseRatio, float hatRatio) {
        this.baseRatio = Math.max(0.1f, Math.min(1.0f, baseRatio));
        this.hatRatio = Math.max(0.1f, Math.min(maxHatRatio, hatRatio));
        updateDimensions();
    }

    /**
     * Establece el tamaño del hat en relación al tamaño de la base
     * @param ratio Valor entre 0.1 y 0.8 (10% a 80% del tamaño de la base)
     */
    public void setHatToBaseRatio(float ratio) {
        float hatToBaseRatio = Math.max(0.1f, Math.min(0.8f, ratio));
        this.hatRatio = this.baseRatio * hatToBaseRatio;
        updateDimensions();
    }

    /**
     * Configuración rápida: Joystick pequeño
     */
    public void setSmallJoystick() {
        setJoystickSizeRatios(0.4f, 0.15f);
    }

    /**
     * Configuración rápida: Joystick mediano (por defecto)
     */
    public void setMediumJoystick() {
        setJoystickSizeRatios(0.5f, 0.2f);
    }

    /**
     * Configuración rápida: Joystick grande
     */
    public void setLargeJoystick() {
        setJoystickSizeRatios(0.7f, 0.25f);
    }

    /**
     * Configuración rápida: Joystick con hat grande
     */
    public void setLargeHatJoystick() {
        setJoystickSizeRatios(0.5f, 0.3f);
    }

    // Método para actualizar dimensiones y redibujar
    private void updateDimensions() {
        if (getWidth() > 0 && getHeight() > 0) {
            setupDimensions();
            drawJoystick(joystickX, joystickY);
        }
    }

    // Getters para obtener los ratios actuales
    public float getBaseRatio() {
        return baseRatio;
    }

    public float getHatRatio() {
        return hatRatio;
    }

    public float getCurrentBaseRadius() {
        return baseRadius;
    }

    public float getCurrentHatRadius() {
        return hatRadius;
    }

    // Método para establecer el callback programáticamente
    public void setJoystickListener(JoystickListener listener) {
        this.joystickCallback = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent mEvent) {
        if(view.equals(this)) {
            switch(mEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    float displacement = (float) Math.sqrt(
                        Math.pow(mEvent.getX() - centerX, 2) + 
                        Math.pow(mEvent.getY() - centerY, 2));

                    if(displacement < baseRadius) {
                        joystickX = mEvent.getX();
                        joystickY = mEvent.getY();
                    } else {
                        float ratio = baseRadius / displacement;
                        joystickX = centerX + (mEvent.getX() - centerX) * ratio;
                        joystickY = centerY + (mEvent.getY() - centerY) * ratio;
                    }

                    if(joystickCallback != null) {
                        joystickCallback.onJoystickMoved(
                            (joystickX - centerX) / baseRadius,
                            (joystickY - centerY) / baseRadius,
                            getId()
                        );
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    // Retornar al centro suavemente
                    joystickX = centerX;
                    joystickY = centerY;
                    if(joystickCallback != null) {
                        joystickCallback.onJoystickMoved(0, 0, getId());
                    }
                    break;
            }

            drawJoystick(joystickX, joystickY);
            return true;
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupDimensions();
        drawJoystick(centerX, centerY);
    }
}