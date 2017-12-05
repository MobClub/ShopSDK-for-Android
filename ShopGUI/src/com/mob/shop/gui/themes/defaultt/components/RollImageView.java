package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

public class RollImageView extends AsyncImageView {
	private int mBorderThickness = 3;
	private int mBorderColor = Color.WHITE;

	public RollImageView(Context context) {
		super(context);
	}

	public RollImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomAttributes(attrs);
	}

	public RollImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomAttributes(attrs);
	}

	private void setCustomAttributes(AttributeSet attrs) {
		mBorderThickness = 2;
		mBorderColor = Color.parseColor("#ffffff");
	}

	public void drawBorder(int color) {
		mBorderThickness = ResHelper.dipToPx(getContext(), 2);
		mBorderColor = color;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		this.measure(0, 0);

		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

		int w = getWidth(), h = getHeight();

		int radius = (w < h ? w : h) / 2 - mBorderThickness;

		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(mBorderColor);
		canvas.drawCircle(w / 2, h / 2, radius , paint);
		Bitmap roundBitmap = getCroppedBitmap(bitmap,radius);
		canvas.drawBitmap(roundBitmap, w / 2 -radius, h / 2 - radius, null);
	}

	private Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;
		if (bmp.getWidth() != diameter || bmp.getHeight() != diameter)
			scaledSrcBmp = Bitmap.createScaledBitmap(bmp, diameter, diameter, false);
		else scaledSrcBmp = bmp;
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(), Bitmap.Config
				.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(mBorderThickness, mBorderThickness, scaledSrcBmp.getWidth()-mBorderThickness, scaledSrcBmp.getHeight()-mBorderThickness);

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.GREEN);
		canvas.drawCircle(scaledSrcBmp.getWidth()/2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth()/2-mBorderThickness,paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		return output;
	}
}
