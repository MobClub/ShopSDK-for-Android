package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.entity.ExpressCompany;
import com.mob.shop.gui.R;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.pages.ExpressStDReferPage;
import com.mob.shop.gui.pay.DialogUtils;
import com.mob.shop.gui.themes.defaultt.adapter.ExpressStdReferAdapter;
import com.mob.shop.gui.themes.defaultt.components.SideBar;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.themes.defaultt.entity.ExpressCompanyBundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjin on 2017/11/2.
 */

public class ExpressStDReferPageAdapter extends DefaultThemePageAdapter<ExpressStDReferPage> {

	private TitleView expressName;
	private ListView expandableListView;
	private TextView dialogTxt;
	private SideBar sideBar;
	private List<ExpressCompany> expressCompanies = null;
	private Activity activity = null;
	private TextView selectTxt;
	private ExpressCompanyBundle expressCompanyBundle;

	private ExpressStdReferAdapter expressStdReferAdapter;
	private ExpressCompany expressCompany = null;

	@Override
	public void onCreate(ExpressStDReferPage page, Activity activity) {
		super.onCreate(page, activity);
		this.activity = activity;
		View view = LayoutInflater.from(activity).inflate(R.layout.shopsdk_default_page_express_std_refer, null);
		activity.setContentView(view);
		initView(view, page);
		initData();
		setExpressCompanyBundle(page.getExpressCompanyBundle());
	}

	private void initView(View view, Page page) {
		expressName = (TitleView) view.findViewById(R.id.expressNameTitleView);
		expressName.initTitleView(page, "shopsdk_default_express_std_refer_title", null, null, true);
		expandableListView = (ListView) view.findViewById(R.id.expressStdList);
		dialogTxt = (TextView) view.findViewById(R.id.dialogNameI);
		sideBar = (SideBar) view.findViewById(R.id.Expresssidrbar);
		selectTxt = (TextView) view.findViewById(R.id.selectExpressCompany);
		selectTxt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finishSelected();
			}
		});
	}

	private void setExpressCompanyBundle(ExpressCompanyBundle expressCompanyBundle) {
		this.expressCompanyBundle = expressCompanyBundle;
	}

	//选择完成回到填写页面
	private void finishSelected() {
		if (expressCompanies != null) {
			expressCompanyBundle.onResult(expressCompany);
		}
		finish();
	}

	private void initData() {
		DialogUtils.showLoading(getPage().getContext());
		getExpressCompanyList();
		expressCompanies = new ArrayList<ExpressCompany>();
		expressStdReferAdapter = new ExpressStdReferAdapter(activity, true, expressCompanies);
		expandableListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		expandableListView.setAdapter(expressStdReferAdapter);
		expressStdReferAdapter.notifyDataSetChanged();
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = expressStdReferAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					expandableListView.setSelection(position);
				}
			}
		});
		expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				expressCompany = expressCompanies.get(i);
				expressStdReferAdapter.setExpressCompany(expressCompany);
				for (int k = 0; k < adapterView.getCount(); k++) {
					View oneChild = adapterView.getChildAt(k);
					if (oneChild != null) {
						oneChild.findViewById(R.id.selectedExpressIcon).setVisibility(View.GONE);
					}
				}
				View childView = view.findViewById(R.id.selectedExpressIcon);
				if (childView != null && childView instanceof ImageView) {
					String txt = ((TextView) view.findViewById(R.id.textView_collewgue_fragment_bumen)).getText().toString().trim();
					if (txt.equals(expressCompany.getAbbreviation())) {
						view.findViewById(R.id.selectedExpressIcon).setVisibility(View.VISIBLE);
					}
				}
			}
		});
	}

	public void getExpressCompanyList() {
		ShopSDK.getExpressCompanies(new MyOperationCallback());
	}

	class MyOperationCallback extends OperationCallback<List<ExpressCompany>> {

		@Override
		public void onSuccess(List<ExpressCompany> data) {
			cancel();
			if (expressCompanies == null) {
				expressCompanies = new ArrayList<ExpressCompany>();
			}
			expressCompanies.addAll(data);
			expandableListView.deferNotifyDataSetChanged();
			expandableListView.invalidate();
			expandableListView.setSelection(0);
		}

		@Override
		public void onFailed(Throwable t) {
			cancel();
		}
	}

	private void cancel() {
		DialogUtils.hideLoading(getPage().getContext());
	}
}
