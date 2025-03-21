package com.example.minijeu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final GameThread thread;
    private boolean gameOver = false;
    private boolean isJumping = false;
    private float jumpVelocity = 0;
    private float gravity = 2;
    private int groundY = 400;
    private int jumpHeight = 150;
    private int characterWidth = 70;
    private int characterHeight = 150;
    private int characterX = 0;
    private int characterY = 0;
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private int obstacleSpeed = 10;
    private static final int MIN_DISTANCE_BETWEEN_OBSTACLES = 200;


    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);
        //this.context = context;
        groundY = getHeight() - 100;
        characterY = groundY - characterHeight;
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
        groundY = getHeight() - 100;
        characterY = groundY - characterHeight;

        thread.setRunning(true);
        thread.start();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas == null) return;

        Paint paint = new Paint();

        paint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getWidth(), getHeight() - 100, paint);

        paint.setColor(Color.GREEN);
        canvas.drawRect(0, getHeight() - 100, getWidth(), getHeight(), paint);

        Bitmap spriteSheetPrincess = BitmapFactory.decodeResource(getResources(), R.drawable.princess);
        Bitmap princess = Bitmap.createScaledBitmap(spriteSheetPrincess, spriteSheetPrincess.getWidth() * 3, spriteSheetPrincess.getHeight() * 3,false);
        canvas.drawBitmap(princess, characterX, characterY - characterHeight + 10, null);

        Bitmap spriteSheet = BitmapFactory.decodeResource(getResources(), R.drawable.axe_sprite);
        Bitmap axe = Bitmap.createScaledBitmap(spriteSheet, spriteSheet.getWidth() * 3, spriteSheet.getHeight() * 3,false);

        for (Obstacle obstacle : obstacles) {
            //canvas.drawBitmap(GameCharacters.GHOST.getSprite(0, 0), obstacle.x, groundY - obstacle.size, null);
            canvas.drawBitmap(axe, obstacle.x, groundY - obstacle.size, null);

        }

        if (gameOver) {
            Paint textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(80);
            canvas.drawText("GameOver", getWidth() / 2 - 200, getHeight() / 2, textPaint);
        }
    }

    public void update() {
        if (!gameOver) {
            if (isJumping) {
                characterY += jumpVelocity;
                jumpVelocity += gravity;
                if (characterY >= groundY - characterHeight) {
                    characterY = groundY - characterHeight;
                    isJumping = false;
                }
            }

            Iterator<Obstacle> iterator = obstacles.iterator();
            while(iterator.hasNext()) {
                Obstacle obstacle = iterator.next();
                obstacle.x -= obstacleSpeed;

                if (characterX + characterWidth > obstacle.x && characterX < obstacle.x +
                obstacle.size && characterY + characterHeight > groundY - obstacle.size) {
                    gameOver = true;
                    thread.setRunning(false);
                }

                if (obstacle.x < -obstacle.size) {
                    iterator.remove();
                }
            }

            if (Math.random() < 0.02) {
                //obstacles.add(new Obstacle(getWidth(), (int) (Math.random() * 50) + 50));
                generateObstacle();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isJumping) {
            isJumping = true;
            jumpVelocity = -30;
        }
        return super.onTouchEvent(event);
    }

    private static class Obstacle {
        int x;
        int size;

        Obstacle(int x, int size) {
            this.x = x;
            this.size = size;
        }
    }

    private void generateObstacle() {
        if (obstacles.isEmpty()) {
            obstacles.add(new Obstacle(getWidth(), (int) (Math.random() * 50) + 50));
        } else {
            Obstacle lastObstacle = obstacles.get(obstacles.size() -1);

            if (lastObstacle.x < getWidth() - MIN_DISTANCE_BETWEEN_OBSTACLES) {
                obstacles.add(new Obstacle(getWidth(), (int) (Math.random() * 50) + 50));
            }
        }
    }
}