package com.tiho.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Pipe extends BaseObject{
    public static int speed;
    public static int range = 400;

    public Pipe(float x, float y, int width, int height){
        super(x, y, width, height);
        speed = 10*Constants.SCREEN_WIDTH/1080;
    }

    public void draw(Canvas canvas){
        this.x -= speed;
        canvas.drawBitmap(this.getBm(), this.x, this.y, null);
    }

    public void randomY(){
        Random r = new Random();
        range++;

        if(range > 1000){
            range = 400;
        }

        this.y = Constants.SCREEN_HEIGHT/3 - this.height + r.nextInt( range + 1);
    }

    @Override
    public void setBm(Bitmap bm) {
        this.bm = Bitmap.createScaledBitmap(bm, this.width, this.height, true);
    }
}