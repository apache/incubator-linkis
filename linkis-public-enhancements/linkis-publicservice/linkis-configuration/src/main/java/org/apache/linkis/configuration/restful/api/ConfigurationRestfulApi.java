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

package org.apache.linkis.configuration.restful.api;

import org.apache.linkis.common.conf.Configuration;
import org.apache.linkis.configuration.entity.*;
import org.apache.linkis.configuration.exception.ConfigurationException;
import org.apache.linkis.configuration.service.CategoryService;
import org.apache.linkis.configuration.service.ConfigurationService;
import org.apache.linkis.configuration.util.ConfigurationConfiguration;
import org.apache.linkis.configuration.util.JsonNodeUtil;
import org.apache.linkis.configuration.util.LabelEntityParser;
import org.apache.linkis.manager.label.entity.engine.EngineTypeLabel;
import org.apache.linkis.manager.label.entity.engine.UserCreatorLabel;
import org.apache.linkis.manager.label.utils.LabelUtils;
import org.apache.linkis.server.BDPJettyServerHelper;
import org.apache.linkis.server.Message;
import org.apache.linkis.server.utils.ModuleUserUtils;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/configuration")
public class ConfigurationRestfulApi {

    @Autowired private ConfigurationService configurationService;

    @Autowired private CategoryService categoryService;

    ObjectMapper mapper = new ObjectMapper();

    private static final String NULL = "null";

