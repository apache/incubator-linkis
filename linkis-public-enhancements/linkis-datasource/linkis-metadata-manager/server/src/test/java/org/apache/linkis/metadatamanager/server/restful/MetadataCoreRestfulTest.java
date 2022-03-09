package org.apache.linkis.metadatamanager.server.restful;

import org.apache.linkis.common.exception.ErrorException;
import org.apache.linkis.metadatamanager.common.domain.MetaPartitionInfo;
import org.apache.linkis.metadatamanager.server.WebApplicationServer;
import org.apache.linkis.metadatamanager.server.service.MetadataAppService;
import org.apache.linkis.server.Message;
import org.apache.linkis.server.MessageStatus;
import org.apache.linkis.server.security.SecurityFilter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.isA;


@ExtendWith({SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest(classes = {WebApplicationServer.class})
class MetadataCoreRestfulTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private MetadataAppService metadataAppService;

    private static MockedStatic<SecurityFilter> securityFilter;

    @BeforeAll
    private static void init(){
        securityFilter = Mockito.mockStatic(SecurityFilter.class);
    }

    @AfterAll
    private static void close(){
        securityFilter.close();
    }


    @Test
    void testGetDatabases() {
        try {
            String dataSourceId = "1l";
            String url = String.format("/metadatamanager/dbs/%s",dataSourceId);
            MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
            params.add("system","");
            MvcUtils mvcUtils = new MvcUtils(mockMvc);
            Message res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.ERROR() == res.getStatus() && res.getMessage().contains("'system' is missing"));
            params.add("system","hive");
            Mockito.when(metadataAppService.getDatabasesByDsId(dataSourceId,",hive",null)).thenReturn(new ArrayList<>());
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.SUCCESS() == res.getStatus());

            Mockito.doThrow(new ErrorException(1,"")).when(metadataAppService).getDatabasesByDsId(dataSourceId,",hive",null);
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.ERROR() == res.getStatus() && res.getMessage().contains("Fail to get database list"));
        }catch (Exception e){
            //ignore
        }

    }

    @Test
    void testGetTables() throws Exception {
        String dataSourceId = "1l";
        String database = "hivedb";
        String url = String.format("/metadatamanager/tables/%s/db/%s",dataSourceId,database);
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("system","");
        MvcUtils mvcUtils = new MvcUtils(mockMvc);
        Message res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
        Assertions.assertTrue(MessageStatus.ERROR() == res.getStatus() && res.getMessage().contains("'system' is missing"));

        params.add("system","hive");
        Mockito.when(metadataAppService.getTablesByDsId(dataSourceId,database,",hive",null)).thenReturn(new ArrayList<>());
        res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
        Assertions.assertTrue(MessageStatus.SUCCESS() == res.getStatus());

        Mockito.doThrow(new ErrorException(1,"")).when(metadataAppService).getTablesByDsId(dataSourceId,database,",hive",null);
        res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
        Assertions.assertTrue(MessageStatus.ERROR() == res.getStatus() && res.getMessage().contains("Fail to get table list"));
    }

    @Test
    void testGetTableProps() {
        try {
            String dataSourceId = "1l";
            String database = "hivedb";
            String table = "testtab";
            String url = String.format("/metadatamanager/props/%s/db/%s/table/%s",dataSourceId,database,table);
            MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
            params.add("system","");
            MvcUtils mvcUtils = new MvcUtils(mockMvc);
            Message res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.ERROR() == res.getStatus() && res.getMessage().contains("'system' is missing"));

            params.add("system","hive");
            Mockito.when(metadataAppService.getTablePropsByDsId(dataSourceId,database,table,",hive",null)).thenReturn(new HashMap<>());
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.SUCCESS() == res.getStatus());

            Mockito.doThrow(new ErrorException(1,"")).when(metadataAppService).getTablePropsByDsId(dataSourceId,database,table,",hive",null);
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.ERROR() == res.getStatus() && res.getMessage().contains("Fail to get table properties"));
        }catch (Exception e) {
            //ignore
        }

    }

    @Test
    void testGetPartitions() {
        try {
            String dataSourceId = "1l";
            String database = "hivedb";
            String table = "testtab";
            String url = String.format("/metadatamanager/partitions/%s/db/%s/table/%s",dataSourceId,database,table);
            MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
            params.add("system","");
            MvcUtils mvcUtils = new MvcUtils(mockMvc);
            Message res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.ERROR() == res.getStatus() && res.getMessage().contains("'system' is missing"));

            params.add("system","hive");
            Mockito.when(metadataAppService.getPartitionsByDsId(dataSourceId,database,table,",hive",null)).thenReturn(new MetaPartitionInfo());
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.SUCCESS() == res.getStatus());

            Mockito.doThrow(new ErrorException(1,"")).when(metadataAppService).getPartitionsByDsId(dataSourceId,database,table,",hive",null);
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.ERROR() == res.getStatus() && res.getMessage().contains("Fail to get partitions"));
        }catch (Exception e){
            //ignore
        }

    }

    @Test
    void testGetColumns() {
        try {
            String dataSourceId = "1l";
            String database = "hivedb";
            String table = "testtab";
            String url = String.format("/metadatamanager/columns/%s/db/%s/table/%s",dataSourceId,database,table);
            MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
            params.add("system","");
            MvcUtils mvcUtils = new MvcUtils(mockMvc);
            Message res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.ERROR() == res.getStatus() && res.getMessage().contains("'system' is missing"));

            params.add("system","hive");
            Mockito.when(metadataAppService.getColumns(dataSourceId,database,table,",hive",null)).thenReturn(new ArrayList<>());
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.SUCCESS() == res.getStatus());

            Mockito.doThrow(new ErrorException(1,"")).when(metadataAppService).getColumns(dataSourceId,database,table,",hive",null);
            res = mvcUtils.getMessage(mvcUtils.buildMvcResultGet(url,params));
            Assertions.assertTrue(MessageStatus.ERROR() == res.getStatus() && res.getMessage().contains("Fail to get column list"));
        }catch (Exception e){
            //ignore
        }

    }
}