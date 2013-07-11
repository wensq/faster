/**
 * ${fileName} was created on ${date} ${time}
 *
 * Copyright (c) ${year} EASTCOM Software Technology Co., Ltd. All rights reserved.
 */
package ${basePackage}.criteria;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.eastcom.intsight.resource.common.criteria.IRMSEntityCriteria;
import ${basePackage}.model.${model};

/**
 * @author ${author}
 */
@XmlRootElement(name = "Criteria")
@XmlAccessorType(XmlAccessType.FIELD)
public class ${model}Criteria extends ${parent}Criteria<${model}> {

}
