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

/**
 * User: sqwen
 */
public class BooleanUtils {

    /**
     * 将布尔值转换为指定的字符串显示，而不是默认的true、false
     *
     * @param b
     *            需要转换的布尔值
     * @param trueDisplayString
     *            true时显示的字符串
     * @param falseDisplayString
     *            false时显示的字符串
     * @return 指定的字符串
     */
    public static String booleanToString(boolean b, String trueDisplayString,
                                         String falseDisplayString) {
        return b ? trueDisplayString : falseDisplayString;
    }

    /**
     * 将指定的字符串根据真值列表转换为布尔值，考虑大小写
     *
     * @param boolString
     *            需要检测的字符串
     * @param trueDisplayStrings
     *            一串字符串参数，符合其中任何一个参数就转化为true
     * @return 布尔值
     */
    public static boolean booleanFromTrueString(String boolString,
                                                String... trueDisplayStrings) {
        for (String trueDisplayString : trueDisplayStrings) {
            if (boolString.equals(trueDisplayString)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将指定的字符串根据真值列表转换为布尔值，忽略大小写
     *
     * @param boolString
     *            需要检测的字符串
     * @param trueDisplayStrings
     *            一串字符串参数，不管大小写，符合其中任何一个参数就转化为true
     * @return 布尔值
     */
    public static boolean booleanFromTrueStringIgnoreCase(String boolString,
                                                          String... trueDisplayStrings) {
        for (String trueDisplayString : trueDisplayStrings) {
            if (boolString.equalsIgnoreCase(trueDisplayString)) {
                return true;
            }
        }
        return false;
    }

    public static String booleanToOnOffString(boolean b) {
        return booleanToString(b, "On", "Off");
    }

    public static boolean booleanFromOnOffString(String boolString) {
        return booleanFromTrueStringIgnoreCase(boolString, "On");
    }

}
