package com.mob.shop.gui.themes.defaultt.components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mob.shop.gui.R;

/**
 * Created by yjin on 2017/9/7.
 */

public class PricesTextView extends TextView {

    private PriceSelector listener;
    private OnClickListener onClickListener;
    private int normalColor = Color.parseColor("#666666");
    private int pressColor = Color.parseColor("#f7583c");
    private int currentColor ;
    private int textSize = 16;
    private Drawable normal;
    private Drawable priceHigh;
    private Drawable priceLow;
    private int currentDrawable;
    private AllProductTabSelectedConfig config;

    public PricesTextView(Context context) {
        super(context);
        init(context);
    }

    public PricesTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PricesTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        currentColor = normalColor;
        setTextColor(normalColor);
        setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        Resources resources = context.getResources();
        normal = resources.getDrawable(R.drawable.good_detail_price_normal);
        setGravity(Gravity.CENTER_HORIZONTAL);
        normal.setBounds(0,0,normal.getMinimumWidth(),normal.getMinimumHeight());
        priceHigh = resources.getDrawable(R.drawable.good_detail_price_high);
        priceHigh.setBounds(0,0,priceHigh.getMinimumWidth(),priceHigh.getMinimumHeight());
        priceLow = resources.getDrawable(R.drawable.good_detail_price_low);
        priceLow.setBounds(0,0,priceLow.getMinimumWidth(),priceLow.getMinimumHeight());
        setCompoundDrawables(null,null,normal,null);
    }

    public void setConfig(AllProductTabSelectedConfig config){
        this.config = config;
    }


    @Override
    public void setOnClickListener(OnClickListener l) {
        onClickListener = l;
        super.setOnClickListener(new MainOnClickListener());
    }

    public void setOnClickListener(PriceSelector l) {
        listener = l;
        super.setOnClickListener(new MainOnClickListener());
    }

    class MainOnClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            config.selectPrices();
            if(currentColor == normalColor){
                setTextColor(pressColor);
                currentColor = pressColor;
                setCompoundDrawables(null,null,priceHigh,null);
                currentDrawable = 1;
                if(onClickListener != null){
                    onClickListener.onClick(v);
                }
                if(listener != null){
                    listener.onAsc(v);
                }
            } else {
                if(currentDrawable == 1){
                    setCompoundDrawables(null,null,priceLow,null);
                    currentDrawable = 2;
                    if(listener != null){
                        listener.onDesc(v);
                    }
                } else {
					// 取消价格排序时回调
//                    currentColor = normalColor;
//                    setTextColor(normalColor);
//                    setCompoundDrawables(null,null,normal,null);
//                    if(listener != null){
//                        listener.onClick(v);
//                    }
					// 去除“取消价格排序”功能，只支持升序降序的切换
					setCompoundDrawables(null,null,priceHigh,null);
					currentDrawable = 1;
					if(listener != null){
						listener.onAsc(v);
					}
                }
            }
        }
    }

    /**
     * 重置色值
     */
    public void reset(){
        currentColor = normalColor;
        setTextColor(normalColor);
        setCompoundDrawables(null,null,normal,null);
    }

    public interface PriceSelector extends OnClickListener{
        void onAsc(View view);
        void onDesc(View view);
	}
}
