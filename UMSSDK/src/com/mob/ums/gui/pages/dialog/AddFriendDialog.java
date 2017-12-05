package com.mob.ums.gui.pages.dialog;

import android.content.Context;

import com.mob.jimu.gui.Dialog;
import com.mob.jimu.gui.Theme;
import com.mob.ums.OperationCallback;
import com.mob.ums.User;

import java.util.HashMap;

public class AddFriendDialog extends Dialog<AddFriendDialog> {
	private User user;
	private OperationCallback<Void> callback;
	
	public AddFriendDialog(Context context, Theme theme) {
		super(context, theme);
	}
	
	protected void applyParams(HashMap<String, Object> params) {
		super.applyParams(params);
		this.user = (User) params.get("user");
		this.callback = (OperationCallback<Void>) params.get("callback");
	}
	
	public User getUser() {
		return user;
	}
	
	public OperationCallback<Void> getCallback() {
		return callback;
	}
	
	public Theme getTheme() {
		return super.getTheme();
	}
	
	public static class Builder extends Dialog.Builder<AddFriendDialog> {
		
		public Builder(Context context, Theme theme) {
			super(context, theme);
		}
		
		public void setUser(User user) {
			set("user", user);
		}
		
		public void setCallback(OperationCallback<Void> callback) {
			set("callback", callback);
		}
	}
}
