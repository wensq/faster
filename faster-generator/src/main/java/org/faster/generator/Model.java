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

package org.faster.generator;

import org.apache.commons.lang3.time.FastDateFormat;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sqwen
 */
public class Model {

    private String destBaseDir;

    private String basePackage;

    private String model;

    private String parent = "GenericEntity";

    private String table;

    private String remark;

    private String resourceType;

    private String xmlRootElement;

    private String fileName;

    private String year;

    private String date;

    private String time;

    private Map<String, Object> properties;

    private String baseDir;
    
    public Model() {}

    public Model(ModelSetting modelSetting) {
        model = modelSetting.getModelName();
        parent = modelSetting.getParent();
        table = modelSetting.getTable();
        remark = modelSetting.getRemark();
    }

    public File getModelFile() {
        return new File(
                new StringBuilder(getBaseDir())
                    .append("/model/")
                    .append(getModelFileName())
                    .toString()
        );
    }

    private String getBaseDir() {
        if (baseDir == null) {
            baseDir =  new StringBuilder(destBaseDir)
                    .append("/")
                    .append(GeneratorUtils.convertPackageToRelativeFilePath(basePackage))
                    .toString();
        }
        return baseDir;
    }

    public String getModelFileName() {
        return model + ".java";
    }

    public File getCriteriaFile() {
        return new File(
                new StringBuilder(getBaseDir())
                    .append("/criteria/")
                    .append(getCriteriaFileName())
                    .toString()
        );
    }

    public String getCriteriaFileName() {
        return model + "Criteria.java";
    }

    public File getServiceFile() {
        return new File(
                new StringBuilder(getBaseDir())
                    .append("/service/")
                    .append(getServiceFileName())
                    .toString()
        );
    }

    public String getServiceFileName() {
        return model + "Service.java";
    }

    public File getServiceImplFile() {
        return new File(
                new StringBuilder(getBaseDir())
                    .append("/service/hibernate/")
                    .append(getServiceImplFileName())
                    .toString()
        );
    }

    public String getServiceImplFileName() {
        return new StringBuilder("Hibernate")
                .append(model)
                .append("Service.java")
                .toString();
    }

    public Model setProperty(String name, Object value) {
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }
        properties.put(name, value);
        return this;
    }

    public Map<String, Object> getDataModel() {
        Map<String, Object> dm = new HashMap<String, Object>();
        if (properties != null) {
            dm.putAll(properties);
        }
        dm.put("basePackage", basePackage);
        dm.put("model", model);
        dm.put("parent", parent);
        dm.put("table", table);
        dm.put("remark", remark);
        Calendar now = Calendar.getInstance();
        dm.put("year", year == null ? now.get(Calendar.YEAR) + "" : year);
        dm.put("date", date == null ? FastDateFormat.getInstance("yyyy-MM-dd").format(now) : date);
        dm.put("time", time == null ? FastDateFormat.getInstance("HH:mm:ss").format(now) : time);
        dm.put("fileName", fileName);
        return dm;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getXmlRootElement() {
        return xmlRootElement;
    }

    public void setXmlRootElement(String xmlRootElement) {
        this.xmlRootElement = xmlRootElement;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDestBaseDir() {
        return destBaseDir;
    }

    public void setDestBaseDir(String destBaseDir) {
        this.destBaseDir = destBaseDir;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
