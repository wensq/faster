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

package org.faster.orm.service.api;

import org.faster.orm.model.GenericEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author sqwen
 */
public interface Execute<PO extends GenericEntity<ID>, ID extends Serializable> {

    int execute(String queryString);

    int execute(String queryString, Object value);

    int execute(String queryString, Object... values);

    int execute(String queryString, List<?> values);

    int executeSQL(String sql);

    int executeSQL(String sql, Object value);

    int executeSQL(String sql, Object... values);

    int executeSQL(String sql, List<?> values);

}
