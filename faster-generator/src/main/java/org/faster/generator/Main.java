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
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author sqwen
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Configuration file must be specified!");
            System.exit(1);
        }
        String cfgFile = args[0];
        System.out.printf("Reading configuration from: %s\n", cfgFile);
        Settings settings = new Settings(cfgFile);
        if (settings.isOverwrite()) {
            if (settings.isCoreOutputEnabled()) {
                System.out.printf("* Core files(Model/Criteria/Serivce/ServiceImpl) destination directory: %s\n", settings.getCoreOutputDir());
            }
            if (settings.isRsOutputEnabled()) {
                System.out.printf("* RS files(DTO/RS) destination directory: %s\n", settings.getRsOutputDir());
            }
            System.out.printf("* Templates source directory: %s\n", settings.getTemplateDir());
            System.out.printf("* Generate base package: %s\n", settings.getBasePackage());
            System.out.println("* Generate models:");
            for (ModelSetting ms : settings.getModelSettings()) {
                System.out.println("- " + ms.getModelName());
            }
            System.out.printf("Are you sure to continue? [y/N] ", settings.getBasePackage());
            if (!new BufferedReader(new InputStreamReader(System.in)).readLine().equalsIgnoreCase("y")) {
                System.out.println("Exit.");
                System.exit(1);
            }
        }

        Configuration cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading(new File(settings.getTemplateDir()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));

        CodeGenerateManager manager = new CodeGenerateManager(cfg, settings);
        manager.generateAll();
    }
}
