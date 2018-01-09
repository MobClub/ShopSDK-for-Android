package com.mob.shop.demo.util;

import android.content.Context;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by weishj on 2017/12/20.
 */

public class Utils {
	private static final String TAG = Utils.class.getSimpleName();

	public static String getAppName(Context context) {
		String appName;
		int appLbl = context.getApplicationInfo().labelRes;
		if(appLbl > 0) {
			appName = context.getString(appLbl);
		} else {
			appName = String.valueOf(context.getApplicationInfo().nonLocalizedLabel);
		}

		return appName;
	}

	public static String getIPAddress() {
		try {
			Enumeration e = NetworkInterface.getNetworkInterfaces();

			while(e.hasMoreElements()) {
				NetworkInterface intf = (NetworkInterface)e.nextElement();
				Enumeration enumIpAddr = intf.getInetAddresses();

				while(enumIpAddr.hasMoreElements()) {
					InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
					if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (Throwable t) {
			Log.w(TAG, t);
		}

		return "0.0.0.0";
	}
}
