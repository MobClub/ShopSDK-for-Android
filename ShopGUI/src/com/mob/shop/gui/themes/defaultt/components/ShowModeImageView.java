package com.mob.shop.gui.themes.defaultt.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.mob.shop.gui.R;

/**
 * Created by yjin on 2017/9/7.
 */

public class ShowModeImageView extends ImageView {
    private ShowModeGridList showModeGridList;
    private int currentShowMode = 0;
    private int listView ;
    private int gridView;
    public ShowModeImageView(Context context) {
        super(context);
        init(context);
    }

    public ShowModeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShowModeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        listView = R.drawable.good_detail_list_show;
        gridView = R.drawable.good_detail_grid_show;
        setImageResource(listView);
    }

    public void setShowModeGridList(ShowModeGridList showModeGridList){
        this.showModeGridList = showModeGridList;
    }

    @Override
    public boolean callOnClick() {
        onSelectMode();
        return super.callOnClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            onSelectMode();
        }
        return super.onTouchEvent(event);
    }

    private void onSelectMode(){
		// 需要显示gridView的情况有两种：currentShowMode == listView 和 currentShowMode == 0（初始状态）
        if(currentShowMode != gridView){
            setImageResource(gridView);
            currentShowMode = gridView;
            if(showModeGridList != null)
            showModeGridList.showGridView();
        } else {
            currentShowMode = listView;
            setImageResource(listView);
            if(showModeGridList != null)
            showModeGridList.showListView();
        }
    }

    public interface ShowModeGridList{
        void showGridView();
        void showListView();
    }
}
