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
package org.faster.opm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 嵌套参数解析器
 *
 * @author sqwen
 * @version 1.0, 2012-5-13
 */
public class NestParamParser {

    // 同级字段分隔符
    private char fieldDelimiter = ',';

    // 子字段开始标记
    private char startChildTag = '[';

    // 子字段结束标记
    private char endChildTag = ']';

    // ESCAPE标记
    private char escapeTag = '\\';

    // 是否对取到的值进行trim()操作
    private boolean trim = true;

    public NestParamParser() {}

    public char getFieldDelimiter() {
        return fieldDelimiter;
    }

    public void setFieldDelimiter(char fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    public char getStartChildTag() {
        return startChildTag;
    }

    public void setStartChildTag(char startChildTag) {
        this.startChildTag = startChildTag;
    }

    public char getEndChildTag() {
        return endChildTag;
    }

    public void setEndChildTag(char endChildTag) {
        this.endChildTag = endChildTag;
    }

    public char getEscapeTag() {
        return escapeTag;
    }

    public void setEscapeTag(char escapeTag) {
        this.escapeTag = escapeTag;
    }

    public boolean isTrim() {
        return trim;
    }

    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    public final List<NestParam> parse(String raw) {
        if (isBlank(raw)) {
            return Collections.emptyList();
        }

        List<NestParam> params = new LinkedList<NestParam>();
        parse(raw, params);
        return params;
    }

    private final int parse(String raw, List<NestParam> params) {
        NestParam param = new NestParam();
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (escape) {
                sb.append(c);
                escape = false;
                continue;
            }

            if (c == escapeTag) {
                escape = true;
                continue;
            }

            if (c == fieldDelimiter) {
                if (isBlank(param.getName())) {
                    param.setName(getValue(sb));
                }
                params.add(param);

                param = new NestParam();
                sb.delete(0, sb.length());
                continue;
            }

            if (c == startChildTag) {
                param.setName(sb.toString().trim());
                List<NestParam> childParam = new ArrayList<NestParam>();
                i += parse(raw.substring(i + 1), childParam);
                param.setChildParams(childParam);
                continue;
            }

            if (c == endChildTag) {
                if (isBlank(param.getName())) {
                    param.setName(getValue(sb));
                }
                params.add(param);
                return i + 1;
            }

            sb.append(c);
        }

        if (isBlank(param.getName())) {
            param.setName(getValue(sb));
        }

        params.add(param);
        return raw.length();
    }

    private String getValue(StringBuilder sb) {
        return trim ? sb.toString().trim() : sb.toString();
    }

}
