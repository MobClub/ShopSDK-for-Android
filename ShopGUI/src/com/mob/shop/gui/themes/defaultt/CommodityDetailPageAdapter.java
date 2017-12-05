package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.CommodityDetailPage;
import com.mob.shop.gui.pages.dialog.ProgressDialog;

/**
 * 商品详情浏览页
 */
public class CommodityDetailPageAdapter extends DefaultThemePageAdapter<CommodityDetailPage> {
	private CommodityDetailPage page;
	private WebView webView;
	private ImageView iv;
	private ProgressDialog pd;

	@Override
	public void onCreate(CommodityDetailPage page, Activity activity) {
		super.onCreate(page, activity);
		this.page = page;
		View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_commoditydetail,
				null);
		activity.setContentView(view);
		findView(view);
		initView();
		initListener();
		webView.loadUrl(page.getUrl());
	}

	private void findView(View view) {
		webView = (WebView) view.findViewById(R.id.webView);
		iv = (ImageView) view.findViewById(R.id.ivBack);

	}

	private void initView() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				dissmissProgressDialog();

			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				showProgressDialog();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				dissmissProgressDialog();
			}
		});
	}

	private void initListener() {
		iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (webView.canGoBack()) {
					webView.goBack();
				} else {
					finish();
				}
			}
		});
	}

	@Override
	public boolean onKeyEvent(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
		}
		return super.onKeyEvent(keyCode, event);
	}

	private void showProgressDialog() {
		if (pd == null) {
			pd = new ProgressDialog.Builder(page.getContext(), page.getTheme()).show();
		} else if (!pd.isShowing()) {
			pd.show();
		}
	}

	private void dissmissProgressDialog() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}

	@Override
	public void onDestroy(CommodityDetailPage page, Activity activity) {
		super.onDestroy(page, activity);
		if (pd != null) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
			pd = null;
		}
	}
}
