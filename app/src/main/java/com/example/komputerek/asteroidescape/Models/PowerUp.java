package com.example.komputerek.asteroidescape.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.komputerek.asteroidescape.R;

import java.util.Random;

/**
 * Created by komputerek on 27.01.18.
 */

public class PowerUp {

    private Bitmap bitmap;
    private int x, y;
    private int speed;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private Rect hitBox;

    public PowerUp(Context context, int screenX, int screenY) {
        Random generator = new Random();
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wrenches);
        this.speed = generator.nextInt(6) + 5;
        this.maxX = screenX;
        this.minX = 0;
        this.maxY = screenY;
        this.minY = 0;
        this.x = screenX;
        this.y = generator.nextInt(maxY) - bitmap.getHeight();
        this.hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void scaleBitmap(int x) {
        if (x < 1000) {
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3,
                    bitmap.getHeight() / 3, false);
        } else if (x < 1200) {
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2,
                    bitmap.getHeight() / 2, false);
        }
    }

    public void update(int playerSpeed) {
        x -= playerSpeed;
        x -= speed;
        if (x < minX - bitmap.getWidth()) {
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
            scaleBitmap(maxX);
        }
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rect hitBox) {
        this.hitBox = hitBox;
    }
}
