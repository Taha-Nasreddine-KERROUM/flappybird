package com.tiho.flappybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Heart extends BaseObject{
    public static int speed;
    public static int range = 900;
    private boolean visible = true;
    private boolean active = true;

    public Heart(float x, float y, int width, int height){
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

        if(range > 1500){
            range = 900;
        }

        this.y = Constants.SCREEN_HEIGHT/4 - this.height + r.nextInt( range + 1);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    // Getter and setter for active flag
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setBm(Bitmap bm) {
        this.bm = Bitmap.createScaledBitmap(bm, this.width, this.height, true);
    }
}
