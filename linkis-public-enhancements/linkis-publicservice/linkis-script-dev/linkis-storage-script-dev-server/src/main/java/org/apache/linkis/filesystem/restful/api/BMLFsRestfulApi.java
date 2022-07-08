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

package org.apache.linkis.filesystem.restful.api;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.linkis.common.io.FsPath;
import org.apache.linkis.filesystem.bml.BMLHelper;
import org.apache.linkis.filesystem.exception.WorkSpaceException;
import org.apache.linkis.filesystem.exception.WorkspaceExceptionManager;
import org.apache.linkis.server.Message;
import org.apache.linkis.server.utils.ModuleUserUtils;
import org.apache.linkis.storage.script.*;
import org.apache.linkis.storage.script.writer.StorageScriptFsWriter;
import org.apache.linkis.storage.source.FileSource;
import org.apache.linkis.storage.source.FileSource$;

import org.apache.commons.math3.util.Pair;
import org.apache.http.Consts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags="BMLFS管理")
@RestController
@RequestMapping(path = "/filesystem")
public class BMLFsRestfulApi {

    @Autowired BMLHelper bmlHelper;
    @ApiOperation(value="打开ScriptFromBML",notes="openScriptFromBML",response = Message.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceId", required = false, dataType = "String", value = "资源Id"),
            @ApiImplicitParam(name = "version",required = false, dataType = "String", value = "版本"),
            @ApiImplicitParam(name = "creator",required = false,  dataType = "String", value = "创建者"),
            @ApiImplicitParam(name = "projectName", required = false, dataType = "String", value = "项目名称"),
            @ApiImplicitParam(name = "fileName",required = true,  dataType = "String", value = "文件名称")
    })
    @RequestMapping(path = "/openScriptFromBML", method = RequestMethod.GET)
    public Message openScriptFromBML(
            HttpServletRequest req,
            @RequestParam(value = "resourceId", required = false) String resourceId,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "creator", required = false) String creator,
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "fileName", defaultValue = "test.sql") String fileName)
            throws IOException, WorkSpaceException {
        String userName = ModuleUserUtils.getOperationUser(req, "openScriptFromBML");
        Map<String, Object> query = bmlHelper.query(userName, resourceId, version);
        InputStream inputStream = (InputStream) query.get("stream");
        try (FileSource fileSource =
                FileSource$.MODULE$.create(new FsPath(fileName), inputStream)) {
            Pair<Object, ArrayList<String[]>> collect = fileSource.collect()[0];
            Message message;
            try {
                message = new Gson().fromJson(collect.getSecond().get(0)[0], Message.class);
                if (message == null) throw WorkspaceExceptionManager.createException(80019);
            } catch (Exception e) {
                return Message.ok()
                        .data("scriptContent", collect.getSecond().get(0)[0])
                        .data("metadata", collect.getFirst());
            }
            if (message.getStatus() != 0) {
                throw new WorkSpaceException(80020, message.getMessage());
            }
            return Message.ok()
                    .data("scriptContent", collect.getSecond().get(0)[0])
                    .data("metadata", collect.getFirst());
        }
    }
    @ApiOperation(value="/product/openScriptFromBML",notes="/product/openScriptFromBML",response = Message.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceId", required = false, dataType = "String", value = "资源Id"),
            @ApiImplicitParam(name = "version",required = false, dataType = "String", value = "版本"),
            @ApiImplicitParam(name = "creator",required = false,  dataType = "String", value = "创建者"),
            @ApiImplicitParam(name = "fileName",required = true,  dataType = "String", value = "文件名称")
    })
    @RequestMapping(path = "/product/openScriptFromBML", method = RequestMethod.GET)
    public Message openScriptFromProductBML(
            HttpServletRequest req,
            @RequestParam(value = "resourceId", required = false) String resourceId,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "creator", required = false) String creator,
            @RequestParam(value = "fileName", defaultValue = "test.sql") String fileName)
            throws IOException, WorkSpaceException {
        String userName = ModuleUserUtils.getOperationUser(req, "openScriptFromBML");
        if (!StringUtils.isEmpty(creator)) {
            userName = creator;
        }
        Map<String, Object> query = bmlHelper.query(userName, resourceId, version);
        InputStream inputStream = (InputStream) query.get("stream");
        try (FileSource fileSource =
                FileSource$.MODULE$.create(new FsPath(fileName), inputStream)) {
            Pair<Object, ArrayList<String[]>> collect = fileSource.collect()[0];
            Message message;
            try {
                message = new Gson().fromJson(collect.getSecond().get(0)[0], Message.class);
                if (message == null) {
                    throw WorkspaceExceptionManager.createException(80019);
                }
            } catch (Exception e) {
                return Message.ok()
                        .data("scriptContent", collect.getSecond().get(0)[0])
                        .data("metadata", collect.getFirst());
            }
            if (message.getStatus() != 0) {
                throw new WorkSpaceException(80020, message.getMessage());
            }
            return Message.ok()
                    .data("scriptContent", collect.getSecond().get(0)[0])
                    .data("metadata", collect.getFirst());
        }
    }
    @ApiOperation(value="保存来自BML的script",notes="保存来自BML的script",response = Message.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scriptContent", required = true, dataType = "String", value = "内容"),
            @ApiImplicitParam(name = "resourceId",required = false, dataType = "String", value = "资源ID"),
            @ApiImplicitParam(name = "creator",required = false,  dataType = "String", value = "创建者"),
            @ApiImplicitParam(name = "fileName",required = false,  dataType = "String", value = "文件名称"),
            @ApiImplicitParam(name = "projectName",required = false,  dataType = "String", value = "项目名称"),
            @ApiImplicitParam(name = "metadata",required = false,  dataType = "String", value = "元数据")
    })
    @ApiOperationSupport(ignoreParameters = {"json"})
    @RequestMapping(path = "/saveScriptToBML", method = RequestMethod.POST)
    public Message saveScriptToBML(HttpServletRequest req, @RequestBody Map<String, Object> json)
            throws IOException {

        String scriptContent = (String) json.get("scriptContent");
        Map<String, Object> params = (Map<String, Object>) json.get("metadata");
        String fileName = (String) json.get("fileName");
        String resourceId = (String) json.get("resourceId");
        String creator = (String) json.get("creator");
        String projectName = (String) json.get("projectName");
        String userName = ModuleUserUtils.getOperationUser(req, "saveScriptToBML" + fileName);
        ScriptFsWriter writer =
                StorageScriptFsWriter.getScriptFsWriter(
                        new FsPath(fileName), Consts.UTF_8.toString(), null);
        Variable[] v = VariableParser.getVariables(params);
        List<Variable> variableList =
                Arrays.stream(v)
                        .filter(var -> !StringUtils.isEmpty(var.value()))
                        .collect(Collectors.toList());
        writer.addMetaData(new ScriptMetaData(variableList.toArray(new Variable[0])));
        writer.addRecord(new ScriptRecord(scriptContent));
        try (InputStream inputStream = writer.getInputStream()) {
            String version;
            if (resourceId == null) {
                //  新增文件
                Map<String, Object> bmlResponse = new HashMap<>();
                if (!StringUtils.isEmpty(projectName)) {
                    bmlResponse = bmlHelper.upload(userName, inputStream, fileName, projectName);
                } else {
                    bmlResponse = bmlHelper.upload(userName, inputStream, fileName);
                }
                resourceId = bmlResponse.get("resourceId").toString();
                version = bmlResponse.get("version").toString();
            } else {
                //  更新文件
                Map<String, Object> bmlResponse =
                        bmlHelper.update(userName, resourceId, inputStream);
                resourceId = bmlResponse.get("resourceId").toString();
                version = bmlResponse.get("version").toString();
            }
            return Message.ok().data("resourceId", resourceId).data("version", version);
        }
    }
}
