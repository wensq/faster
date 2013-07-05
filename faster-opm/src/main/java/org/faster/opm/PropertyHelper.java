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

import org.faster.opm.annotation.ExcludeProperty;
import org.faster.opm.annotation.Index;
import org.faster.opm.annotation.Property;
import org.faster.opm.annotation.PropertyStrategy;
import org.faster.opm.annotation.Resource;
import org.faster.opm.annotation.ResourceID;
import org.faster.util.DateTimes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isBlank;


/**
 * 属性操作辅助类
 *
 * @author sqwen
 * @date 2012-4-22
 */
public class PropertyHelper {

    private PropertyHelper() {}

    // 已分析的类
    private final static Set<Class<?>> parsedClasses = new HashSet<Class<?>>();

    // 资源ID方法对应表
    private final static Map<Class<?>, Method> resourceIdMethodMap = new ConcurrentHashMap<Class<?>, Method>();

    // 资源ID字段对应表
    private final static Map<Class<?>, Field> resourceIdFieldMap = new ConcurrentHashMap<Class<?>, Field>();

    // 资源属性方法对应表
    private final static Map<Class<?>, List<Method>> propertyMethodsMap = new ConcurrentHashMap<Class<?>, List<Method>>();

    // 带索引的资源属性方法对应表
    private final static Map<Class<?>, List<Method>> indexMethodsMap = new ConcurrentHashMap<Class<?>, List<Method>>();

    // 资源属性字段对应表
    private final static Map<Class<?>, List<Field>> propertyFieldsMap = new ConcurrentHashMap<Class<?>, List<Field>>();

    // 带索引的资源属性字段对应表
    private final static Map<Class<?>, List<Field>> indexFieldsMap = new ConcurrentHashMap<Class<?>, List<Field>>();

    // 唯一索引映射
    // key：资源类型
    // value：这个资源类型的唯一索引名称列表
    private final static Map<String, List<String>> uniqueIndexMap = new HashMap<String, List<String>>();

    public static final boolean isResourceIDMethod(Method method) {
        return method.isAnnotationPresent(ResourceID.class);
    }

    public static final boolean isResourceIDField(Field field) {
        return field.isAnnotationPresent(ResourceID.class);
    }

