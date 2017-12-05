package com.mob.shop.gui.themes.defaultt.components.searchbar;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.Transition;

/**
 * Created by yjin on 2017/9/19.
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public class FadeInTransition extends AutoTransition {
	private static final int FADE_IN_DURATION = 200;

	private FadeInTransition() {

	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static Transition createTransition() {
		AutoTransition transition = new AutoTransition();
		transition.setDuration(FADE_IN_DURATION);
		return transition;
	}
}
