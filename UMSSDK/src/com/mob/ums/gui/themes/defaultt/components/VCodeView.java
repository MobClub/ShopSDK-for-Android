package com.mob.ums.gui.themes.defaultt.components;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.jimu.gui.Page;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.OKCancelDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;

import java.util.HashMap;

public abstract class VCodeView extends LinearLayout {
	private Page<?> page;
	private EditText etCode;
	private TextView tvSend;
	private boolean stopCountDown;
	private ProgressDialog pd;

	public VCodeView(Page<?> page) {
		super(page.getContext());
		this.page = page;
		init(getContext());
		tvSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				askToSend();
			}
		});
	}

	private void init(Context context) {
		setOrientation(VERTICAL);

		LinearLayout ll = new LinearLayout(context);
		int dp53 = ResHelper.dipToPx(context, 53);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp53);
		addView(ll, lp);

		ImageView iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER_INSIDE);
		iv.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_vcode"));
		int dp24 = ResHelper.dipToPx(context, 24);
		lp = new LayoutParams(dp24, dp24);
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(iv, lp);

		etCode = new EditText(context);
		etCode.setPadding(0, 0, 0, 0);
		etCode.setBackground(null);
		etCode.setSingleLine();
		etCode.setTextColor(0xff3b3947);
		etCode.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		etCode.setHintTextColor(0xff969696);
		etCode.setHint(ResHelper.getStringRes(context, "umssdk_default_vcode"));
		etCode.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		etCode.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.leftMargin = dp24;
		lp.rightMargin = dp24;
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(etCode, lp);

		View vSep = new View(context);
		vSep.setBackgroundColor(0xffeaeaea);
		lp = new LayoutParams(1, ResHelper.dipToPx(context, 33));
		int dp11 = ResHelper.dipToPx(context, 11);
		lp.rightMargin = dp11;
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(vSep, lp);

		tvSend = new TextView(context);
		tvSend.setTextColor(0xffe4554c);
		tvSend.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tvSend.setText(ResHelper.getStringRes(context, "umssdk_default_send_vcode"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.rightMargin = dp11;
		ll.addView(tvSend, lp);

		vSep = new View(context);
		vSep.setBackgroundColor(0xffeaeaea);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		addView(vSep, lp);
	}

	public String getVCode() {
		if (etCode.isEnabled()) {
			return etCode.getText().toString().trim();
		} else {
			return null;
		}
	}

	protected abstract boolean beforeSend();

	protected abstract void onCountDown(int secondsLeft);

	protected abstract void afterSend(boolean smartVCode);

	protected abstract boolean isRegiterType();

	protected void askToSend() {
		if (!beforeSend()) {
			OKCancelDialog.Builder builder = new OKCancelDialog.Builder(page.getContext(), page.getTheme());
			int resId = ResHelper.getStringRes(page.getContext(), "umssdk_default_confirm_phone_number");
			builder.setTitle(page.getContext().getString(resId));
			resId = ResHelper.getStringRes(page.getContext(), "umssdk_default_we_are_go_to_send_vcode_to_x");
			builder.setMessage(page.getContext().getString(resId, "+" + getContry() + " " + getPhone()));
			builder.noPadding();
			builder.setOnClickListener(new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_POSITIVE) {
						sendVcode();
					}
					dialog.dismiss();
				}
			});
			builder.show();
		}
	}

	private void sendVcode() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = new ProgressDialog.Builder(page.getContext(), page.getTheme()).show();
		OperationCallback<Boolean> cb = new OperationCallback<Boolean>() {
			public void onSuccess(Boolean data) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				tvSend.setOnClickListener(null);
				afterSend(data);
				if (data) {
					etCode.setText(ResHelper.getStringRes(getContext(), "umssdk_default_smart_verified"));
					etCode.setEnabled(false);
				} else {
					startCountDown(60);
				}
			}

			public void onFailed(Throwable t) {
//				t.printStackTrace();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				ErrorDialog.Builder builder = new ErrorDialog.Builder(page.getContext(), page.getTheme());
				int resId = ResHelper.getStringRes(page.getContext(), "umssdk_default_send_vcode");
				builder.setTitle(page.getContext().getString(resId));
				try {
					HashMap<String, Object> errMap = new Hashon().fromJson(t.getMessage());
					if (errMap != null && errMap.containsKey("detail")) {
						builder.setMessage((String) errMap.get("detail"));
					} else {
						builder.setMessage(t.getMessage());
					}
				} catch (Throwable tt) {
					builder.setMessage(t.getMessage());
				}
				builder.setThrowable(t);
				builder.show();
			}
		};
		if (isRegiterType()) {
			UMSSDK.sendVerifyCodeForRegitser(getContry(), getPhone(), cb);
		} else {
			UMSSDK.sendVerifyCodeForResetPassword(getContry(), getPhone(), cb);
		}
	}

	protected abstract String getContry();

	protected abstract String getPhone();

	private void startCountDown(final int seconds) {
		final long stopTime = System.currentTimeMillis() + seconds * 1000;
		stopCountDown = false;
		UIHandler.sendEmptyMessage(0, new Callback() {
			private int secondsLeft = -1;
			public boolean handleMessage(Message msg) {
				int left = (int) (stopTime - System.currentTimeMillis());
				if (left >= 0) {
					left /= 1000;
					if (!stopCountDown) {
						UIHandler.sendEmptyMessageDelayed(0, 300, this);
					}
					if (secondsLeft != left) {
						secondsLeft = left;
						onCountDown(secondsLeft);
					}
				}
				return false;
			}
		});
	}

	public void stopCountDown() {
		stopCountDown = true;
	}
}
