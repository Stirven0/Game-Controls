package com.aa.game_controls.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class GameControlBase extends SurfaceView 
    implements SurfaceHolder.Callback {
    
    protected SurfaceHolder surfaceHolder;
    protected ControlConfig config;
    protected Paint backgroundPaint;
    
    public GameControlBase(Context context) {
        super(context);
        initBase(context, null);
    }

    public GameControlBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBase(context, attrs);
    }

    public GameControlBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBase(context, attrs);
    }

    private void initBase(Context context, AttributeSet attrs) {
        // Configuración base de SurfaceView
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        
        // Configuración por defecto
        config = new ControlConfig();
        
        // Paint para el fondo
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.TRANSPARENT);
        
        // Configuración común de SurfaceView
        setZOrderOnTop(true);
        //setZOrderMediaOverlay(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    // === MÉTODOS ABSTRACTOS QUE DEBEN IMPLEMENTAR LAS SUBCLASES ===
    
    protected abstract void setupControlDimensions();
    protected abstract void drawControl(Canvas canvas);
    
    // === MÉTODOS COMUNES PARA TODOS LOS CONTROLES ===
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupControlDimensions();
        drawToSurface();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setupControlDimensions();
        drawToSurface();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Cleanup si es necesario
    }

    protected void drawToSurface() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    synchronized (surfaceHolder) {
                        // 1. Limpiar solo el área necesaria
                        clearCanvas(canvas);
                        // 2. Dibujar control específico
                        drawControl(canvas);
                    }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    protected void clearCanvas(Canvas canvas) {
        // LIMPIA SOLO EL FONDO, NO TODO EL CANVAS
        if (config.isBackgroundTransparent()) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        } else {
            canvas.drawColor(config.getBackgroundColor());
        }
    }

    public void requestRedraw() {
        drawToSurface();
    }

    // === CONFIGURACIÓN COMÚN ===
    
    public void setBackgroundColor(int color) {
        config.setBackgroundColor(color);
        backgroundPaint.setColor(color);
        requestRedraw();
    }

    public void setBackgroundTransparent(boolean transparent) {
        config.setBackgroundTransparent(transparent);
        requestRedraw();
    }

    public void setControlAlpha(int alpha) {
        config.setAlpha(alpha);
        requestRedraw();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupControlDimensions();
        requestRedraw();
    }
}