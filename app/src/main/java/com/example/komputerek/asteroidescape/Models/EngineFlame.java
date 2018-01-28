package com.example.komputerek.asteroidescape.Models;

import java.util.Random;

/**
 * Created by komputerek on 26.12.17.
 */

public class EngineFlame {

    private int x, y;
    private int speed;
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private Random generator;

    public EngineFlame(PlayerShip player) {
        generator = new Random();
        x = player.getX() + 61;
        minY = player.getY() + 24;
        maxY = player.getY() + 27;
        y = generator.nextInt(3) + maxY;
        maxX = x;
        speed = generator.nextInt(25);
    }

    public void update(int playerSpeed, int playerY) {
        x -= playerSpeed;
        x -= speed;
        if (x<0) {
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(3) + 48 + playerY;
            speed = generator.nextInt(35)+35;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
