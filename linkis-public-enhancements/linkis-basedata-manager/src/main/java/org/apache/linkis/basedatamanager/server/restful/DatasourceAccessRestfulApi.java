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
package org.apache.linkis.basedatamanager.server.restful;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.linkis.basedatamanager.server.domain.DatasourceAccessEntity;
import org.apache.linkis.basedatamanager.server.service.DatasourceAccessService;
import org.apache.linkis.server.Message;
import org.apache.linkis.server.utils.ModuleUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(tags = "DatasourceAccessRestfulApi")
@RestController
@RequestMapping(path = "/basedata-manager/datasource-access")
public class DatasourceAccessRestfulApi {

    @Autowired
    DatasourceAccessService datasourceAccessService;

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "HttpServletRequest", name = "request", value = ""),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "searchName", value = ""),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "currentPage", value = ""),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "")
    })
    @ApiOperation(value = "list", notes = "Query list data of Datasource Access", httpMethod = "GET")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public Message list(HttpServletRequest request, String searchName, Integer currentPage, Integer pageSize) {
        ModuleUserUtils.getOperationUser(request,"Query list data of Datasource Access,search name:"+searchName);
        PageInfo pageList = datasourceAccessService.getListByPage(searchName,currentPage,pageSize);
        return Message.ok("").data("list", pageList);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "HttpServletRequest", name = "request", value = ""),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "")
    })
    @ApiOperation(value = "get", notes = "Get a Datasource Access Record", httpMethod = "GET")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Message get(HttpServletRequest request,@PathVariable("id") Long id) {
        ModuleUserUtils.getOperationUser(request, "Get a Datasource Access Record,id:"+id.toString());
        DatasourceAccessEntity datasourceAccess = datasourceAccessService.getById(id);
        return Message.ok("").data("item", datasourceAccess);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "HttpServletRequest", name = "request", value = ""),
            @ApiImplicitParam(paramType = "body", dataType = "DatasourceAccessEntity", name = "datasourceAccess", value = "")
    })
    @ApiOperation(value = "add", notes = "", httpMethod = "POST")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Message add(HttpServletRequest request,@RequestBody DatasourceAccessEntity datasourceAccess) {
        ModuleUserUtils.getOperationUser(request, "Add a Datasource Access Record,"+datasourceAccess.toString());
        datasourceAccess.setAccessTime(new Date());
        boolean result = datasourceAccessService.save(datasourceAccess);
        return Message.ok("").data("result", result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "HttpServletRequest", name = "request", value = ""),
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "id", value = "")
    })
    @ApiOperation(value = "remove", notes = "Remove a Datasource Access Record", httpMethod = "DELETE")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public Message remove(HttpServletRequest request,@PathVariable("id") Long id) {
        ModuleUserUtils.getOperationUser(request, "Remove a Datasource Access Record,id:"+id.toString());
        boolean result = datasourceAccessService.removeById(id);
        return Message.ok("").data("result", result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "HttpServletRequest", name = "request", value = ""),
            @ApiImplicitParam(paramType = "body", dataType = "DatasourceAccessEntity", name = "datasourceAccess", value = "")
    })
    @ApiOperation(value = "update", notes = "Update a Datasource Access Record", httpMethod = "PUT")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public Message update(HttpServletRequest request,@RequestBody DatasourceAccessEntity datasourceAccess) {
        ModuleUserUtils.getOperationUser(request, "Update a Datasource Access Record,id:"+datasourceAccess.getId().toString());
        boolean result = datasourceAccessService.updateById(datasourceAccess);
        return Message.ok("").data("result", result);
    }


}
