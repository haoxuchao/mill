package com.cn.android.ui.dialog;

import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.cn.android.R;
import com.cn.android.common.MyDialogFragment;
import com.hjq.dialog.base.BaseDialog;
import com.hjq.image.ImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 可进行拷贝的副本
 */
public final class BigImgDialog {

    public static final class Builder
            extends MyDialogFragment.Builder<Builder> {

        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.close)
        TextView close;

        public Builder setImg(String img) {
            ImageLoader.with(getContext()).load(img).into(this.img);
            return this;
        }

        public Builder(FragmentActivity activity) {
            super(activity);

            setContentView(R.layout.dialog_big_img);
            setCanceledOnTouchOutside(true);
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM);
            setGravity(Gravity.CENTER);
        }

        @OnClick(R.id.close)
        public void onViewClicked() {
            dismiss();
        }
    }
}