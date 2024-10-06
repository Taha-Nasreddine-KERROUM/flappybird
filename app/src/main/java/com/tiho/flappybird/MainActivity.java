package com.tiho.flappybird;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {
    public static LinearLayout scoreLayout, heartBar;
    public static TextView txt_score_over, txt_best_score;
    public static RelativeLayout rl_game_over;
    public static Button btn_start;
    public static ImageView img_start;
    public GameView game_view;
    public static int hearts = 3;
    public static ImageView[] heartViews;


    private static int[] numberImages = {
            R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven,
            R.drawable.eight, R.drawable.nine
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSystemUI();

        EdgeToEdge.enable(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the locale to English
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        DisplayMetrics dw = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dw);
        Constants.SCREEN_WIDTH = dw.widthPixels;
        Constants.SCREEN_HEIGHT = dw.heightPixels;

        setContentView(R.layout.activity_main);

        // Initialize UI elements
        scoreLayout = findViewById(R.id.score_layout);
        txt_best_score = findViewById(R.id.txt_best_score);
        txt_score_over = findViewById(R.id.txt_score_over);
        rl_game_over = findViewById(R.id.rl_game_over);
        btn_start = findViewById(R.id.btn_start);
        game_view = findViewById(R.id.game_view);
        img_start = findViewById(R.id.img_start);
        heartBar = findViewById(R.id.heartBar);

        heartViews = new ImageView[]{
                findViewById(R.id.heart1),
                findViewById(R.id.heart2),
                findViewById(R.id.heart3)
        };
        updateHeartBar();

        heartBar.setVisibility(View.INVISIBLE);


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_view.setStart(true);
                scoreLayout.setVisibility(View.VISIBLE);
                heartBar.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.INVISIBLE);
                img_start.setVisibility(View.INVISIBLE);
            }
        });

        rl_game_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hearts = 3;
                updateHeartBar();

                btn_start.setVisibility(View.VISIBLE);
                img_start.setVisibility(View.VISIBLE);
                rl_game_over.setVisibility(View.INVISIBLE);

                game_view.setStart(false);
                game_view.resetGame();
            }
        });
    }

    public static void updateHeartBar() {
        for (int i = 0; i < heartViews.length; i++) {
            if (i < hearts) {
                heartViews[i].setImageResource(R.drawable.heart);
            } else {
                heartViews[i].setImageResource(R.drawable.noheart);
            }
        }
    }


    // Method to hide system UI for fullscreen
    private void hideSystemUI() {
        // Make sure the app is in fullscreen mode by hiding both status and navigation bars
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Re-apply fullscreen mode when the app regains focus
        if (hasFocus) {
            hideSystemUI();
        }
    }

    public static void updateScore(int score) {
        scoreLayout.removeAllViews();  // Clear previous views
        String scoreString = String.valueOf(score);  // Convert score to string

        for (int i = 0; i < scoreString.length(); i++) {
            int digit = Character.getNumericValue(scoreString.charAt(i));  // Get each digit
            ImageView imageView = new ImageView(scoreLayout.getContext());  // Create new ImageView
            imageView.setImageResource(numberImages[digit]);  // Set the corresponding image

            // Adjust the size of the image (set a smaller width and height)
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    200,  // width in pixels (adjust this as needed)
                    200   // height in pixels (adjust this as needed)
            );
            imageView.setLayoutParams(layoutParams);  // Apply the layout params
            scoreLayout.addView(imageView);  // Add the ImageView to the layout
        }
    }

}