package com.mob.shop.gui.themes.defaultt.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yjin on 2017/9/4.
 */

public class TabarSelectedView extends LinearLayout{
    private TextView salesCounts;
    private TextView newProducts;
    private TextView prices;
    private TextView selectButton;

    public TabarSelectedView(Context context) {
        super(context);
        initView(context);
    }

    public TabarSelectedView(Context context,AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabarSelectedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TabarSelectedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /**
     * 初始化数据
     */
    private void initView(Context context){
        setOrientation(VERTICAL);
        salesCounts = new TextView(context);
        selectButton.setText("销量");
        newProducts = new TextView(context);
        newProducts.setText("新品");
        prices = new TextView(context);
        prices.setText("价格");
        selectButton = new TextView(context);
        selectButton.setText("筛选");
        addView(salesCounts);
        addView(newProducts);
        addView(prices);
        addView(selectButton);
    }
}
