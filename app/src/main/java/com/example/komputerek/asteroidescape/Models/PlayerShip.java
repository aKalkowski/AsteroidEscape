package com.example.komputerek.asteroidescape.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.example.komputerek.asteroidescape.R;

/**
 * Created by komputerek on 24.12.17.
 */

public class PlayerShip {

    private final int GRAVITY = -12;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;
    private Bitmap bitmap;
    private int x, y;
    private int speed = 0;
    private boolean boosting;
    private Rect hitBox;
    private int shieldStrength;

    public PlayerShip(Context context, int screenX, int screenY) {
        x = 50;
        y = 50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        boosting = false;
        maxY = screenY - bitmap.getHeight();
        minY = 0;
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        shieldStrength = 2;
    }

    public void update(Context context) {
        if (boosting) {
            speed += 2;
        } else {
            speed -= 5;
        }
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        } else if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
        y -= speed + GRAVITY;
        if (y < minY) {
            y = minY;
        } else if (y > maxY) {
            y = maxY;
        }
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();

        switch (this.shieldStrength) {
            case 2:
                setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ship));
                break;
            case 1:
                setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shipdmg1));
                break;
            case 0:
                setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shipdmg2));
                break;
            case -1:
                setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shipwrecked));
                break;
        }
        scaleBitmap(maxX);
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

    public void rotateWreck(int angle, Bitmap bitmap) {
        Animation rotate = new RotateAnimation(0, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setFillAfter(true);
    }

    public void reduceShieldStrength() {
        shieldStrength--;
    }

    public void increaseShielsStrength() {
        shieldStrength++;
    }

    public void startBoosting() {
        boosting = true;
    }

    public void stopBoosting() {
        boosting = false;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public int getShieldStrength() {
        return shieldStrength;
    }


}
