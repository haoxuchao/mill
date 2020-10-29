package com.cn.android.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.aware.DiscoverySession;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.common.MyActivity;
import com.cn.android.common.MyApplication;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.hjq.bar.TitleBar;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteActivity extends MyActivity implements PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.invite_code_peompt)
    ImageView inviteCodePeompt;
    @BindView(R.id.qr_bg)
    ImageView qrBg;
    @BindView(R.id.qr_img)
    ImageView qrImg;
    @BindView(R.id.invite_code)
    TextView inviteCode;
    @BindView(R.id.copy)
    TextView copy;
    private Bitmap bitmap;
    PublicInterfaceePresenetr presenetr;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2000:
                    savePicture(bitmap, "fenxiang.png");
                    break;
            }
        }
    };
    private String img;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite;
    }

    @Override
    protected void initView() {
        inviteCode.setText(String.format("邀请码：%s",UserBean().getId()));
        presenetr=new PublicInterfaceePresenetr(this);
        qrImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                returnBitMap(img);
                return false;
            }
        });
    }

    public Bitmap returnBitMap(final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    Message message = new Message();
                    message.what = 2000;
                    mHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return bitmap;
    }

    /**
     * 保存图片
     *
     * @param bm
     * @param fileName
     */
    File myCaptureFile;

    public void savePicture(Bitmap bm, String fileName) {
        Log.i("xing", "savePicture: ------------------------");
        if (null == bm) {
            Log.i("xing", "savePicture: ------------------图片为空------");
            return;
        }
        //建立指定文件夹
        File foder = new File(Environment.getExternalStorageDirectory(), "zzp_sale");
        if (!foder.exists()) {
            foder.mkdirs();
        }
        myCaptureFile = new File(foder, fileName);
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            //压缩保存到本地
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), myCaptureFile.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + myCaptureFile.getPath())));
        ToastUtils.show("保存成功!");

    }

    @Override
    protected void initData() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectPoster, Constant.selectPoster);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.copy)
    public void onViewClicked() {

        MyApplication.copy(inviteCode.getText().toString().trim().replace("邀请码：",""), getActivity());
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map=new HashMap<>();
        map.put("userid",UserBean().getId());
        return map;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        img=data;
        ImageLoader.with(getActivity()).load(img).into(qrImg);
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
