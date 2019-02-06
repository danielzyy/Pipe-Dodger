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

import pl.droidsonroids.gif.GifImageView;

public class start extends AppCompatActivity {
    TextView danielye;
    Button itemsButton;
    Button startButton;
    Button storeButton;
    LinearLayout itemsMenu;
    GifImageView flappyBird;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        danielye = findViewById(R.id.danielye);
        itemsButton = findViewById(R.id.itemsButton);
        startButton = findViewById(R.id.startButton);
        storeButton = findViewById(R.id.storeButton);
        itemsMenu = findViewById(R.id.itemsMenu);
        flappyBird = findViewById(R.id.flappyBird);
        SharedPreferences settings = getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
        String character = settings.getString("CHARACTER", "yellowBird");
        if (character.equals("chicken")) {
            flappyBird.setImageResource(R.drawable.chickenfly);
        } else if (character.equals("helmet")) {
            flappyBird.setImageResource(R.drawable.helmetfly);
        } else if (character.equals("dragon")) {
            flappyBird.setImageResource(R.drawable.dragonfly);
        } else if (character.equals("fly")) {
            flappyBird.setImageResource(R.drawable.flyfly);
        } else if (character.equals("monster")) {
            flappyBird.setImageResource(R.drawable.monsterfly);
        } else  {
            flappyBird.setImageResource(R.drawable.birdfly);
        }
        itemsMenu.setVisibility(View.INVISIBLE);
    }

    public void startGame(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    public void store(View view) {
        startActivity(new Intent(getApplicationContext(), store.class));
    }
    public void showItemsMenu(View view) {
        flappyBird.setVisibility(View.INVISIBLE);
        danielye.setVisibility(View.INVISIBLE);
        itemsButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);
        storeButton.setVisibility(View.INVISIBLE);
        itemsMenu.setVisibility(View.VISIBLE);
    }
    public void backToMenu(View view) {
        itemsMenu.setVisibility(View.INVISIBLE);
        flappyBird.setVisibility(View.VISIBLE);
        danielye.setVisibility(View.VISIBLE);
        itemsButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.VISIBLE);
        storeButton.setVisibility(View.VISIBLE);
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
