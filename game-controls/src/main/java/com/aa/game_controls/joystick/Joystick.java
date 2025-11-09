package com.aa.game_controls.joystick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aa.game_controls.base.GameControlBase;

public class Joystick extends GameControlBase {

    /* ---------- Interfaz completa ---------- */
    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int id);

        /* Nuevos eventos (default para no romper implementaciones anteriores) */
        default void onJoystickDown  (float xPercent, float yPercent, int id) { }
        default void onJoystickUp    (float xPercent, float yPercent, int id) { }
        default void onJoystickReturn(int id) { }
        default void onJoystickDoubleTap(float xPercent, float yPercent, int id) { }
        default void onJoystickLongPress(float xPercent, float yPercent, int id) { }
    }

    /* ---------- Variables visuales ---------- */
    private float centerX, centerY, baseRadius, hatRadius;
    private float joystickX, joystickY;

    private Paint basePaint, hatPaint;
    private JoystickConfig joystickConfig;
    private JoystickListener joystickCallback;

    /* ---------- Variables para gestos ---------- */
    private long downTime, lastTapTime;
    private int  tapCount;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable longPressRunnable;

    private static final int DOUBLE_TAP_WINDOW = 300; // ms
    private static final int LONG_PRESS_TIME   = 500; // ms

    /* ---------- Constructores ---------- */
    public Joystick(Context context) {
        super(context);
        initJoystick(context);
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        initJoystick(context);
    }

    /* ---------- Inicialización ---------- */
    private void initJoystick(Context context) {
        joystickConfig = new JoystickConfig();

        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }

        basePaint = new Paint();
        basePaint.setColor(joystickConfig.getBaseColor());
        basePaint.setAlpha(100);
        basePaint.setAntiAlias(true);

        hatPaint = new Paint();
        hatPaint.setColor(joystickConfig.getHatColor());
        hatPaint.setAlpha(200);
        hatPaint.setAntiAlias(true);

        setBackgroundTransparent(true);
    }

    /* ---------- Dimensionado ---------- */
    @Override
    protected void setupControlDimensions() {
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        joystickX = centerX;
        joystickY = centerY;

        float minDim = Math.min(getWidth(), getHeight());
        baseRadius = minDim * joystickConfig.getBaseRatio();
        hatRadius  = minDim * joystickConfig.getHatRatio();

        if (hatRadius >= baseRadius) hatRadius = baseRadius * 0.8f;
    }

    /* ---------- Dibujo ---------- */
    @Override
    protected void drawControl(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, baseRadius, basePaint);
        canvas.drawCircle(joystickX, joystickY, hatRadius,  hatPaint);
    }

    /* ---------- Touch completo ---------- */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!config.isEnabled()) return false;

        float x = event.getX();
        float y = event.getY();
        float px = (x - centerX) / baseRadius;
        float py = (y - centerY) / baseRadius;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = SystemClock.elapsedRealtime();

                /* Doble-tap */
                if (SystemClock.elapsedRealtime() - lastTapTime < DOUBLE_TAP_WINDOW) {
                    tapCount++;
                    if (tapCount == 2 && joystickCallback != null)
                        joystickCallback.onJoystickDoubleTap(px, py, getId());
                } else {
                    tapCount = 1;
                }
                lastTapTime = SystemClock.elapsedRealtime();

                /* Long-press */
                longPressRunnable = () -> {
                    if (joystickCallback != null)
                        joystickCallback.onJoystickLongPress(px, py, getId());
                };
                handler.postDelayed(longPressRunnable, LONG_PRESS_TIME);

                /* Notificar down */
                if (joystickCallback != null)
                    joystickCallback.onJoystickDown(px, py, getId());
                return true;

            case MotionEvent.ACTION_MOVE:
                float dx = x - centerX;
                float dy = y - centerY;
                float displacement = (float) Math.sqrt(dx * dx + dy * dy);
            
                float hatLimit = baseRadius -hatRadius;
                
                if (displacement <= hatLimit) {
                    joystickX = x;
                    joystickY = y;
                } else {
                    float ratio = hatLimit / displacement;
                    joystickX = centerX + dx * ratio;
                    joystickY = centerY + dy * ratio;
                }

                if (joystickCallback != null) {
                    float xPct = (joystickX - centerX) / baseRadius;
                    float yPct = (joystickY - centerY) / baseRadius;
                    joystickCallback.onJoystickMoved(xPct, yPct, getId());
                }
                break;

            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(longPressRunnable);

                if (joystickCallback != null)
                    joystickCallback.onJoystickUp(px, py, getId());

                /* Volver al centro */
                if (joystickConfig.isReturnToCenter()) {
                    joystickX = centerX;
                    joystickY = centerY;
                    if (joystickCallback != null)
                        joystickCallback.onJoystickReturn(getId());
                }
                break;
        }

        requestRedraw();
        return true;
    }

    /* ---------- API pública ---------- */
    public void setJoystickListener(JoystickListener l) {
        this.joystickCallback = l;
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