package com.aa.game_controls.joystick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aa.game_controls.base.GameControlBase;
import com.aa.game_controls.base.ControlConfig;

public class Joystick extends GameControlBase {
    
    private float centerX, centerY, baseRadius, hatRadius;
    private float joystickX, joystickY;
    private JoystickListener joystickCallback;
    private Paint basePaint, hatPaint;
    private JoystickConfig joystickConfig;

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }

    public Joystick(Context context) {
        super(context);
        initJoystick(context);
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        initJoystick(context);
    }

    private void initJoystick(Context context) {
        joystickConfig = new JoystickConfig();
        
        if(context instanceof JoystickListener) {
        	joystickCallback = (JoystickListener)context;
        }
        // Paints específicos del joystick
        basePaint = new Paint();
        basePaint.setColor(joystickConfig.getBaseColor());
        basePaint.setAlpha(100);
        basePaint.setAntiAlias(true);
        
        hatPaint = new Paint();
        hatPaint.setColor(joystickConfig.getHatColor());
        hatPaint.setAlpha(200);
        hatPaint.setAntiAlias(true);
        
        // Configurar como completamente transparente por defecto
        setBackgroundTransparent(true);
    }

    @Override
    protected void setupControlDimensions() {
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        joystickX = centerX;
        joystickY = centerY;
        
        float minDimension = Math.min(getWidth(), getHeight());
        baseRadius = minDimension * joystickConfig.getBaseRatio();
        hatRadius = minDimension * joystickConfig.getHatRatio();
        
        // Asegurar que el hat no sea más grande que la base
        if (hatRadius >= baseRadius) {
            hatRadius = baseRadius * 0.8f;
        }
    }

    @Override
    protected void drawControl(Canvas canvas) {
        // SOLO DIBUJAR LOS ELEMENTOS DEL JOYSTICK
        // El fondo ya fue manejado por clearCanvas() en la clase base
        
        // Dibujar base del joystick
        canvas.drawCircle(centerX, centerY, baseRadius, basePaint);
        
        // Dibujar "hat" del joystick
        canvas.drawCircle(joystickX, joystickY, hatRadius, hatPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!config.isEnabled()) return false;
        
        float x = event.getX();
        float y = event.getY();
        
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float displacement = (float) Math.sqrt(
                    Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                
                if(displacement < baseRadius) {
                    joystickX = x;
                    joystickY = y;
                } else {
                    float ratio = baseRadius / displacement;
                    joystickX = centerX + (x - centerX) * ratio;
                    joystickY = centerY + (y - centerY) * ratio;
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
                joystickX = centerX;
                joystickY = centerY;
                if(joystickCallback != null) {
                    joystickCallback.onJoystickMoved(0, 0, getId());
                }
                break;
        }
        
        requestRedraw();
        return true;
    }

    // === MÉTODOS ESPECÍFICOS DEL JOYSTICK ===
    
    public void setJoystickListener(JoystickListener listener) {
        this.joystickCallback = listener;
    }
    
    public void setBaseColor(int color) {
        basePaint.setColor(color);
        requestRedraw();
    }
    
    public void setHatColor(int color) {
        hatPaint.setColor(color);
        requestRedraw();
    }
    
    public void setBaseSizeRatio(float ratio) {
        joystickConfig.setBaseRatio(ratio);
        setupControlDimensions();
        requestRedraw();
    }
    
    public void setHatSizeRatio(float ratio) {
        joystickConfig.setHatRatio(ratio);
        setupControlDimensions();
        requestRedraw();
    }
}