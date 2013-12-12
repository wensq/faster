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
package org.faster.orm.service.api;

import org.faster.orm.model.GenericEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author sqwen
 */
public interface Build<PO extends GenericEntity<ID>, ID extends Serializable> {

	PO build();

	PO build(String propertyName, Object propertyValue);

	PO buildFromMap(Map<String, ?> attributes);

	List<PO> buildFromMaps(Collection<Map<String, ?>> attributesCollection);

	PO build(Object form);

	PO build(Object form, String[] permitPropertyNames);

	List<PO> buildFromForms(Collection<?> forms);

	List<PO> buildFromForms(Collection<?> forms, String[] permitPropertyNames);

}
