package com.example.komputerek.asteroidescape;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("HiScores", MODE_PRIVATE);

        final Button buttonPlay = (Button) findViewById(R.id.buttonPlay);

        final TextView textFastestTime = (TextView) findViewById(R.id.textHighScore);

        buttonPlay.setOnClickListener(this);

        long fastestTime = prefs.getLong("fastestTime", 1000000);

        textFastestTime.setText("Fastest time: " + formatTime(fastestTime));
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }
}
