package com.danielye.appdanielye;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
public class MainActivity extends AppCompatActivity {
    private TextView scoreLabel;
    private TextView startLabel;
    private TextView pointCounter;
    private ImageView box;
    private ImageView crash;
    private ImageView bottomPipe;
    private ImageView topPipe;
    private ImageView[] bonuses;
    private ImageView[] livesArr;
    private int chosenBonus;
    private int numOfBonuses;
    // Size
    private int frameHeight;
    private int boxHeight;
    private int boxWidth;
    private int screenWidth;
    private int screenHeight;
    private float gapWidth;
    private int bonusSize;
    // Position
    private float boxY;
    private float boxX;
    private float prevDiff;
    private float pipeX;
    private float gapY;
    private float topPipeY;
    private float bottomPipeY;
    private float bonusX;
    private float bonusY;
    private float diff;
    //Speed
    private float pipeSpeed;
    private float bonusYSpeed;
    private float gapYSpeed;
    private float crashSpeed;
    // Initialize Class
    private Handler handler;
    private Timer timer;
    // Score
    private int score = 0;
    //touch
    float initialY;
    float finalY;
    // Status Check
    private boolean action_flg = false;
    private boolean start_flg = false;
    private boolean pipe_reset = false;
    private boolean crashed = false;
    private boolean immune = false;
    //bonus
    private double randomBonus;
    private int bonusCounter;
    private int prevBonus;
    private int lives;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //defines layout of views, buttons, images, and labels
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        pointCounter = (TextView) findViewById(R.id.pointCounter);
        //hide pointCounter
        pointCounter.setVisibility(View.INVISIBLE);
        box = (ImageView) findViewById(R.id.box);
        crash = (ImageView) findViewById(R.id.crash);
        crash.setVisibility(View.INVISIBLE);
        topPipe = (ImageView) findViewById(R.id.topPipe);
        bottomPipe = (ImageView) findViewById(R.id.bottomPipe);
        numOfBonuses = 4;
        bonuses = new ImageView[numOfBonuses]; //array for 4 bonuses
        bonuses[0] = (ImageView) findViewById(R.id.gapBonus);
        bonuses[1] = (ImageView) findViewById(R.id.slowBonus);
        bonuses[2] = (ImageView) findViewById(R.id.pointsBonus);
        bonuses[3] = (ImageView) findViewById(R.id.lifeBonus);
        livesArr = new ImageView[3];
        livesArr[0] = (ImageView) findViewById(R.id.life1);
        livesArr[1] = (ImageView) findViewById(R.id.life2);
        livesArr[2] = (ImageView) findViewById(R.id.life3);
        for (int i = 0;i<livesArr.length;i++) {
            livesArr[i].setVisibility(View.INVISIBLE);
        }
        handler = new Handler();
        timer = new Timer();
        // Get screen size.
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        pipeSpeed = screenWidth / 250;
        bonusYSpeed = screenHeight / 500;
        crashSpeed = screenHeight / 450;
        gapYSpeed = screenHeight / 930;
        gapWidth = (float)(screenHeight/8);
        boxX = screenWidth / 5;
//        Log.v("height",screenHeight+"");
//        Log.v("gap",gapWidth+"");
        // Move pipes and bonus out of screen and set box in place
        box.setX(boxX);
        bottomPipe.setX(screenWidth);
        topPipe.setX(screenWidth);
        for (int i = 0;i<numOfBonuses;i++) {
            bonuses[i].setX(screenWidth);
        }
        bonusX = screenWidth;
        scoreLabel.setText("Score : 0");
        chosenBonus = -1;
        randomBonus = 1;
        bonusCounter = -1;
        prevBonus = -1;
        diff = 0;
        lives = 0;
    }
    public void changePos() {
        hitCheck();
        if (crashed==true) {
            boxY+=crashSpeed*1.3;
            if (boxY+boxHeight>=frameHeight) {
                boxY=frameHeight-boxHeight;
            }
            crash.setY(boxY);
        } else {
            //move bonus
//            Log.v("bonus:", chosenBonus + "");
            if (chosenBonus >= 0) {
                if (score >= 10) {
                    //move bonus up and down only after 10 points
                    bonusY += bonusYSpeed;
                    if (bonusY > frameHeight - bonuses[0].getHeight()) {
                        bonusY = frameHeight - bonuses[0].getHeight();
                        bonusYSpeed *= -1;
                    } else if (bonusY < 0) {
                        bonusY = 0;
                        bonusYSpeed *= -1;
                    }
                }
                //move bonus left until it reaches the end of the screen
                if (bonusX <= -bonusSize) {
                    bonusX = screenWidth;
                    bonuses[chosenBonus].setX(bonusX);
                    chosenBonus = -1;
                } else {
                    bonusX -= pipeSpeed;
                    bonuses[chosenBonus].setX(bonusX);
                    bonuses[chosenBonus].setY(bonusY);
                }
            }
            //increase score count when pipes pass box
            if (pipe_reset == false && pipeX + bottomPipe.getWidth() < boxX) {
                score++;
                scoreLabel.setText("Score : " + score);
                pointCounter.setText("" + score);
                pipe_reset = true;
            }
            // move top and bottom pipe to the edge
            pipeX -= pipeSpeed;
            if (pipeX < -bottomPipe.getWidth()) {
                pipe_reset = false;
                //keeping track of the duration of each bonus
                if (bonusCounter != -1) {
                    bonusCounter++;
                    if (prevBonus == 0 && bonusCounter == 3) { //gap bonus active
                        gapWidth /= 1.1;
                        prevBonus = -1;
                        bonusCounter = -1;
                    } else if (prevBonus == 1 && bonusCounter == 3) { //slow bonus active
                        pipeSpeed /= 0.75;
                        gapYSpeed /= 0.75;
                        prevBonus = -1;
                        bonusCounter = -1;
                    }
                }
                //increase speed of pipes and decrease gapWidth every 10 points
                if (score > 0 && score % 10 == 0) {
                    pipeSpeed *= 1.07;
                    gapWidth *= 0.95;
                    if (gapWidth < boxHeight*1.5) {
                        gapWidth = (float)(boxHeight*1.5);
                    }
                }
                pipeX = screenWidth + bottomPipe.getWidth();
                //choose random y-value for gap, and add 0.7 gapWidth padding on each side so gap dosent go on edge
                gapY = (int) (Math.random() * (frameHeight - 2.4 * gapWidth) + 0.7 * gapWidth);
                randomBonus = Math.random();
            }
            if (score % 10 == 0 && score > 0) {
                if (randomBonus < 0.5)
                    gapY += gapYSpeed;
                else
                    gapY -= gapYSpeed;
                if (gapY > frameHeight - gapWidth) {
                    gapY = frameHeight - gapWidth;
                    gapYSpeed *= -1;
                } else if (gapY < 0) {
                    gapY = 0;
                    gapYSpeed *= -1;
                }
            }
            topPipeY = gapY - topPipe.getHeight();
            bottomPipeY = gapY + gapWidth;
            bottomPipe.setX(pipeX);
            bottomPipe.setY(bottomPipeY);
            topPipe.setX(pipeX);
            topPipe.setY(topPipeY);
            //spawn bonus randomly. 60% chance of spawning every 3 points
            if (score > 0 && score % 3 == 0 && pipeX <= screenWidth / 2 && pipeX > boxX + boxWidth && chosenBonus == -1 && randomBonus < 0.5) {
                chosenBonus = (int) (Math.random() * numOfBonuses);
                bonusY = (float) (Math.random() * (frameHeight - bonusSize));
                bonuses[chosenBonus].setX(bonusX);
                bonuses[chosenBonus].setY(bonusY);
            }
            // Move Box
            if (action_flg && finalY != 0 && initialY != 0) {
                diff = finalY - initialY;
                //increase sensitivity for touch movement
                diff *= 1.5;
                if (diff != prevDiff) {
                    boxY += diff;
                    if (boxY < 0) boxY = 0;
                    if (boxY > frameHeight - boxHeight) boxY = frameHeight - boxHeight;
                    box.setY(boxY);
                    prevDiff = diff;
                }
            }
        }
    }
    public void hitCheck()   {
        //once crashed bird reached bottom of screen, go to results page
        if (crashed==true && boxY+boxHeight==frameHeight) {
            // Stop Timer!!
            if (timer!=null)
                timer.cancel();
            timer = null;
            //end game
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
        //check if immunity expired
        float topPipeEdge = topPipeY+topPipe.getHeight();
        float bottomPipeEdge = bottomPipeY;
        // If the edge of the box hits the pipe, game over.
        if ((boxY<=topPipeEdge||boxY+boxHeight>=bottomPipeEdge)&&(pipeX<=boxX+boxWidth&&pipeX+bottomPipe.getWidth()>=boxX)&&crashed==false&&immune==false) {
            //if you have more than one life, remove one, and start immunity
            if (lives>0) {
//                Log.v("lives ",lives+"");
                livesArr[lives-1].setVisibility(View.INVISIBLE);
                immune=true;
                lives--;
            } else {
                //switch bird avatars
                crash.setX(box.getX());
                crash.setY(box.getY());
                boxY = crash.getY();
                box.setVisibility(View.INVISIBLE);
                crash.setVisibility(View.VISIBLE);
                crashed = true;
            }
        }
        //once you have passed the pipes with immunity, remove immunity
        if (pipeX+bottomPipe.getWidth()<boxX&&crashed==false&&immune==true){
            immune = false;
        }
        //check for bonus hits
        if (chosenBonus>=0 && ((bonusX<=boxX+boxWidth && bonusX>=boxX)||(bonusX+bonusSize<=boxX+boxWidth&& bonusX+bonusSize>=boxX))) {
            //if top or bottom y-values of the box when its moving is inside bonus y-values, it counts as a hit
            if ((boxY<=bonusY+bonusSize&&boxY>=bonusY)||(boxY+boxHeight>=bonusY&&boxY+boxHeight<=bonusY+bonusSize)) {
                //chosenBonus: 0 = gapBonus, 1 = slowBonus, 2 = pointsBonus
                if (chosenBonus==0) {
                    gapWidth*=1.1;
                    bonusCounter = 0;
                    prevBonus = 0;
                } else if (chosenBonus==1) {
                    pipeSpeed*=0.75;//decrease pipeSpeed by 25% for 3 pipes
                    gapYSpeed*=0.75;
                    bonusCounter = 0;
                    prevBonus = 1;
                } else if (chosenBonus==2) {
                    score++;
                    scoreLabel.setText("Score : " + score);
                    pointCounter.setText("" + score);
                } else if (chosenBonus==3) {
                    lives++;
                    if (lives>3) {
                        lives=3;
                    }
                    livesArr[lives-1].setVisibility(View.VISIBLE);
                }
                bonusX = screenWidth;
                bonuses[chosenBonus].setX(bonusX);
                chosenBonus = -1;
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (start_flg == false) {
            start_flg = true;
            // Why get frame height and box height here?
            // Because the UI has not been set on the screen in OnCreate()!!
            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            //make pointCounter visible
            pointCounter.setVisibility(View.VISIBLE);
            //set randomized pipes
            pipeX = frame.getWidth();
            gapY = (float)(Math.random() * (frameHeight - 2 * gapWidth) + 0.7 * gapWidth); //add 0.7 gapWidth padding on each side so gap dosent go on edge
            topPipeY = gapY - topPipe.getHeight();
            bottomPipeY = gapY + gapWidth;
            bottomPipe.setX(pipeX);
            bottomPipe.setY(bottomPipeY);
            topPipe.setX(pipeX);
            topPipe.setY(topPipeY);
            boxY = box.getY();
            // The box is a square.(height and width are the same.)
            boxHeight = box.getHeight();
            boxWidth = box.getWidth();
            bonusSize = bonuses[0].getHeight();
            startLabel.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 5);
        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                initialY = me.getY();
                finalY = initialY;
            } else
            if (me.getAction() == MotionEvent.ACTION_MOVE) {
                action_flg = true;
                initialY = finalY;
                finalY = me.getY();
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                finalY = initialY;
                action_flg = false;
            }
        }
        return true;
    }
    // Override Return Button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode()==KeyEvent.KEYCODE_BACK&&start_flg==false) {
                startActivity(new Intent(getApplicationContext(), start.class));
            } else {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
