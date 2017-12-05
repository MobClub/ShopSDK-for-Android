package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.shop.gui.R;
import com.mob.tools.utils.ResHelper;

public class CountView extends LinearLayout {
	private EditText etCount;

	private int count = 1;
	private int maxCount = 1;
	private int step = 1;

	private OnCountClickListener onCountClickListener;
	private View rootView;

	public CountView(Context context) {
		this(context, null);
	}

	public CountView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CountView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_view_count, null);
		addView(view);
		etCount = (EditText) view.findViewById(R.id.etCount);
		TextView tvSub = (TextView) view.findViewById(R.id.tvSub);
		TextView tvAdd = (TextView) view.findViewById(R.id.tvAdd);

		tvSub.setOnClickListener(new CountClickListener());
		tvAdd.setOnClickListener(new CountClickListener());
		etCount.setEnabled(false);
	}


	public void setCount(int count) {
		this.count = count < 0 ? step : (count < step ? step : (count > maxCount ? maxCount : count));
		etCount.setText(String.valueOf(this.count));
	}

	public int getCount() {
		return count;
	}

	public boolean checkCount(int checkCount) {
		if (onCountClickListener != null) {
			if (checkCount > maxCount) {
				String msg = etCount.getContext().getResources().getString(ResHelper.getStringRes(etCount.getContext()
						, "shopsdk_default_add_failed"));
				onCountClickListener.onFailed(msg);
			} else if (checkCount < step) {
				String msg = etCount.getContext().getResources().getString(ResHelper.getStringRes(etCount.getContext()
						, "shopsdk_default_sub_failed"));
				onCountClickListener.onFailed(msg);
			} else if (checkCount % step != 0) {
				String msg = etCount.getContext().getResources().getString(ResHelper.getStringRes(etCount.getContext()
						, "shopsdk_default_input_failed"));
				onCountClickListener.onFailed(msg);
			} else {
				onCountClickListener.changeCount(checkCount);
				return true;
			}
		}
		return false;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount < 0 ? 0 : maxCount;
	}

	public void setStep(int step) {
		this.step = step < 0 ? 1 : step;
	}

	public void setCounts(int count, int maxCount, int step, boolean isEditable) {
		setCount(count);
		setMaxCount(maxCount);
		setStep(step);
		etCount.setEnabled(isEditable);
	}

	private class CountClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v instanceof TextView) {
				if (v.getId() == R.id.tvSub) {
					if (count - step >= step) {
						count -= step;
						etCount.setText(String.valueOf(count));
					}
					checkCount(count);
				} else if (v.getId() == R.id.tvAdd) {
					if (count + step <= maxCount) {
						count += step;
						etCount.setText(String.valueOf(count));
					}
					checkCount(count);
				}
			}

		}
	}


	public void setOnCountClickListener(OnCountClickListener onCountClickListener) {
		this.onCountClickListener = onCountClickListener;
	}

	public interface OnCountClickListener {
		void onFailed(String msg);

		void changeCount(int count);
	}
}
