/**
 * ${fileName} was created on ${date} ${time}
 *
 * Copyright (c) ${year} EASTCOM Software Technology Co., Ltd. All rights reserved.
 */
package ${basePackage}.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.faster.opm.annotation.Property;
import org.faster.opm.annotation.Resource;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.eastcom.intsight.resource.common.model.IRMSEntity;

/**
<#if remark??>
 * ${remark}
 *
</#if>
 * @author sqwen
 */
@Resource(type = "${model?upper_case}")
@XmlRootElement(name = "${model?upper_case}")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "${table}", schema = "${schema}")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class ${model} extends ${parent} {

    private static final long serialVersionUID = -7867563577584448439L;

}
