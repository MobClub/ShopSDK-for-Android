package com.mob.ums.gui.themes.defaultt.components;

import android.content.Context;
import android.view.View;

public class HeaderFooterProvider {
	private enum Style {
		DEFAULT,
		WHITE
	}
	
	private Context context;
	private HeaderFooterView llHeader;
	private HeaderFooterView footerView;
	private boolean type;
	private Style style;
	
	public HeaderFooterProvider(Context context) {
		this.context = context;
		style = Style.DEFAULT;
	}
	
	public void setWhiteStyle() {
		style = Style.WHITE;
	}
	
	public View getHeaderView() {
		if (llHeader == null) {
			llHeader = new HeaderFooterView(context) {
				protected String getPullArrowResName() {
					switch (style) {
						case WHITE: return "umssdk_default_ptr_arr_d_white";
						default: return "umssdk_default_ptr_arr_d";
					}
				}
				
				protected String getReleaseArrowResName() {
					switch (style) {
						case WHITE: return "umssdk_default_ptr_arr_u_white";
						default: return "umssdk_default_ptr_arr_u";
					}
				}
				
				protected String getPullMessageResName() {
					return "umssdk_default_pull_to_refresh";
				}
				
				protected String getReleaseMessageResName() {
					return "umssdk_default_release_to_refresh";
				}
				
				protected String getLoadingMessageResName() {
					return "umssdk_default_refreshing";
				}
				
				protected int getTextColor() {
					switch (style) {
						case WHITE: return 0xffffffff;
						default: return 0xff979797;
					}
				}
			};
		}
		return llHeader;
	}
	
	public void onPullDown(int percent) {
		llHeader.onPull(percent);
	}
	
	public void onRefresh() {
		type = true;
		llHeader.onRequest();
	}
	
	public View getFooterView() {
		if (footerView == null) {
			footerView = new HeaderFooterView(context) {
				protected String getPullArrowResName() {
					switch (style) {
						case WHITE: return "umssdk_default_ptr_arr_u_white";
						default: return "umssdk_default_ptr_arr_u";
					}
				}
				
				protected String getReleaseArrowResName() {
					switch (style) {
						case WHITE: return "umssdk_default_ptr_arr_d_white";
						default: return "umssdk_default_ptr_arr_d";
					}
				}
				
				protected String getPullMessageResName() {
					return "umssdk_default_pull_to_request_next";
				}
				
				protected String getReleaseMessageResName() {
					return "umssdk_default_release_to_refresh";
				}
				
				protected String getLoadingMessageResName() {
					return "umssdk_default_refreshing";
				}
				
				protected int getTextColor() {
					switch (style) {
						case WHITE: return 0xffffffff;
						default: return 0xff979797;
					}
				}
			};
		}
		return footerView;
	}
	
	public void onPullUp(int percent) {
		footerView.onPull(percent);
	}
	
	public void onRequestNext() {
		type = false;
		footerView.onRequest();
	}
	
	public void onReversed() {
		if (type) {
			llHeader.reverse();
		} else {
			footerView.reverse();
		}
	}
}
