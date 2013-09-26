/**
 * LatLngBound.java was created on 2011-6-30 下午04:34:28
 *
 * Copyright (c) 2010 EASTCOM Software Technology Co., Ltd. All rights reserved.
 */
package org.faster.orm.criteria;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 经纬度区域
 *
 * @author sqwen
 * @version 1.0, 2011-6-30
 */
public class LatLngBound {

	private static String AREA_PATTERN = "(\\d+|\\d+\\.\\d+),(\\d+|\\d+\\.\\d+)-(\\d+|\\d+\\.\\d+),(\\d+|\\d+\\.\\d+)";

	private Double topLeftLatitude;

	private Double topLeftLongitude;

	private Double bottomRightLatitude;

	private Double bottomRightLongitude;

	private boolean valid = false;

	//--------------------
	//	自定义函数
	//--------------------

	public LatLngBound(String area) {
		Pattern p = Pattern.compile(AREA_PATTERN);
		Matcher m = p.matcher(area);
		if (m.matches()) {
			valid = true;
			topLeftLatitude = Double.valueOf( m.group(1) );
			topLeftLongitude = Double.valueOf( m.group(2) );
			bottomRightLatitude = Double.valueOf( m.group(3) );
			bottomRightLongitude = Double.valueOf( m.group(4) );
		}
	}

	public Double getMinLatitude() {
		return Math.min(topLeftLatitude, bottomRightLatitude);
	}

	public Double getMaxLatitude() {
		return Math.max(topLeftLatitude, bottomRightLatitude);
	}

	public Double getMinLongitude() {
		return Math.min(topLeftLongitude, bottomRightLongitude);
	}

	public Double getMaxLongitude() {
		return Math.max(topLeftLongitude, bottomRightLongitude);
	}

	//--------------------
	//	getter/setter
	//--------------------

	public boolean isValid() {
		return valid;
	}

	public Double getTopLeftLatitude() {
		return topLeftLatitude;
	}

	public void setTopLeftLatitude(Double topLeftLatitude) {
		this.topLeftLatitude = topLeftLatitude;
	}

	public Double getTopLeftLongitude() {
		return topLeftLongitude;
	}

	public void setTopLeftLongitude(Double topLeftLongitude) {
		this.topLeftLongitude = topLeftLongitude;
	}

	public Double getBottomRightLatitude() {
		return bottomRightLatitude;
	}

	public void setBottomRightLatitude(Double bottomRightLatitude) {
		this.bottomRightLatitude = bottomRightLatitude;
	}

	public Double getBottomRightLongitude() {
		return bottomRightLongitude;
	}

	public void setBottomRightLongitude(Double bottomRightLongitude) {
		this.bottomRightLongitude = bottomRightLongitude;
	}

}
