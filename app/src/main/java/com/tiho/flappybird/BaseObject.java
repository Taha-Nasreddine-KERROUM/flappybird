package com.tiho.flappybird;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class BaseObject {
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected Rect rect;
    protected Bitmap bm;

    public BaseObject() {

    }

    public BaseObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public Rect getRect() {
        return new Rect((int)this.x, (int)this.y, (int)this.x + (int)this.width, (int)this.y + (int)this.height);
    }

    public Rect getBirdRect() {
        int marginLeft = 350;   // Reduce the left side
        int marginTop = 230;     // Reduce the top side
        int marginRight = 250;  // Reduce the right side
        int marginBottom = 65;  // Reduce the bottom side

        return new Rect(
                (int)this.x + marginLeft,
                (int)this.y + marginTop,
                (int)this.x + (int)this.width - marginRight,
                (int)this.y + (int)this.height - marginBottom
        );
    }


    public Rect getPipeRect() {
        int marginLeft = 10;   // Reduce the left side
        int marginTop = 20;     // Reduce the top side
        int marginRight = 30;  // Reduce the right side
        int marginBottom = 20;  // Reduce the bottom side

        return new Rect(
                (int)this.x + marginLeft,
                (int)this.y + marginTop,
                (int)this.x + (int)this.width - marginRight,
                (int)this.y + (int)this.height - marginBottom
        );
    }

    public Rect getHeartRect() {
        return new Rect((int)this.x, (int)this.y, (int)this.x + (int)this.width, (int)this.y + (int)this.height);
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}
