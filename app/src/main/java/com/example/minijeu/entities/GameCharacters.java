package com.example.minijeu.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.minijeu.MainActivity;
import com.example.minijeu.R;

public enum GameCharacters {
    GHOST(R.drawable.ghost_spritesheet);

    private Bitmap spriteSheet;
    private Bitmap[][] sprites = new Bitmap[7][4];

    private BitmapFactory.Options options = new BitmapFactory.Options();

    GameCharacters(int resID) {
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID);
        System.out.println("Width: " + spriteSheet.getWidth() + " Height: " + spriteSheet.getHeight());
        for (int j = 0; j < sprites.length; j++) {
            for (int i = 0; i < sprites[j].length; i++) {
                sprites[j][i] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, 32 * i, 32 * j, 32, 32));
            }
        }
    }

    public Bitmap getSpriteSheet() {
        return spriteSheet;
    }

    public Bitmap getSprite(int yPos, int xPos) {
        return sprites[yPos][xPos];
    }

    private Bitmap getScaledBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 3, bitmap.getHeight() * 3,false);
    }
}
