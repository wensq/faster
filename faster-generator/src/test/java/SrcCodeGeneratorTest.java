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

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import junit.framework.TestCase;
import org.apache.commons.lang3.time.FastDateFormat;
import org.faster.generator.CodeGenerator;
import org.faster.generator.Model;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * @author sqwen
 */
public class SrcCodeGeneratorTest extends TestCase {

    public void testGenerate() {
        Configuration cfg = new Configuration();
        try {
            cfg.setDirectoryForTemplateLoading(new File("/Users/sqwen/Development/java/github/faster/faster-generator/src/main/template/general"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));

        String destBaseDir = "/Users/sqwen/Development/java/eastcom/intsight-2.0/intsight-resource/resource-core/resource-corenetwork/src/main/java";
        String basePackage = "com.eastcom.intsight.resource.corenetwork";
        Model model = new Model();
        model.setDestBaseDir(destBaseDir);
        model.setBasePackage(basePackage);
        model.setModel("MSS");
        model.setFileName("MSS.java");
        model.setParent("IRMSEntity");
        model.setTable("VIEW_IRMS_CORE_MSS");
        model.setRemark("MSCSERVER: 移动软交换中心，简称MSS");
        Calendar now = Calendar.getInstance();
        model.setYear(now.get(Calendar.YEAR) + "");
        model.setDate(FastDateFormat.getInstance("yyyy-MM-dd").format(now));
        model.setTime(FastDateFormat.getInstance("HH:mm:ss").format(now));

        model.setProperty("schema", "IRMS");
        model.setProperty("author", "sqwen");
        model.setProperty("version", "1.0");

        CodeGenerator gen = new CodeGenerator(cfg, model.getDataModel());

        gen.generate("Model.ftl", model.getModelFile(), true);

        gen.generate("Criteria.ftl", model.getCriteriaFile(), true);
        System.out.println();

        gen.generate("Service.ftl", model.getServiceFile(), true);
        System.out.println();

        gen.generate("ServiceImpl.ftl", model.getServiceImplFile(), true);
        System.out.println();

        System.out.flush();
    }
}
