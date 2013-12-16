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

import org.faster.cache.CacheMissHandler;
import org.faster.cache.CacheStrategy;
import org.faster.cache.CacheSupport;
import org.faster.orm.criteria.GenericCriteria;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.pagination.PagedList;
import org.faster.orm.service.GenericService;
import org.faster.util.Beans;
import org.hibernate.criterion.DetachedCriteria;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用范型WebService基类
 *
 * @author sqwen
 */
@SuppressWarnings("rawtypes")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
public abstract class GenericWebService<CRITERIA extends GenericCriteria<PO>, PO extends GenericEntity<ID>, ID extends Serializable>
		extends CacheSupport<PO> {

	public static final String QUERY_PARAM_CACHE_STRATEGY = "cache";

	protected final Class<PO> poClass;

	protected String poClassName;

	private final String cacheGroup;

	// 默认缓存1天的数据
	protected Integer cacheExpireSeconds = 24 * 60 * 60;

	@SuppressWarnings("unchecked")
	public GenericWebService() {
		poClass = (Class<PO>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
		poClassName = poClass.getSimpleName();
		cacheGroup = poClass.getCanonicalName();
	}

	@Path("cache/expire_seconds")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public int getCacheExpireSeconds() {
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

    public OpResult create(PO dto) {
        if (getPermitPropertyNames() == null) {
            return OpResult.failed("Create denied: not provide permit properties");
        }

        if (dto.getId() != null) {
            return update(dto.getId(), dto);
        }

        try {
            PO po = getGenericService().build(dto, getPermitPropertyNames());
            getGenericService().persist(po);
            return OpResult.SUCCESS;
        } catch (Exception e) {
            logger.error("Add " + poClassName + " failed: " + dto, e);
            return OpResult.failed(e.getMessage());
        }
    }

    public OpResult update(ID id, PO dto) {
        if (getPermitPropertyNames() == null) {
            return OpResult.failed("Update denied: not provide permit properties");
        }

        PO po = getGenericService().get(id);
        if (po == null) {
            return OpResult.failed(poClassName + "#" + id + " does not exists");
        }

        try {
            Beans.slicePopulate(po, dto, isIgnoreNull(), getPermitPropertyNames());
            beforeUpdate(po, dto);
            getGenericService().update(po);
            return OpResult.SUCCESS;
        } catch (Exception e) {
            logger.error("Update " + poClassName + "#" + id + " failed: " + dto, e);
            return OpResult.failed(e.getMessage());
        }
    }

    public OpResult update(ID[] ids, PO dto) {
        if (getPermitPropertyNames() == null) {
            return OpResult.failed("Update denied: not provide permit properties");
        }

        for (ID id : ids) {
            PO po = getGenericService().get(id);
            if (po == null) {
                continue;
            }

            try {
                Beans.slicePopulate(po, dto, isIgnoreNull(), getPermitPropertyNames());
                beforeUpdate(po, dto);
                getGenericService().update(po);
            } catch (Exception e) {
                logger.error("Update " + poClassName + "#" + id + " failed: " + dto, e);
                return OpResult.failed(e.getMessage());
            }
        }
        return OpResult.SUCCESS;
    }


    protected void beforeUpdate(PO po, PO dto) {}

    protected String[] getPermitPropertyNames() {
       return null;
    }

    protected boolean isIgnoreNull() {
        return true;
    }

    public OpResult destroy(ID id) {
        try {
            getGenericService().delete(id);
            return OpResult.SUCCESS;
        } catch (Exception e) {
            logger.error("Delete " + poClassName + "#" + id + " failed.", e);
            return OpResult.failed(e.getMessage());
        }
    }

    public OpResult destroy(ID[] ids) {
        for (ID id : ids) {
            try {
                getGenericService().delete(id);
            } catch (Exception e) {
                logger.error("Delete " + poClassName + "#" + id + " failed.", e);
                return OpResult.failed(e.getMessage());
            }
        }
        return OpResult.SUCCESS;
    }

    protected void renderCriteria(CRITERIA criteria) {}

	/**
	 * 在对象返回前修饰一下属性
	 *
	 * @param po 持久化对象
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

	public int count(CRITERIA criteria) {
        renderCriteria(criteria);
		String key = buildGlobalCacheKey("count:" + criteria.buildCacheKey());
        final CRITERIA c = criteria;
		return (Integer) findObjectFromCache(key, CacheStrategy.fromString(c.getCacheStrategy()),
				cacheExpireSeconds,
				new CacheMissHandler() {

					@Override
					public Object doFind() {
						DetachedCriteria dc = c.buildCriteria();
						return getGenericService().countByCriteria(dc);
					}
				});
	}

	@SuppressWarnings("unchecked")
	public DataCollection<PO> filter(CRITERIA criteria) {
        renderCriteria(criteria);
		String key = buildGlobalCacheKey("filter:" + criteria.buildCacheKey());
        final CRITERIA c = criteria;
		return (DataCollection<PO>) findObjectFromCache(key, CacheStrategy.fromString(c.getCacheStrategy()),
				cacheExpireSeconds,
				new CacheMissHandler() {

					@Override
					public Object doFind() {
						List<PO> pos = getGenericService().findAllByCriteria(c);
						return buildDataCollection(pos);
					}
				});
	}

	@SuppressWarnings("unchecked")
	public DataCollection<PO> search(CRITERIA criteria) {
        renderCriteria(criteria);
		String key = buildGlobalCacheKey("search:" + criteria.buildCacheKey());
        final CRITERIA c = criteria;
		return (DataCollection<PO>) findObjectFromCache(key, CacheStrategy.fromString(c.getCacheStrategy()),
				cacheExpireSeconds,
				new CacheMissHandler() {

					@Override
					public Object doFind() {
						PagedList<PO> pos = getGenericService().findPageByCriteria(c);
						return new DataCollection(pos);
					}
				});
	}

	protected DataCollection<PO> buildDataCollection(List<PO> pos) {
        DataCollection<PO> dc = new DataCollection<PO>();
		if (pos != null && !pos.isEmpty()) {
			for (PO po : pos) {
				renderBeforeReturn(po);
			}
		}
		dc.setData(pos);
		return dc;
	}

    /**
     * Some Utilities
     */

    protected String getFilename(HttpHeaders header) {
        MultivaluedMap<String, String> heades = header.getRequestHeaders();
        String cdHeader = heades.getFirst("Content-Disposition");
        if (cdHeader == null) {
            return null;
        }
        Pattern p = Pattern.compile("filename=\".*\"");
        Matcher m = p.matcher(cdHeader);
        return m.find() ? m.group() : null;
    }

}
