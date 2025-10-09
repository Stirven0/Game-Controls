package com.example.gamecontrolsproyect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private float playerX = 500, playerY = 500;
    private Paint paint;

    public GameView(Context context) {
        super(context);
        setup();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        getHolder().addCallback(this);
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    public void updatePlayer(float dx, float dy) {
        playerX += dx * 10;
        playerY += dy * 10;
        draw();
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.WHITE);
            canvas.drawRect(playerX, playerY, playerX + 100, playerY + 100, paint);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }
    
    public void setPlayerColor(int color) {
    	paint.setColor(color);    
        draw();
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {}
}
