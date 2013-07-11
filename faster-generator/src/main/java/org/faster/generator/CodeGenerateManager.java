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

import freemarker.template.Configuration;

import java.io.File;
import java.util.Map;

/**
 * @author sqwen
 */
public class CodeGenerateManager {

    private Configuration conf;

    private Settings settings;

    private int successCount = 0;
    private int failedCount = 0;

    public CodeGenerateManager(Configuration conf, Settings settings) {
        this.conf = conf;
        this.settings = settings;
    }

    public void generateAll() {
        for (ModelSetting ms : settings.getModelSettings()) {
            System.out.printf("Processing model: %s\n", ms.getModelName());
            Model model = new Model(ms);
            model.setBasePackage(settings.getBasePackage());
            model.setProperties(settings.getProperties());
            Map<String, Object> data = model.getDataModel();
            CodeGenerator generator = new CodeGenerator(conf, data);
            if (settings.isCoreOutputEnabled()) {
                model.setDestBaseDir(settings.getCoreOutputDir());
                generate(generator, "Model.ftl", model.getModelFile(), settings.isOverwrite());
                generate(generator, "Criteria.ftl", model.getCriteriaFile(), settings.isOverwrite());
                generate(generator, "Service.ftl", model.getServiceFile(), settings.isOverwrite());
                generate(generator, "ServiceImpl.ftl", model.getServiceImplFile(), settings.isOverwrite());
            }
        }
        System.out.println();
        System.out.printf("Source code generate complete. (SUCCESS=%d FAILED=%d)\n", successCount, failedCount);
    }

    private void generate(CodeGenerator generator, String templateFile, File destFile, boolean overwrite) {
        System.out.printf("+ Generating %s...", destFile.getName());
        if (generator.generate(templateFile, destFile, overwrite)) {
            successCount++;
            System.out.println("[SUCCESS]");
        } else {
            failedCount++;
            System.out.println("[FAILED]");
        }
    }
}
