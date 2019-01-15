package com.danielye.appdanielye;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class store extends AppCompatActivity {
    private RewardedVideoAd mRewardedVideoAd;
    private TextView coinsCounter;
    private int coins;
    private String unlock;
    private int cost;
    private LinearLayout confirm;
    private LinearLayout insufficient;
    private String chosenSkin;
    private int skinIndex;
    private int numOfSkins;
    private Button[] buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        coinsCounter = (TextView) findViewById(R.id.coinsCounter);
        confirm = findViewById(R.id.confirm);
        insufficient = findViewById(R.id.insufficient);
        confirm.setVisibility(View.INVISIBLE);
        insufficient.setVisibility(View.INVISIBLE);
        SharedPreferences settings = getSharedPreferences("COINS", Context.MODE_PRIVATE);
        coins = settings.getInt("COINS", 0);
        coinsCounter.setText(""+coins);
        settings = getSharedPreferences("UNLOCK", Context.MODE_PRIVATE);
        unlock = settings.getString("UNLOCK", "100000"); //1 - unlocked skin, 0 - locked skin
        numOfSkins = 6;
        buttons = new Button[numOfSkins]; //array for 6 skin buttons
        buttons[0] = findViewById(R.id.yellowBirdButton);
        buttons[1] = findViewById(R.id.chickenButton);
        buttons[2] = findViewById(R.id.helmetButton);
        buttons[3] = findViewById(R.id.dragonButton);
        buttons[4] = findViewById(R.id.flyButton);
        buttons[5] = findViewById(R.id.monsterButton);
        //set each button to UNLOCK or SELECT depending on if the player has unlocked it or not
        for (int i=0;i<unlock.length();i++) {
            if (unlock.charAt(i)=='1') {
                buttons[i].setBackgroundColor(Color.WHITE);
                buttons[i].setTextColor(Color.BLACK);
                buttons[i].setText("Select");
            } else {
                buttons[i].setBackgroundColor(Color.RED);
                buttons[i].setTextColor(Color.WHITE);
                buttons[i].setText("Unlock");
            }
        }
    }


    public void yellowBird(View view) {
        SharedPreferences settings = getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("CHARACTER", "yellowBird");
        editor.commit();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    public void chick(View view) {
        if (unlock.charAt(1)=='1') {
            SharedPreferences settings = getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("CHARACTER", "chicken");
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            cost = 10;
            chosenSkin = "chicken";
            skinIndex = 1;
            showConfirmMessage();
        }
    }
    public void helmet(View view) {
        if (unlock.charAt(2)=='1') {
            SharedPreferences settings = getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("CHARACTER", "helmet");
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            cost = 10;
            chosenSkin = "helmet";
            skinIndex = 2;
            showConfirmMessage();
        }
    }
    public void dragon(View view) {
        if (unlock.charAt(3)=='1') {
            SharedPreferences settings = getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("CHARACTER", "dragon");
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            cost = 20;
            chosenSkin = "dragon";
            skinIndex = 3;
            showConfirmMessage();
        }
    }
    public void fly(View view) {
        if (unlock.charAt(4)=='1') {
            SharedPreferences settings = getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("CHARACTER", "fly");
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            cost = 20;
            chosenSkin = "fly";
            skinIndex = 4;
            showConfirmMessage();
        }
    }
    public void monster(View view) {
        if (unlock.charAt(5)=='1') {
            SharedPreferences settings = getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("CHARACTER", "monster");
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            cost = 20;
            chosenSkin = "monster";
            skinIndex = 5;
            showConfirmMessage();
        }
    }
    public void showConfirmMessage() {
        for (int i = 0;i<numOfSkins;i++) {
            buttons[i].setVisibility(View.INVISIBLE);
        }
        TextView confirmMessage = findViewById(R.id.confirmMessage);
        confirmMessage.setText("Purchase skin for "+cost+" coins?");
        confirm.setVisibility(View.VISIBLE);
    }
    public void buy(View view) {
        if (coins>=cost) { //if you have enough coins, you can buy it
            coins -= cost;
            SharedPreferences settings = getSharedPreferences("COINS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("COINS", coins);
            editor.commit();
            coinsCounter.setText(""+coins);
            settings = getSharedPreferences("CHARACTER", Context.MODE_PRIVATE);
            editor = settings.edit();
            editor.putString("CHARACTER", chosenSkin);
            editor.commit();
            if (skinIndex+1==numOfSkins) {
                unlock = unlock.substring(0, skinIndex) + "1";
            } else {
                unlock = unlock.substring(0, skinIndex) + "1" + unlock.substring(skinIndex+1);
            }
            settings = getSharedPreferences("UNLOCK", Context.MODE_PRIVATE);
            editor = settings.edit();
            editor.putString("UNLOCK", unlock);
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            confirm.setVisibility(View.INVISIBLE);
            insufficient.setVisibility(View.VISIBLE);
        }
    }
    public void cancel(View view) {
        for (int i = 0;i<numOfSkins;i++) {
            buttons[i].setVisibility(View.VISIBLE);
        }
        confirm.setVisibility(View.INVISIBLE);
    }
    public void keepPlaying(View view) {
        insufficient.setVisibility(View.INVISIBLE);
        for (int i = 0;i<numOfSkins;i++) {
            buttons[i].setVisibility(View.VISIBLE);
        }
    }
    public void watchAd(View view)  {
       MobileAds.initialize(this, "ADMOB ID");
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        loadRewardedVideoAd();
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem reward) {
                coins += 2;
                SharedPreferences settings = getSharedPreferences("COINS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("COINS", coins);
                editor.commit();
                coinsCounter.setText(""+coins);
                insufficient.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onRewardedVideoAdLoaded() {
                mRewardedVideoAd.show();
            }
            @Override
            public void onRewardedVideoAdOpened() {
            }
            @Override
            public void onRewardedVideoStarted() {

            }
            @Override
            public void onRewardedVideoAdLeftApplication() {
                insufficient.setVisibility(View.INVISIBLE);
                for (int i = 0;i<numOfSkins;i++) {
                    buttons[i].setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onRewardedVideoAdClosed() {
                insufficient.setVisibility(View.INVISIBLE);
                for (int i = 0;i<numOfSkins;i++) {
                    buttons[i].setVisibility(View.VISIBLE);
                }
                Toast.makeText(getApplicationContext(),"+2 Coins",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRewardedVideoCompleted() {

            }
            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                insufficient.setVisibility(View.INVISIBLE);
                for (int i = 0;i<numOfSkins;i++) {
                    buttons[i].setVisibility(View.VISIBLE);
                }
                Toast.makeText(getApplicationContext(),"Sorry, Ad Failed to Load",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build()); //test ad unit ID
    }
}
