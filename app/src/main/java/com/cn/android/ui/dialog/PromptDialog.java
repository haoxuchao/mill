package com.cn.android.ui.dialog;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.cn.android.R;
import com.cn.android.common.MyDialogFragment;
import com.hjq.dialog.base.BaseDialog;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
public final class PromptDialog {

    public static final class Builder
            extends MyDialogFragment.Builder<Builder> implements View.OnClickListener {

        private TextView confirm,prompt1,prompt2;
        public Builder setPrompt1(String prompt){
            prompt1.setText(prompt);
            return this;
        }
        public Builder setPrompt2(String prompt){
            prompt2.setText(prompt);
            prompt2.setVisibility(View.VISIBLE);
            return this;
        }
        public Builder(FragmentActivity activity) {
            super(activity);
            setContentView(R.layout.dialog_prompt);
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM);
            setGravity(Gravity.BOTTOM);
            prompt1=findViewById(R.id.prompt1);
            prompt2=findViewById(R.id.prompt2);
            confirm=findViewById(R.id.confirm);
            confirm.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            dismiss();
        }
    }
}