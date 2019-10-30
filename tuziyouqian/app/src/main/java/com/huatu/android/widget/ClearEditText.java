package com.huatu.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.huatu.android.R;

/**
 * Created by MrFu on 15/7/30.
 */
public class ClearEditText extends AppCompatEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearTextIcon;
    private Drawable mMoreTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;
    private int i;
    //自定义的删除按钮
    private Drawable mClearIcon;
    private boolean isShowClear, isAlwaysShow;
    private int forcusColor;
    private int normalColor;
    private float clearIcon_marginright;

    ;

    public ClearEditText(final Context context) {
        super(context);
        //init(context);
    }


    public ClearEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyClearEditText);
        mClearIcon = ta.getDrawable(R.styleable.MyClearEditText_clear_icon);
        isShowClear = ta.getBoolean(R.styleable.MyClearEditText_ishow_clear_icon, true);
        forcusColor = ta.getColor(R.styleable.MyClearEditText_focustextcolor, getResources().getColor(R.color.login_text_70));
        normalColor = ta.getColor(R.styleable.MyClearEditText_normaltextcolor, getResources().getColor(R.color.unfocus));
        clearIcon_marginright = ta.getDimension(R.styleable.MyClearEditText_clear_icon_marginright, getResources().getDimension(R.dimen.DIMEN_20_0PX));
        //清除的图片是否一直都是显示的   true是一直都显示（默认的）   false是只有在输入内容的时候才显示   如果isShowClear这个属性必须设置成true的情况下
        isAlwaysShow = ta.getBoolean(R.styleable.MyClearEditText_clear_icon_isalwaysshow, true);
        init(context);
        ta.recycle();


    }


    public ClearEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(final Context context) {

        if (isShowClear) {
            i = (int) clearIcon_marginright;
            final Drawable drawable;
            if (mClearIcon != null) {
                drawable = mClearIcon;
            } else {
                drawable = ContextCompat.getDrawable(context, R.mipmap.ic_delete);
            }
            final Drawable wrappedDrawable = DrawableCompat.wrap(drawable); //Wrap the drawable so that it can be tinted pre Lollipop
            //  DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());// by zhangzhiqiang

            mClearTextIcon = wrappedDrawable;

            mClearTextIcon.setBounds(-i, 0, mClearTextIcon.getIntrinsicHeight() - i, mClearTextIcon.getIntrinsicHeight());
            setClearIconVisible(false);
        }
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }

    public OnFocusChangeListener getFocusListener() {
        return mOnFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (isShowClear) {
                if (isAlwaysShow) {
                    setClearIconVisible(getText().length() > 0);
                    // setClearIconVisible(true);  //add zhang
                }
            } else {
                if (isShowClear) {
                    setClearIconVisible(getText().length() > 0);
                } else {
                    setClearIconVisible(false);
                }
            }
        } else {

            if (isShowClear) { //add zhang
                setClearIconVisible(false);

            }


        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (isShowClear) {
            final int x = (int) motionEvent.getX() + i;
            if (x > getWidth()) {
                return true;
            }
            if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    setError(null);
                    setText(null);
                }
                return true;
            }
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(view, motionEvent);
    }

    @Override
    public final void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (isFocused()) {
            if (isShowClear) {

                if (isAlwaysShow) {
                    //setClearIconVisible(true); //add zhang  一直显示
                    setClearIconVisible(getText().length() > 0);   //有内容了才显示
                } else {
                    setClearIconVisible(text.length() > 0);

                }

            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setClearIconVisible(final boolean visible) {
        if (isShowClear) {
            mClearTextIcon.setVisible(visible, false);
            final Drawable[] compoundDrawables = getCompoundDrawables();
            setCompoundDrawablePadding(20);
            setCompoundDrawables(
                    compoundDrawables[0],
                    compoundDrawables[1],
                    visible ? mClearTextIcon : null,
                    compoundDrawables[3]);
        }
    }

}
