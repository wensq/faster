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
package org.faster.orm.criteria;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.faster.orm.criteria.GenericCriteria.ALL;
import static org.faster.orm.criteria.GenericCriteria.NOT_NULL;
import static org.faster.orm.criteria.GenericCriteria.NULL;

import org.faster.commons.Strings;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * 查询条件修饰工具类，提供对常用字段的通用render封装，防止出现冗余代码
 *
 * @author sqwen
 */
public class CriteriaRender {

	private CriteriaRender() {}

	public static final void renderIntegerField(DetachedCriteria dc, String fieldName, String value) {
		if (isBlank(value) || ALL.equalsIgnoreCase(value)) {
			return;
		}

		if (NULL.equalsIgnoreCase(value)) {
			dc.add(Restrictions.isNull(fieldName));
			return;
		}

		if (NOT_NULL.equalsIgnoreCase(value)) {
			dc.add(Restrictions.isNotNull(fieldName));
		}
	}

	/**
	 * 为某个字段添加相等的查询条件
	 *
	 * @param dc
	 *            查询条件封装
	 * @param fieldName
	 *            字段名
	 * @param value
	 *            查找的值
	 */
	public static void renderField(DetachedCriteria dc, String fieldName, Object value) {
		if (value == null) {
			return;
		}

		if (value instanceof String) {
			String stringValue = (String) value;
			if (isBlank(stringValue) || ALL.equalsIgnoreCase(stringValue)) {
				return;
			}

			if (NULL.equalsIgnoreCase(stringValue)) {
				dc.add(Restrictions.isNull(fieldName));
				return;
			}

			if (NOT_NULL.equalsIgnoreCase(stringValue)) {
				dc.add(Restrictions.isNotNull(fieldName));
				return;
			}
		}

		dc.add(Restrictions.eq(fieldName, value));
	}

	/**
	 * 为某个字段添加like的查询条件
	 *
	 * @param dc
	 *            查询条件封装
	 * @param fieldName
	 *            字段名
	 * @param value
	 *            相似的值
	 */
	public static void renderFieldLike(DetachedCriteria dc, String fieldName, String value) {
		if (isBlank(value) || ALL.equalsIgnoreCase(value)) {
			return;
		}

		if (NULL.equalsIgnoreCase(value)) {
			dc.add(Restrictions.isNull(fieldName));
			return;
		}

		if (NOT_NULL.equalsIgnoreCase(value)) {
			dc.add(Restrictions.isNotNull(fieldName));
			return;
		}

		dc.add(Restrictions.ilike(fieldName, value, MatchMode.ANYWHERE));
	}

	/**
	 * 修饰查询子对象ID的查询方法
	 *
	 * @param dc
	 *            查询条件
	 * @param subObject
	 *            子对象字段名称 ，如: vendor
	 * @param subObjectId
	 *            子对象ID
	 */
	public static void renderSubObjectId(DetachedCriteria dc, String subObject, Object subObjectId) {
		if (subObjectId == null) {
			return;
		}
		dc.createAlias(subObject, subObject);
		dc.add(Restrictions.eq(subObject + ".id", subObjectId));
	}

	/**
	 * 修饰查询子对象字段的查询方法
	 *
	 * @param dc
	 *            查询方法
	 * @param subObjectField
	 *            子对象字段，如：vendor.name
	 * @param value
	 *            子对象值
	 */
	public static void renderSubObjectFieldEq(DetachedCriteria dc, String subObjectField, Object value) {
		if (value == null) {
			return;
		}
		String[] ss = subObjectField.split("\\.");
		dc.createAlias(ss[0], ss[0]);
		dc.add(Restrictions.eq(subObjectField, value));
	}

	/**
	 * 修饰模糊查询子对象字段的查询方法
	 *
	 * @param dc
	 *            查询方法
	 * @param subObjectField
	 *            子对象字段，如：vendor.name
	 * @param value
	 *            子对象字符串值
	 */
	public static void renderSubObjectFieldLike(DetachedCriteria dc, String subObjectField, String value) {
		if (isBlank(value)) {
			return;
		}
		String[] ss = subObjectField.split("\\.");
		dc.createAlias(ss[0], ss[0]);
		dc.add(Restrictions.ilike(subObjectField, value.trim(), MatchMode.ANYWHERE));
	}

	/**
	 * 过滤某个字段的多个整型值
	 *
	 * @param dc
	 *            查询条件封装
	 * @param fieldName
	 *            字段名称，如type
	 * @param integerValues
	 *            需要过滤的多个字段值，字段值用逗号分隔，每个字段值为整形，如：1,2,3
	 */
	public static void renderFieldByIntegerFilterValues(DetachedCriteria dc, String fieldName, String integerValues) {
		if (isBlank(integerValues)) {
			return;
		}
		Integer[] array = Strings.toIntegerArray(integerValues.trim(), ",");
		if (array.length == 1) {
			dc.add(Restrictions.eq(fieldName, array[0]));
		} else {
			dc.add(Restrictions.in(fieldName, array));
		}
	}

