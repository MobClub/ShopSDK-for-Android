package com.mob.ums.gui.themes.defaultt.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.User;
import com.mob.ums.datatype.City;
import com.mob.ums.datatype.Country;
import com.mob.ums.datatype.Gender;
import com.mob.ums.datatype.Province;

import java.util.Locale;

public class ItemUserView extends RelativeLayout {
	private int resBgFemale;
	private int resBgMale;
	private int resBgSecret;
	private int resIconFemale;
	private int resIconMale;
	private int resIconSecret;
	private int resDefaultAvatar;

	private AsyncImageView ivLogo;
	private LinearLayout llActions;
	private TextView tvRight;
	private TextView tvNickName;
	private TextView tvSignature;
	private LinearLayout llGenderAge;
	private ImageView ivGender;
	private TextView tvAge;
	private TextView tvLocation;

	public ItemUserView(Context context) {
		super(context);
		resBgFemale = ResHelper.getBitmapRes(context, "umssdk_default_useritem_gender_bg_female");
		resBgMale = ResHelper.getBitmapRes(context, "umssdk_default_useritem_gender_bg_male");
		resBgSecret = ResHelper.getBitmapRes(context, "umssdk_default_useritem_gender_bg_secret");
		resIconFemale = ResHelper.getBitmapRes(context, "umssdk_defalut_gender_f");
		resIconMale = ResHelper.getBitmapRes(context, "umssdk_defalut_gender_m");
		resIconSecret = ResHelper.getBitmapRes(context, "umssdk_defalut_gender_u");
		resDefaultAvatar = ResHelper.getBitmapRes(context, "umssdk_default_avatar_rect");
		init(context);
	}

	private void init(Context context) {
		int dp15 = ResHelper.dipToPx(context, 15);
		int dp10 = ResHelper.dipToPx(context, 10);
		setPadding(dp15, dp10, dp15, dp10);
		setBackgroundColor(0xffffffff);

		ivLogo = new AsyncImageView(context);
		ivLogo.setId(1);
		ivLogo.setRound(ResHelper.dipToPx(context, 2));
		ivLogo.setScaleToCropCenter(true);
		int dp52 = ResHelper.dipToPx(context, 52);
		LayoutParams lp = new LayoutParams(dp52, dp52);
		lp.addRule(CENTER_VERTICAL);
		lp.addRule(ALIGN_PARENT_LEFT);
		lp.rightMargin = dp10;
		addView(ivLogo, lp);

		RelativeLayout rlRight = new RelativeLayout(context);
		rlRight.setId(2);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_RIGHT);
		lp.addRule(ALIGN_TOP, ivLogo.getId());
		lp.addRule(ALIGN_BOTTOM, ivLogo.getId());
		lp.leftMargin = dp10;
		addView(rlRight, lp);

