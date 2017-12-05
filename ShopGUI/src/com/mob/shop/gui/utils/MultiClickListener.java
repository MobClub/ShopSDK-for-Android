package com.mob.shop.gui.utils;

import android.view.View;

public abstract class MultiClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    public abstract void onMultiClick(View v);

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onMultiClick(v);
        }
    }
}
