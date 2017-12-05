package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.gui.pages.CountryCodeSelectorePage;
import com.mob.ums.gui.themes.defaultt.components.GroupListView;
import com.mob.ums.gui.themes.defaultt.components.GroupListView.OnItemClickListener;
import com.mob.ums.gui.themes.defaultt.components.SmsCountryListView;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.util.HashMap;

public class CountryCodeSelectorePageAdapter extends DefaultThemePageAdapter<CountryCodeSelectorePage> {
	private RelativeLayout rlSearch;
	private ImageView ivSearch;
	private EditText etSearch;
	private SmsCountryListView lvCountry;
	private InputMethodManager imm;

	public void onCreate(final CountryCodeSelectorePage page, Activity activity) {
		super.onCreate(page, activity);
		initPage(activity);
		page.prepareData(new Callback() {
			public boolean handleMessage(Message msg) {
				if (msg.what == 1) {
					onCountryListGot(page.getCountries());
				} else {
					finish();
				}
				return false;
			}
		});
	}

	private void initPage(Activity activity) {
		final LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		activity.setContentView(llPage);

		// title
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(new TitleView(activity) {
			protected void onReturn() {
				finish();
			}

			protected String getTitleResName() {
				return "umssdk_default_country";
			}
		}, lp);

		rlSearch = new RelativeLayout(activity);
		int dp15 = ResHelper.dipToPx(activity, 15);
		int dp10 = ResHelper.dipToPx(activity, 10);
		rlSearch.setPadding(dp15, dp10, dp15, dp10);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(rlSearch, lp);

		ivSearch = new ImageView(activity);
		ivSearch.setId(1);
		int dp25 = ResHelper.dipToPx(activity, 25);
		RelativeLayout.LayoutParams lprl = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp25);
		lprl.addRule(RelativeLayout.CENTER_IN_PARENT);
		ivSearch.setScaleType(ScaleType.CENTER_INSIDE);
		int resid = ResHelper.getBitmapRes(activity, "umssdk_default_sms_country_search");
		ivSearch.setImageResource(resid);
		ivSearch.setBackgroundResource(ResHelper.getBitmapRes(activity, "umssdk_default_sms_country_search_bg"));
		rlSearch.addView(ivSearch, lprl);

		etSearch = new EditText(activity);
		lprl = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp25);
		etSearch.setBackground(null);
		etSearch.setGravity(Gravity.CENTER);
		etSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
		etSearch.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		etSearch.setPadding(dp10, 0, dp10, 0);
		etSearch.setSingleLine();
		etSearch.setHintTextColor(0xffffffff);
		etSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		etSearch.setVisibility(View.GONE);
		etSearch.setBackgroundResource(ResHelper.getBitmapRes(activity, "umssdk_default_sms_country_search_bg"));
		rlSearch.addView(etSearch, lprl);

		lvCountry = new SmsCountryListView(activity);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lvCountry.setBackgroundColor(0);
		llPage.addView(lvCountry, lp);
	}

	private void onCountryListGot(final HashMap<String, String> countryRules) {
		rlSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ivSearch.setVisibility(View.GONE);
				etSearch.setVisibility(View.VISIBLE);
				etSearch.getText().clear();
				etSearch.requestFocus();
				showIME();
			}
		});

		etSearch.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				lvCountry.onSearch(s.toString().toLowerCase());
			}

			public void afterTextChanged(Editable s) {

			}
		});

		lvCountry.initSearchEngine();
		lvCountry.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(GroupListView parent, View view, int group, int position) {
				if (position >= 0) {
					String[] country = lvCountry.getCountry(group, position);
					if (countryRules != null && countryRules.containsKey(country[1])) {
						String code = country[1];
						if (code != null && !code.equals(getPage().getLoginCode())) {
							HashMap<String, Object> res = new HashMap<String, Object>();
							res.put("code", code);
							getPage().setResult(res);
						}
						finish();
					} else {
						int resId = ResHelper.getStringRes(lvCountry.getContext(),
								"umssdk_default_country_not_support_currently");
						if (resId > 0) {
							Toast.makeText(lvCountry.getContext(), resId, Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
	}

	private void showIME() {
		if (imm == null) {
			imm = (InputMethodManager) MobSDK.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
	}

	public boolean onKeyEvent(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& ivSearch.getVisibility() == View.GONE) {
			ivSearch.setVisibility(View.VISIBLE);
			etSearch.setVisibility(View.GONE);
			etSearch.setText("");
			return true;
		}
		return super.onKeyEvent(keyCode, event);
	}
}
