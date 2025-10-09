package com.aa.game_controllers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

public class ABButtonsView extends SurfaceView implements View.OnTouchListener {

    private Paint paintA, paintB;
    private float centerAX, centerAY, radiusA;
    private float centerBX, centerBY, radiusB;
    private ABListener listener;

    public interface ABListener {
        void onAButtonPressed();

        void onBButtonPressed();
    }

    public ABButtonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        if (context instanceof ABListener) {
            listener = (ABListener) context;
        }
        paintA = new Paint();
        paintA.setColor(Color.GREEN);
        paintB = new Paint();
        paintB.setColor(Color.RED);
        
        
        
    }

    @Override
    public boolean onTouch(View view, MotionEvent mEvent) {
        if(mEvent.getAction() == MotionEvent.ACTION_DOWN) {
        	float x = mEvent.getX();
            float y = mEvent.getY();
            
            float distA = (float) Math.sqrt(Math.pow(x - centerAX, 2) + Math.pow(y - centerAY, 2));
            float distB = (float) Math.sqrt(Math.pow(x - centerBX, 2) + Math.pow(y - centerBY, 2));
            
            if(distA <= radiusA && listener != null) {
            	listener.onAButtonPressed();
                return true;
            }
            if(distB <= radiusB && listener != null) {
            	listener.onBButtonPressed();
                return true;
            }
            drawButtons();
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        radiusA = radiusB = Math.min(w, h) /6f;
        
        centerAX = w - radiusA * 2.5f;
        centerAY = h - radiusA * 3f;
        
        centerBX = w - radiusB * 2.5f;
        centerBY = h - radiusB * 1.2f;
        
        drawButtons();
    }

    private void drawButtons() {
    	if(getHolder().getSurface().isValid()) {
    		Canvas canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            
            canvas.drawCircle(centerAX, centerAY, radiusA, paintA);
            canvas.drawCircle(centerBX, centerBY, radiusB, paintB);
            getHolder().unlockCanvasAndPost(canvas);
    	}
    }
}
