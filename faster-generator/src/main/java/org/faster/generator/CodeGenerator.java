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
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author sqwen
 */
public class CodeGenerator {

    private Map<String, Object> model;

    private Configuration conf;

    public CodeGenerator(Configuration conf, Map<String, Object> model) {
        this.conf = conf;
        this.model = model;
    }

    public boolean generate(String templateName, File file, boolean overwrite) {
        if (file.exists()) {
            if (overwrite && file.delete()) {
                directGenerate(templateName, file);
                return true;
            } else {
                return false;
            }
        }

        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        directGenerate(templateName, file);
        return true;
    }

    private void directGenerate(String templateName, File destFile) {
        Template template = null;
        try {
            template = conf.getTemplate(templateName);
        } catch (IOException e) {
            throw new RuntimeException("Can't load template: " + templateName, e);
        }
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(destFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Dest file does not exist!", e);
        }
        try {
            model.put("fileName", destFile.getName());
            template.process(model, out);
            out.flush();
        } catch (TemplateException e) {
            throw new RuntimeException("Generate source code failed.", e);
        } catch (IOException e) {
            throw new RuntimeException("Can't write generated source code.", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
