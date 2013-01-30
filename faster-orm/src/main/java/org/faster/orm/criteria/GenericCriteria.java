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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.faster.util.DateTimes;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * 通用的范型查询条件基类
 *
 * @author sqwen
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericCriteria<PO> {

	public static final String ALL = "<ALL>";

	public static final String NULL = "<NULL>";

	public static final String NOT_NULL = "<NOTNULL>";

	// 排序字段: 格式为：字段名-desc|asc，字段名必选，后面可选
	@QueryParam("sort")
	@XmlAttribute
	@NoQuery
	protected String sort;

	// 从1开始计数
	@QueryParam("page")
	@DefaultValue("1")
	@XmlAttribute
	@NoQuery
	protected int page = 1;

	@QueryParam("limit")
	@DefaultValue("10")
	@XmlAttribute
	@NoQuery
	protected int limit = 10;

	/**
	 * 缓存策略：
	 * <p>
	 * yes - 启用缓存，默认
	 * <p>
	 * no - 不启用缓存
	 * <p>
	 * delete/flush - 清除缓存
	 */
	@QueryParam("cache")
	@DefaultValue("YES")
	@XmlAttribute
	@NoQuery
	protected String cache;

	@XmlTransient
	@NoQuery
	protected final Class<PO> persistClass;

	// --------------------
	// 自定义函数
	// --------------------

	@SuppressWarnings("unchecked")
	public GenericCriteria() {
		persistClass = (Class<PO>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	/**
	 * 再构造查询条件前提供给子类的回调接口
	 * <p>
	 * 子类可以根据实际情况进行处理（比如参数归一化）
	 */
	protected void beforeBuildCriteria() {}

	public DetachedCriteria buildCriteria() {
		beforeBuildCriteria();
		DetachedCriteria dc = DetachedCriteria.forClass(persistClass);
		List<Field> queryFields = QueryHelper.getQueryFields(this.getClass());
		Set<String> aliases = new HashSet<String>(); // 已创建的别名列表
		for (Field f : queryFields) {
			try {
				Object obj = f.get(this);
				boolean ignoreNull = QueryHelper.isIgnoreNull(f);
				if (obj == null) {
					if (ignoreNull) {
						continue;
					}
				}

				String fieldName = QueryHelper.getFieldName(f);
				if (fieldName.contains(".")) {
					String[] subFieldNames = fieldName.split("\\.");
					String alias = subFieldNames[0];
					if (!aliases.contains(alias)) {
						dc.createAlias(alias, alias);
						aliases.add(alias);
					}
					// TODO 支持多级alias
				}

				if (obj == null && !ignoreNull) {
					dc.add(Restrictions.isNull(fieldName));
					continue;
				}

				String stringValue = String.valueOf(obj);
				if (stringValue.startsWith(NULL)) {
					dc.add(Restrictions.isNull(fieldName));
					continue;
				}

				if (stringValue.startsWith(NOT_NULL)) {
					dc.add(Restrictions.isNotNull(fieldName));
					continue;
				}

				boolean ignoreEmptyOrZero = QueryHelper.isIgnoreEmptyOrZero(f);
				DataType dt = QueryHelper.getDataType(f);
				switch (dt) {
				case STRING:
					if (ignoreEmptyOrZero && isBlank(stringValue)) {
						continue;
					}
					MatchMode mm = QueryHelper.getDefaultStringMatchMode(f);
					switch (mm) {
					case EQ:
						dc.add(Restrictions.eq(fieldName, stringValue));
						continue;
					case LIKE:
						dc.add(Restrictions.like(fieldName, stringValue, org.hibernate.criterion.MatchMode.ANYWHERE));
						continue;
					case LIKE_START:
						dc.add(Restrictions.like(fieldName, stringValue, org.hibernate.criterion.MatchMode.START));
						continue;
					case LIKE_END:
						dc.add(Restrictions.like(fieldName, stringValue, org.hibernate.criterion.MatchMode.END));
						continue;
					case ILIKE:
						dc.add(Restrictions.ilike(fieldName, stringValue, org.hibernate.criterion.MatchMode.ANYWHERE));
						continue;
					case ILIKE_START:
						dc.add(Restrictions.ilike(fieldName, stringValue, org.hibernate.criterion.MatchMode.START));
						continue;
					case ILIKE_END:
						dc.add(Restrictions.ilike(fieldName, stringValue, org.hibernate.criterion.MatchMode.END));
						continue;
					default:
						throw new IllegalArgumentException("Invalid MatchMode for String: " + mm);
					}
				case INTEGER:
					if (ignoreEmptyOrZero && stringValue.equals("0")) {
						continue;
					}
					CriteriaRender.renderFieldByIntegerFilterValues(dc, fieldName, stringValue);
					continue;
				case LONG:
					if (ignoreEmptyOrZero && stringValue.equals("0")) {
						continue;
					}
					CriteriaRender.renderFieldByLongFilterValues(dc, fieldName, stringValue);
					continue;
				case DATE:
					// TODO 改为根据匹配模式自动生成限制条件
					Date date = DateTimes.parseStringToDate(stringValue);
					dc.add(Restrictions.eq(fieldName, date));
					continue;
				case BOOLEAN:
					Boolean bool = BooleanUtils.toBooleanObject(stringValue);
					dc.add(Restrictions.eq(fieldName, bool));
					continue;
				default:
					throw new IllegalArgumentException("Unsupported DataType: " + dt);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}

		renderCriteria(dc);

		if (isNotBlank(sort)) {
			String[] fields = sort.split("-");
			if (fields.length < 2 || "desc".equalsIgnoreCase(fields[1])) {
				dc.addOrder(Order.desc(fields[0]));
			} else {
				dc.addOrder(Order.asc(fields[0]));
			}
		}

		return dc;
	}

	/**
	 * 由子类实现对DetachedCriteria的修饰，添加自定义业务相关的查询条件
	 *
	 * @param dc
	 *            查询条件封装
	 */
	protected void renderCriteria(DetachedCriteria dc) {}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * 是否分页返回
	 */
	public boolean isPagination() {
		return limit > 0;
	}

	// --------------------
	// getter/setter
	// --------------------

	public int getPage() {
		return page;
	}

	public void setPage(int pageNo) {
		this.page = pageNo;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int pageSize) {
		this.limit = pageSize;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCacheStrategy() {
		return cache;
	}

	public void setCacheStrategy(String cacheStrategy) {
		this.cache = cacheStrategy;
	}

}
