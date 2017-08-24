package com.tistory.puzzleleaf.rankofalcohol.contents.battle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tistory.puzzleleaf.rankofalcohol.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BattleActivity extends AppCompatActivity implements SensorEventListener {

    private static final int ANIMATION = 100;
    private int progress =0;
    private int oldProgress =0;

    private boolean progressLock = false;

    //sensor
    private SensorManager mSensorManager;
    private Sensor accSensor;
    private Sensor mGyroscope;
    int accelXValue;
    int accelYValue;
    int accelZValue;
    int gyroX;
    int gyroY;
    int gyroZ;

    private int progressNum = 0;

    //타이머
    long baseTime;
    long pauseTime;


    @BindView(R.id.progress) View screenLockProgress;
    @BindView(R.id.battle_timer) TextView battleTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        overridePendingTransition(0,0);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        screenLockProgress.getBackground().setLevel(0);
        sensorInit();

    }

    private void sensorInit(){
        //센서 매니저 얻기
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //엑셀러로미터 센서(가속)
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }


    //센서값 얻어오기
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroX = Math.round(event.values[0] * 1000);
            gyroY = Math.round(event.values[1] * 1000);
            gyroZ = Math.round(event.values[2] * 1000);
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelXValue = (int) event.values[0];
            accelYValue = (int) event.values[1];
            accelZValue = (int) event.values[2];


            int sum = Math.abs(accelXValue) + Math.abs(accelYValue) + Math.abs(accelZValue);
            if(sum>30){
                updataProgress(sum+Math.abs(gyroX)/1000 + Math.abs(gyroY)/1000 + Math.abs(gyroZ)/1000);
                Log.d("qwe","흔드는 중");
                Log.d("qwe",String.valueOf(Math.abs(gyroX)) + " " + String.valueOf(Math.abs(gyroY)) + " " + String.valueOf(Math.abs(gyroZ)));
                Log.d("qwe", String.valueOf(progress) + " " + String.valueOf(oldProgress) + " " + String.valueOf(progressLock));
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mGyroscope,SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, accSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
        mSensorManager.unregisterListener(this);

    }


    @OnClick(R.id.battle_start)
    public void battleStart(){
        baseTime = SystemClock.elapsedRealtime();
        timerHandler.sendEmptyMessage(0);
    }


    private Handler timerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            battleTimer.setText(getTime());
            timerHandler.sendEmptyMessage(0);
        }
    };

    private String getTime(){
        long now = SystemClock.elapsedRealtime();
        long outTime = now - baseTime;
        String easy_outTime = String.format("%02d:%02d:%02d",outTime/1000/60,(outTime/1000)%60,(outTime%1000)/10);
        return easy_outTime;
    }

    synchronized private void updataProgress(int val){
        if(progressLock){
            return;
        }
        progressLock = true;
        Log.d("qwe","lock");
        progressNum = val;
        progress = screenLockProgress.getBackground().getLevel();
        oldProgress = progress;
        handler.sendEmptyMessageDelayed(ANIMATION, 100);
    }


    private Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress+=progressNum;
            if(oldProgress<10000) {
                Log.d("qwe", String.valueOf(progress) + " " + String.valueOf(oldProgress));
                if (progress <= oldProgress + progressNum) {
                    screenLockProgress.getBackground().setLevel(progress);
                    updateHandler.sendEmptyMessageDelayed(ANIMATION, 1);
                    Log.d("qwe","증가!");
                }
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ANIMATION:
                    if(oldProgress>=10000){
                        timerHandler.removeMessages(0);
                        screenLockProgress.getBackground().setLevel(0);
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressLock = false;
                            }
                        },300);
                    }
                    else if (progress <= oldProgress+progressNum) {
                        updateHandler.sendEmptyMessage(ANIMATION);
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressLock = false;
                                Log.d("qwe","unlock");
                            }
                        },100);
                    }
                    break;
            }

        }
    };
}