    @RequestMapping(path = "/addKeyForEngine", method = RequestMethod.GET)
    public Message addKeyForEngine(
            HttpServletRequest req,
            @RequestParam(value = "engineType", required = false) String engineType,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "keyJson", required = false) String keyJson)
            throws ConfigurationException {
        if (StringUtils.isBlank(engineType)
                || StringUtils.isBlank(version)
                || StringUtils.isBlank(token)) {
            throw new ConfigurationException("params cannot be empty!");
        }
        // todo 检验token
        if (!token.equals(ConfigurationConfiguration.COPYKEYTOKEN)) {
            throw new ConfigurationException("token is error");
        }
        ConfigKey configKey = BDPJettyServerHelper.gson().fromJson(keyJson, ConfigKey.class);
        configurationService.addKeyForEngine(engineType, version, configKey);
        // TODO: 2019/12/30  configKey参数校验
        return Message.ok();
    }

    @RequestMapping(path = "/getFullTreesByAppName", method = RequestMethod.GET)
    public Message getFullTreesByAppName(
            HttpServletRequest req,
            @RequestParam(value = "engineType", required = false) String engineType,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "creator", required = false) String creator)
            throws ConfigurationException {
        String username = ModuleUserUtils.getOperationUser(req, "getFullTreesByAppName");
        if (creator != null && (creator.equals("通用设置") || creator.equals("全局设置"))) {
            engineType = "*";
            version = "*";
            creator = "*";
        }
        List labelList =
                LabelEntityParser.generateUserCreatorEngineTypeLabelList(
                        username, creator, engineType, version);
        ArrayList<ConfigTree> configTrees =
                configurationService.getFullTreeByLabelList(labelList, true);
        return Message.ok().data("fullTree", configTrees);
    }

    @RequestMapping(path = "/getCategory", method = RequestMethod.GET)
    public Message getCategory(HttpServletRequest req) {
        List<CategoryLabelVo> categoryLabelList = categoryService.getAllCategory();
        return Message.ok().data("Category", categoryLabelList);
    }

    @RequestMapping(path = "/createFirstCategory", method = RequestMethod.POST)
    public Message createFirstCategory(HttpServletRequest request, @RequestBody JsonNode jsonNode)
            throws ConfigurationException {
        String username = ModuleUserUtils.getOperationUser(request, "createFirstCategory");
        checkAdmin(username);
        String categoryName = jsonNode.get("categoryName").asText();
        String description = jsonNode.get("description").asText();
        if (StringUtils.isEmpty(categoryName) || categoryName.equals(NULL)) {
            throw new ConfigurationException("categoryName is null, cannot be added");
        }
        if (StringUtils.isEmpty(categoryName) || categoryName.contains("-")) {
            throw new ConfigurationException("categoryName cannot be included '-'");
        }
        categoryService.createFirstCategory(categoryName, description);
        return Message.ok();
    }

    @RequestMapping(path = "/deleteCategory", method = RequestMethod.POST)
    public Message deleteCategory(HttpServletRequest request, @RequestBody JsonNode jsonNode)
            throws ConfigurationException {
        String username = ModuleUserUtils.getOperationUser(request, "deleteCategory");
        checkAdmin(username);
        Integer categoryId = jsonNode.get("categoryId").asInt();
        categoryService.deleteCategory(categoryId);
        return Message.ok();
    }

    @RequestMapping(path = "/createSecondCategory", method = RequestMethod.POST)
    public Message createSecondCategory(HttpServletRequest request, @RequestBody JsonNode jsonNode)
            throws ConfigurationException {
        Integer categoryId = jsonNode.get("categoryId").asInt();
        String engineType = jsonNode.get("engineType").asText();
        String version = jsonNode.get("version").asText();
        String description = jsonNode.get("description").asText();
        if (categoryId <= 0) {
            throw new ConfigurationException("creator is null, cannot be added");
        }
        if (StringUtils.isEmpty(engineType) || engineType.toLowerCase().equals(NULL)) {
            throw new ConfigurationException("engine type is null, cannot be added");
        }
        if (StringUtils.isEmpty(version) || version.toLowerCase().equals(NULL)) {
            version = LabelUtils.COMMON_VALUE;
        }
        categoryService.createSecondCategory(categoryId, engineType, version, description);
        return Message.ok();
    }

    @RequestMapping(path = "/saveFullTree", method = RequestMethod.POST)
    public Message saveFullTree(HttpServletRequest req, @RequestBody JsonNode json)
            throws IOException, ConfigurationException {
        List fullTrees = mapper.treeToValue(json.get("fullTree"), List.class);
        String creator = JsonNodeUtil.getStringValue(json.get("creator"));
        String engineType = JsonNodeUtil.getStringValue(json.get("engineType"));
        if (creator != null && (creator.equals("通用设置") || creator.equals("全局设置"))) {
            creator = "*";
        }
        String username = ModuleUserUtils.getOperationUser(req, "saveFullTree");
        ArrayList<ConfigValue> createList = new ArrayList<>();
        ArrayList<ConfigValue> updateList = new ArrayList<>();
        for (Object o : fullTrees) {
            String s = BDPJettyServerHelper.gson().toJson(o);
            ConfigTree fullTree = BDPJettyServerHelper.gson().fromJson(s, ConfigTree.class);
            List<ConfigKeyValue> settings = fullTree.getSettings();
            Integer userLabelId =
                    configurationService.checkAndCreateUserLabel(settings, username, creator);
            for (ConfigKeyValue setting : settings) {
                configurationService.updateUserValue(setting, userLabelId, createList, updateList);
            }
        }
        String engine = null;
        String version = null;
        if (engineType != null) {
            String[] tmpString = engineType.split("-");
            if (tmpString.length != 2) {
                throw new ConfigurationException(
                        "The saved engine type parameter is incorrect, please send it in a fixed format, such as spark-2.4.3(保存的引擎类型参数有误，请按照固定格式传送，例如spark-2.4.3)");
            }
            engine = tmpString[0];
            version = tmpString[1];
        }
        configurationService.updateUserValue(createList, updateList);
        configurationService.clearAMCacheConf(username, creator, engine, version);
        Message message = Message.ok();
        return message;
    }

    @RequestMapping(path = "/engineType", method = RequestMethod.GET)
    public Message listAllEngineType(HttpServletRequest request) {
        String[] engineType = configurationService.listAllEngineType();
        return Message.ok().data("engineType", engineType);
    }

    @RequestMapping(path = "/updateCategoryInfo", method = RequestMethod.POST)
    public Message updateCategoryInfo(HttpServletRequest request, @RequestBody JsonNode jsonNode)
            throws ConfigurationException {
        String description = null;
        Integer categoryId = null;
        try {
            description = jsonNode.get("description").asText();
            categoryId = jsonNode.get("categoryId").asInt();
        } catch (Exception e) {
            throw new ConfigurationException("请求参数不完整，请重新确认");
        }
        if (description != null) {
            categoryService.updateCategory(categoryId, description);
        }
        return Message.ok();
    }

    @RequestMapping(path = "/rpcTest", method = RequestMethod.GET)
    public Message rpcTest(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "creator", required = false) String creator,
            @RequestParam(value = "engineType", required = false) String engineType,
            @RequestParam(value = "version", required = false) String version) {
        configurationService.queryGlobalConfig(username);
        EngineTypeLabel engineTypeLabel = new EngineTypeLabel();
        engineTypeLabel.setVersion(version);
        engineTypeLabel.setEngineType(engineType);
        configurationService.queryDefaultEngineConfig(engineTypeLabel);
        UserCreatorLabel userCreatorLabel = new UserCreatorLabel();
        userCreatorLabel.setCreator(creator);
        userCreatorLabel.setUser(username);
        configurationService.queryConfig(userCreatorLabel, engineTypeLabel, "wds.linkis.rm");
        Message message = Message.ok();
        return message;
    }

    private void checkAdmin(String userName) throws ConfigurationException {
        if (!Configuration.isAdmin(userName)) {
            throw new ConfigurationException("only admin can modify category(只有管理员才能修改目录)");
        }
    }
}
