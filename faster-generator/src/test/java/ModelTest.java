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

import junit.framework.TestCase;
import org.faster.generator.Model;

import java.io.IOException;

/**
 * @author sqwen
 */
public class ModelTest extends TestCase {

    public void testGetModelFile() throws IOException {
        Model model = new Model();
        model.setDestBaseDir("/Users/sqwen/Development/java/eastcom/intsight-2.0/intsight-resource/resource-core/resource-corenetwork/src/main/java");
        model.setModel("MSS");
        model.setBasePackage("com.eastcom.intsight.resource.corenetwork");
        System.out.println(model.getModelFile().getCanonicalPath());
        System.out.println(model.getCriteriaFile().getCanonicalPath());
        System.out.println(model.getServiceFile().getCanonicalPath());
        System.out.println(model.getServiceImplFile().getCanonicalPath());
    }

}
