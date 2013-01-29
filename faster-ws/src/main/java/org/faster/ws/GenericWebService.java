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
package org.faster.ws;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.faster.cache.CacheMissHandler;
import org.faster.cache.CacheSupport;
import org.faster.cache.CacheStrategy;
import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.pagination.PagedList;
import org.faster.orm.service.GenericService;
import org.hibernate.criterion.DetachedCriteria;

/**
 * 通用范型WebService基类
 * <p>
 *
 * @author sqwen
 */
@SuppressWarnings("rawtypes")
@Consumes({ MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public abstract class GenericWebService<DTOS extends GenericDTOs, CRITERIA extends GenericCriteria<PO>, PO extends GenericEntity<ID>, ID extends Serializable>
		extends CacheSupport<PO> {

	public static final String CACHE_STRATEGY_QUERY_PARAM = "_cache";

	protected final Class<DTOS> dtosClass;

	protected final Class<PO> poClass;

	protected final String resourceType;

	private final String cacheGroup;

	// 默认缓存1天的数据
	protected Integer cacheExpireSeconds = 24 * 60 * 60;

	@SuppressWarnings("unchecked")
	public GenericWebService() {
		dtosClass = (Class<DTOS>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		poClass = (Class<PO>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[2];
		resourceType = poClass.getSimpleName();
		cacheGroup = poClass.getCanonicalName();
	}

	@Path("cache/expire_seconds")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public int setCacheExpireSeconds() {
		return cacheExpireSeconds;
	}

	@Path("cache/expire_seconds")
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String setCacheExpireSeconds(Integer cacheExpireSeconds) {
		this.cacheExpireSeconds = cacheExpireSeconds;
		return "Cache expire seconds now is set to " + cacheExpireSeconds + " seconds";
	}

	protected abstract GenericService<PO, ID> getGenericService();

	/**
	 * 以保存的对象全局类名作为Cache组名
	 */
	@Override
	protected String getCacheGroup() {
		return cacheGroup;
	}

	/**
	 * 在对象返回前修饰一下属性
	 *
	 * @param po
	 *            此DTO对应的PO
	 */
	protected void renderBeforeReturn(PO po) {}

	public PO get(final ID id, String cacheStrategy) {
		String key = buildGlobalCacheKey("id:" + id);
		return findFromCache(key, CacheStrategy.fromString(cacheStrategy), cacheExpireSeconds, new CacheMissHandler() {

			@Override
			public Object doFind() {
				PO po = getGenericService().get(id);
				if (po != null) {
					renderBeforeReturn(po);
				}
				return po;
			}
		});
	}

	public int count(final CRITERIA criteria) {
		String key = buildGlobalCacheKey("count:" + criteria);
		return (Integer) findObjectFromCache(key, CacheStrategy.fromString(criteria.getCacheStrategy()),
				cacheExpireSeconds,
				new CacheMissHandler() {

					@Override
					public Object doFind() {
						DetachedCriteria dc = criteria.buildCriteria();
						return getGenericService().countByCriteria(dc);
					}
				});
	}

	@SuppressWarnings("unchecked")
	public DTOS filter(final CRITERIA criteria) {
		String key = buildGlobalCacheKey("filter:" + criteria);
		return (DTOS) findObjectFromCache(key, CacheStrategy.fromString(criteria.getCacheStrategy()),
				cacheExpireSeconds,
				new CacheMissHandler() {

					@Override
					public Object doFind() {
						List<PO> pos = getGenericService().findAllByCriteria(criteria);
						return buildDTOs(pos);
					}
				});
	}

	@SuppressWarnings("unchecked")
	public DTOS search(final CRITERIA criteria) {
		String key = buildGlobalCacheKey("search:" + criteria);
		return (DTOS) findObjectFromCache(key, CacheStrategy.fromString(criteria.getCacheStrategy()),
				cacheExpireSeconds,
				new CacheMissHandler() {

					@Override
					public Object doFind() {
						PagedList<PO> pos = getGenericService().findPageByCriteria(criteria);
						return buildDTOs(pos);
					}
				});
	}

	@SuppressWarnings("unchecked")
	protected DTOS buildDTOs(List<PO> pos) {
		DTOS dtos;
		try {
			dtos = dtosClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (pos != null && !pos.isEmpty()) {
			for (PO po : pos) {
				renderBeforeReturn(po);
			}
		}
		dtos.setValues(pos);
		return dtos;
	}

}
