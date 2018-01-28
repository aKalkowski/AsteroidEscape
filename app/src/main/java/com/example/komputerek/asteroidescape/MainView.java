package com.example.komputerek.asteroidescape;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.komputerek.asteroidescape.Models.EngineFlame;
import com.example.komputerek.asteroidescape.Models.Obstacle;
import com.example.komputerek.asteroidescape.Models.PlayerShip;
import com.example.komputerek.asteroidescape.Models.PowerUp;
import com.example.komputerek.asteroidescape.Models.SpaceDust;

import java.util.ArrayList;

/**
 * Created by komputerek on 24.12.17.
 */

public class MainView extends SurfaceView implements Runnable {

    public ArrayList<SpaceDust> dustList = new ArrayList<>();
    public ArrayList<EngineFlame> engineFlameListRed = new ArrayList<>();
    public ArrayList<EngineFlame> engineFlameListYellow = new ArrayList<>();

    volatile boolean playing;
    Thread gameThread = null;

    private PlayerShip player;
    private Obstacle enemy1;
    private Obstacle enemy2;
    private Obstacle enemy3;
    private Obstacle enemy4;
    private Obstacle enemy5;
    private PowerUp powerUp;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;
    private int screenX;
    private int screenY;
    private Context context;
    private boolean gameEnded;
    private boolean debugMode;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public MainView(Context context, int x, int y) {
        super(context);
        this.context = context;
        surfaceHolder = getHolder();
        paint = new Paint();
        screenX = x;
        screenY = y;
        debugMode = false;
        int numSpecks = 120;
        for (int i = 0; i < numSpecks; i++) {
            dustList.add(new SpaceDust(screenX, screenY));
        }
        startGame();
        int flameParticles = 10;
        for (int i = 0; i < flameParticles; i++) {
            engineFlameListRed.add(new EngineFlame(player));
        }
        for (int i = 0; i < flameParticles; i++) {
            engineFlameListYellow.add(new EngineFlame(player));
        }
        prefs = context.getSharedPreferences("HiScores", context.MODE_PRIVATE);
        editor = prefs.edit();
        fastestTime = prefs.getLong("fastestTime", 1000000);

    }

    private void startGame() {
        player = new PlayerShip(context, screenX, screenY);
        enemy1 = new Obstacle(context, screenX, screenY);
        enemy2 = new Obstacle(context, screenX, screenY);
        enemy3 = new Obstacle(context, screenX, screenY);
        powerUp = new PowerUp(context, screenX, screenY);
        if (screenX > 1000) {
            enemy4 = new Obstacle(context, screenX, screenY);
        }
        if (screenX > 1200) {
            enemy5 = new Obstacle(context, screenX, screenY);
        }
        distanceRemaining = 10000;
        timeTaken = 0;
        timeStarted = System.currentTimeMillis();
        gameEnded = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.startBoosting();
                if (gameEnded) {
                    startGame();
                }
                break;
        }
        return true;
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        player.update(context);
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());
        if (screenX > 1000) {
            enemy4.update(player.getSpeed());
        }
        if (screenX > 1200) {
            enemy5.update(player.getSpeed());
        }
        for (SpaceDust sd : dustList) {
            sd.update(player.getSpeed());
        }
        for (EngineFlame ef : engineFlameListRed) {
            ef.update(player.getSpeed(), player.getY());
        }
        for (EngineFlame ef : engineFlameListYellow) {
            ef.update(player.getSpeed(), player.getY());
        }
        if (Rect.intersects(player.getHitBox(), powerUp.getHitBox())) {
            player.increaseShielsStrength();
        }
        boolean hitDetected = false;
        if(Rect.intersects(player.getHitBox(), enemy1.getHitBox())) {
            enemy1.setX(-150);
            hitDetected = true;
        }
        if(Rect.intersects(player.getHitBox(), enemy2.getHitBox())) {
            enemy2.setX(-150);
            hitDetected = true;
        }
        if(Rect.intersects(player.getHitBox(), enemy3.getHitBox())) {
            enemy3.setX(-150);
            hitDetected = true;
        }
        if (screenX > 1000) {
            if (Rect.intersects(player.getHitBox(), enemy4.getHitBox())) {
                hitDetected = true;
                enemy4.setX(-150);
            }
        }
        if (screenX > 1200) {
            if (Rect.intersects(player.getHitBox(), enemy5.getHitBox())) {
                hitDetected = true;
                enemy5.setX(-150);
            }
        }
        if (hitDetected) {
            player.reduceShieldStrength();
            if(player.getShieldStrength() < 0) {
                gameEnded = true;
            }
        }
        if (!gameEnded) {
            distanceRemaining -= player.getSpeed();
            timeTaken = (System.currentTimeMillis() - timeStarted);
        }
        if (distanceRemaining < 0) {
            if (timeTaken < fastestTime) {
                editor.putLong("fastestTime", timeTaken);
                editor.commit();
                fastestTime = timeTaken;
            }
            distanceRemaining = 0;
            gameEnded = true;
        }
    }

    private String formatTime(long time) {
        long seconds = (time) / 1000;
        long thousands = (time) - (seconds * 1000);
        String strThousands = "" + thousands;
        if (thousands < 100) {
            strThousands = "0" + thousands;
        }
        if (thousands < 10) {
            strThousands = "0" + strThousands;
        }
        String stringTime = "" + seconds + "." + strThousands;
        return stringTime;
    }

