package com.tistory.puzzleleaf.rankofalcohol.animation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.MouseEvent;

/**
 * Created by cmtyx on 2017-04-08.
 */

public class MainAnimation extends PApplet {

    int maxStar = 500;
    int starLimit = 300;
    int moonSize;
    int moonAlphSize;
    int moonShadowSize;

    //그림자 이동 관련
    float location = 0;
    float moveSpeed;
    float startShadow;
    float endShadow;

    PFont font;

    Star myStar[];
    String [] test ={"달과 별 그리고 술","MOOOON","Hello"};
    int my = 0;

    ArrayList<Point> myArr;


    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastReceiver = new BroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("Star");
        getActivity().registerReceiver(broadcastReceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void settings() {
        super.settings();
        fullScreen();


    }

    public void setup()
    {
        myArr = new ArrayList(); // 글자
        myStar = new Star[maxStar];

        font = loadFont("NanumBarunpen-Bold-50.vlw");
        for(int i=0;i<maxStar;i++){
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
       // drawMoon();
       // drawShadow(location,height/4);
       // location-=moveSpeed;

        if(location<endShadow){
            location = startShadow;
        }

        for(int i=0;i<starLimit;i++){
            myStar[i].shine();
            myStar[i].display();
        }
        if(myArr.size()>0) {
            for (int i = 0; i < myArr.size(); i++) {
                try {
                    myArr.get(i).update();
                    myArr.get(i).display();
                }catch (Exception e){

                }
            }
        }

    }

    @Override
    public void mousePressed() {
        super.mousePressed();
        if(mouseY<height/2) {
            moveSpeed += 1;
        }else{
            if(starLimit<maxStar-1) {
                starLimit += 50;
            }
        }
        myArr.clear();
        setText(test[my++%3]);
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

    void setText(String txt)
    {
        PGraphics pg = createGraphics(width, height);
        pg.beginDraw();
        pg.fill(0);
        pg.textAlign(CENTER);
        pg.textFont(font,width/8);
        pg.text(txt,width/2,height/2);
        pg.endDraw();
        pg.loadPixels();

        int val = 8;

        for(int h=0;h<height;h+=val) {
            for (int w = 0; w < width; w += val) {
                if (pg.pixels[width * h + w] != 0) {
                    Point temp = new Point(w, h);
                    myArr.add(temp);
                }
            }
        }
        pg.bitmap.recycle();
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

    class Point
    {
        PVector pos;
        PVector des;
        PVector vel;
        PVector acc;

        float limit = 1;
        int speed = (int)random(8,16);
        float size = random(3,7);

        Point(float x, float y)
        {
            pos = new PVector(random(0,width),random(0,height));
            des = new PVector(x,y);
            vel = new PVector(0,0);
            acc = new PVector(0,0);
        }

        void display()
        {
            fill(255);
            ellipse(pos.x,pos.y,size,size);
        }

        void update()
        {
            float distance = dist(pos.x,pos.y,des.x,des.y);
            float proximityMult = (float) 1.0;

            if(distance<50)
            {
                proximityMult = distance/50;
            }

            //벡터의 방향과 크기를 정해준다.
            PVector toward = new PVector(this.des.x,this.des.y);
            toward.sub(pos);
            toward.normalize();
            toward.mult(proximityMult*speed);


            PVector steer = new PVector(toward.x,toward.y);
            steer.sub(vel);


            acc.add(steer);
            vel.add(acc);
            pos.add(vel);

            acc.mult(0);
        }

        boolean isEnd()
        {
            float distance = dist(pos.x,pos.y,des.x,des.y);
            if(distance <5)
                return true;
            else
                return false;
        }
    }


    private class BroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("Star")){
                myArr.clear();
                String text = intent.getStringExtra("star");
                setText(text);
            }
        }
    }



}
