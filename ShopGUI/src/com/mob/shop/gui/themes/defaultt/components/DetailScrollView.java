package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class DetailScrollView extends ScrollView {
	float downX;
	float downY;
	private OnScrollListener onScrollListener;
	/**
	 * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
	 */
	private int lastScrollY;

	private int scrollHeight = 1500;

	public DetailScrollView(Context context) {
		super(context);
	}

	public DetailScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DetailScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = ev.getX();
				downY = ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				float disX = Math.abs(downX - ev.getX());
				float disY = Math.abs(downY - ev.getY());
				if (downY + getScrollY() > scrollHeight) {
					if (disY >= 20) {
						return true;
					} else {
						return false;
					}
				} else {
					if (disX > 3) {
						return false;
					} else {
						if (disY >= 10) {
							return true;
						} else {
							return false;
						}
					}
				}
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 设置滚动接口
	 *
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}


	/**
	 * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
	 */
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int scrollY = DetailScrollView.this.getScrollY();
			//此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
			if (lastScrollY != scrollY) {
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 5);
			}
			if (onScrollListener != null) {
				onScrollListener.onScroll(scrollY);
			}
		}
	};

	/**
	 * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
	 * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
	 * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理
	 * MyScrollView滑动的距离
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (onScrollListener != null) {
			onScrollListener.onScroll(lastScrollY = this.getScrollY());
		}
		switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				handler.sendMessageDelayed(handler.obtainMessage(), 5);
				break;
		}
		return super.onTouchEvent(ev);
	}

	public void setScrollHeight(int scrollHeight) {
		this.scrollHeight = scrollHeight;
	}

	/**
	 * 滚动的回调接口
	 *
	 * @author xiaanming
	 */
	public interface OnScrollListener {
		/**
		 * 回调方法， 返回MyScrollView滑动的Y方向距离
		 *
		 * @param scrollY 、
		 */
		void onScroll(int scrollY);
	}
}
