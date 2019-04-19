package com.mod.tuziyouqian.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mod.tuziyouqian.R;
import com.mod.tuziyouqian.base.App;


public class PersonItemView extends RelativeLayout {
    Drawable icon;
    String title;
    String content;
    int titleColor;
    int contentColor;
    boolean isShowLine;
    boolean show_icon;
    ImageView ivIcon;
    TextView tvTitle;
    TextView tvContent;
    View mLine;

    public PersonItemView(Context context) {
        this(context, (AttributeSet) null);
    }

    public PersonItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PersonItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.person_item);
        icon = ta.getDrawable(R.styleable.person_item_item_icon);
        title = ta.getString(R.styleable.person_item_item_title);
        content = ta.getString(R.styleable.person_item_item_content);
        titleColor = ta.getColor(R.styleable.person_item_item_titleColor, ContextCompat.getColor(context, R.color.color_333333));
        contentColor = ta.getColor(R.styleable.person_item_item_contentColor, ContextCompat.getColor(context, R.color.color_999999));
        isShowLine = ta.getBoolean(R.styleable.person_item_show_line, true);
        show_icon = ta.getBoolean(R.styleable.person_item_show_icon, true);
        LayoutInflater.from(context).inflate(R.layout.item_layout_person, this);
        ivIcon = findViewById(R.id.ivIcon);
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        mLine = findViewById(R.id.mLine);
        init();
        ta.recycle();


    }

    private void init() {
        ivIcon.setVisibility(show_icon ? VISIBLE : GONE);
        ivIcon.setImageDrawable(icon);
        tvTitle.setTextColor(titleColor);
        tvTitle.setText(title);
        tvContent.setTextColor(contentColor);
        tvContent.setText(content);
        mLine.setVisibility(isShowLine ? VISIBLE : INVISIBLE);
    }

    public void setContent(CharSequence content) {
        tvContent.setText(content);
    }

    public void setContentColor(int resColor) {
        tvContent.setTextColor(App.getResColor(resColor));
    }

    public void setTitle(CharSequence content) {
        tvTitle.setText(content);
    }

    public void setTitleColor(int resColor) {
        tvTitle.setTextColor(App.getResColor(resColor));
    }


}
