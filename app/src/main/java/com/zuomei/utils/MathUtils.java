package com.zuomei.utils;

import java.math.BigDecimal;

public class MathUtils {

	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	/**
	 * 加法运算
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double add(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.add(b2).doubleValue();
	}
	
	public static double add(double d1, double d2,double d3,double d4) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		BigDecimal b3 = new BigDecimal(Double.toString(d3));
		BigDecimal b4 = new BigDecimal(Double.toString(d4));
		return b1.add(b2).add(b3).add(b4).doubleValue();
	}

	
	/**
	 * 减法运算
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double sub(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(Double.toString(d1));
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 乘法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 除法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