	public static void renderFieldByLongFilterValues(DetachedCriteria dc, String fieldName, String integerValues) {
		if (isBlank(integerValues)) {
			return;
		}
		Long[] array = Strings.toLongArray(integerValues.trim(), ",");
		if (array.length == 1) {
			dc.add(Restrictions.eq(fieldName, array[0]));
		} else {
			dc.add(Restrictions.in(fieldName, array));
		}
	}

	/**
	 * 添加查询子对象的ID在某个列表内的条件
	 *
	 * @param dc
	 *            查询条件封装
	 * @param subObjects
	 *            子对象名称列表，如：
	 * @param filterValues
	 *            需要过滤的多个字段值，字段值用逗号分隔，每个字段值为整形
	 */
	public static void renderSubObjectIdsByIntegerFilterValues(DetachedCriteria dc, String[] subObjects,
			String filterValues) {
		if (subObjects == null || subObjects.length == 0 || isBlank(filterValues)) {
			return;
		}
		Integer[] array = Strings.toIntegerArray(filterValues.trim(), ",");
		for (String fieldName : subObjects) {
			dc.createAlias(fieldName, fieldName);
		}
		if (array.length == 1) {
			if (subObjects.length == 1) {
				dc.add(Restrictions.eq(subObjects[0] + ".id", array[0]));
			} else {
				LogicalExpression le = Restrictions.or(
						Restrictions.eq(subObjects[0] + ".id", array[0]),
						Restrictions.eq(subObjects[1] + ".id", array[0]));
				for (int i = 2; i < subObjects.length; i++) {
					le = Restrictions.or(le, Restrictions.eq(subObjects[i] + ".id", array[0]));
				}
				dc.add(le);
			}
		} else {
			if (subObjects.length == 1) {
				dc.add(Restrictions.in(subObjects[0] + ".id", array));
			} else {
				LogicalExpression le = Restrictions.or(
						Restrictions.in(subObjects[0] + ".id", array),
						Restrictions.in(subObjects[1] + ".id", array));
				for (int i = 2; i < subObjects.length; i++) {
					le = Restrictions.or(le, Restrictions.eq(subObjects[i] + ".id", array));
				}
				dc.add(le);
			}
		}
	}

	/**
	 * 查询某个整数是否在某个以逗号分隔的整数字段里面
	 *
	 * @param dc
	 *            查询条件
	 * @param integersFiledName
	 *            整形字段名，字段的值为逗号分隔的整数，如字段名为mgwIds，值为：1011,1980,508
	 * @param id
	 *            查询的整数值
	 */
	public static void renderIntegersField(DetachedCriteria dc, String integersFiledName, Integer id) {
		if (id == null) {
			return;
		}

		LogicalExpression criteria = buildLogicalExpressionForIntegersField(integersFiledName, id);
		if (criteria != null) {
			dc.add(criteria);
		}
	}

	/**
	 * 为整形列表字段构造查询条件
	 *
	 * @param integersFiledName
	 *            用逗号分隔的整数字段名
	 * @param id
	 *            需要查询的整数
	 * @return 查询逻辑表达式
	 */
	public static LogicalExpression buildLogicalExpressionForIntegersField(String integersFiledName, Integer id) {
		if (id == null) {
			return null;
		}

		return Restrictions.or(
				Restrictions.eq(integersFiledName, id + ""),
				Restrictions.or(
						Restrictions.like(integersFiledName, id + ",", MatchMode.START),
						Restrictions.or(
								Restrictions.like(integersFiledName, "," + id, MatchMode.END),
								Restrictions.like(integersFiledName, "," + id + ",", MatchMode.ANYWHERE)
								)
						)
				);
	}

	/**
	 * 为整形列表字段构造查询条件
	 *
	 * @param dc
	 *            查询条件
	 * @param integersFiledName
	 *            用逗号分隔的整数字段名
	 * @param integerIds
	 *            需要查询的一批整数，逗号分隔
	 */
	public static void renderIntegersField(DetachedCriteria dc, String integersFiledName, String integerIds) {
		if (isBlank(integerIds)) {
			return;
		}

		Integer[] ids = Strings.toIntegerArray(integerIds.trim(), ",");
		LogicalExpression criteria = buildLogicalExpressionForIntegersField(integersFiledName, ids[0]);

		for (int i = 1; i < ids.length; i++) {
			LogicalExpression newCriteria = buildLogicalExpressionForIntegersField(integersFiledName, ids[i]);
			criteria = Restrictions.or(criteria, newCriteria);
		}
		dc.add(criteria);
	}

}
