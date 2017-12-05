package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.mob.tools.gui.AsyncImageView;

public class ShopImageView extends AsyncImageView {

	public ShopImageView(Context context) {
		super(context);
	}

	public ShopImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShopImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			if (this.getDrawable() != null) {
				this.getDrawable().draw(canvas);
			}
		} catch (Throwable t) {
			super.onDraw(canvas);
		}

	}
}