		llActions = new LinearLayout(context);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, ResHelper.dipToPx(context, 27));
		lp.addRule(CENTER_VERTICAL);
		rlRight.addView(llActions, lp);

		tvRight = new TextView(context);
		tvRight.setTextColor(0xff969696);
		tvRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		tvRight.setMaxLines(1);
		tvRight.setGravity(Gravity.TOP);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlRight.addView(tvRight, lp);

		tvNickName = new TextView(context);
		tvNickName.setId(3);
		tvNickName.setGravity(Gravity.LEFT | Gravity.TOP);
		tvNickName.setTextColor(0xff000000);
		tvNickName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		tvNickName.setMaxLines(1);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(ALIGN_TOP, ivLogo.getId());
		lp.addRule(RIGHT_OF, ivLogo.getId());
		lp.addRule(LEFT_OF, rlRight.getId());
		int dp2 = ResHelper.dipToPx(context,2);
		lp.topMargin = -dp2;
		addView(tvNickName, lp);

		tvSignature = new TextView(context);
		tvSignature.setId(4);
		tvSignature.setTextColor(0xff969696);
		tvSignature.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		tvSignature.setSingleLine(true);
		tvSignature.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(ALIGN_BOTTOM, ivLogo.getId());
		lp.addRule(RIGHT_OF, ivLogo.getId());
		lp.addRule(LEFT_OF, rlRight.getId());
		lp.bottomMargin = -dp2;
		addView(tvSignature, lp);

		RelativeLayout rlCenter = new RelativeLayout(context);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(BELOW, tvNickName.getId());
		lp.addRule(ABOVE, tvSignature.getId());
		lp.addRule(RIGHT_OF, ivLogo.getId());
		lp.addRule(LEFT_OF, rlRight.getId());
		addView(rlCenter, lp);

		llGenderAge = new LinearLayout(context);
		llGenderAge.setId(1);
		int dp5 = ResHelper.dipToPx(context, 5);
		llGenderAge.setPadding(dp5, 0, dp5, 0);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, ResHelper.dipToPx(context, 13));
		lp.rightMargin = dp5;
		lp.addRule(CENTER_VERTICAL);
		rlCenter.addView(llGenderAge, lp);

		ivGender = new ImageView(context);
		ivGender.setScaleType(ScaleType.CENTER_INSIDE);
		LinearLayout.LayoutParams lpll = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpll.gravity = Gravity.CENTER_VERTICAL;
		lpll.rightMargin = dp5;
		llGenderAge.addView(ivGender, lpll);

		tvAge = new TextView(context);
		tvAge.setTextColor(0xffffffff);
		tvAge.setGravity(Gravity.CENTER);
		tvAge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		lpll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		llGenderAge.addView(tvAge, lpll);

		tvLocation = new TextView(context);
		tvLocation.setTextColor(0xff969696);
		tvLocation.setGravity(Gravity.CENTER_VERTICAL);
		tvLocation.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		tvLocation.setMaxLines(1);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.addRule(RIGHT_OF, llGenderAge.getId());
		lp.addRule(CENTER_VERTICAL);
		lp.leftMargin = dp5;
		rlCenter.addView(tvLocation, lp);
	}

	public void setUser(User user) {
		String unEdit = getContext().getString(ResHelper.getStringRes(getContext(),"umssdk_default_hint_space"));
		String url = null;
		if (!user.avatar.isNull() && user.avatar.get().length > 0) {
			url = user.avatar.get()[0];
		}
		ivLogo.execute(url, resDefaultAvatar);
		if (TextUtils.isEmpty(user.nickname.get()) || user.nickname.isNull()) {
			tvNickName.setText(unEdit);
		} else {
			tvNickName.setText(user.nickname.get());
		}
		if (user.age.isNull()) {
			tvAge.setText(unEdit);
		} else {
			tvAge.setText(String.valueOf(user.age.get()));
		}
		Country c = user.country.isNull() ? null : user.country.get();
		Province p = user.province.isNull() ? null : user.province.get();
		City ct = user.city.isNull() ? null : user.city.get();
		setLocation(c, p, ct);
		if (TextUtils.isEmpty(user.signature.get()) || user.signature.isNull()) {
			tvSignature.setText(unEdit);
		} else {
			tvSignature.setText(user.signature.get());
		}
		setGender(user.gender.get());
	}

	private void setLocation(Country country, Province province, City city) {
		String area = "";
		Context context = getContext();
		if ("zh".equals(Locale.getDefault().getLanguage())) {
			if (country != null) {
				area += context.getString(ResHelper.getStringRes(context, country.resName()));
			}
			if (province != null) {
				if (area.length() > 0) {
					area += ",";
				}
				area += context.getString(ResHelper.getStringRes(context, province.resName()));
			}
			if (city != null) {
				if (area.length() > 0) {
					area += ",";
				}
				area += context.getString(ResHelper.getStringRes(context, city.resName()));
			}
		} else {
			if (city != null) {
				area += context.getString(ResHelper.getStringRes(context, city.resName()));
			}
			if (province != null) {
				if (area.length() > 0) {
					area += ",";
				}
				area += context.getString(ResHelper.getStringRes(context, province.resName()));
			}
			if (country != null) {
				if (area.length() > 0) {
					area += ",";
				}
				area += context.getString(ResHelper.getStringRes(context, country.resName()));
			}
		}
		if (!TextUtils.isEmpty(area)) {
			tvLocation.setText(area);
		}
	}

	private void setGender(Gender gender) {
		if (gender != null) {
			switch (gender.code()) {
				case Gender.Female.CODE: {
					llGenderAge.setBackgroundResource(resBgFemale);
					ivGender.setImageResource(resIconFemale);
				} break;
				case Gender.Male.CODE: {
					llGenderAge.setBackgroundResource(resBgMale);
					ivGender.setImageResource(resIconMale);
				} break;
				case Gender.Secret.CODE: {
					llGenderAge.setBackgroundResource(resBgSecret);
					ivGender.setImageResource(resIconSecret);
				} break;
			}
		} else {
			llGenderAge.setBackgroundResource(resBgSecret);
			ivGender.setImageResource(resIconSecret);
		}
	}

	public void addAction(String textResName, int textColor, String bgResName, OnClickListener listener) {
		TextView tvAction = new TextView(getContext());
		int resId = ResHelper.getBitmapRes(getContext(), bgResName);
		if (resId > 0) {
			tvAction.setBackgroundResource(resId);
		}
		tvAction.setMaxLines(1);
		tvAction.setMinWidth(ResHelper.dipToPx(getContext(), 50));
		int dp6 = ResHelper.dipToPx(getContext(), 6);
		tvAction.setPadding(dp6, 0, dp6, 0);
		tvAction.setGravity(Gravity.CENTER);
		resId = ResHelper.getStringRes(getContext(), textResName);
		if (resId > 0) {
			tvAction.setText(resId);
		}
		tvAction.setTextColor(textColor);
		tvAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		LinearLayout.LayoutParams lpll = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		if (llActions.getChildCount() > 0) {
			lpll.leftMargin = ResHelper.dipToPx(getContext(), 15);
		}
		llActions.addView(tvAction, lpll);
		tvAction.setOnClickListener(listener);
	}

	public void setRightText(String text) {
		tvRight.setText(text);
	}

	public void hideGender() {
		llGenderAge.setVisibility(GONE);
	}

	public void hideLocation() {
		tvLocation.setVisibility(GONE);
	}

	public void setSignature(String signature) {
		tvSignature.setText(signature);
	}

	public void clearAction() {
		llActions.removeAllViews();
	}

}
