package com.cn.android.ui.adapter;

import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.VideoBean;
import com.cn.android.utils.CustomMedia.AGVideo;
import com.cn.android.utils.CustomMedia.JZMediaExo;
import com.cn.android.utils.CustomMedia.JZMediaIjk;
import com.cn.android.widget.VideoPlayer;
import com.hjq.image.ImageLoader;

import java.util.List;

import cn.jzvd.JzvdStd;

public class VideoAdapter extends BaseQuickAdapter<VideoBean, BaseViewHolder> {
    boolean show;
    public String carnum;
    LookListener mlooklistener;

    public void setMlooklistener(LookListener mlooklistener) {
        this.mlooklistener = mlooklistener;
    }

    public VideoAdapter(@Nullable List<VideoBean> data) {
        super(R.layout.item_video,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoBean item) {
        VideoPlayer jzvd = helper.getView(R.id.jz_video);
        // 设置视频封面、url地址
        jzvd.setUp(item.getUrl(),"", JzvdStd.SCREEN_STATE_OFF, JZMediaExo.class);
        jzvd.posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        jzvd.setMlooklist(new VideoPlayer.Looklist() {
            @Override
            public void onCLook() {
                if(mlooklistener!=null){
                    mlooklistener.onLook(helper.getLayoutPosition());
                }
            }
        });
        helper.setText(R.id.user_name,item.getNickname());
        ImageView imageView=helper.getView(R.id.like_img);
        helper.setText(R.id.goods_prompt_tv,item.getShop_name());
        if(item.getIs_praise()==1){
            imageView.setImageResource(R.mipmap.aixin_red);
        }else{
            imageView.setImageResource(R.mipmap.shoucang);
        }
        helper.setVisible(R.id.car_num,show);
        helper.setText(R.id.car_num,carnum);
        helper.addOnClickListener(R.id.like_img);
        helper.addOnClickListener(R.id.guanzhu);
        helper.addOnClickListener(R.id.now_pay);
        helper.addOnClickListener(R.id.car_img);
        helper.setGone(R.id.guanzhu,item.getIs_focus()!=1);
        ImageLoader.with(helper.itemView.getContext()).load(item.getHead_img()).circle().into(helper.getView(R.id.user_img));
        helper.setText(R.id.like_num,String.valueOf(item.getPraise_num()));
        // 设置饺子视频播放器播放的模式，根据父容器大小来展示视频内容
        jzvd.setVideoImageDisplayType(JzvdStd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
        ImageLoader.with(helper.itemView.getContext()).load(item.getUrl()).into(jzvd.posterImageView);
        if (helper.getAdapterPosition() == 0 ) {
            jzvd.startVideo();
        }
    }
    public void setShowThis(boolean isShow,String num){
        show=isShow;
        carnum=num;
    }
    public interface LookListener{
        void onLook(int pos);
    }
}
