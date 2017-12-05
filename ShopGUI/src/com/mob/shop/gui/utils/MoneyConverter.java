package com.mob.shop.gui.utils;


import java.text.DecimalFormat;

public class MoneyConverter {
	public static double conversionDouble(int money) {
		return (double) money / 100;
	}

	public static int conversionInt(int money) {
		return money / 100;
	}

	public static String conversionString(int money) {
		return money % 100 == 0 ? String.valueOf(conversionInt(money)) : String.valueOf(new DecimalFormat("0.00")
				.format(conversionDouble(money)));
	}

	public static int toCent(String money) {
		return (int)(Double.parseDouble(money) * 100);
	}

	public static String conversionMoneyStr(int money){
		return "￥" + conversionString(money);
	}
}
