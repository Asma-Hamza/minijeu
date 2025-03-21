package com.example.minijeu;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final GameThread thread;
    private int x = 0;
    private int x2 = 300;
    private boolean gameOver = false;
    private Context context;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        this.context = context;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas == null) return;

        SharedPreferences sharedPref = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
        int valeur_y = sharedPref.getInt("valeur_y", 0);
        Log.d("valeur_y", String.valueOf(valeur_y));

        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        paint.setColor(Color.rgb(250, 0, 0));

        Paint paintBlue = new Paint();
        paintBlue.setColor(Color.rgb(0, 0, 250));

        if (!gameOver) {
            canvas.drawRect(x, valeur_y, x + 100, valeur_y + 100, paint);
            canvas.drawRect(x2, valeur_y, x2 + 100, valeur_y + 100, paintBlue);
        } else {
            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(60);
            canvas.drawText("Game Over", getWidth() / 2 - 100, getHeight() / 2, textPaint);
        }
    }

    public void update() {
        if (!gameOver) {
            x = (x + 2) % 300;

            x2 = (x2 - 2 + 300) % 300;

            if (Math.abs(x -x2) < 100) {
                gameOver = true;
                thread.setRunning(false);
            }
        }
    }

}