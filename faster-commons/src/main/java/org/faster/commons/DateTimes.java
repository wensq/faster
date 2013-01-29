/*
 * Copyright (c) 2013 @iSQWEN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.faster.commons;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author sqwen
 */
public class DateTimes {

	public static final long DAY = 86400000;

	public static final long HOUR = 3600000;

	public static final long MINUTE = 60000;

	public static final long SECOND = 1000;

	public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String TIMESTAMP_PATTERN_SLASH = "yyyy/MM/dd HH:mm:ss";

	public static final String TIMESTAMP_PATTERN_UNDERSCORE = "yyyy-MM-dd_HH:mm:ss";

	public static final String TIMESTAMP_FORMAT_TRUNCATE_SECOND = "yyyy-MM-dd HH:mm:00";

	public static final String TIMESTAMP_FORMAT_TRUNCATE_MINUTE = "yyyy-MM-dd HH:00:00";

	public static final String SHORT_TIMESTAMP_PATTERN = "MM-dd HH:mm";

	public static final String DATE_PATTERN = "yyyy-MM-dd";

	public static final String TIMESTAMP_PATTERN_COMPACT = "yyyyMMddHHmmss";

	public static final String[] COMMON_TIMESTAMP_PATTERN = new String[] { TIMESTAMP_PATTERN,
			TIMESTAMP_PATTERN_UNDERSCORE };

	private DateTimes() {}

	public static final String formatDateToString(Date date, String formatPattern) {
		if (date == null) {
			return "";
		}
		return FastDateFormat.getInstance(formatPattern).format(date);
	}

	/**
	 * 将日期转换为时间戳格式的字符串
	 *
	 * @param date
	 *            日期对象
	 * @return 时间戳格式的字符串
	 */
	public static final String formatDateToTimestampString(Date date) {
		return formatDateToString(date, TIMESTAMP_PATTERN);
	}

	public static final String formatDateToShortTimestampString(Date date) {
		return formatDateToString(date, SHORT_TIMESTAMP_PATTERN);
	}

	public static final String formatDateToDateString(Date date) {
		return formatDateToString(date, DATE_PATTERN);
	}

	public static final String nowToTimestampString() {
		return formatDateToTimestampString(new Date());
	}

	public static final String formatDateToCompactTimestampString(Date date) {
		return formatDateToString(date, TIMESTAMP_PATTERN_COMPACT);
	}

	public static final String nowCompactTimestampString() {
		return formatDateToCompactTimestampString(new Date());
	}

	/**
	 * 字符串转换为其格式所符合的日期类型
	 */
	public final static Date parseStringToDate(String str, String pattern) {
		try {
			return DateUtils.parseDate(str, pattern);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Date[" + str
					+ "] doesn't match pattern[" + pattern + "].", e);
		}
	}

	/**
	 * 字符串转换为其格式所符合的日期类型
	 *
	 * @param str
	 *            将要被转换的字符串
	 * @param patterns
	 *            比如2010-05-26 16:11:00
	 * @return 日期对象
	 */
	public static final Date parseStringToDate(String str, String[] patterns) {
		try {
			return DateUtils.parseDate(str, patterns);
		} catch (ParseException e) {
			String pattern = StringUtils.join(patterns, ",");
			throw new IllegalArgumentException("Date[" + str
					+ "] doesn't match pattern[" + pattern + "].", e);
		}
	}

	public static final Date parseStringToDate(String timeString) {
		return parseStringToDate(timeString, COMMON_TIMESTAMP_PATTERN);
	}

	public static final Date parseStringToDateAndTruncateToMinute(String timeString) {
		Date date = parseStringToDate(timeString);
		return DateUtils.truncate(date, Calendar.MINUTE);
	}

	public static final Date parseStringToDateAndTruncateToHour(String timeString) {
		Date date = parseStringToDate(timeString);
		return DateUtils.truncate(date, Calendar.HOUR_OF_DAY);
	}