    public static final boolean isPropertyMethod(Resource resource, Method method) {
        if (method.isAnnotationPresent(Property.class)) {
            return true;
        }
        if (resource.defaultPropertyStrategy() == PropertyStrategy.EXCLUDE) {
            return false;
        }
        if (Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        return !method.isAnnotationPresent(ExcludeProperty.class);
    }

// public static final boolean isIndexMethod(Resource resource, Method method) {
// return method.isAnnotationPresent(Index.class);
// }
//
// public static final boolean isUniqueIndexMethod(Resource resource, Method
// method) {
// Index index = method.getAnnotation(Index.class);
// return index != null && index.unique();
// }

    public static final boolean isPropertyField(Resource resource, Field field) {
        if (field.isAnnotationPresent(org.faster.opm.annotation.Property.class)) {
            return true;
        }
        if (resource.defaultPropertyStrategy() == PropertyStrategy.EXCLUDE) {
            return false;
        }
        if (Modifier.isStatic(field.getModifiers())) {
            return false;
        }
        return !field.isAnnotationPresent(ExcludeProperty.class);
    }

// public static final boolean isIndexField(Resource resource, Field field) {
// return field.isAnnotationPresent(Index.class);
// }

    public static final Method getResourceIDMethod(Class<?> clazz) {
        Method m = resourceIdMethodMap.get(clazz);
        if (m == null && !parsedClasses.contains(clazz)) {
            synchronized (clazz) {
                if (!parsedClasses.contains(clazz)) {
                    parseClass(clazz);
                    m = resourceIdMethodMap.get(clazz);
                }
            }
        }
        return m;
    }

    public static final Field getResourceIDField(Class<?> clazz) {
        Field f = resourceIdFieldMap.get(clazz);
        if (f == null && !parsedClasses.contains(clazz)) {
            synchronized (clazz) {
                if (!parsedClasses.contains(clazz)) {
                    parseClass(clazz);
                    f = resourceIdFieldMap.get(clazz);
                }
            }
        }
        return f;
    }

    public static final List<Method> getPropertyMethods(Class<?> clazz) {
        List<Method> ret = propertyMethodsMap.get(clazz);
        if (ret == null && !parsedClasses.contains(clazz)) {
            synchronized (clazz) {
                if (!parsedClasses.contains(clazz)) {
                    parseClass(clazz);
                    ret = propertyMethodsMap.get(clazz);
                }
            }
        }
        return ret;
    }

    public static final List<Field> getPropertyFields(Class<?> clazz) {
        List<Field> ret = propertyFieldsMap.get(clazz);
        if (ret == null && !parsedClasses.contains(clazz)) {
            synchronized (clazz) {
                if (!parsedClasses.contains(clazz)) {
                    parseClass(clazz);
                    ret = propertyFieldsMap.get(clazz);
                }
            }
        }
        return ret;
    }

    public static final List<Method> getIndexMethods(Class<?> clazz) {
        List<Method> ret = indexMethodsMap.get(clazz);
        if (ret == null && !parsedClasses.contains(clazz)) {
            synchronized (clazz) {
                if (!parsedClasses.contains(clazz)) {
                    parseClass(clazz);
                    ret = indexMethodsMap.get(clazz);
                }
            }
        }
        return ret;
    }

    public static final List<Field> getIndexFields(Class<?> clazz) {
        List<Field> ret = indexFieldsMap.get(clazz);
        if (ret == null && !parsedClasses.contains(clazz)) {
            synchronized (clazz) {
                if (!parsedClasses.contains(clazz)) {
                    parseClass(clazz);
                    ret = indexFieldsMap.get(clazz);
                }
            }
        }
        return ret;
    }

    private static final void parseClass(Class<?> clazz) {
        Resource resource = clazz.getAnnotation(Resource.class);
        List<Class<?>> classes = new LinkedList<Class<?>>();
        Class<?> searchClass = clazz;
        do {
            classes.add(0, searchClass);
        } while ((searchClass = searchClass.getSuperclass()) != null);

        List<Method> propertyMethods = new LinkedList<Method>();
        List<Method> indexMethods = new LinkedList<Method>();
        List<String> uniqueIndexes = new LinkedList<String>();
        Set<String> propertyMethodNames = new HashSet<String>();
        boolean findResourceId = false;
        for (Class<?> cls : classes) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                if (!method.getName().startsWith("get")) {
                    continue;
                }
                if (method.getName().equals("getClass")) {
                    continue;
                }
                if (isResourceIDMethod(method)) {
                    if (findResourceId) {
                        throw new IllegalArgumentException("Find multiple ResourceId annotation, only allow one");
                    } else {
                        resourceIdMethodMap.put(clazz, method);
                        findResourceId = true;
                    }
                }
                if (isPropertyMethod(resource, method)) { // @ResourceId与@Property允许同时设置在同一个方法上
                    propertyMethods.add(method);
                    propertyMethodNames.add(convertGetterMethodName(method.getName()));
                    Index index = method.getAnnotation(Index.class);
                    if (index != null) {
                        indexMethods.add(method);
                        if (index.unique()) {
                            uniqueIndexes.add(getIndexName(index, method));
                        }
                    }
                }
            }
        }

