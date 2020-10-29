package com.cn.android.ui.dialog;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.cn.android.R;
import com.cn.android.common.MyDialogFragment;
import com.hjq.dialog.base.BaseDialog;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class PayDialog {
    public static final class Builder
            extends MyDialogFragment.Builder<CopyDialog.Builder> {

        @BindView(R.id.close)
        ImageView close;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.price_tv)
        TextView priceTv;
        @BindView(R.id.tvnum)
        TextView tvnum;
        @BindView(R.id.jia_btn)
        ImageView jiaBtn;
        @BindView(R.id.jian_btn)
        ImageView jianBtn;
        @BindView(R.id.num_tv)
        TextView numTv;
        @BindView(R.id.pay_btn)
        TextView payBtn;
        int num = 1;
        String mType;
        public OnClickListener onClickListener;


        public Builder setOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder setPrice(String money) {
            price.setText(String.format("%sEB", money));
            return this;
        }

        public Builder setPayType(String Type) {
            payBtn.setText(Type);
            mType = Type;
            return this;
        }

        public Builder(FragmentActivity activity) {
            super(activity);
            setContentView(R.layout.dialog_pay);
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM);
            setGravity(Gravity.BOTTOM);
        }

        @OnClick({R.id.close, R.id.jia_btn, R.id.jian_btn, R.id.pay_btn})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.close:
                    dismiss();
                    break;
                case R.id.jia_btn:
                    ++num;
                    numTv.setText(String.valueOf(num));
                    break;
                case R.id.jian_btn:
                    if (num > 1) {
                        --num;
                        numTv.setText(String.valueOf(num));
                    }else {
                        ++num;
                        ToastUtils.show("最少选择一个商品");
                    }
                    break;
                case R.id.pay_btn:
                    dismiss();
                    if (onClickListener != null) {
                        if (mType.equals("立即支付")) {
                            onClickListener.Pay(num, price.getText().toString().trim());
                        } else {
                            onClickListener.addCar(num, price.getText().toString().trim());
                        }
                    }
                    break;
            }
        }
    }

    public interface OnClickListener {
        void addCar(int num, String price);

        void Pay(int num, String price);
    }
}
