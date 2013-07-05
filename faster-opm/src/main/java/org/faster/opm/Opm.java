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

import org.faster.opm.annotation.Index;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 对象属性映射(Object-Property Mapping)操作类
 * <p>
 * OPM是线程安全对象
 *
 * @author sqwen
 * @date 2012-4-21
 */
public class Opm<T> {

    public Resource build(T instance) {
        Class<?> clazz = instance.getClass();
        Resource res = new Resource();
        res.setType(ResourceHelper.getResourceType(clazz));
        res.setId(PropertyHelper.getResourceID(instance));
        List<Property> properties = new ArrayList<Property>();

        List<Field> fs = PropertyHelper.getPropertyFields(clazz);
        for (Field f : fs) {
            Property p = new Property();
            p.setId(PropertyHelper.getId(f));
            p.setName(PropertyHelper.getName(f));
            p.setGroup(PropertyHelper.getGroup(f));
            p.setValue(PropertyHelper.getValue(instance, f));
            properties.add(p);
        }

        List<Method> ms = PropertyHelper.getPropertyMethods(clazz);
        for (Method m : ms) {
            Property p = new Property();
            p.setId(PropertyHelper.getId(m));
            p.setName(PropertyHelper.getName(m));
            p.setGroup(PropertyHelper.getGroup(m));
            p.setValue(PropertyHelper.getValue(instance, m));
            properties.add(p);
        }

        res.setPropertis(properties);
        return res;
    }

    public Map<String, String> getIndex(T instance) {
        Class<?> clazz = instance.getClass();

        Map<String, String> map = new HashMap<String, String>();

        List<Field> fs = PropertyHelper.getIndexFields(clazz);
        for (Field f : fs) {
            Index index = f.getAnnotation(Index.class);
            String name = PropertyHelper.getIndexName(index, f);
            String value = PropertyHelper.getValue(instance, f);
            if (isNotBlank(value)) {
                map.put(name, value);
            }
        }

        List<Method> ms = PropertyHelper.getIndexMethods(clazz);
        for (Method m : ms) {
            Index index = m.getAnnotation(Index.class);
            String name = PropertyHelper.getIndexName(index, m);
            String value = PropertyHelper.getValue(instance, m);
            if (isNotBlank(value)) {
                map.put(name, value);
            }
        }

        return map;
    }

    public List<Resource> buildAll(Collection<T> instances) {
        if (instances == null || instances.isEmpty()) {
            return Collections.emptyList();
        }

        List<Resource> ret = new ArrayList<Resource>(instances.size());
        for (T instance : instances) {
            ret.add(build(instance));
        }
        return ret;
    }

    public List<Map<String, String>> getIndexes(Collection<T> instances) {
        if (instances == null || instances.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, String>> ret = new ArrayList<Map<String, String>>(instances.size());
        for (T instance : instances) {
            ret.add(getIndex(instance));
        }
        return ret;
    }

}
