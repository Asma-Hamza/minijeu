package com.example.minijeu.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.minijeu.MainActivity;
import com.example.minijeu.R;

public enum GamePrincess {
    AXE(R.drawable.axe_sprite);

    private Bitmap spriteSheet;
    private Bitmap[][] sprites = new Bitmap[1][1];

    private BitmapFactory.Options options = new BitmapFactory.Options();

    GamePrincess(int resID) {
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID);
        sprites[0][0] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, 32, 32, 32, 32));
    }

    public Bitmap getSpriteSheet() {
        return spriteSheet;
    }

    public Bitmap getSprite() {
        return sprites[0][0];
    }

    private Bitmap getScaledBitmap(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 3, bitmap.getHeight() * 3,false);
    }

}
