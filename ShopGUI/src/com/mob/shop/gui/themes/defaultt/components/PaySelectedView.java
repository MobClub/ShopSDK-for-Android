package com.mob.shop.gui.themes.defaultt.components;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.datatype.PayWay;
import com.mob.shop.datatype.builder.OrderPayBuilder;
import com.mob.shop.gui.R;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;
import com.mob.shop.gui.pay.PayManager;
import com.mob.shop.gui.utils.MoneyConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by yjin on 2017/11/1.
 */

public class PaySelectedView extends LinearLayout implements View.OnClickListener{

	private TextView payCancel;
	private TextView payAccount;
	private ImageView wXpay;
	private ImageView alipay;
	private TextView payAction;
	private RelativeLayout wXpayLayout;
	private RelativeLayout alipayLayout;
	private int payType = 0;
	private String txDesc;
	private Context context;
	private Theme page;
	private OrderPayBuilder orderPayBuilder;

	private PayCancelImpl payCancelImpl;

	public PaySelectedView(Context context) {
		super(context);
		init(context);
	}

	public PaySelectedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PaySelectedView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context){
		iniView(context);
		txDesc = context.getString(R.string.shopsdk_default_pay_account_show);
	}

	private void iniView(Context context){
		View layoutInflater = LayoutInflater.from(context).inflate(R.layout.shopsdk_pay_select_action,null);
		payCancel = (TextView)layoutInflater.findViewById(R.id.payCancelAction);
		payCancel.setOnClickListener(this);
		payAccount = (TextView)layoutInflater.findViewById(R.id.payAccount);
		alipay = (ImageView)layoutInflater.findViewById(R.id.aliyPayIcon);
		wXpay = (ImageView)layoutInflater.findViewById(R.id.wXPayIcon);
		wXpayLayout = (RelativeLayout)layoutInflater.findViewById(R.id.wXlayout) ;
		wXpayLayout.setOnClickListener(this);
		alipayLayout = (RelativeLayout)layoutInflater.findViewById(R.id.aliylayout) ;
		alipayLayout.setOnClickListener(this);
		payAction = (TextView) layoutInflater.findViewById(R.id.paySelectFinish);
		payAction.setOnClickListener(this);
		wXpay.setVisibility(View.INVISIBLE);
		addView(layoutInflater);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
	}

	public void setData(Context context, Theme page, OrderPayBuilder orderPayBuilder){
		String  account = MoneyConverter.conversionString(orderPayBuilder.paidMoney);
		payAccount.setText(Html.fromHtml(getResources().getString(R.string.shopsdk_default_pay_account_show,formatMoney(account,2))));
		this.context = context;
		this.page = page;
		this.orderPayBuilder = orderPayBuilder;
	}

	public static String formatMoney(String s, int len)
	{
		if (s == null || s.length() < 1) {
			return "";
		}
		NumberFormat formater = null;
		double num = Double.parseDouble(s);
		if (len == 0) {
			formater = new DecimalFormat("###,###");

		} else {
			StringBuffer buff = new StringBuffer();
			buff.append("###,###.");
			for (int i = 0; i < len; i++) {
				buff.append("#");
			}
			formater = new DecimalFormat(buff.toString());
		}
		String result = formater.format(num);
		if(result.indexOf(".") == -1)
		{
			result = "￥" + result + ".00";
		}
		else
		{
			result = "￥" + result;
		}
		return result;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id == R.id.payCancelAction){
			onClose();
		} else if(id == R.id.wXlayout){
			selectWxPayType();
		} else if(id == R.id.aliylayout){
			selectAliyPayType();
		} else if(id == R.id.paySelectFinish){
			pay();
		}
	}

	//关闭取消支付
	private void onClose(){
		if(payCancelImpl != null){
			payCancelImpl.payCancel();
		}
	}

	private void selectAliyPayType(){
		payType = 0;
		wXpay.setVisibility(View.INVISIBLE);
		alipay.setVisibility(View.VISIBLE);
	}

	private void selectWxPayType(){
		payType = 1;
		alipay.setVisibility(View.INVISIBLE);
		wXpay.setVisibility(View.VISIBLE);
	}

	private void pay(){
		if(payType == 0){
			orderPayBuilder.payWay = PayWay.ALIPAY;
		} else if(payType == 1){
			orderPayBuilder.payWay = PayWay.WECHAT;
		}
		PayManager.getInstance().pay(context,page,orderPayBuilder);
		if(payCancelImpl != null){
			payCancelImpl.onPay();
		}
	}

	public void setPayCancel(PayCancelImpl payCancelImpl){
		this.payCancelImpl = payCancelImpl;
	}

	public interface PayCancelImpl{
		void payCancel();
		void onPay();
	}
}
