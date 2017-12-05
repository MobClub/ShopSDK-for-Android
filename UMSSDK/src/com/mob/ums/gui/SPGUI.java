package com.mob.ums.gui;

import com.mob.MobSDK;
import com.mob.tools.utils.SharePrefrenceHelper;

import java.util.ArrayList;

public class SPGUI {
	private static final String APP_DB_NAME = "umssdk-gui";
	private static final int APP_DB_VERSION = 1;
	private static final String KEY_FRIEND_REQUESTS = "friend_requests";

	private static SharePrefrenceHelper sp;

	private static final synchronized void ensureNotNull() {
		if (sp == null) {
			sp = new SharePrefrenceHelper(MobSDK.getContext());
			sp.open(APP_DB_NAME, APP_DB_VERSION);
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> getFriendRequests() {
		ensureNotNull();
		Object o = sp.get(KEY_FRIEND_REQUESTS);
		if (o != null) {
			try {
				return (ArrayList<String>) o;
			} catch (Throwable t) {}
		}
		return null;
	}
	
	public static void setFriendRequests(ArrayList<String> requests) {
		ensureNotNull();
		sp.put(KEY_FRIEND_REQUESTS, requests);
	}
	
}
