package com.tistory.puzzleleaf.rankofalcohol.contents.battle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.contents.battle.dialog.BattleResultDialog;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.BattleObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BattleActivity extends AppCompatActivity implements SensorEventListener {

    private static final int ANIMATION = 100;
    private int progress =0;
    private int oldProgress =0;

    private boolean progressLock = false;

    private BattleResultDialog battleResultDialog;

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

    //진동
    private Vibrator vibrator;

    //타이머
    private long baseTime;
    private long outTime;

    //유저 데이터
    private BattleObject battleObject;
    private String resultChannel;
    private String resultKey;
    private boolean isBattlePlay = false;

    @BindView(R.id.progress) View screenLockProgress;
    @BindView(R.id.battle_timer) TextView battleTimer;
    @BindView(R.id.battle_channel) EditText battleChannel;
    @BindView(R.id.battle_name) EditText battleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        screenLockProgress.getBackground().setLevel(1);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
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
                updataProgress(sum+Math.abs(gyroX)/800 + Math.abs(gyroY)/800 + Math.abs(gyroZ)/800);
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
    }


    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void battleChannelSetting(){
        if(!checkNameChannel()){
            return;
        }
        isBattlePlay = true;

        mSensorManager.registerListener(this, mGyroscope,SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, accSensor,SensorManager.SENSOR_DELAY_FASTEST);

        battleObject = new BattleObject(battleName.getText().toString());
        resultChannel = battleChannel.getText().toString();
        resultKey = FbDataBase.database.getReference().child("Battle").child(resultChannel).push().getKey();
        FbDataBase.database.getReference().child("Battle").child(resultChannel).child(resultKey).setValue(battleObject);

        baseTime = SystemClock.elapsedRealtime();
        battleResultDialog = new BattleResultDialog(this,resultChannel);
        timerHandler.sendEmptyMessage(0);
    }

    private boolean checkNameChannel(){
        if(battleName.length()>0 && battleChannel.length()>0){
            return true;
        }else {
            Toast.makeText(this,"닉네임과 채널을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void battleResultSetting(){
        battleObject.setOutTime(outTime);
        vibrator.vibrate(1000);
        FbDataBase.database.getReference().child("Battle").child(resultChannel).child(resultKey).setValue(battleObject);
    }

    @OnClick(R.id.battle_start)
    public void battleStart(){
        if(isBattlePlay){
            Toast.makeText(this,"한 번만 플레이 가능합니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        battleChannelSetting();
    }

    @OnClick(R.id.battle_result)
    public void battleResult(){
        if(battleResultDialog==null){
            Toast.makeText(this,"플레이 이후에 결과를 확인해 주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        battleResultDialog.show();
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
        outTime = now - baseTime;
        return String.format("%02d:%02d:%02d",outTime/1000/60,(outTime/1000)%60,(outTime%1000)/10);
    }

    synchronized private void updataProgress(int val){
        if(progressLock){
            return;
        }
        progressLock = true;
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
                        battleResultSetting();
                        Log.d("qwe", String.valueOf(outTime));
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

    @OnClick(R.id.battle_back)
    public void battleBackClick(){
        finish();
    }
}
