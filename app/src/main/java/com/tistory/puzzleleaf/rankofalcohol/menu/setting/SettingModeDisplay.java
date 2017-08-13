package com.tistory.puzzleleaf.rankofalcohol.menu.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.util.mode.ModePreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingModeDisplay extends AppCompatActivity {

    @BindView(R.id.setting_display_R_seek) SeekBar rSeekBar;
    @BindView(R.id.setting_display_G_seek) SeekBar gSeekBar;
    @BindView(R.id.setting_display_B_seek) SeekBar bSeekBar;
    @BindView(R.id.setting_display_palette) View palette;
    @BindView(R.id.setting_display_palette_str) TextView paletteString;
    @BindView(R.id.setting_display_text) TextView settingDisplayText;
    @BindView(R.id.setting_display_text_edit) EditText settingDisplayEdit;
    @BindView(R.id.setting_display_register) Button settingDisplayEditRegister;

    private ModePreference modePreference;

    private int rColor;
    private int gColor;
    private int bColor;
    private String myColorStr;
    private int myColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_display);
        ButterKnife.bind(this);

        seekBarInit();
        colorInit();
        registerInit();
        preferenceInit();
        displayInit();

    }

    private void displayInit(){
        settingDisplayText.setText(modePreference.getDisplayModeTextPreference());
    }
    private void colorInit(){
        rColor = rSeekBar.getProgress();
        gColor = gSeekBar.getProgress();
        bColor = bSeekBar.getProgress();
    }

    private void preferenceInit(){
        modePreference = new ModePreference(this);
    }

    private void registerInit(){
        settingDisplayEditRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingDisplayText.setText(settingDisplayEdit.getText().toString());
            }
        });
    }

    private void colorMaker(){
        myColor = Color.rgb(rColor,gColor,bColor);
        myColorStr = Integer.toHexString(myColor);

        paletteString.setText(myColorStr);
        palette.setBackgroundColor(myColor);
        settingDisplayText.setTextColor(myColor);
    }

    private void seekBarInit(){
        rSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rColor = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                colorMaker();
            }
        });

        gSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gColor = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                colorMaker();
            }
        });

        bSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bColor = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                colorMaker();
            }
        });
    }

    @OnClick(R.id.setting_display_back)
    public void settingDisplayBack(){
        onBackPressed();
    }

    @OnClick(R.id.setting_display_apply)
    public void settingDisplayApply(){
        colorMaker();
        modePreference.saveDisplayModeTextPreference(settingDisplayText.getText().toString());
        modePreference.saveDisplayModeColorPreference(myColorStr.substring(2));
        Toast.makeText(this,"등록되었습니다.",Toast.LENGTH_SHORT).show();
        finish();
    }

}
