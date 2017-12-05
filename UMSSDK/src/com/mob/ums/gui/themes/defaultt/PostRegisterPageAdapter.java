package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.datatype.Country;
import com.mob.ums.datatype.Gender;
import com.mob.ums.gui.pages.PostRegisterPage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.pickers.ImagePicker;
import com.mob.ums.gui.pickers.ImagePicker.OnImageGotListener;
import com.mob.ums.gui.pickers.SingleValuePicker.Choice;
import com.mob.ums.gui.themes.defaultt.components.SingleChoiceView;
import com.mob.ums.gui.themes.defaultt.components.SingleLineView;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.util.HashMap;

public class PostRegisterPageAdapter extends DefaultThemePageAdapter<PostRegisterPage>
		implements OnImageGotListener {
	private AsyncImageView ivAvatar;
	private SingleLineView llNick;
	private SingleChoiceView llGender;
	private SingleChoiceView llArea;
	private String avatarId;
	private String[] avatarUrls;
	private ProgressDialog pd;
	
	public void onCreate(PostRegisterPage page, Activity activity) {
		super.onCreate(page, activity);
		initPage(activity);
	}
	
	private void initPage(Activity activity) {
		LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		TranslateAnimation scrollRightAnim = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0);
		scrollRightAnim.setDuration(300);
		llPage.setAnimation(scrollRightAnim);
		activity.setContentView(llPage);
		
		// title
		TitleView llTitle = new TitleView(activity) {
			protected String getTitleResName() {
				return "umssdk_default_complete_info";
			}
			
			protected String getRightLabelResName() {
				return "umssdk_default_skip";
			}
			
			protected void onRightClick() {
				finish();
			}
		};
		llTitle.disableReturn();
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(llTitle, lp);
		
		// avatar
		RelativeLayout rlAvatar = new RelativeLayout(activity);
		int dp40 = ResHelper.dipToPx(activity, 40);
		int dp60 = ResHelper.dipToPx(activity, 60);
		int dp50 = ResHelper.dipToPx(activity, 50);
		lp = new LayoutParams(dp60, dp60);
		lp.topMargin = dp40;
		lp.bottomMargin = dp50;
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		llPage.addView(rlAvatar, lp);
		rlAvatar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ImagePicker.Builder builder = new ImagePicker.Builder(getPage().getContext(), getPage().getTheme());
				builder.setOnImageGotListener(PostRegisterPageAdapter.this);
				builder.show();
			}
		});
		
		ivAvatar = new AsyncImageView(activity);
		ivAvatar.setImageResource(ResHelper.getBitmapRes(activity, "umssdk_default_avatar"));
		ivAvatar.setRound(dp60);
		RelativeLayout.LayoutParams lprl = new RelativeLayout.LayoutParams(dp60, dp60);
		rlAvatar.addView(ivAvatar, lprl);
		
		ImageView iv = new ImageView(activity);
		iv.setBackgroundResource(ResHelper.getBitmapRes(activity, "umssdk_defalut_take_photo"));
		lprl = new RelativeLayout.LayoutParams(dp40 / 2, dp40 / 2);
		lprl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lprl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlAvatar.addView(iv, lprl);
		
		llNick = new SingleLineView(getPage());
		llNick.setTitleResName("umssdk_default_nickname");
		llNick.setHintResName("umssdk_default_enter_your_nickname");
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(llNick, lp);
		
		llGender = new SingleChoiceView(getPage());
		int resId = ResHelper.getStringRes(activity, "umssdk_default_gender");
		llGender.setTitle(activity.getString(resId));
		llGender.setChoices(SingleChoiceView.createChoice(Gender.class));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(llGender, lp);
		
		llArea = new SingleChoiceView(getPage());
		resId = ResHelper.getStringRes(activity, "umssdk_default_area");
		llArea.setTitle(activity.getString(resId));
		llArea.setChoices(SingleChoiceView.createChoice(Country.class));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(llArea, lp);
		
		// action button
		TextView tv = new TextView(activity);
		tv.setBackgroundResource(ResHelper.getBitmapRes(activity, "umssdk_default_login_btn"));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		tv.setTextColor(0xffffffff);
		tv.setText(ResHelper.getStringRes(activity, "umssdk_default_submit"));
		tv.setGravity(Gravity.CENTER);
		tv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateUser();
			}
		});
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 45));
		lp.topMargin = ResHelper.dipToPx(activity, 40);
		lp.leftMargin = ResHelper.dipToPx(activity, 15);
		lp.rightMargin = lp.leftMargin;
		llPage.addView(tv, lp);
	}
	
	public void onOmageGot(String id, String[] url) {
		this.avatarId = id;
		this.avatarUrls = url;
		if (url != null && url.length > 0) {
			int resId = ResHelper.getBitmapRes(ivAvatar.getContext(), "umssdk_default_avatar");
			ivAvatar.execute(url[0], resId);
		}
	}
	
	private void updateUser() {
		User user = getPage().getUser();
		final HashMap<String, Object> update = new HashMap<String, Object>();
		String nick = llNick.getText() != null ? llNick.getText().trim() : null;
		if (!TextUtils.isEmpty(nick)) {
			update.put(user.nickname.getName(), nick);
		}
		Choice[] gender = llGender.getSelections();
		if (gender != null && gender.length > 0 && gender[0].ext != null) {
			update.put(user.gender.getName(), gender[0].ext);
		}
		Choice[] area = llArea.getSelections();
		if (area != null && area.length == 3) {
			Choice country = area[0];
			Choice province = area[1];
			Choice city = area[2];
			if (country != null && country.ext != null) {
				update.put(user.country.getName(), country.ext);
			}
			if (province != null && province.ext != null) {
				update.put(user.province.getName(), province.ext);
			}
			if (city != null && city.ext != null) {
				update.put(user.city.getName(), city.ext);
			}
		}
		if (!TextUtils.isEmpty(avatarId) && avatarUrls != null && avatarUrls.length > 0) {
			update.put(user.avatarId.getName(), avatarId);
			update.put(user.avatar.getName(), avatarUrls);
		}
		
		if (update.size() > 0) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
			UMSSDK.updateUserInfo(update, new OperationCallback<Void>() {
				public void onSuccess(Void data) {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					User me = getPage().getUser();
					me.parseFromMap(update);
					finish();
				}
				
				public void onFailed(Throwable t) {
//					t.printStackTrace();
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					ErrorDialog.Builder builder = new ErrorDialog.Builder(getPage().getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_complete_info");
					builder.setTitle(getPage().getContext().getString(resId));
					builder.setMessage(t.getMessage());
					builder.setThrowable(t);
					builder.show();
				}
			});
		} else {
			finish();
		}
	}
	
}
