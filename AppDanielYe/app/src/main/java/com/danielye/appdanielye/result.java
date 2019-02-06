package com.danielye.appdanielye;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class result extends AppCompatActivity {
    private LinearLayout info;
    private Button startButton;
    private Button storeButton;
    private Button homeButton;
    private Button infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        info = findViewById(R.id.info);
        info.setVisibility(View.INVISIBLE);
        startButton = findViewById(R.id.startButton);
        storeButton = findViewById(R.id.storeButton);
        homeButton = findViewById(R.id.homeButton);
        infoButton = findViewById(R.id.infoButton);
        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            highScoreLabel.setText("High Score : " + score);
            // Update High Score
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        } else {
            highScoreLabel.setText("High Score : " + highScore);

        }

    }
    public void showInfo(View view) {
        startButton.setVisibility(View.INVISIBLE);
        storeButton.setVisibility(View.INVISIBLE);
        homeButton.setVisibility(View.INVISIBLE);
        infoButton.setVisibility(View.INVISIBLE);
        info.setVisibility(View.VISIBLE);
    }
    public void closeInfo(View view) {
        info.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        storeButton.setVisibility(View.VISIBLE);
        homeButton.setVisibility(View.VISIBLE);
        infoButton.setVisibility(View.VISIBLE);
    }
    public void home(View view) {
        startActivity(new Intent(getApplicationContext(), start.class));
    }
    public void tryAgain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    public void store(View view) {
        startActivity(new Intent(getApplicationContext(), store.class));
    }
    // Disable Return Button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
