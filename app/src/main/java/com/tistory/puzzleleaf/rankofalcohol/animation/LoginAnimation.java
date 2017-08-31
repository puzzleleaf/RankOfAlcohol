package com.tistory.puzzleleaf.rankofalcohol.animation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.util.Log;
import android.view.SurfaceHolder;

import com.tistory.puzzleleaf.rankofalcohol.LoginActivity;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by cmtyx on 2017-04-08.
 */

public class LoginAnimation extends PApplet {

    int starLimit = 200;
    int moonSize;
    int moonAlphSize;
    int moonShadowSize;

    //그림자 이동 관련
    float location = 0;
    float moveSpeed;
    float startShadow;
    float endShadow;


    Star myStar[];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public void settings() {
        super.settings();
        fullScreen();
    }

    public void setup()
    {
        myStar = new Star[starLimit];

        for(int i=0;i<starLimit;i++){
            myStar[i] = new Star();
        }

        moonSize = width/4;
        moonAlphSize = moonSize+30;
        moonShadowSize = moonSize+100;

        startShadow = moonSize+width/2+100;
        endShadow = width/2-moonSize-100;
        location = startShadow;
        moveSpeed = width/800;

    }

    public void draw()
    {
        background(7,20,29,100);
        drawMoon();
        drawShadow(location,height/4);
        location-=moveSpeed;

        if(location<endShadow){
            location = startShadow;
        }

        for(int i=0;i<starLimit;i++){
            myStar[i].shine();
            myStar[i].display();
        }

    }


    void drawMoon(){
        for(int i=1; i<50; i++) {
            fill(245,10);
            ellipse(width/2, height/4,moonAlphSize-i*5,moonAlphSize+30-i*5);
            ellipse(width/2, height/4,moonSize,moonSize);
        }
    }

    void drawShadow(float x, float y) {
        for(int i=1; i<30; i++) {
            fill(7,20,29,100);
            ellipse(x, y,moonShadowSize-i*5,moonShadowSize-i*5);
        }
    }

    //별 객체
    class Star
    {
        PVector location;
        float brightness;
        float starSize;
        float light;
        float starMinSize = 2;
        float starMaxSize = 5;

        Star()
        {
            noStroke();
            location = new PVector(random(width),random(height));
            starSize = random(starMinSize,starMaxSize);
            light = random(3,10);
            brightness =30;
        }

        void display()
        {
            for(int i=0;i<starSize;i++)
            {
                fill(255,brightness-i*5);
                ellipse(location.x,location.y,i,i);
            }
        }

        //빛나는 효과
        void shine()
        {
            if(brightness>255){
                brightness =255;
                light = random(-3,-10);

            }
            if(brightness<0)
            {
                location = new PVector(random(width),random(height));
                light = random(3,10);
                starSize = random(starMinSize,starMaxSize);
                brightness =0 ;
            }
            brightness +=light;

        }

    }


}
