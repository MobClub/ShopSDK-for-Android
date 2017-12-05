package com.mob.shop.gui.themes.defaultt.entity;

import android.content.Context;

import com.mob.shop.gui.base.Page;

/**
 * Created by yjin on 2017/11/2.
 */

public class PayEntity {
	private Context context;
	private Page page;
	private String noSerial;
	private String subject;
	private String body;
	private int account;

	public PayEntity(){

	}
	public PayEntity(Context context, Page page, String noSerial, String subject, String body, int account) {
		this.context = context;
		this.page = page;
		this.noSerial = noSerial;
		this.subject = subject;
		this.body = body;
		this.account = account;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getNoSerial() {
		return noSerial;
	}

	public void setNoSerial(String noSerial) {
		this.noSerial = noSerial;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}
}
