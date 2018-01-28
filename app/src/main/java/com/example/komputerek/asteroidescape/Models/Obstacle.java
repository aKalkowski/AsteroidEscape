package com.example.komputerek.asteroidescape.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.komputerek.asteroidescape.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by komputerek on 25.12.17.
 */

public class Obstacle {

    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private Rect hitBox;
    private ArrayList<Bitmap> images = new ArrayList<>();

    public Obstacle(Context context, int screenX, int screenY) {
        images.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorite1));
        images.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorite2));
        images.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorite3));
        images.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorite4));
        Random randImage = new Random();
        bitmap = images.get(randImage.nextInt(images.size()));
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(int playerSpeed) {
        x -= playerSpeed;
        x -= speed;
        if (x < minX - bitmap.getWidth()) {
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
            bitmap = images.get(generator.nextInt(4));
            scaleBitmap(maxX);
        }
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public Rect getHitBox() {
        return hitBox;
    }
}
