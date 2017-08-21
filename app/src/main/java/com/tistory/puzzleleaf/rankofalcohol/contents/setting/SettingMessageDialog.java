package com.tistory.puzzleleaf.rankofalcohol.contents.setting;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tistory.puzzleleaf.rankofalcohol.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cmtyx on 2017-08-10.
 */

class SettingMessageDialog extends Dialog{

    interface OnApplyListener{
        void onApplySelected(String ch);
    }

     SettingMessageDialog(@NonNull Context context,View.OnClickListener cancelListener, OnApplyListener onApplyListener) {
         super(context);
         this.cancelListener = cancelListener;
         this.applyListener = onApplyListener;

    }

    private View.OnClickListener cancelListener;
    private OnApplyListener applyListener;

    @BindView(R.id.setting_message_apply) TextView messageApply;
    @BindView(R.id.setting_message_cancel) TextView messageCancel;
    @BindView(R.id.setting_message_edit) EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_message_dialog);
        ButterKnife.bind(this);
        this.setCanceledOnTouchOutside(false);

        messageCancel.setOnClickListener(cancelListener);
        messageApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ch = editText.getText().toString();
                if(ch.equals("")){
                    Toast.makeText(getContext(),"채널을 입력해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                applyListener.onApplySelected(ch);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}
