package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.DeliveryType;
import com.mob.shop.datatype.entity.ExpressCompany;
import com.mob.shop.gui.Callback;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.pages.ExpressStDReferPage;
import com.mob.shop.gui.pages.FillInExpressNumberPage;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.themes.defaultt.entity.ExpressCompanyBundle;

import java.util.HashMap;

import static java.lang.Boolean.TRUE;

/**
 * Created by yjin on 2017/11/2.
 */

public class FillInExpressNumberPageAdapter extends DefaultThemePageAdapter<FillInExpressNumberPage> implements View.OnClickListener ,ExpressCompanyBundle {

	private TitleView titleView;
	private RelativeLayout selectExpress;
	private TextView shipperName;
	private EditText inExpressNo;
	private TextView expressSubmit;
	private long commondId;
	private ExpressCompany expressInfo;


	@Override
	public void onCreate(FillInExpressNumberPage page, Activity activity) {
		super.onCreate(page, activity);
		View view = LayoutInflater.from(activity).inflate(R.layout.shopsdk_default_page_fill_in_number,null);
		titleView = (TitleView)view.findViewById(R.id.expressTitleView);
		commondId = page.getOrderCommodityId();
		initView(view,page);
		activity.setContentView(view);
	}

	private void initView(View view,Page page){
		titleView.initTitleView(page, "shopsdk_default_fill_in_express_for_refund_title", null, null, true);
		selectExpress = (RelativeLayout)view.findViewById(R.id.selecteExpress);
		selectExpress.setOnClickListener(this);
		shipperName = (TextView)view.findViewById(R.id.shipperName);
		inExpressNo = (EditText)view.findViewById(R.id.expressNumber);
		expressSubmit =(TextView)view.findViewById(R.id.expressRefundSubmit);
		expressSubmit.setOnClickListener(this);
	}

	private void setTextExpressCompany(){
		shipperName.setText(expressInfo.getAbbreviation());
	}

	//选择快递公司
	private void selectExpressName(){
		ExpressStDReferPage expressStDReferPage = new ExpressStDReferPage(getPage().getTheme());
		expressStDReferPage.show(getPage().getContext(),null);
		expressStDReferPage.setExpressCompanyBundle(this);
	}

	//提交快递信息
	public void expressSubmit(){
		if(invaliteValue()){
			ShopSDK.fillInExpressInfo(commondId,inExpressNo.getText().toString().trim(),expressInfo.getId(),
					DeliveryType.EXPRESS,new FillInOperactionCallback(getPage().getCallback()));
		}
	}

	@Override
	public void onResult(ExpressCompany expressCompany) {
		this.expressInfo = expressCompany;
		setTextExpressCompany();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id == R.id.expressRefundSubmit){
			expressSubmit();
		} else if(id == R.id.selecteExpress){
			selectExpressName();
		}
	}

	class FillInOperactionCallback extends SGUIOperationCallback {

		public FillInOperactionCallback(Callback callback) {
			super(callback);
		}

		@Override
		public void onSuccess(Object data) {
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("isSubmit",TRUE);
			getPage().setResult(params);
			finish();
		}

		@Override
		public boolean onResultError(Throwable t) {
			return super.onResultError(t);
		}
	}

	private boolean invaliteValue(){
		if(commondId <= 0){
			return false;
		}

		if(expressInfo == null){
			return false;
		}

		String inputStr = inExpressNo.getText().toString().trim();
		if(TextUtils.isEmpty(inputStr)){
			return false;
		}

		if(expressInfo == null){
			return false;
		}
		return true;
	}
}
