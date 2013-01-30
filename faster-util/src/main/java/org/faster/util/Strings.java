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
package org.faster.util;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author sqwen
 */
public class Strings {

	public static final String DEFAULT_DELIMITER = "&&";

	private static final String FORMAT_PATTERN = "{0,choice,0#|1#1 天, }{1,number,integer}:{2,number,00}:{3,number,00}.{4,number,00}";

	private static final Map<String, Pattern> patternMap = new HashMap<String, Pattern>();

	/**
	 * 防止被实例化
	 */
	private Strings() {}

	public static void rejectIfNullOrEmpty(String text, String errMessage) {
		if (text == null || text.trim().length() == 0) {
			throw new IllegalArgumentException(errMessage);
		}
	}

	public static boolean isNullOrEmpty(String text) {
		return text == null || text.length() == 0;
	}

	public static boolean isBlank(String text) {
		return text == null || text.trim().length() == 0;
	}

	public static Matcher getFirstMatch(String text, String regEx) {
		Pattern pattern = getPattern(regEx);
		return pattern.matcher(text);
	}

	public static Pattern getPattern(String regEx) {
		return getPattern(regEx, true);
	}

	public static Pattern getPattern(String uncompiledPattern, boolean regex) {
		String key = uncompiledPattern + regex;
		Pattern pattern = patternMap.get(key);
		if (pattern == null) {
			synchronized (patternMap) {
				pattern = patternMap.get(key);
				if (pattern == null) {
					if (regex) {
						pattern = Pattern.compile(uncompiledPattern,
								Pattern.MULTILINE);
					} else {
						pattern = Pattern.compile(uncompiledPattern,
								Pattern.LITERAL);
					}
					patternMap.put(key, pattern);
				}
			}
		}
		return pattern;
	}

	public static boolean find(String pattern, String chars) {
		return find(pattern, true, chars);
	}

