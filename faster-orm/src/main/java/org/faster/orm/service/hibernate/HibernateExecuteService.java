/*
 * Copyright (c) 2014 @iSQWEN. All rights reserved.
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

package org.faster.orm.service.hibernate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.faster.orm.model.GenericEntity;
import org.faster.orm.service.GenericService;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import java.io.Serializable;
import java.util.List;

/**
 * @author sqwen
 */
public abstract class HibernateExecuteService<PO extends GenericEntity<ID>, ID extends Serializable>
        extends HibernateDaoSupport<PO, ID>
        implements GenericService<PO, ID> {

    public int execute(String queryString) {
        return execute(queryString, (Object[]) null);
    }

    public int execute(String queryString, Object value) {
        return execute(queryString, new Object[] { value });
    }

    public int execute(final String queryString, final Object... values) {
        StopWatch sw = null;
        if (log.isInfoEnabled()) {
            sw = new StopWatch();
            sw.start();
            log.info("Executing [{}] with params [{}]", queryString, toParamString(values));
        }

        Query queryObject = getSession().createQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i, values[i]);
            }
        }

        int count = queryObject.executeUpdate();
        if (log.isInfoEnabled()) {
            log.info("Execute complete, affected {} records. ({} ms)", count, sw.getTime());
        }
        return count;
    }

    private static final String toParamString(Object... values) {
        return values == null ? "" : StringUtils.join(values);
    }

    public int execute(final String queryString, final List<?> values) {
        return execute(queryString, values.toArray(new Object[values.size()]));
    }

    @Override
    public int executeSQL(String sql) {
        return executeSQL(sql, (Object[]) null);
    }

    @Override
    public int executeSQL(String sql, Object value) {
        return executeSQL(sql, new Object[] {value});
    }

    @Override
    public int executeSQL(String sql, Object... values) {
        StopWatch sw = null;
        if (log.isInfoEnabled()) {
            sw = new StopWatch();
            sw.start();
            log.info("Executing SQL [{}] with params [{}]", sql, toParamString(values));
        }

        SQLQuery queryObject = getSession().createSQLQuery(sql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i, values[i]);
            }
        }

        int count = queryObject.executeUpdate();
        if (log.isInfoEnabled()) {
            log.info("SQL execute complete, affected {} records. ({} ms)", count, sw.getTime());
        }
        return count;
    }

    @Override
    public int executeSQL(String sql, List<?> values) {
        return executeSQL(sql, values.toArray(new Object[values.size()]));
    }
}
