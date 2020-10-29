package com.cn.android.ui.dialog;

import android.app.Dialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.cn.android.R;
import com.cn.android.common.MyDialogFragment;
import com.hjq.dialog.base.BaseDialog;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 可进行拷贝的副本
 */
public final class WayBillDialog {

    public static final class Builder
            extends MyDialogFragment.Builder<Builder> {

        @BindView(R.id.way_bill_tv)
        TextView wayBillTv;
        @BindView(R.id.edit_way_bill)
        EditText editWayBill;
        @BindView(R.id.cancel)
        TextView cancel;
        @BindView(R.id.confirm)
        TextView confirm;
        OnListener onListener;

        public Builder setOnListener(OnListener onListener) {
            this.onListener = onListener;
            return this;
        }

        public Builder setWayBillTv(String wayBillTv) {
            if(wayBillTv.contains("数量")){
                editWayBill.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
            this.wayBillTv.setText(wayBillTv);
            return this;
        }

        public Builder setCancel(String cancel) {
            this.cancel.setText(cancel);
            return this;
        }

        public Builder setConfirm(String confirm) {
            this.confirm .setText(confirm);
            return this;
        }

        public Builder(FragmentActivity activity) {
            super(activity);
            setContentView(R.layout.dialog_waybill);
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM);
            setGravity(Gravity.CENTER);
        }

        @OnClick({R.id.cancel, R.id.confirm})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.cancel:
                    dismiss();
                    break;
                case R.id.confirm:
                    dismiss();
                    if(onListener!=null){
                        if(TextUtils.isEmpty(editWayBill.getText().toString().trim())){
                            ToastUtils.show("请输入");
                            return;
                        }
                        onListener.onConfirm(editWayBill.getText().toString().trim());
                    }
                    break;
            }
        }
    }
    public interface  OnListener{
        void onConfirm(String content);
    }
}