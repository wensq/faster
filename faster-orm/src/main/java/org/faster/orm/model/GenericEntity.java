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
package org.faster.orm.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.faster.commons.Beans;
import org.faster.commons.DateTimes;
import org.hibernate.collection.internal.AbstractPersistentCollection;

/**
 * 实体类父类
 * <p>
 * 实现了做了基类的一些基本功能
 * <p>
 * 用户自定义的实体类可以继承此类，也可以不继承此类
 *
 * @author sqwen
 */
@SuppressWarnings("serial")
public abstract class GenericEntity<ID extends Serializable> implements Cloneable, Serializable {

	public GenericEntity() {}

	public GenericEntity(Map<String, ?> attributes) {
		updateAttributes(attributes);
	}

	public abstract ID getId();

	public void updateAttributes(Map<String, ?> attributes) {
		Beans.populate(this, attributes);
	}

	public void updateAttribute(String attributeName, Object attributeValue) {
		Beans.setProperty(this, attributeName, attributeValue);
	}

	public void updateAttributes(Object from) {
		Beans.populate(this, from);
	}

	public void updateAttributes(Object from, String... slicePropertyName) {
		if (slicePropertyName == null || slicePropertyName.length == 0) {
			updateAttributes(from);
			return;
		}

		Beans.slicePopulate(this, from, slicePropertyName);
	}

	/**
	 * 覆盖toString方法
	 * <p>
	 * ToStringStyle取值为ToStringStyle.SHORT_PREFIX_STYLE
	 * <p>
	 * 调试的时候注意会自动取所有引用的值，会触发所有的Hibernate的延迟加载
	 * <p>
	 * 如果遇到性能问题，必须覆盖这个默认的toString方法的实现，或者避免调用这个默认的toString方法。
	 * <p>
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		List<Class<?>> classes = new LinkedList<Class<?>>();
		Class<?> searchClass = getClass();
		do {
			classes.add(0, searchClass);
		} while ((searchClass = searchClass.getSuperclass()) != null);

		ToStringBuilder tsb = new ToStringBuilder(this, getToStringStyle());
		for (Class<?> clazz : classes) {
			for (Field field : clazz.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				field.setAccessible(true);
				Object obj = null;
				try {
					obj = field.get(this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (obj instanceof AbstractPersistentCollection) {
					continue;
				}
				if (obj instanceof Calendar) {
					obj = DateTimes.formatDateToTimestampString(((Calendar) obj).getTime());
				} else if (obj instanceof Date) {
					obj = DateTimes.formatDateToTimestampString((Date) obj);
				}
				tsb.append(field.getName(), obj);
			}
		}
		return tsb.toString();
	}

	protected ToStringStyle getToStringStyle() {
		return ToStringStyle.SHORT_PREFIX_STYLE;
	}

}
