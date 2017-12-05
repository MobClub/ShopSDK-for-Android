package com.mob.shop.gui.base;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.mob.shop.biz.ShopLog;
import com.mob.shop.gui.Callback;
import com.mob.shop.gui.pages.dialog.ErrorDialog;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ResHelper;

public abstract class Page<P extends Page<P>> extends FakeActivity {
	protected final String TAG = getClass().getSimpleName();
	private final Theme theme;
	private final P self;
	private final PageAdapter<P> adapter;
	protected Bundle transBundle;
	protected static Callback callback;

	@SuppressWarnings("unchecked")
	public Page(Theme theme) {
		this.theme = theme;
		self = (P) this;
		adapter = theme.getPageAdapter(self);
		adapter.setPage(self);
	}

	public Page(Theme theme,Bundle savedInstanced){
		this(theme);
		if(savedInstanced != null){
			this.transBundle = savedInstanced;
		} else {
			this.transBundle = new Bundle();
		}
	}



	public Bundle getReBundle(){
		return transBundle;
	}

	protected PageAdapter<P> myAdapter() {
		return adapter;
	}

	public Callback getCallback() {
		return callback;
	}

	public void setCallback(Callback callback) {
		Page.callback = callback;
	}

	public Theme getTheme() {
		return theme;
	}

	public void onCreate() {
		adapter.onCreate(self, activity);
	}

	public void onStart() {
		adapter.onStart(self, activity);
	}

	public void onPause() {
		adapter.onPause(self, activity);
		DeviceHelper.getInstance(activity).hideSoftInput(activity.getWindow().getDecorView());
	}

	public void onResume() {
		adapter.onResume(self, activity);
	}

	public void onStop() {
		adapter.onStop(self, activity);
	}

	public void onRestart() {
		adapter.onRestart(self, activity);
	}

	public void onDestroy() {
		adapter.onDestroy(self, activity);
	}

	public void setContentView(View view) {
		adapter.onSetContentView(view, self, activity);
		super.setContentView(view);
	}

	public boolean onKeyEvent(int keyCode, KeyEvent event) {
		return adapter.onKeyEvent(keyCode, event);
	}

	public void toastNetworkError() {
		Toast.makeText(self.getContext(), ResHelper.getStringRes(self.getContext(),
				"shopsdk_default_err_msg_network_error"), Toast.LENGTH_SHORT).show();
	}

	public void toastMessage(String msg) {
		Toast.makeText(self.getContext(), msg, Toast.LENGTH_SHORT).show();
	}

	public void toastMessage(int msgResId) {
		if (msgResId <= 0) {
			ShopLog.getInstance().w(ShopLog.FORMAT, TAG, "toastMessage", "resourceID is wrong. resId=" + msgResId);
			return;
		}
		try {
			Toast.makeText(self.getContext(), msgResId, Toast.LENGTH_SHORT).show();
		} catch (Throwable t) {
			ShopLog.getInstance().w(ShopLog.FORMAT, TAG, "toastMessage", "resource can not be found. resId=" + msgResId, t);
		}
	}

	public void dialogErrorMessage(int titleResId, int msgResId) {
		String title = "";
		if (titleResId > 0) {
			try {
				title = self.getContext().getString(titleResId);
			} catch (Throwable t) {
				ShopLog.getInstance().w(ShopLog.FORMAT, TAG, "dialogErrorMessage", "resource can not be found. titleResId=" + titleResId, t);
			}
		}
		String msg = "";
		if (msgResId > 0) {
			try {
				msg = self.getContext().getString(msgResId);
			} catch (Throwable t) {
				ShopLog.getInstance().w(ShopLog.FORMAT, TAG, "dialogErrorMessage", "resource can not be found. msgResId=" + msgResId, t);
			}
		}
		if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(msg)) {
			ErrorDialog.Builder builder = new ErrorDialog.Builder(self.getContext(), self.getTheme());
			builder.setTitle(title);
			builder.setMessage(msg);
			builder.show();
		}
	}

	public void dialogErrorMessage(String title, String msg) {
		if (TextUtils.isEmpty(title) && TextUtils.isEmpty(msg)) {
			ShopLog.getInstance().w(ShopLog.FORMAT, TAG, "dialogErrorMessage", "title & msg are both empty");
			return;
		}
		ErrorDialog.Builder builder = new ErrorDialog.Builder(self.getContext(), self.getTheme());
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.show();
	}
}
