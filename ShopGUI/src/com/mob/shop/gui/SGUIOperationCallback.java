package com.mob.shop.gui;


import com.mob.shop.OperationCallback;
import com.mob.shop.biz.api.exception.ShopError;
import com.mob.shop.biz.api.exception.ShopException;

public class SGUIOperationCallback<T> extends OperationCallback<T> {
	private Callback callback;

	public SGUIOperationCallback(Callback callback) {
		this.callback = callback;
	}

	@Override
	public void onSuccess(T data) {
		super.onSuccess(data);
	}

	@Override
	public void onFailed(Throwable t) {
		if (callback == null) {
			onResultError(t);
			return;
		}
		if (onResultError(t) && t instanceof ShopException) {
			if (((ShopException) t).getCode() == ShopError.C_USER_NOT_LOGGED_IN.getCode() || ((ShopException) t)
					.getCode() == 4113005) {
				unLoginError(t);
			}
		}
	}

	public void unLoginError(Throwable t) {
		if (callback != null) {
			callback.login();
		}
	}

	/**
	 *
	 * @param t
	 * @return True if want to trigger a 'login' callback, false if not, default true
	 */
	protected boolean onResultError(Throwable t) {
		return true;
	}

}
