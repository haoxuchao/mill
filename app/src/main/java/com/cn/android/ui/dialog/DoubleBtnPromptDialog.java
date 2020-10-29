package com.cn.android.ui.dialog;

import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.cn.android.R;
import com.cn.android.common.MyDialogFragment;

import com.hjq.dialog.base.BaseDialog;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 可进行拷贝的副本
 */
public final class DoubleBtnPromptDialog {

    public static final class Builder
            extends MyDialogFragment.Builder<Builder> implements View.OnClickListener, DialogInterface.OnKeyListener {

        private OnListener mOnListener;
        private TextView message, cancel, confirm;

        public Builder setMessage(String message) {
            this.message.setText(message);
            return this;
        }

        public Builder setCancel(String cancel) {
            this.cancel .setText(cancel) ;
            return this;
        }

        public Builder setConfirm(String confirm) {
            this.confirm.setText(confirm);
            return this;
        }

        public OnListener getmOnListener() {
            return mOnListener;
        }

        public Builder setmOnListener(OnListener mOnListener) {
            this.mOnListener = mOnListener;
            return this;
        }

        public Builder(FragmentActivity activity) {
            super(activity);
            setCanceledOnTouchOutside(false);
            setCancelable(false);
            setContentView(R.layout.dialog_double_prompt);
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM);
            setGravity(Gravity.CENTER);
            message = findViewById(R.id.message);
            confirm = findViewById(R.id.confirm);
            cancel = findViewById(R.id.cancel);
            cancel.setOnClickListener(this);
            confirm.setOnClickListener(this);
//          setOnKeyListener(this);
        }

        @Override
        public void onClick(View v) {

            if (mOnListener != null) {
                switch (v.getId()) {
                    case R.id.cancel:
                        mOnListener.onCancel(getDialog());
                        break;
                    case R.id.confirm:
                        mOnListener.onConfirm(getDialog());
                        break;
                }

            }

        }

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
            return false;
        }
    }

    public interface OnListener {


        /**
         * 点击取消时回调
         */
        void onCancel(BaseDialog dialog);

        void onConfirm(BaseDialog dialog);

    }
}