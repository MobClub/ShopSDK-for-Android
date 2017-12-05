package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mob.tools.utils.ResHelper;
import com.mob.ums.gui.pickers.TextPicker;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.util.HashMap;

public class TextPickerAdapter extends DefaultThemePageAdapter<TextPicker> {
	
	public void onCreate(TextPicker page, Activity activity) {
		super.onCreate(page, activity);
		activity.getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
				| LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		initPage(activity);
	}
	
	private void initPage(final Activity activity) {
		final LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		activity.setContentView(llPage);
		
		final EditText et = new EditText(activity);
		
		// title
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(new TitleView(activity) {
			protected void onReturn() {
				finish();
			}
			
			protected String getTitleResName() {
				return getPage().getTitleResName();
			}
			
			protected String getRightLabelResName() {
				return "umssdk_default_finish";
			}
			
			protected void onRightClick() {
				HashMap<String, Object> res = new HashMap<String, Object>();
				res.put("text", et.getText().toString());
				getPage().setResult(res);
				finish();
			}
		}, lp);
		
		// input
		int dp15 = ResHelper.dipToPx(activity, 15);
		et.setPadding(dp15, dp15, dp15, dp15);
		et.setBackground(null);
		et.setTextColor(0xff3b3947);
		et.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		et.setHintTextColor(0xff969696);
		et.setHint(ResHelper.getStringRes(activity, getPage().getHintResName()));
		et.setGravity(Gravity.LEFT | Gravity.TOP);
		lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		llPage.addView(et, lp);
		if (!TextUtils.isEmpty(getPage().getDefaultValue())) {
			et.setText(getPage().getDefaultValue());
			et.setSelection(et.getText().length());
		}
		if (getPage().isNumeral()) {
			et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		}
	}
	
}
