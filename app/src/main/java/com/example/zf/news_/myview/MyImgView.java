package com.example.zf.news_.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;

import com.example.zf.news_.util.ImgHttpUtil;

/**
 * Created by Fy on 2018/2/20.
 */

public class MyImgView extends AppCompatImageView {

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            MyImgView.this.setImageBitmap(bitmap);
        }
    };

    public MyImgView(Context context) {
        super(context);
    }

    public MyImgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageUrl(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = ImgHttpUtil.loadImg(url);
                Message message = Message.obtain();
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }).start();
    }
}
