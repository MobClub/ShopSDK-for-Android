package com.mob.shop.gui.themes.defaultt.components.searchbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.shop.gui.R;
import com.mob.shop.gui.themes.defaultt.components.searchbar.impl.InputResultCallBack;
import com.mob.tools.utils.ResHelper;

/**
 * Created by yjin on 2017/9/19.
 */

public class SearchBar extends TransformingToolbar implements TextView.OnEditorActionListener {
	private EditText editText;
	private TextView cancelPage;
	private Activity activity;
	private InputResultCallBack inputResultCallBack;
	private Drawable drawable = null;

	public SearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(ResHelper.getBitmapRes(context,"shopsdk_default_title_bg"));
		drawable = getResources().getDrawable(R.drawable.ic_action_search);
	}

	public void setActivity(Activity activity){
		this.activity = activity;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		inflate(getContext(), R.layout.merge_search, this);
		editText = (EditText) findViewById(R.id.toolbar_search_edittext);
		editText.setOnEditorActionListener(this);
		cancelPage = (TextView)findViewById(R.id.cancelPage);
		cancelPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(activity != null){
					activity.finish();
				}
			}
		});
	}

	public void setInputResultCallBack(InputResultCallBack inputResultCallBack){
		this.inputResultCallBack = inputResultCallBack;
	}

	@Override
	public void showContent() {
		super.showContent();
		editText.requestFocus();
	}

	public void clearText() {
		editText.setText(null);
	}

	public void setText(String txt) {
		editText.setText(txt);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			if(inputResultCallBack != null){
				String result = editText.getText().toString().trim();
				if(!TextUtils.isEmpty(result)){
					inputResultCallBack.inputOnResult(result);
				}
				inputResultCallBack.requestSearch();

			}
			return true;
		}
		return false;
	}

}
