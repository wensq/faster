/**
 * ${fileName} was created on ${date} ${time}
 *
 * Copyright (c) ${year} EASTCOM Software Technology Co., Ltd. All rights reserved.
 */
package ${basePackage}.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.faster.opm.annotation.Property;
import org.faster.opm.annotation.ResourceID;
import org.faster.orm.model.GenericEntity;

/**
 * @author sqwen
 */
@SuppressWarnings("serial")
@MappedSuperclass
public class ${model} extends ${parent}<${idType}> {

    @ResourceID
    @Property(name = "标识", group = "标识信息,基本信息")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected ${idType} ${idName};

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
