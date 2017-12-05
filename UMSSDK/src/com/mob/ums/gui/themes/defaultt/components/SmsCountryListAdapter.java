package com.mob.ums.gui.themes.defaultt.components;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;
import com.mob.ums.UMSSDK;
import com.mob.ums.gui.pages.CountryCodeSelectorePage.SearchEngine;
import com.mob.ums.gui.themes.defaultt.components.GroupListView.GroupAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmsCountryListAdapter extends GroupAdapter {
	private HashMap<Character, ArrayList<String[]>> rawData;
	private ArrayList<String> titles;
	private ArrayList<ArrayList<String[]>> countries;
	private SearchEngine sEngine;

	public SmsCountryListAdapter(GroupListView view) {
		super(view);
		rawData = UMSSDK.getAllVCodeCountries();
		search(null);
	}

	public void initSearchEngine() {
		sEngine = new SearchEngine();
		ArrayList<String> countries = new ArrayList<String>();
		for (Map.Entry<Character, ArrayList<String[]>> ent : rawData.entrySet()) {
			ArrayList<String[]> cl = ent.getValue();
			for (String[] paire : cl) {
				countries.add(paire[0]);
			}
		}
		sEngine.setIndex(countries);
		search(null);
	}

	public void search(String token) {
		ArrayList<String> res = sEngine == null ? null : sEngine.match(token);
		boolean isEmptyToken = false;
		if (res == null || res.size() <= 0) {
			res = new ArrayList<String>();
			isEmptyToken = true;
		}

		HashMap<String, String> resMap = new HashMap<String, String>();
		for (String r : res) {
			resMap.put(r, r);
		}

		titles = new ArrayList<String>();
		countries = new ArrayList<ArrayList<String[]>>();
		for (Map.Entry<Character, ArrayList<String[]>> ent : rawData.entrySet()) {
			ArrayList<String[]> cl = ent.getValue();
			ArrayList<String[]> list = new ArrayList<String[]>();
			for (String[] paire : cl) {
				if (isEmptyToken || resMap.containsKey(paire[0])) {
					list.add(paire);
				}
			}
			if (list.size() > 0) {
				titles.add(String.valueOf(ent.getKey()));
				countries.add(list);
			}
		}
	}

	public int getGroupCount() {
		return titles == null ? 0 : titles.size();
	}

	public int getCount(int group) {
		if (countries == null) {
			return 0;
		}

		ArrayList<String[]> list = countries.get(group);
		if (list == null) {
			return 0;
		}

		return list.size();
	}

	public String getGroupTitle(int group) {
		if (titles.size() != 0) {
			return titles.get(group).toString();
		} else {
			return null;
		}
	}

	public String[] getItem(int group, int position) {
		String[] countriesArray = null;
		if (countries.size() != 0) {
			try {
				countriesArray = countries.get(group).get(position);
			} catch (Throwable t) {
//				t.printStackTrace();
			}
			return countriesArray;
		} else {
			return null;
		}
	}

	/**
	 * 获取组标题的view,如 组 A
	 */
	@SuppressWarnings("deprecation")
	public View getTitleView(int group, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LinearLayout ll = new LinearLayout(parent.getContext());
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setBackgroundDrawable(null);
			convertView = ll;

			TextView tv = new TextView(parent.getContext());
			tv.setBackgroundColor(0xffeaeaea);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(parent.getContext(), 13));
			tv.setTextColor(0xff979797);
			int paddingLeft = ResHelper.dipToPx(parent.getContext(), 15);
			tv.setPadding(paddingLeft, 0, paddingLeft, 0);
			tv.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ResHelper.dipToPx(parent.getContext(), 25)));
			ll.addView(tv);
		}

		String title = getGroupTitle(group);
		TextView tv = (TextView) ((LinearLayout) convertView).getChildAt(0);
		tv.setText(title);
		return convertView;
	}

	/**
	 * listview 滑动时，改变组的标题
	 */
	public void onGroupChange(View titleView, String title) {
		TextView tv = (TextView) ((LinearLayout) titleView).getChildAt(0);
		tv.setText(title);
	}

	/**
	 * 设置国家列表listview组中的item项
	 */
	@SuppressWarnings("deprecation")
	public View getView(int group, int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LinearLayout ll = new LinearLayout(parent.getContext());
			ll.setLayoutParams(new AbsListView.LayoutParams(
					AbsListView.LayoutParams.MATCH_PARENT, ResHelper.dipToPx(parent.getContext(), 50)));
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setBackgroundDrawable(null);
			convertView = ll;

			TextView tv = new TextView(parent.getContext());
			tv.setTextColor(0xff3b3a42);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(parent.getContext(), 14));
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			lp.leftMargin = ResHelper.dipToPx(parent.getContext(), 15);
			lp.gravity = Gravity.CENTER_VERTICAL;
			ll.addView(tv, lp);

			TextView tvCode = new TextView(parent.getContext());
			tvCode.setTextColor(0xff939393);
			tvCode.setGravity(Gravity.CENTER_VERTICAL);
			tvCode.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(parent.getContext(), 14));
			lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.rightMargin = ResHelper.dipToPx(parent.getContext(), 35);
			lp.gravity = Gravity.CENTER_VERTICAL;
			ll.addView(tvCode, lp);
		}

		String[] data = getItem(group, position);
		if (data != null) {
			TextView tv = (TextView) ((LinearLayout) convertView).getChildAt(0);
			tv.setText(data[0]);
			TextView tvCode = (TextView) ((LinearLayout) convertView).getChildAt(1);
			tvCode.setText("+" + data[1]);
		}
		return convertView;
	}

}