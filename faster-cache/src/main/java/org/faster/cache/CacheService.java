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
package org.faster.cache;

import java.util.Collection;

/**
 * cache操作接口
 *
 * @author sqwen
 */
public interface CacheService {

	/**
	 * 根据外部的key获取内部使用的key。
	 * <p>
	 * 外部key没有什么限制，内部key有长度、字符等限制，所以需要转换。
	 *
	 * @param outerKey 外部键值
	 * @return 内部使用的键值
	 */
	String buildInternalKey(String outerKey);

	void putInCache(String key, int expiration, Object obj);

	void flush(String key);

	void flush(Collection<String> keys);

	/**
	 * 从Cache中根据key获取对象，如果找不到直接进行查找
	 *
	 * @param key 对象对应的key
	 * @param expiration 超时时间， 小于0代表不从cache中找 等于0代表采用默认的最大超时 大于0代表超时的秒数
	 * @param handler cache没有命中时的直接查找策略
	 * @return 查找到的对象，没找到返回空
	 */
	Object getFromCache(String key, int expiration, CacheMissHandler handler);

}
