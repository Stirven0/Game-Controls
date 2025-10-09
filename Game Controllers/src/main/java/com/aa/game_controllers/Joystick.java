package com.aa.game_controllers;

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
    private JoystickListener joystickCallback;
    
    public interface JoystickListener{
        void onJoystickMoved(float xPorcent, float yPorcent, int id);
    }
    
    private void setupDimensions() {
    	centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;
        baseRadius = Math.min(getWidth(), getHeight()) / 3f;
        hatRadius = Math.min(getWidth(), getHeight()) / 3f;
        
    }
    
    private void drawJoystick(float newX, float newY) {
    	if(getHolder().getSurface().isValid()) {
    		Canvas canvas = getHolder().lockCanvas();
            Paint paint = new Paint();
            
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            paint.setColor(Color.GRAY);
            canvas.drawCircle(centerX, centerY, baseRadius, paint);
            
            
            paint.setColor(Color.BLACK);
            canvas.drawCircle(newX, newY, hatRadius, paint);
            getHolder().unlockCanvasAndPost(canvas);
    	}
    }
    public Joystick(Context context, AttributeSet attrs){
        super(context, attrs);
        setOnTouchListener(this);
        if(context instanceof JoystickListener) {
        	joystickCallback = (JoystickListener) context;
        }
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setFocusable(true);
        setFocusableInTouchMode(true);
        post( () -> {
            setupDimensions();
            drawJoystick(centerX, centerY);
        });
    }
    
    @Override
    public boolean onTouch(View view, MotionEvent mEvent) {
        if(view.equals(this)) {
        	if(mEvent.getAction() != MotionEvent.ACTION_UP) {
        		float displacement = (float) Math.sqrt(
                    Math.pow(mEvent.getX() - centerX, 2) + 
                    Math.pow(mEvent.getY() - centerY, 2));
                float ratio = baseRadius / displacement;
                float drawX, drawY;
                if(displacement < baseRadius) {
                	drawX = mEvent.getX();
                    drawY = mEvent.getY();
                } else {
                	drawX = centerX + (mEvent.getX() - centerX) * ratio;
                    drawY = centerY + (mEvent.getY() - centerY) * ratio;
                }
                joystickCallback.onJoystickMoved(
                    (drawX - centerX) / baseRadius,
                    (drawY - centerY) / baseRadius,
                    getId()
                );
                drawJoystick(drawX, drawY);
        	}else{
                joystickCallback.onJoystickMoved(0, 0, getId());
            }
            
            return true;
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int arg0, int arg1, int arg2, int arg3) {
        super.onSizeChanged(arg0, arg1, arg2, arg3);
        setupDimensions();
        drawJoystick(centerX, centerY);
    }
}
