/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.apache.linkis.manager.label;

import org.apache.linkis.manager.label.builder.factory.LabelBuilderFactory;
import org.apache.linkis.manager.label.builder.factory.LabelBuilderFactoryContext;
import org.apache.linkis.manager.label.builder.factory.StdLabelBuilderFactory;
import org.apache.linkis.manager.label.entity.Label;
import org.apache.linkis.manager.label.entity.node.AliasServiceInstanceLabel;
import org.apache.linkis.manager.label.exception.LabelErrorException;


public class TestLabelBuilder {

    public static void main(String[] args) throws LabelErrorException {
        LabelBuilderFactory labelBuilderFactory = LabelBuilderFactoryContext.getLabelBuilderFactory();
        Label<?> engineType = labelBuilderFactory.createLabel("engineType", "hive-1.2.1");
        System.out.println(engineType.getFeature());

        AliasServiceInstanceLabel emInstanceLabel = labelBuilderFactory.createLabel(AliasServiceInstanceLabel.class);
        emInstanceLabel.setAlias("hello");
        System.out.println(emInstanceLabel.getStringValue());
    }
}
