/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.linkis.metadata.query.server.restful;

import org.apache.linkis.common.exception.ErrorException;
import org.apache.linkis.metadata.query.common.domain.MetaPartitionInfo;
import org.apache.linkis.metadata.query.server.WebApplicationServer;
import org.apache.linkis.metadata.query.server.service.MetadataQueryService;
import org.apache.linkis.server.Message;
import org.apache.linkis.server.MessageStatus;
import org.apache.linkis.server.security.SecurityFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;

@ExtendWith({SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest(classes = {WebApplicationServer.class})
class MetadataCoreRestfulTest {

    @Autowired protected MockMvc mockMvc;

    @MockBean private MetadataQueryService metadataQueryService;

    private static MockedStatic<SecurityFilter> securityFilter;

    @BeforeAll
    private static void init() {
        securityFilter = Mockito.mockStatic(SecurityFilter.class);
    }

    @AfterAll
    private static void close() {
        securityFilter.close();
    }

    @Test
    void testGetDatabases() {
        try {
            String dataSourceId = "1l";
            String url = String.format("/metadataQuery/dbs/%s", dataSourceId);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("system", "");
            MvcUtils mvcUtils = new MvcUtils(mockMvc);
            Message res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(
                    MessageStatus.ERROR() == res.getStatus()
                            && res.getMessage().contains("'system' is missing"));
            params.add("system", "hive");
            Mockito.when(metadataQueryService.getDatabasesByDsId(dataSourceId, ",hive", null))
                    .thenReturn(new ArrayList<>());
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(MessageStatus.SUCCESS() == res.getStatus());

            Mockito.doThrow(new ErrorException(1, ""))
                    .when(metadataQueryService)
                    .getDatabasesByDsId(dataSourceId, ",hive", null);
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(
                    MessageStatus.ERROR() == res.getStatus()
                            && res.getMessage().contains("Fail to get database list"));
        } catch (Exception e) {
            // ignore
        }
    }

    @Test
    void testGetTables() throws Exception {
        String dataSourceId = "1l";
        String database = "hivedb";
        String url = String.format("/metadataQuery/tables/%s/db/%s", dataSourceId, database);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("system", "");
        MvcUtils mvcUtils = new MvcUtils(mockMvc);
        Message res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
        Assertions.assertTrue(
                MessageStatus.ERROR() == res.getStatus()
                        && res.getMessage().contains("'system' is missing"));

        params.add("system", "hive");
        Mockito.when(metadataQueryService.getTablesByDsId(dataSourceId, database, ",hive", null))
                .thenReturn(new ArrayList<>());
        res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
        Assertions.assertTrue(MessageStatus.SUCCESS() == res.getStatus());

        Mockito.doThrow(new ErrorException(1, ""))
                .when(metadataQueryService)
                .getTablesByDsId(dataSourceId, database, ",hive", null);
        res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
        Assertions.assertTrue(
                MessageStatus.ERROR() == res.getStatus()
                        && res.getMessage().contains("Fail to get table list"));
    }

    @Test
    void testGetTableProps() {
        try {
            String dataSourceId = "1l";
            String database = "hivedb";
            String table = "testtab";
            String url =
                    String.format(
                            "/metadataQuery/props/%s/db/%s/table/%s",
                            dataSourceId, database, table);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("system", "");
            MvcUtils mvcUtils = new MvcUtils(mockMvc);
            Message res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(
                    MessageStatus.ERROR() == res.getStatus()
                            && res.getMessage().contains("'system' is missing"));

            params.add("system", "hive");
            Mockito.when(
                            metadataQueryService.getTablePropsByDsId(
                                    dataSourceId, database, table, ",hive", null))
                    .thenReturn(new HashMap<>());
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(MessageStatus.SUCCESS() == res.getStatus());

            Mockito.doThrow(new ErrorException(1, ""))
                    .when(metadataQueryService)
                    .getTablePropsByDsId(dataSourceId, database, table, ",hive", null);
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(
                    MessageStatus.ERROR() == res.getStatus()
                            && res.getMessage().contains("Fail to get table properties"));
        } catch (Exception e) {
            // ignore
        }
    }

    @Test
    void testGetPartitions() {
        try {
            String dataSourceId = "1l";
            String database = "hivedb";
            String table = "testtab";
            String url =
                    String.format(
                            "/metadataQuery/partitions/%s/db/%s/table/%s",
                            dataSourceId, database, table);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("system", "");
            MvcUtils mvcUtils = new MvcUtils(mockMvc);
            Message res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(
                    MessageStatus.ERROR() == res.getStatus()
                            && res.getMessage().contains("'system' is missing"));

            params.add("system", "hive");
            Mockito.when(
                            metadataQueryService.getPartitionsByDsId(
                                    dataSourceId, database, table, ",hive", false, null))
                    .thenReturn(new MetaPartitionInfo());
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(MessageStatus.SUCCESS() == res.getStatus());

            Mockito.doThrow(new ErrorException(1, ""))
                    .when(metadataQueryService)
                    .getPartitionsByDsId(dataSourceId, database, table, ",hive", false, null);
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(
                    MessageStatus.ERROR() == res.getStatus()
                            && res.getMessage().contains("Fail to get partitions"));
        } catch (Exception e) {
            // ignore
        }
    }

    @Test
    void testGetColumns() {
        try {
            String dataSourceId = "1l";
            String database = "hivedb";
            String table = "testtab";
            String url =
                    String.format(
                            "/metadataQuery/columns/%s/db/%s/table/%s",
                            dataSourceId, database, table);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("system", "");
            MvcUtils mvcUtils = new MvcUtils(mockMvc);
            Message res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(
                    MessageStatus.ERROR() == res.getStatus()
                            && res.getMessage().contains("'system' is missing"));

            params.add("system", "hive");
            Mockito.when(
                            metadataQueryService.getColumnsByDsId(
                                    dataSourceId, database, table, ",hive", null))
                    .thenReturn(new ArrayList<>());
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(MessageStatus.SUCCESS() == res.getStatus());

            Mockito.doThrow(new ErrorException(1, ""))
                    .when(metadataQueryService)
                    .getColumnsByDsId(dataSourceId, database, table, ",hive", null);
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url, params));
            Assertions.assertTrue(
                    MessageStatus.ERROR() == res.getStatus()
                            && res.getMessage().contains("Fail to get column list"));
        } catch (Exception e) {
            // ignore
        }
    }
}
