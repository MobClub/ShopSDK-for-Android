package com.mob.shop.gui.utils;

import android.text.TextUtils;

import com.mob.MobSDK;
import com.mob.shop.datatype.entity.Province;
import com.mob.shop.gui.themes.defaultt.entity.GoodSelectedTypeItem;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.SharePrefrenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by weishj on 2017/9/14.
 */

public class SGUISPDB {
	private static final String DB_NAME = "ShopSDKGUI_SPDB";
	private static final int DB_VERSION = 1;
	private static final String KEY_AREA = "key_area";
	private static final String KEY_SEARCHHISTORY = "key_searchhistory";
	private static final String KEY_PRODUCT_SEARCH_HISTORY = "key_product_search_history";
	private static final String KEY_SEARCHREFUNDHISTORY = "key_searchrefundhistory";

	private static final SharePrefrenceHelper sp;

	static {
		sp = new SharePrefrenceHelper(MobSDK.getContext());
		sp.open(DB_NAME, DB_VERSION);
	}

	public static void setArea(List<Province> list) {
		String json = (new Hashon()).fromObject(list);
		sp.putString(KEY_AREA, json);
	}

	public static ArrayList<Province> getArea() {
		String area = sp.getString(KEY_AREA);
		ArrayList<Province> list = new ArrayList<Province>();
		if(TextUtils.isEmpty(area)){
			return list;
		}
		HashMap<String, Object> map = new Hashon().fromJson(area);
		ArrayList<Object> a = (ArrayList<Object>) map.get("fakelist");
		Hashon hashon = new Hashon();
		if (a != null && a.size() > 0) {
			Province tmp;
			for (Object o : a) {
				String oStr = hashon.fromHashMap((HashMap<String, Object>)o);
				tmp = hashon.fromJson(oStr, Province.class);
				list.add(tmp);
			}
		}
		return list;
	}

	public static void setSearchHistory(GoodSelectedTypeItem search) {
		String searchHistory = sp.getString(KEY_SEARCHHISTORY);
		HashMap<String, Object> map = new Hashon().fromJson(searchHistory);
		List<Object> os = (ArrayList<Object>) map.get("fakelist");
		ArrayList<GoodSelectedTypeItem> list = new ArrayList<GoodSelectedTypeItem>();
		Hashon hashon = new Hashon();
		if (os != null) {
			GoodSelectedTypeItem tmp;
			for (Object o : os) {
				try {
					String oStr = hashon.fromHashMap((HashMap<String, Object>)o);
					tmp = hashon.fromJson(oStr, GoodSelectedTypeItem.class);
					list.add(tmp);
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			}

			for (GoodSelectedTypeItem item : list) {
				if (item.getKey().equals(search.getKey())) {
					return;
				}
			}
		}

		list.add(search);
		sp.putString(KEY_SEARCHHISTORY, (new Hashon()).fromObject(list));
	}

	public static void clearSearchHistory() {
		sp.remove(KEY_SEARCHHISTORY);
	}

	public static ArrayList<GoodSelectedTypeItem> getSearchHistory() {
		String searchHistory = sp.getString(KEY_SEARCHHISTORY);
		if (TextUtils.isEmpty(searchHistory)) {
			return null;
		}
		HashMap<String, Object> map = new Hashon().fromJson(searchHistory);
		ArrayList<Object> a = (ArrayList<Object>) map.get("fakelist");
		ArrayList<GoodSelectedTypeItem> list = new ArrayList<GoodSelectedTypeItem>();
		Hashon hashon = new Hashon();
		if (a == null) {
			GoodSelectedTypeItem tmp = hashon.fromJson(searchHistory, GoodSelectedTypeItem.class);
			list.add(tmp);
			return list;
		}
		GoodSelectedTypeItem tmp;
		for (Object o : a) {
			try {
				String oStr = hashon.fromHashMap((HashMap<String, Object>)o);
				tmp = hashon.fromJson(oStr, GoodSelectedTypeItem.class);
				list.add(tmp);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
		return list;
	}

	public static void setProductSearchHistory(GoodSelectedTypeItem search) {
		ArrayList<GoodSelectedTypeItem> list = getProductSearchHistory();
		if (list == null) {
			list = new ArrayList<GoodSelectedTypeItem>();
		}
		for (GoodSelectedTypeItem item : list) {
			if (item.getKey().equals(search.getKey())) {
				return;
			}
		}
		list.add(search);
		sp.put(KEY_PRODUCT_SEARCH_HISTORY, list);
	}

	public static ArrayList<GoodSelectedTypeItem> getProductSearchHistory() {
		Object obj = sp.get(KEY_PRODUCT_SEARCH_HISTORY);
		if (obj == null) {
			return null;
		} else {
			return (ArrayList<GoodSelectedTypeItem>) obj;
		}
	}

	public static void clearProductSearchHistory() {
		sp.remove(KEY_PRODUCT_SEARCH_HISTORY);
	}

	public static void setSearchRefundHistory(GoodSelectedTypeItem search) {
		String searchHistory = sp.getString(KEY_SEARCHREFUNDHISTORY);
		HashMap<String, Object> map = new Hashon().fromJson(searchHistory);
		List<Object> os = (ArrayList<Object>) map.get("fakelist");
		ArrayList<GoodSelectedTypeItem> list = new ArrayList<GoodSelectedTypeItem>();
		Hashon hashon = new Hashon();
		if (os != null) {
			GoodSelectedTypeItem tmp;
			for (Object o : os) {
				try {
					String oStr = hashon.fromHashMap((HashMap<String, Object>)o);
					tmp = hashon.fromJson(oStr, GoodSelectedTypeItem.class);
					list.add(tmp);
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			}

			for (GoodSelectedTypeItem item : list) {
				if (item.getKey().equals(search.getKey())) {
					return;
				}
			}
		}

		list.add(search);
		sp.putString(KEY_SEARCHREFUNDHISTORY, (new Hashon()).fromObject(list));
	}

	public static void clearSearchRefundHistory() {
		sp.remove(KEY_SEARCHREFUNDHISTORY);
	}

	public static ArrayList<GoodSelectedTypeItem> getSearchRefundHistory() {
		String searchHistory = sp.getString(KEY_SEARCHREFUNDHISTORY);
		if (TextUtils.isEmpty(searchHistory)) {
			return null;
		}
		HashMap<String, Object> map = new Hashon().fromJson(searchHistory);
		ArrayList<Object> a = (ArrayList<Object>) map.get("fakelist");
		ArrayList<GoodSelectedTypeItem> list = new ArrayList<GoodSelectedTypeItem>();
		Hashon hashon = new Hashon();
		if (a == null) {
			GoodSelectedTypeItem tmp = hashon.fromJson(searchHistory, GoodSelectedTypeItem.class);
			list.add(tmp);
			return list;
		}
		GoodSelectedTypeItem tmp;
		for (Object o : a) {
			try {
				String oStr = hashon.fromHashMap((HashMap<String, Object>)o);
				tmp = hashon.fromJson(oStr, GoodSelectedTypeItem.class);
				list.add(tmp);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
		return list;
	}

}
