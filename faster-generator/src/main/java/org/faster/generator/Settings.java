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

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sqwen
 */
public class Settings {

    private String templateDir;

    private String coreOutputDir;

    private String rsOutputDir;

    private String basePackage;

    private boolean overwrite;

    private boolean coreOutputEnabled;

    private boolean rsOutputEnabled;

    private Map<String, Object> properties;

    private List<ModelSetting> modelSettings;

    public Settings(String cfgFile) {
        InputStream in = null;
        try {
            in = new FileInputStream(cfgFile);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(cfgFile + " does not exist.");
        }
        try {
            Document doc = new SAXReader().read(in);
            Element root = doc.getRootElement();
            Element dirEle = root.element("dir");
            templateDir = dirEle.elementText("template").trim();
            coreOutputDir = dirEle.elementText("core").trim();
            rsOutputDir = dirEle.elementText("rs").trim();
            basePackage = root.attributeValue("basePackage").trim();
            overwrite = Boolean.valueOf(root.attributeValue("overwrite").trim());
            String outputValues = root.attributeValue("output").trim();
            coreOutputEnabled = outputValues.contains("core");
            rsOutputEnabled = outputValues.contains("rs");

            List<Element> propertyEles = root.elements("property");
            properties = new HashMap<String, Object>(propertyEles.size());
            for (Element ele : propertyEles) {
                properties.put(ele.attributeValue("name"), ele.attributeValue("value"));
            }

            List<Element> modelEles = root.elements("model");
            modelSettings = new ArrayList<ModelSetting>(modelEles.size());
            for (Element ele : modelEles) {
                ModelSetting ms = new ModelSetting();
                ms.setModelName(ele.attributeValue("name"));
                ms.setTable(ele.attributeValue("table"));
                ms.setParent(ele.attributeValue("parent"));
                ms.setRemark(ele.attributeValue("remark"));
                modelSettings.add(ms);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public String getCoreOutputDir() {
        return coreOutputDir;
    }

    public String getRsOutputDir() {
        return rsOutputDir;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public List<ModelSetting> getModelSettings() {
        return modelSettings;
    }

    public boolean isCoreOutputEnabled() {
        return coreOutputEnabled;
    }

    public boolean isRsOutputEnabled() {
        return rsOutputEnabled;
    }
}