        List<Field> propertyFields = new LinkedList<Field>();
        List<Field> indexFields = new LinkedList<Field>();
        for (Class<?> cls : classes) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (isResourceIDField(field)) {
                    if (findResourceId) {
                        throw new IllegalArgumentException("Find multiple ResourceId annotation, only allow one");
                    } else {
                        resourceIdFieldMap.put(clazz, field);
                        findResourceId = true;
                    }
                }
                if (propertyMethodNames.contains(field.getName())) {
                    continue; // 忽略已经在相应getter方法设置了@Property标注的字段
                }
                if (isPropertyField(resource, field)) { // @ResourceId与@Property允许同时设置在同一个字段上
                    propertyFields.add(field);
                    Index index = field.getAnnotation(Index.class);
                    if (index != null) {
                        indexFields.add(field);
                        if (index.unique()) {
                            uniqueIndexes.add(getIndexName(index, field));
                        }
                    }
                }
            }
        }

        if (!resourceIdMethodMap.containsKey(clazz) && !resourceIdFieldMap.containsKey(clazz)) {
            throw new IllegalArgumentException("There is no ResourceId annotated.");
        }
        parsedClasses.add(clazz);
        propertyMethodsMap.put(clazz, propertyMethods);
        indexMethodsMap.put(clazz, indexMethods);
        propertyFieldsMap.put(clazz, propertyFields);
        indexFieldsMap.put(clazz, indexFields);
        String resourceType = ResourceHelper.getResourceType(clazz);
        uniqueIndexMap.put(resourceType, uniqueIndexes);
    }

    public static final String getIndexName(Index index, Method m) {
        return isBlank(index.name()) ? PropertyHelper.getId(m) : index.name();
    }

    public static final String getIndexName(Index index, Field f) {
        return isBlank(index.name()) ? PropertyHelper.getId(f) : index.name();
    }

    public static final String getResourceID(Object obj) {
        Class<?> clazz = obj.getClass();
        Method m = getResourceIDMethod(clazz);
        if (m != null) {
            return getResourceID(obj, m);
        }

        Field f = getResourceIDField(clazz);
        if (f != null) {
            return getResourceID(obj, f);
        } else {
            throw new IllegalArgumentException("There is no ResourceId annotated.");
        }
    }

    public static final String getResourceID(Object obj, Method m) {
        Object value;
        try {
            value = m.invoke(obj, new Object[0]);
            if (value != null) {
                return convertToString(value);
            }
            return "<null>";
        } catch (Exception e) {
            throw new OpmException("Can't get resource id from object: " + obj + " by method: " + m.getName(), e);
        }
    }

    public static final String getResourceID(Object obj, Field f) {
        Object value;
        try {
            value = f.get(obj);
            if (value != null) {
                return convertToString(value);
            }
            return "<null>";
        } catch (Exception e) {
            throw new OpmException("Can't get resource id from object: " + obj + " by field: " + f.getName(), e);
        }
    }

    /**
     * 将get方法进行转换，如getId -> id，getName -> name
     *
     * @param name
     *            方法名
     * @return 转换后的方法名
     */
    public static final String convertGetterMethodName(String name) {
        return new StringBuilder(name.length() - 3)
                .append(Character.toLowerCase(name.charAt(3)))
                .append(name.substring(4))
                .toString();
    }

    public static final String getId(Method m) {
        Property p = m.getAnnotation(Property.class);
        return p == null || isBlank(p.id()) ? convertGetterMethodName(m.getName()) : p.id();
    }

    public static final String getId(Field f) {
        Property p = f.getAnnotation(Property.class);
        return p == null || isBlank(p.id()) ? f.getName() : p.id();
    }

    public static final String getName(Method m) {
        Property p = m.getAnnotation(Property.class);
        return p == null || isBlank(p.name()) ? convertGetterMethodName(m.getName()) : p.name();
    }

    public static final String getName(Field f) {
        Property p = f.getAnnotation(Property.class);
        return p == null || isBlank(p.name()) ? f.getName() : p.name();
    }

    public static final String getGroup(Method f) {
        Property p = f.getAnnotation(Property.class);
        return p == null || isBlank(p.group()) ? null : p.group();
    }

    public static final String getGroup(Field f) {
        Property p = f.getAnnotation(Property.class);
        return p == null || isBlank(p.group()) ? null : p.group();
    }

    public static final String getValue(Object obj, Method m) {
        Object value;
        try {
            value = m.invoke(obj, new Object[0]);
            if (value != null) {
                return convertToString(value);
            }
            Property p = m.getAnnotation(Property.class);
            return p == null ? null : p.defaultValue();
        } catch (Exception e) {
            throw new OpmException("Can't get value from object: " + obj + " by method: " + m.getName(), e);
        }
    }

    public static final String getValue(Object obj, Field f) {
        Object value;
        try {
            value = f.get(obj);
            if (value != null) {
                return convertToString(value);
            }
            Property p = f.getAnnotation(Property.class);
            return p == null ? null : p.defaultValue();
        } catch (Exception e) {
            throw new OpmException("Can't get value from object: " + obj + " by field: " + f.getName(), e);
        }
    }

    protected static final String convertToString(Object value) {
        if (value instanceof Date) {
            return DateTimes.formatDateToTimestampString((Date) value);
        }
        if (value instanceof Calendar) {
            return DateTimes.formatDateToTimestampString(((Calendar) value).getTime());
        }
        return String.valueOf(value);
    }

    public static final boolean isUniqueIndex(String resourceType, String propertyId) {
        List<String> list = uniqueIndexMap.get(resourceType);
        return list == null ? false : list.contains(propertyId);
    }

}
