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
package org.faster.opm;

import org.faster.opm.annotation.Resource;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Resource辅助类
 *
 * @author sqwen
 * @date 2012-4-22
 */
public class ResourceHelper {

    private ResourceHelper() {}

    /**
     * 是否有资源标注
     *
     * @param clazz 类名
     * @return
     */
    public static final boolean isResource(Class<?> clazz) {
        return clazz != null && clazz.isAnnotationPresent(Resource.class);
    }

    public static final boolean isResourceObject(Object obj) {
        return obj != null && isResource(obj.getClass());
    }

    public static final String getResourceType(Class<?> clazz) {
        Resource resource = clazz.getAnnotation(Resource.class);
        if (resource == null) {
            return null;
        }
        return isBlank(resource.type()) ? clazz.getSimpleName() : resource.type();
    }

}