//    private void generatePowerUp() {
//        ThreadLocalRandom.current().nextLong()
//    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            paint.setColor(Color.argb(255, 255, 255, 255));
            for (SpaceDust sd : dustList) {
                canvas.drawPoint(sd.getX(), sd.getY(), paint);
            }
            if (player.getShieldStrength() >= 0) {
                for (EngineFlame ef : engineFlameListRed) {
                    paint.setColor(Color.argb(255, 255, 0, 0));
                    canvas.drawPoint(ef.getX(), ef.getY(), paint);
                }
                for (EngineFlame ef : engineFlameListYellow) {
                    paint.setColor(Color.argb(255, 255, 255, 0));
                    canvas.drawPoint(ef.getX(), ef.getY(), paint);
                }
            }
            if (debugMode) {
                canvas.drawRect(player.getHitBox().left, player.getHitBox().top, player.getHitBox().right,
                        player.getHitBox().bottom, paint);
                canvas.drawRect(enemy1.getHitBox().left, enemy1.getHitBox().top, enemy1.getHitBox().right,
                        enemy1.getHitBox().bottom, paint);
                canvas.drawRect(enemy2.getHitBox().left, enemy2.getHitBox().top, enemy2.getHitBox().right,
                        enemy2.getHitBox().bottom, paint);
                canvas.drawRect(enemy3.getHitBox().left, enemy3.getHitBox().top, enemy3.getHitBox().right,
                        enemy3.getHitBox().bottom, paint);
                if (screenX > 1000) {
                    canvas.drawRect(enemy4.getHitBox().left, enemy4.getHitBox().top, enemy4.getHitBox().right,
                            enemy4.getHitBox().bottom, paint);
                }
                if (screenX > 1200) {
                    canvas.drawRect(enemy5.getHitBox().left, enemy5.getHitBox().top, enemy5.getHitBox().right,
                            enemy5.getHitBox().bottom, paint);
                }
            }
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);
            canvas.drawBitmap(enemy1.getBitmap(), enemy1.getX(), enemy1.getY(), paint);
            canvas.drawBitmap(enemy2.getBitmap(), enemy2.getX(), enemy2.getY(), paint);
            canvas.drawBitmap(enemy3.getBitmap(), enemy3.getX(), enemy3.getY(), paint);
            if (screenX > 1000) {
                canvas.drawBitmap(enemy4.getBitmap(), enemy4.getX(), enemy4.getY(), paint);
            }
            if (screenX > 1200) {
                canvas.drawBitmap(enemy5.getBitmap(), enemy5.getX(), enemy5.getY(), paint);
            }
            if (!gameEnded) {
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(25);
                //canvas.drawText("Fastest: " + fastestTime + " s", 10, 20, paint);
                canvas.drawText("Fastest: " + formatTime(fastestTime) + "s", 10, 20, paint);
                //canvas.drawText("Time: " + timeTaken + " s", screenX / 2, 20, paint);
                canvas.drawText("Time: " + formatTime(timeTaken) + "s", screenX / 2, 20, paint);
                canvas.drawText("Distance: " + distanceRemaining / 1000 + " km", screenX / 3, screenY - 20, paint);
                canvas.drawText("Shield: " + player.getShieldStrength(), 10, screenY - 20, paint);
                canvas.drawText("Speed: " + player.getSpeed() * 60 + " mps", (screenX / 3) * 2, screenY - 20, paint);
            } else {
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over!", screenX/2, 100, paint);
                paint.setTextSize(25);
//                canvas.drawText("Fastest: " + fastestTime + " s", screenX/2, 160, paint);
//                canvas.drawText("Time: " + timeTaken + " s", screenX/2, 200, paint);
                canvas.drawText("Fastest: " + formatTime(fastestTime) + "s", screenX/2, 160, paint);
                canvas.drawText("Time: " + formatTime(timeTaken) + "s", screenX/2, 200, paint);
                canvas.drawText("Distance remaining: " + distanceRemaining/1000 + " km", screenX/2, 240, paint);
                paint.setTextSize(80);
                canvas.drawText("Tap to replay!", screenX/2, 350, paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
