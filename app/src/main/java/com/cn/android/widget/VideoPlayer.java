package com.cn.android.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.cn.android.R;

import cn.jzvd.JzvdStd;

public class VideoPlayer extends JzvdStd {
    private Context context;
    public boolean isfirst=true;
   public Looklist mlooklist;

    public void setMlooklist(Looklist mlooklist) {
        this.mlooklist = mlooklist;
    }

    public VideoPlayer(Context context) {
        super(context);
        this.context = context;

    }

    public void init() {
        isfirst = true;
    }


    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public void onStateAutoComplete() {
        posterImageView.setVisibility(View.GONE);
        if (screen == SCREEN_FULLSCREEN) {
            onStateAutoComplete();
            setUp((String) jzDataSource.getCurrentUrl(), "", JzvdStd.SCREEN_FULLSCREEN);
        } else {
            super.onStateAutoComplete();
            setUp((String) jzDataSource.getCurrentUrl(), "", JzvdStd.STATE_NORMAL);
        }
        //循环播放
        startVideo();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
//        Log.e("sss",progress+"%");
        if (isfirst) {
//            Log.e("sss",getCurrentPositionWhenPlaying()/1000+"");
            if (getCurrentPositionWhenPlaying() / 1000 == 10) {
                mlooklist.onCLook();
                isfirst=false;
            }
            if (progress == 100 && getCurrentPositionWhenPlaying() / 1000 < 10) {
                mlooklist.onCLook();
                isfirst=false;
            }


        }
    }

    @Override
    public void startVideo() {
        isfirst=true;
        Log.e("sss","startVideo");
        super.startVideo();
    }

    @Override
    public void setUp(String url, String title, int screen) {
        super.setUp(url, title, screen);

    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_std;
    }
    public interface  Looklist{
        void onCLook();
    }


}
