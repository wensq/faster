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
package org.faster.orm.service;

import org.faster.orm.model.GenericEntity;
import org.faster.orm.service.api.Build;
import org.faster.orm.service.api.Count;
import org.faster.orm.service.api.Create;
import org.faster.orm.service.api.Delete;
import org.faster.orm.service.api.Execute;
import org.faster.orm.service.api.Exists;
import org.faster.orm.service.api.Find;
import org.faster.orm.service.api.FindAll;
import org.faster.orm.service.api.FindPage;
import org.faster.orm.service.api.Get;
import org.faster.orm.service.api.Initialize;
import org.faster.orm.service.api.Persist;
import org.faster.orm.service.api.Project;
import org.faster.orm.service.api.ProjectPage;
import org.faster.orm.service.api.Save;
import org.faster.orm.service.api.Stats;
import org.faster.orm.service.api.Update;
import org.faster.orm.service.api.Validate;
import org.faster.orm.service.api.with.option.FindAllWithOption;
import org.faster.orm.service.api.with.option.FindPageWithOption;
import org.faster.orm.service.api.with.option.FindWithOption;
import org.faster.orm.service.api.with.option.GetWithOption;
import org.faster.orm.service.api.with.option.ProjectPageWithOption;
import org.faster.orm.service.api.with.option.ProjectWithOption;

import java.io.Serializable;

/**
 * @author sqwen
 */
public interface GenericService<PO extends GenericEntity<ID>, ID extends Serializable>
		extends Execute<PO, ID>,
        Build<PO, ID>, Persist<PO, ID>, Save<PO, ID>, Create<PO, ID>, Update<PO, ID>, Delete<PO, ID>,
		Count<PO, ID>, Exists<PO, ID>,
		Get<PO, ID>, Find<PO, ID>, FindAll<PO, ID>, FindPage<PO, ID>,
		GetWithOption<PO, ID>, FindWithOption<PO, ID>, FindAllWithOption<PO, ID>, FindPageWithOption<PO, ID>,
		Project<PO, ID>, ProjectPage<PO, ID>,
		ProjectWithOption<PO, ID>, ProjectPageWithOption<PO, ID>,
		Stats<PO, ID>, Validate<PO, ID>, Initialize<PO, ID> {

}
