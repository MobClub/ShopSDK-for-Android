package com.mob.shop.gui.themes.defaultt.entity;


import com.mob.shop.datatype.EnumType;

public enum RefundReason implements EnumType {
	NOTWANT("shopsdk_default_refund_reason_notwant"),
	DESCRIBENOTCORRECT("shopsdk_default_refund_reason_describenotcorrect"),
	MISSING("shopsdk_default_refund_reason_missing"),
	DELIVERNOTCORRECT("shopsdk_default_refund_reason_delivernotcorrect"),
	NOTAPPOINTDELIVER("shopsdk_default_refund_reason_notappointdeliver"),
	OTHER("shopsdk_default_refund_reason_other");

	private String resName;

	RefundReason(String resName) {
		this.resName = resName;
	}

	public String getResName() {
		return resName;
	}
}
