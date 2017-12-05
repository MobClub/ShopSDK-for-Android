package com.mob.shop.gui.themes.defaultt.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * Created by yjin on 2017/9/7.
 */

public class NormalTextView extends TextView {
    private OnClickListener listener;
    private int normalColor = Color.parseColor("#666666");
    private int pressColor = Color.parseColor("#f7583c");
    private int currentColor ;
    private int textSize = 16;

    public NormalTextView(Context context) {
        super(context);
        init(context);
    }

    public NormalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NormalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        currentColor = normalColor;
        setTextColor(normalColor);
        setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.listener = l;
        super.setOnClickListener(new MainOnClickListener());
    }

    class MainOnClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            if(currentColor == normalColor){
                setTextColor(pressColor);
                currentColor = pressColor;
            }
			/**
			 * NormalT extView只变色，不重置颜色，重置交由外界通过调用reset()方法灵活控制
			 */
//            else {
//                currentColor = normalColor;
//                setTextColor(normalColor);
//            }
            listener.onClick(v);
        }
    }

    public void reset(){
        currentColor = normalColor;
        setTextColor(normalColor);
    }

}
