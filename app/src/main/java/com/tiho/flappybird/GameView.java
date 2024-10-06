package com.tiho.flappybird;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

import android.os.Handler;


public class GameView extends View {
    private Bird bird;
    private Handler handler;
    private Runnable r;
    private ArrayList<Pipe> arrPipes;
    private int sumpipe, distance, sign = 2;
    private int score, bestScore = 0;
    private Context context;
    public static boolean start;
    private boolean isHit = false; // Flag to check if the bird has recently hit a pipe
    private static final int HIT_DELAY = 2000; // Delay in milliseconds (5 seconds)

    private ArrayList<Heart> arrHearts;
    private int sumheart;
    private boolean isHeartResetting = false;


    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("best_score", Context.MODE_PRIVATE);
        if(sp!=null){
            bestScore = sp.getInt("best_score", 0);
        }

        score = 0;
        start = false;

        initBird();
        initPipe();
        initHeart();

        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    private void initHeart() {
        sumheart = 2;
        arrHearts = new ArrayList<>();

        for (int i = 0; i < sumheart; i++){
            if (i < sumheart/2){
                this.arrHearts.add(new Heart(Constants.SCREEN_WIDTH + i * ((Constants.SCREEN_WIDTH + 400 * Constants.SCREEN_WIDTH/1080)/(sumheart/2)),
                        0, 100*Constants.SCREEN_WIDTH/1080, 100));
                this.arrHearts.get(this.arrHearts.size() - 1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.heart));
                this.arrHearts.get(this.arrHearts.size() - 1).randomY();
            }
        }
    }

    private void initPipe() {
        sumpipe = 4;
        distance = 400*Constants.SCREEN_HEIGHT/1920;
        arrPipes = new ArrayList<>();

        for (int i = 0; i < sumpipe; i++){
            if (i < sumpipe/2){
                this.arrPipes.add(new Pipe(Constants.SCREEN_WIDTH + i * ((Constants.SCREEN_WIDTH + 400 * Constants.SCREEN_WIDTH/1080)/(sumpipe/2)),
                        0, 200*Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT));
                this.arrPipes.get(this.arrPipes.size() - 1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe2));
                this.arrPipes.get(this.arrPipes.size() - 1).randomY();
            } else {
                this.arrPipes.add(new Pipe(this.arrPipes.get(i - sumpipe/2).getX(),
                        this.arrPipes.get(i - sumpipe/2).getY() + this.arrPipes.get(i - sumpipe/2).getHeight() + this.distance,
                        200*Constants.SCREEN_WIDTH/1080, Constants.SCREEN_HEIGHT));
                this.arrPipes.get(this.arrPipes.size() - 1).setBm(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe1));
            }
        }
    }

    private void initBird() {
        bird = new Bird();
        bird.setWidth(600*Constants.SCREEN_WIDTH/1080);
        bird.setHeight(150*Constants.SCREEN_HEIGHT/1080);

        bird.setX(Constants.SCREEN_WIDTH/3 - bird.getWidth()/2);
        bird.setY(Constants.SCREEN_HEIGHT/2 - bird.getHeight()/2);

        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(getResources(), R.drawable.bird1));
        arrBms.add(BitmapFactory.decodeResource(getResources(), R.drawable.bird2));
        arrBms.add(BitmapFactory.decodeResource(getResources(), R.drawable.bird3));

        bird.setArrBms(arrBms);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        bird.draw(canvas);

        if (start){
            for (int i = 0; i < sumpipe; i++){

                if (bird.getBirdRect().intersect(this.arrPipes.get(i).getPipeRect())
                        || bird.getY() + bird.getHeight() < 0
                        || bird.getY() > Constants.SCREEN_HEIGHT){
                    if (MainActivity.hearts > 0){
                        if(!isHit){
                            MainActivity.hearts--;
                            MainActivity.updateHeartBar();
                            isHit = true;
                        }
                        // Reset the hit flag after the delay
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isHit = false; // Reset hit state after 5 seconds
                            }
                        }, HIT_DELAY);
                    } else {
                        Pipe.speed = 0;
                        MainActivity.txt_score_over.setText(String.valueOf(score));
                        MainActivity.txt_best_score.setText("best score: " + bestScore);
                        MainActivity.scoreLayout.setVisibility(INVISIBLE);
                        MainActivity.heartBar.setVisibility(INVISIBLE);
                        MainActivity.rl_game_over.setVisibility(VISIBLE);
                        Heart.speed = 0;

                    }
                }

                if (this.bird.getX() + this.bird.getWidth()/2 > this.arrPipes.get(i).getX() + this.arrPipes.get(i).getWidth()/2 &&
                        this.bird.getX() + this.bird.getWidth()/2 <= this.arrPipes.get(i).getX() + this.arrPipes.get(i).getWidth()/2 + Pipe.speed &&
                        i < sumpipe/2){
                    score++;
                    MainActivity.updateScore(score);

                    if (score > bestScore){
                        bestScore = score;

                        SharedPreferences sp = context.getSharedPreferences("best_score", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("best_score", bestScore);
                        editor.apply();
                    }
                }

                if(this.arrPipes.get(i).getX() < -arrPipes.get(i).getWidth()){
                    this.arrPipes.get(i).setX(Constants.SCREEN_WIDTH);

                    if (i < sumpipe/2) {
                        this.arrPipes.get(i).randomY();
                    } else {
                        arrPipes.get(i).setY(this.arrPipes.get(i - sumpipe/2).getY() +
                                this.arrPipes.get(i - sumpipe/2).getHeight() + this.distance);
                    }
                }
                this.arrPipes.get(i).draw(canvas);
            }

            for (Heart heart : arrHearts) {
                // Only process the heart if it's active
                if (heart.isActive()) {
                    heart.draw(canvas); // Draw the heart

                    // If the bird intersects with the heart
                    if (bird.getBirdRect().intersect(heart.getHeartRect())) {
                        if (MainActivity.hearts < 3) {
                            MainActivity.hearts++; // Increase heart count
                            MainActivity.updateHeartBar(); // Update the UI
                        }

                        // Deactivate the heart when touched
                        heart.setActive(false);

                        // Schedule the heart to reappear after 2 minutes (120,000 ms)
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                heart.setX(Constants.SCREEN_WIDTH); // Reset the position
                                heart.randomY(); // Set random Y position
                                heart.setActive(true); // Make the heart active again
                            }
                        }, 25000); // 120,000 ms = 2 minutes
                    }

                    // If the heart moves off-screen, reset its position (if it's not already resetting)
                    if (heart.getX() < -heart.getWidth() && !isHeartResetting) {
                        isHeartResetting = true; // Prevent further resets while waiting

                        // Schedule a reset after 2 minutes (120,000 milliseconds)
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                heart.setX(Constants.SCREEN_WIDTH); // Reset position
                                heart.randomY(); // Set random Y position
                                isHeartResetting = false; // Allow future resets
                            }
                        }, 25000); // 30secs
                    }
                }
            }

        } else {
                bird.setDrop(10);
        }

        handler.postDelayed(r, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            bird.setDrop(-20);
        }

        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void resetGame() {
        MainActivity.scoreLayout.removeAllViews();
        score = 0;
        initBird();
        initPipe();
        initHeart();
    }

    //for debugging purposes it displays the png edges
    /*@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create a Paint object to define the rectangle's appearance
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);  // Only draw the outline
        paint.setColor(Color.RED);  // Set rectangle color (e.g., red)
        paint.setStrokeWidth(5);    // Set the thickness of the rectangle outline

        // Draw bird's bounding rectangle
        Rect birdRect = bird.getBirdRect();
        canvas.drawRect(birdRect, paint);  // Draw bird's rectangle

        // Change color for the pipes
        paint.setColor(Color.GREEN);  // Set pipe color (e.g., green)

        // Draw all the pipe rectangles
        for (int i = 0; i < arrPipes.size(); i++) {
            Rect pipeRect = arrPipes.get(i).getPipeRect();
            canvas.drawRect(pipeRect, paint);  // Draw pipe's rectangle
        }

        // Invalidate to trigger re-drawing (optional, based on your game loop)
        invalidate();
    }*/

}
