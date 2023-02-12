package org.apache.linkis.basedatamanager.server.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.linkis.basedatamanager.server.Scan;
import org.apache.linkis.basedatamanager.server.WebApplicationServer;
import org.apache.linkis.basedatamanager.server.request.ConfigurationTemplateSaveRequest;
import org.apache.linkis.common.utils.JsonUtils;
import org.apache.linkis.server.Message;
import org.apache.linkis.server.MessageStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest(classes = {WebApplicationServer.class, Scan.class})
class ConfigurationTemplateRestfulApiTest {
    private Logger logger = LoggerFactory.getLogger(ConfigurationTemplateRestfulApi.class);

    @Autowired
    protected MockMvc mockMvc;

    private ConfigurationTemplateSaveRequest entity;

    @BeforeEach
    public void setup() {
        entity = new ConfigurationTemplateSaveRequest();
        entity.setId(1L);
        entity.setEngineLabelId("1");
        entity.setKey("123");
        entity.setDescription("123");
        entity.setName("123");
        entity.setDefaultValue("123");
        entity.setValidateType("123");
        entity.setValidateRange("123");
        entity.setEngineConnType("123");
        entity.setHidden(0);
        entity.setAdvanced(0);
        entity.setLevel(0);
        entity.setTreeName("123");
    }


    @Test
    void add() throws Exception {
        String url = "/basedata-manager/configuration-template/save";
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = objectMapper.writeValueAsString(entity);
        MvcResult mvcResult =
                mockMvc
                        .perform(
                                MockMvcRequestBuilders.post(url)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(msg))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        Message message =
                JsonUtils.jackson().readValue(mvcResult.getResponse().getContentAsString(), Message.class);
        Assertions.assertEquals(MessageStatus.SUCCESS(), message.getStatus());
        logger.info(String.valueOf(message));
    }

    @Test
    void delete() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        String url = "/basedata-manager/configuration-template/"+"5";
        MvcResult mvcResult =
                mockMvc
                        .perform(MockMvcRequestBuilders.delete(url).params(paramsMap))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        Message message =
                JsonUtils.jackson().readValue(mvcResult.getResponse().getContentAsString(), Message.class);
        Assertions.assertEquals(MessageStatus.SUCCESS(), message.getStatus());
        logger.info(String.valueOf(message));
    }

    @Test
    void getEngineList() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        String url = "/basedata-manager/configuration-template/engin-list";
        MvcResult mvcResult =
                mockMvc
                        .perform(MockMvcRequestBuilders.get(url).params(paramsMap))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        Message message =
                JsonUtils.jackson().readValue(mvcResult.getResponse().getContentAsString(), Message.class);
        Assertions.assertEquals(MessageStatus.SUCCESS(), message.getStatus());
        logger.info(String.valueOf(message));
    }

    @Test
    void getTemplateListByLabelId() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("engineLabelId","5");
        String url = "/basedata-manager/configuration-template/template-list-by-label";
        MvcResult mvcResult =
                mockMvc
                        .perform(MockMvcRequestBuilders.get(url).params(paramsMap))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        Message message =
                JsonUtils.jackson().readValue(mvcResult.getResponse().getContentAsString(), Message.class);
        Assertions.assertEquals(MessageStatus.SUCCESS(), message.getStatus());
        logger.info(String.valueOf(message));
    }

}