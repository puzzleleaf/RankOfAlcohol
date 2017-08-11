package com.tistory.puzzleleaf.rankofalcohol.ui.setting;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.tistory.puzzleleaf.rankofalcohol.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cmtyx on 2017-08-10.
 */

class SettingMessageDialog extends Dialog{


     SettingMessageDialog(@NonNull Context context) {
         super(context);
    }

    @BindView(R.id.setting_message_apply) TextView messageApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_message_dialog);
        ButterKnife.bind(this);
        this.setCanceledOnTouchOutside(false);
        messageApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @OnClick(R.id.setting_message_cancel)
    public void messageCancel(){
        this.dismiss();
    }
}
