package com.mob.shop.gui.utils;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

import java.text.SimpleDateFormat;

/**
 * Created by weishj on 2017/10/20.
 */

public class Utils {
	public static final String FORMAT_DEFAULT = "yyyy.MM.dd";
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";

	public static String long2Time(long time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(Long.valueOf(time));
	}

	public static String long2Time(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DEFAULT);
		return sdf.format(Long.valueOf(time));
	}

	/**
	 * 扩大View的触摸和点击响应范围,最大不超过其父View范围
	 *
	 * @param view
	 * @param top
	 * @param bottom
	 * @param left
	 * @param right
	 */
	public static void expandViewTouchDelegate(final View view, final int top, final int bottom, final int left, final int right) {
		((View) view.getParent()).post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				view.setEnabled(true);
				view.getHitRect(bounds);

				bounds.top -= top;
				bounds.bottom += bottom;
				bounds.left -= left;
				bounds.right += right;

				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
				if (View.class.isInstance(view.getParent())) {
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}

	/**
	 * 还原View的触摸和点击响应范围,最小不小于View自身范围
	 *
	 * @param view
	 */
	public static void restoreViewTouchDelegate(final View view) {
		((View) view.getParent()).post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				bounds.setEmpty();

				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
				if (View.class.isInstance(view.getParent())) {
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}

	public static String getCountDownTime(int time, Accuracy accuracy) {
		long nd = 24 * 60 * 60;//一天的秒数
		long nh = 60 * 60;//一小时的秒数
		long nm = 60;//一分钟的秒数
		long day = time / nd;//计算差多少天
		long hour = time % nd / nh;//计算差多少小时
		long minute = time % nd % nh / nm;//计算差多少分钟
		long second = time % nd % nh % nm;//计算差多少秒//输出结果
		String timeCount = "";
		if (day > 0) {
			timeCount += (day + "天");
		}
		if (hour > 0 && accuracy != Accuracy.DAY) {
			timeCount += (hour + "小时");
		}
		if (minute > 0 && (accuracy == Accuracy.MINUTE || accuracy == Accuracy.SECOND)) {
			timeCount += (minute + "分钟");
		}
		if (second > 0 && accuracy == Accuracy.SECOND) {
			timeCount += (second + "秒");
		}
		return timeCount;
	}

	public enum Accuracy {
		DAY,
		HOUR,
		MINUTE,
		SECOND
	}
}
