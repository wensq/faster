/**
 * ${fileName} was created on ${date} ${time}
 *
 * Copyright (c) ${year} EASTCOM Software Technology Co., Ltd. All rights reserved.
 */
package ${basePackage}.service.hibernate;

import org.springframework.stereotype.Service;

import com.eastcom.intsight.resource.common.service.hibernate.HibernateIRMSEntityService;
import ${basePackage}.model.${model};
import ${basePackage}.service.${model}Service;

/**
 * @author ${author}
 */
@Service
public class Hibernate${model}Service extends Hibernate${parent}Service<${model}> implements ${model}Service {

}
