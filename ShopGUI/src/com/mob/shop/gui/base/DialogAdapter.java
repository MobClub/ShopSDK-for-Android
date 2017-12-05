package com.mob.shop.gui.base;

import android.os.Bundle;

public abstract class DialogAdapter<D extends Dialog<D>> {

	public void init(D dialog) {

	}

	public void onCreate(D dialog, Bundle savedInstanceState) {

	}

	public void onDismiss(D dialog, Runnable afterDismiss) {
		afterDismiss.run();
	}

}
