package com.mod.tuziyouqian.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ZhouZhu on 2016/1/4.
 */
public class ThumbnailView extends AppCompatImageView {

    public ThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //在按下事件中设置滤镜
                setFilter();
                break;
            case MotionEvent.ACTION_UP:
                //由于捕获了Touch事件，需要手动触发Click事件
//                performClick();
            case MotionEvent.ACTION_CANCEL:
                //在CANCEL和UP事件中清除滤镜
                removeFilter();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置滤镜
     */
    private void setFilter() {
        //先获取设置的src图片
        Drawable drawable = getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            //设置滤镜
            drawable.setColorFilter(Color.parseColor("#CBCBCB"), PorterDuff.Mode.MULTIPLY);
        }
    }

    /**
     * 清除滤镜
     */
    private void removeFilter() {
        //先获取设置的src图片
        Drawable drawable = getDrawable();
        //当src图片为Null，获取背景图片
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            //清除滤镜
            drawable.clearColorFilter();
        }
    }


}