package com.tistory.puzzleleaf.rankofalcohol.contents.setting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tistory.puzzleleaf.rankofalcohol.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cmtyx on 2017-08-10.
 */

public class SettingInfoDialog extends Dialog{

    private int settingInfoCase;

     public SettingInfoDialog(@NonNull Context context) {
         super(context);
    }

    public void setSettingInfoCase(int num){
        this.settingInfoCase = num;
    }

    @BindView(R.id.setting_info_animation) ImageView settingInfoAnimation;
    @BindView(R.id.setting_info_text) TextView settingInfoText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info_dialog);
        ButterKnife.bind(this);
        this.setCanceledOnTouchOutside(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadInfoContents(settingInfoCase);

    }

    private void loadInfoContents(int num){
        switch (num){
            case 1:
                Glide.with(getContext()).load(R.raw.main).into(settingInfoAnimation);
                settingInfoText.setText("메인 화면이 뭔가 심심한가요?\n" +
                        "리뷰를 남기면 메인화면이 업데이트 됩니다.");
                break;
            case 2:
                Glide.with(getContext()).load(R.raw.message).into(settingInfoAnimation);
                settingInfoText.setText("누군가와 익명으로 소통하고 싶다면?\n" +
                        "채널을 공유하면 가능합니다!");
                break;
            case 3:
                Glide.with(getContext()).load(R.raw.game).into(settingInfoAnimation);
                settingInfoText.setText("술자리에서 누군가 주인공을 뽑는다면?\n" +
                        "간단하게 즐길 수 있는 미니게임 입니다.");
                break;
            case 4:
                Glide.with(getContext()).load(R.raw.display).into(settingInfoAnimation);
                settingInfoText.setText("강렬하게 메시지를 전달하고 싶다면?\n" +
                        "전광판 모드를 한번 써보세요!");
                break;
        }
    }

    @OnClick(R.id.setting_info_close)
    public void settingInfoClose(){
        this.dismiss();
    }
}