	public static boolean find(String pattern, boolean regex,
			List<String> charsList) {
		Pattern p = Strings.getPattern(pattern, regex);
		for (String chars : charsList) {
			Matcher m = p.matcher(chars);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

	public static boolean find(String pattern, boolean regex, String chars) {
		Pattern p = Strings.getPattern(pattern, regex);
		Matcher m = p.matcher(chars);
		return m.find();
	}

	public static String findMatchString(String pattern, boolean regex,
			List<String> charsList) {
		Pattern p = Strings.getPattern(pattern, regex);
		for (String chars : charsList) {
			Matcher m = p.matcher(chars);
			if (m.find()) {
				return chars.substring(m.start(), m.end());
			}
		}
		return null;
	}

	public static String findMatchString(String pattern, boolean regex,
			String chars) {
		Pattern p = Strings.getPattern(pattern, regex);
		Matcher m = p.matcher(chars);
		if (!m.find()) {
			return null;
		}
		return chars.substring(m.start(), m.end());
	}

	public static String findMatchString(String regEx, String chars) {
		Pattern p = Strings.getPattern(regEx);
		Matcher m = p.matcher(chars);
		if (!m.find()) {
			return null;
		}
		return chars.substring(m.start(), m.end());
	}

	/**
	 * 用默认的分隔符将指定的字符串列表转换为字符串
	 *
	 * @param list
	 *            需要转换的字符串列表
	 * @return 转换成的字符串
	 */
	public static String toString(Collection<String> list) {
		return toString(list, DEFAULT_DELIMITER);
	}

	/**
	 * 用指定的分隔符将指定的字符串列表转换为字符串
	 *
	 * @param list
	 *            需要转换的字符串列表
	 * @param delimiter
	 *            所用的分隔符
	 * @return 转换成的字符串
	 */
	public static String toString(Collection<String> list, String delimiter) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (String str : list) {
			sb.append(str).append(delimiter);
		}
		sb.delete(sb.length() - delimiter.length(), sb.length());
		return sb.toString();
	}

	/**
	 * 用默认的分隔符，将指定的字符串转换为字符串列表
	 *
	 * @param str
	 *            需要转换的字符串
	 * @return 转换为的字符串列表
	 */
	public static Collection<String> toStringCollection(String str) {
		return toStringCollection(str, DEFAULT_DELIMITER);
	}

	/**
	 * 用指定的分隔符，将指定的字符串转换为字符串列表
	 *
	 * @param str
	 *            需要转换的字符串
	 * @param delimiter
	 *            所用的分隔符
	 * @return 转换为的字符串列表
	 */
	public static Collection<String> toStringCollection(String str,
			String delimiter) {
		if (str == null || str.length() == 0) {
			return java.util.Collections.emptyList();
		}
		String[] ids = str.split(delimiter);
		List<String> ret = new ArrayList<String>(ids.length);
		for (String id : ids) {
			ret.add(id.trim());
		}
		return ret;
	}

	public static List<String> toStringList(String str, String delimiter) {
		if (str == null || str.length() == 0) {
			return java.util.Collections.emptyList();
		}
		String[] ids = str.split(delimiter);
		List<String> ret = new ArrayList<String>(ids.length);
		for (String id : ids) {
			if (!isBlank(id)) {
				ret.add(id.trim());
			}
		}
		return ret;
	}

	public static Set<String> toStringSet(String str, String delimiter) {
		if (str == null || str.length() == 0) {
			return Collections.emptySet();
		}
		String[] ids = str.split(delimiter);
		Set<String> ret = new HashSet<String>(ids.length);
		for (String id : ids) {
			if (!isBlank(id)) {
				ret.add(id.trim());
			}
		}
		return ret;
	}

	public static final String[] toStringArray(String str, String delimiter) {
		List<String> list = toStringList(str, delimiter);
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 移除XML文本中的namespace部分
	 *
	 * @param xml
	 *            XML文本
	 * @return 没有namespace的xml文本
	 */
	public static String removeXmlNamespace(String xml) {
		String HEAD_FLAG = "xmlns=";
		String TAIL_FLAG = ">";
		int startIndex = xml.indexOf(HEAD_FLAG);
		if (startIndex < 0) {
			return xml;
		}
		StringBuilder buf = new StringBuilder();
		// startIndex - 1 is blank, so also to remove it
		buf.append(xml.substring(0, startIndex - 1));
		int endIndex = xml.indexOf(TAIL_FLAG, startIndex);
		while ((startIndex = xml.indexOf(HEAD_FLAG, endIndex)) > 0) {
			buf.append(xml.subSequence(endIndex, startIndex - 1));
			endIndex = xml.indexOf(TAIL_FLAG, startIndex);
		}
		buf.append(xml.substring(endIndex, xml.length()));
		return buf.toString();
	}

	/**
	 * 删除特定的XML namespace的标记
	 *
	 * @param xml
	 *            xml文本
	 * @param namespace
	 *            名字空间
	 * @return 去掉指定名字空间标记的xml文本
	 */
	public static String removeXmlNamespace(String xml, String namespace) {
		return removeSpecifiedString(xml, namespace, ":");
	}

	/**
	 * 删除特定两个标志间的内容(包括两个标记)
	 *
	 * @param content
	 *            需要处理的字符串内容
	 * @param headFlag
	 *            头标签
	 * @param tailFlag
	 *            尾标签
	 * @return 删除了头和尾的标签
	 */
	private static String removeSpecifiedString(String content,
			String headFlag, String tailFlag) {
		int startIndex = content.indexOf(headFlag);
		if (startIndex < 0) {
			return content;
		}
        StringBuilder buf = new StringBuilder();
		buf.append(content.substring(0, startIndex));
		int endIndex = content.indexOf(tailFlag, startIndex);
		while ((startIndex = content.indexOf(headFlag, endIndex)) > 0) {
			buf.append(content.subSequence(endIndex, startIndex));
			endIndex = content.indexOf(tailFlag, startIndex);
		}
		buf.append(content.substring(endIndex, content.length()));
		return buf.toString();
	}

	/**
	 * 将以点分隔的字符串转换为明文的形式
	 *
	 * @param rawText 以点分隔的字符串
	 * @return 明文
	 */
	public static String dottedOctetsToPlainText(String rawText) {
		return stringToPlainText(rawText, "\\.", 10);
	}

	/**
	 * 将十六进制字符串转换为明文形式
	 *
	 * @param hexString 十六进制字符串
	 * @return 明文
	 */
	public static String hexStringToPlainText(String hexString) {
		return stringToPlainText(hexString, "\\s", 16);
	}

	/**
	 * 将指定的字符串转换为明文形式
	 *
	 * @param srcText
	 *            源字符串
	 * @param splitReg
	 *            字符串分隔正折表达式
	 * @param radix
	 *            字符串进制
	 * @return 明文文本
	 */
	public static String stringToPlainText(String srcText, String splitReg,
			int radix) {
		String[] fields = srcText.split(splitReg);
		byte[] chars = new byte[fields.length];
		for (int i = 0; i < fields.length; i++) {
			byte c = (byte) Integer.parseInt(fields[i], radix);
			chars[i] = c;
		}
		try {
			return new String(chars, "GB2312");
		} catch (UnsupportedEncodingException e) {
			return new String(chars);
		}
	}

	/**
	 * 将百分之一秒的时间转换为字符串形式
	 *
	 * @param timeticks
	 *            百分之一秒表示的时间
	 * @return 字符串形式
	 */
	public static String convertTimeTicksToString(long timeticks) {
		long days = timeticks / 8640000L;
		timeticks %= 8640000L;
		long hours = timeticks / 360000L;
		timeticks %= 360000L;
		long minutes = timeticks / 6000L;
		timeticks %= 6000L;
		long seconds = timeticks / 100L;
		timeticks %= 100L;
		long centiSeconds = timeticks;
		Long[] var_longs = new Long[] {days, hours, minutes, seconds, centiSeconds};
		return MessageFormat.format(FORMAT_PATTERN, (Object[]) var_longs);
	}

	/**
	 * 将集合里面的对象用字符串来表示
	 *
	 * @param objs
	 *            集合对象
	 * @return 字符串表示的集合对象
	 */
	public static String collectionToString(Collection<?> objs) {
		if (objs == null) {
			return null;
		}
        StringBuilder sb = new StringBuilder();
		int i = 1;
		for (Object obj : objs) {
			sb.append("\t" + i++ + " - " + obj + "\n");
		}
		return sb.toString();
	}



	public static int intValueOf(String intStr) {
		if (intStr == null || intStr.length() == 0) {
			return 0;
		}
		try {
			return Integer.valueOf(intStr);
		} catch (Exception e) {
			return 0;
		}
	}

	public static long longValueOf(String longStr) {
		if (longStr == null || longStr.length() == 0) {
			return 0;
		}
		try {
			return Long.valueOf(longStr);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取指定标记后的整个字符串，只获取符合条件的第一个字符串 示例：
	 * StringHelper.getWholeWordAfter(" Instance ID             : 0 ", ": ") =
	 * "0" StringHelper.getWholeWordAfter(
	 * " Instance Status         : Active       LSR ID               : 221.130.209.10 "
	 * , ": ") = "Active"
	 *
	 * @param srcText 原文本
	 * @param token 标记
     * @return 原文件某个标记之后的第一个字符串
	 */
	public static String getWholeWordAfter(String srcText, String token) {
		if (srcText == null) {
			return null;
		}
		if (token == null) {
			token = " ";
		}
		int tokenIndex = srcText.indexOf(token);
		if (tokenIndex < 0) {
			return null;
		}
		int startIndex = tokenIndex + token.length();
		int endIndex = srcText.indexOf(" ", startIndex);
		if (endIndex < 0) {
			endIndex = srcText.length();
		}
		return srcText.substring(startIndex, endIndex);
	}

	/**
	 * 获取指定标记后的整个字符串，获取符合条件的所有字符串 示例：
	 * StringHelper.getAllWholeWordsAfter(" Instance ID             : 0 ", ": ")
	 * = {"0"} StringHelper.getAllWholeWordsAfter(
	 * " Instance Status         : Active       LSR ID               : 221.130.209.10 "
	 * , ": ") = {"Active", "221.130.209.10"}
	 *
	 * @param srcText 原文本
	 * @param token 标记
	 * @return 原文件某个标记之后的所有字符串
	 */
	public static String[] getAllWholeWordsAfter(String srcText, String token) {
		if (srcText == null) {
			return null;
		}
		if (token == null) {
			token = " ";
		}
		List<String> ret = new LinkedList<String>();
		int tokenIndex;
		while ((tokenIndex = srcText.indexOf(token)) >= 0) {
			int startIndex = tokenIndex + token.length();
			int endIndex = srcText.indexOf(" ", startIndex);
			if (endIndex < 0) {
				endIndex = srcText.length();
			}
			ret.add(srcText.substring(startIndex, endIndex));
			srcText = srcText.substring(endIndex);
		}
		return ret.toArray(new String[ret.size()]);
	}

	/**
	 * 将一个字符串转为Long数组
	 *
	 * @param stringWithDelimiter
	 *            原字符串
	 * @param delimiterPattern
	 *            字符串里面的数字分隔符正则表达式
	 * @return Long数组
	 */
	public static final Long[] toLongArray(String stringWithDelimiter,
			String delimiterPattern) {
		if (isBlank(stringWithDelimiter)) {
			return new Long[0];
		}
		String[] fields = stringWithDelimiter.split(delimiterPattern);
		Long[] array = new Long[fields.length];
		for (int i = 0; i < fields.length; i++) {
			array[i] = Long.valueOf(fields[i]);
		}
		return array;
	}

	/**
	 * 将一个字符串转为Long数组，里面的数字用逗号分隔
	 *
	 * @param stringWithDelimiter
	 *            原字符串
	 * @return Long数组
	 */
	public static final Long[] toLongArray(String stringWithDelimiter) {
		return toLongArray(stringWithDelimiter, ",\\s*");
	}

	/**
	 * 将一个字符串转为Integer数组
	 *
	 * @param stringWithDelimiter
	 *            原字符串
	 * @param delimiterPattern
	 *            字符串里面的数字分隔符正则表达式
	 * @return Integer数组
	 */
	public static final Integer[] toIntegerArray(String stringWithDelimiter,
			String delimiterPattern) {
		if (stringWithDelimiter == null) {
			return new Integer[0];
		}
		String[] fields = stringWithDelimiter.trim().split(delimiterPattern);
		List<Integer> ret = new ArrayList<Integer>(fields.length);
		for (String item : fields) {
            item = item.trim();
			if (item.isEmpty()) {
				continue;
			}
			ret.add(Integer.valueOf(item));
		}
		return ret.toArray(new Integer[ret.size()]);
	}

	/**
	 * 将一个字符串转为Integer数组，里面的数字用逗号分隔
	 *
	 * @param stringWithDelimiter
	 *            原字符串
	 * @return Integer数组
	 */
	public static final Integer[] toIntegerArray(String stringWithDelimiter) {
		return toIntegerArray(stringWithDelimiter, ",\\s*");
	}

	/**
	 * 将一个字符串转为int数组
	 *
	 * @param stringWithDelimiter
	 *            原字符串
	 * @param delimiterPattern
	 *            字符串里面的数字分隔符正则表达式
	 * @return int数组
	 */
	public static final int[] toIntArray(String stringWithDelimiter,
			String delimiterPattern) {
		if (stringWithDelimiter == null) {
			return new int[0];
		}
		String[] fields = stringWithDelimiter.split(delimiterPattern);
		int[] array = new int[fields.length];
		for (int i = 0; i < fields.length; i++) {
			array[i] = Integer.parseInt(fields[i]);
		}
		return array;
	}

	/**
	 * 将一个字符串转为int数组，里面的数字用逗号分隔
	 *
	 * @param stringWithDelimiter
	 *            原字符串
	 * @return int数组
	 */
	public static final int[] toIntArray(String stringWithDelimiter) {
		return toIntArray(stringWithDelimiter, ",\\s*");
	}

	/**
	 * 获取备选数组中第一个非空的字符值
	 *
	 * @param candidates
	 *            备选值
	 * @return 第一个非空的字符串
	 */
	public static final String getFirstNotBlankValue(String... candidates) {
		if (candidates == null) {
			return null;
		}

		for (String v : candidates) {
			if (isNotBlank(v)) {
				return v;
			}
		}
		return null;
	}

}