	public static final Date parseStringToDateAndTruncateToDate(String timeString) {
		Date date = parseStringToDate(timeString);
		return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
	}

	public static final long convertToMilliSecond(int day, int hour, int minute,
			int second, int ms) {
		return day * DAY + hour * HOUR + minute * MINUTE + second * SECOND + ms;
	}

	public static final String formatIntervalToHumanReadableString(int day, int hour,
                                                                   int minute, int second, int ms) {
		StringBuilder sb = new StringBuilder();
		if (day > 0) {
			sb.append(day + "天");
		}
		if (hour > 0) {
			sb.append(hour + "小时");
		}
		if (minute > 0) {
			sb.append(minute + "分钟");
		}
		if (second > 0) {
			sb.append(second + "秒");
		}
		if (ms > 0) {
			sb.append(ms + "毫秒");
		}
		return sb.toString();
	}

	public static long getCurrentTimeId() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return convertCalendarToTimeId(cal);
	}

	public static final long convertDateToTimeId(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return convertCalendarToTimeId(cal);
	}

	public static final long convertCalendarToTimeId(Calendar cal) {
		long year = cal.get(Calendar.YEAR);
		long month = cal.get(Calendar.MONTH) + 1;
		long day = cal.get(Calendar.DAY_OF_MONTH);
		long hour = cal.get(Calendar.HOUR_OF_DAY);
		long minute = cal.get(Calendar.MINUTE);
		long second = cal.get(Calendar.SECOND);
		return year * 10000000000L + month * 100000000 + day * 1000000 + hour
				* 10000 + minute * 100 + second;
	}

	public static final Date convertTimeIdToDate(long timeId) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, (int) (timeId / 10000000000L));
		int left = (int) (timeId % 10000000000L);
		cal.set(Calendar.MONTH, left / 100000000 - 1);
		left = left % 100000000;
		cal.set(Calendar.DAY_OF_MONTH, left / 1000000);
		left = left % 1000000;
		cal.set(Calendar.HOUR_OF_DAY, left / 10000);
		left = left % 10000;
		cal.set(Calendar.MINUTE, left / 100);
		left = left % 100;
		cal.set(Calendar.SECOND, left);
		return cal.getTime();
	}

	public static long getTimeIdBeforeHour(int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, -hours);
		return convertCalendarToTimeId(cal);
	}

	/**
	 * 是否为周末
	 *
	 * @param date
	 *            指定的日期
	 * @return 指定的日期是否为周末
	 */
	public final static boolean isWeekend(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("date must not be null!");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
	}

	public final static int getHourOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public final static int getMinute(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);
	}

	/**
	 *
	 * @param date 日期
	 * @return 某天的开始时间
	 */
	public final static Date getDayDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 用于获取指定date下一天的日期,时分秒为0；
	 *
	 * @param date
	 *            指定的日期
	 * @return 下一天开始的时间
	 */
	public final static Date getNextDay(Date date) {
		return getRecentDay(date, 1);
	}

	/**
	 * 用于获取指定date上一天的日期,时分秒为0；
	 *
	 * @param date
	 *            指定的日期
	 * @return 上一天开始的时间
	 */
	public final static Date getPreviousDay(Date date) {
		return getRecentDay(date, -1);
	}

	public final static Date getRecentDay(Date date, Integer days) {
		date = getDayDate(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	/**
	 *
	 * @param hour
	 *            设定时间范围参数之一,单位:小时.
	 * @param minute
	 *            设定时间范围的参数之一,单位:分钟.
	 * @param second
	 *            设定时间范围的参数之一,单位:秒.
	 * @return Date date一个指定时间范围的对象即一个时间点
	 */
	public final static Date convertToTodayTime(int hour, int minute, int second) {
		Calendar time = Calendar.getInstance();
		time.add(Calendar.HOUR, hour);
		time.add(Calendar.MINUTE, minute);
		time.add(Calendar.SECOND, second);
		return time.getTime();
	}

	public final static Date getTimeAfterMinutes(int afterMinutes) {
		Calendar time = Calendar.getInstance();
		time.add(Calendar.MINUTE, afterMinutes);
		return time.getTime();
	}

	public static final Date buildDateNextHours(int n) {
		Calendar calTime = Calendar.getInstance();
		calTime.add(Calendar.HOUR_OF_DAY, n);
		return DateUtils.truncate(calTime.getTime(), Calendar.HOUR_OF_DAY);
	}

	/**
	 * 取当前时间整点时刻的+n个小时的的整点字符串
	 *
	 * @return 当前时间整点时刻的+n个小时的的整点字符串
	 */
	public static final String buildTimestampStringNextHours(int n) {
		Date date = buildDateNextHours(n);
		return formatDateToTimestampString(date);
	}

	public static String buildDateStringNextHours(int n) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calTime = Calendar.getInstance();
		calTime.add(Calendar.DAY_OF_MONTH, n);
		Date date = calTime.getTime();
		return simpleDateFormat.format(date);
	}

	/**
	 * 将单位为毫秒的时间间隔，转换为易读的字符串，精确到秒 如: 99 -> 99毫秒 2000 -> 2秒 60000 -> 1分钟 3600000
	 * -> 1小时 3660200 -> 1小时1分钟(忽略毫秒)
	 *
	 * @param interval
	 *            以单位为毫秒的时间间隔
	 * @return 易读的字符串
	 */
	public static final String formatIntervalToReadableString(long interval) {
		int day = (int) (interval / DAY);
		int hour = (int) (interval % DAY / HOUR);
		int minute = (int) (interval % HOUR / MINUTE);
		int second = (int) (interval % MINUTE / SECOND);
		return formatIntervalToHumanReadableString(day, hour, minute, second, 0);
	}

	/**
	 * 将单位为毫秒的时间间隔，转换为易读的字符串，精确到毫秒 如: 99 -> 99毫秒 2000 -> 2秒 60000 -> 1分钟 3600000
	 * -> 1小时 3660200 -> 1小时1分钟200毫秒
	 *
	 * @param interval
	 *            以单位为毫秒的时间间隔
	 * @return 易读的字符串
	 */
	public static final String formatIntervalToReadableStringWithMilliSecond(long interval) {
		int day = (int) (interval / DAY);
		int hour = (int) (interval % DAY / HOUR);
		int minute = (int) (interval % HOUR / MINUTE);
		int second = (int) (interval % MINUTE / SECOND);
		int ms = (int) (interval % SECOND);
		return formatIntervalToHumanReadableString(day, hour, minute, second, ms);
	}

	/**
	 * 将单位为毫秒的时间间隔，转换为易读的字符串，精确到秒,将毫秒数转换为妙
	 *
	 * @param interval 以单位为毫秒的时间间隔
	 * @return 易读的字符串
	 */
	public static final String formatIntervalToReadablePrecisionTime(long interval) {
		int day = (int) (interval / DAY);
		int hour = (int) (interval % DAY / HOUR);
		int minute = (int) (interval % HOUR / MINUTE);
		int second = (int) (interval % MINUTE / SECOND);
		int ms = (int) (interval % SECOND);
		return formatToReadableTimeString(day, hour, minute, second, ms);
	}

	public static final String formatToReadableTimeString(int day, int hour,
			int minute, int second, int ms) {
		StringBuilder sb = new StringBuilder();
		double secondNew = second;
		if (day > 0) {
			sb.append(day + "天");
		}
		if (hour > 0) {
			sb.append(hour + "小时");
		}
		if (minute > 0) {
			sb.append(minute + "分钟");
		}
		if (ms > 0) {
			secondNew += (double) ms / SECOND;
		}
		if (secondNew > 0) {
			sb.append(format(secondNew, 2) + "秒");
		}
		return sb.toString();
	}

	private static double format(double value, int scale) {
		return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP)
				.doubleValue();
	}

}
