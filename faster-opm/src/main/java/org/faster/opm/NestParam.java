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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 嵌套参数模型定义
 * <p>
 * 1. 嵌套参数同级字段之间用逗号分隔
 * <p>
 * 2. 用左方括号表示开始子嵌套参数，用右方括号表示结束子嵌套参数
 * <p>
 * 3. 嵌套参数是递归关系，即子嵌套参数还可以以同样的规则嵌套孙嵌套参数
 * <p>
 * 示例：zcxq,lhxq[gyx,gdh],zxq[l1dh[avg],l2dh[sum]]
 * <p>
 * 说明：
 * <p>
 * 第一层嵌套参数有：zcxq,lhxq,zxq
 * <p>
 * 第二层嵌套参数：zcxq无第二层嵌套参数，lhxq的第二层嵌套参数为:gyx,ydh,zxq的第二层嵌套参数为：l1dh,l2dh
 * <p>
 * 第三层嵌套参数：zcxq无第三层嵌套参数，lhxq无第三层嵌套参数,zxq的的第三层嵌套参数为：avg(属于l1dh)和avg(属于l2dh)
 *
 * @author sqwen
 * @version 1.0, 2012-5-13
 */
public class NestParam implements Serializable {

    private static final long serialVersionUID = 8228473978708403349L;

    private String name;

    private List<NestParam> childParams;

    // --------------------
    // feature methods
    // --------------------

    public NestParam() {}

    public NestParam(String name) {
        this.name = name;
    }

    public NestParam(String name, List<NestParam> childParams) {
        this.name = name;
        this.childParams = childParams;
    }

    public boolean existChildParams() {
        return childParams != null && !childParams.isEmpty();
    }

    public void addChildParam(NestParam childParam) {
        if (childParams == null) {
            childParams = new ArrayList<NestParam>();
        }
        childParams.add(childParam);
    }

    public List<String> getChildParamNames() {
        if (!existChildParams()) {
            return Collections.emptyList();
        }

        List<String> ret = new ArrayList<String>(childParams.size());
        for (NestParam param : childParams) {
            ret.add(param.getName());
        }
        return ret;
    }

    public String getChildParamNames(String delimiter) {
        List<String> names = getChildParamNames();
        return StringUtils.join(names, delimiter);
    }

    @Override
    public String toString() {
        ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE);
        tsb.append("name", name);
        if (existChildParams()) {
            tsb.append("childParams", childParams);
        }
        return tsb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (childParams == null ? 0 : childParams.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NestParam other = (NestParam) obj;
        if (childParams == null) {
            if (other.childParams != null) {
                return false;
            }
        } else if (!childParams.equals(other.childParams)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    // --------------------
    // getter/setter
    // --------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NestParam> getChildParams() {
        return childParams;
    }

    public void setChildParams(List<NestParam> childParams) {
        this.childParams = childParams;
    }

}
