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

import javax.ws.rs.QueryParam;

/**
 * @author sqwen
 */
public class SampleCriteria extends GenericCriteria<Sample> {

	@QueryParam("chain")
	protected Boolean chain;

	@QueryParam("provinceId")
	@Query(field = "province.id")
	private Integer provinceId;

	public Boolean getChain() {
		return chain;
	}

	public void setChain(Boolean chain) {
		this.chain = chain;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

}
