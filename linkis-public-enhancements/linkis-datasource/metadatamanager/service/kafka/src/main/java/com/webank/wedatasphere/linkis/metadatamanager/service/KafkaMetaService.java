package com.webank.wedatasphere.linkis.metadatamanager.service;

import com.webank.wedatasphere.linkis.bml.client.BmlClient;
import com.webank.wedatasphere.linkis.bml.client.BmlClientFactory;
import com.webank.wedatasphere.linkis.bml.protocol.BmlDownloadResponse;
import com.webank.wedatasphere.linkis.common.conf.CommonVars;
import com.webank.wedatasphere.linkis.metadatamanager.common.exception.MetaRuntimeException;
import com.webank.wedatasphere.linkis.metadatamanager.common.service.AbstractMetaService;
import com.webank.wedatasphere.linkis.metadatamanager.common.service.MetadataConnection;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.TopicPartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class KafkaMetaService extends AbstractMetaService<KafkaConnection> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaMetaService.class);
    private static final CommonVars<String> TMP_FILE_STORE_LOCATION =
            CommonVars.apply("wds.linkis.server.mdm.service.temp.location", "classpath:/tmp");
    private BmlClient client;

    @PostConstruct
    public void buildClient(){
        client = BmlClientFactory.createBmlClient();
    }

    @Override
    public MetadataConnection<KafkaConnection> getConnection(String operator, Map<String, Object> params) throws Exception {
        Resource resource = new PathMatchingResourcePatternResolver().getResource(TMP_FILE_STORE_LOCATION.getValue());
        String brokers = String.valueOf(params.getOrDefault(KafkaParamsMapper.PARAM_KAFKA_BROKERS.getValue(), ""));
        String principle = String.valueOf(params.getOrDefault(KafkaParamsMapper.PARAM_KAFKA_PRINCIPLE.getValue(), ""));
        KafkaConnection conn;

        if(StringUtils.isNotBlank(principle)){
            LOG.info("Try to connect Kafka MetaStore in kerberos with principle:[" + principle +"]");
            String keytabResourceId = String.valueOf(params.getOrDefault(KafkaParamsMapper.PARAM_KAFKA_KEYTAB.getValue(), ""));
            if(StringUtils.isNotBlank(keytabResourceId)){
                LOG.info("Start to download resource id:[" + keytabResourceId +"]");
                String keytabFilePath = resource.getFile().getAbsolutePath() + "/" + UUID.randomUUID().toString().replace("-", "");
                if(!downloadResource(keytabResourceId, operator, keytabFilePath)){
                    throw new MetaRuntimeException("Fail to download resource i:[" + keytabResourceId +"]");
                }
                conn = new KafkaConnection(brokers, principle, keytabFilePath);
            }else{
                throw new MetaRuntimeException("Cannot find the keytab file in connect parameters");
            }
        }else{
            conn = new KafkaConnection(brokers);
        }

        return new MetadataConnection<>(conn, true);
    }


    @Override
    public List<String> queryDatabases(KafkaConnection connection){
        try {
            return connection.getClient().listTopics().names().get().stream().collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Fail to get Kafka topics(获取topic列表失败)", e);
        }
    }

    @Override
    public List<String> queryTables(KafkaConnection connection, String topic){
        try {
            DescribeTopicsResult describeTopicsResult = connection.getClient().describeTopics(Arrays.asList(topic));
            Map<String, TopicDescription> topicPartitions = describeTopicsResult.all().get();
            return topicPartitions.get(topic).partitions().stream()
                    .map(TopicPartitionInfo::partition).map(Object::toString).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Fail to get Kafka partitions(获取Kafka partitions失败)", e);
        }
    }


    private boolean downloadResource(String resourceId, String user, String absolutePath) throws IOException {
        BmlDownloadResponse downloadResponse = client.downloadResource(user, resourceId, absolutePath);
        if(downloadResponse.isSuccess()){
            IOUtils.copy(downloadResponse.inputStream(), new FileOutputStream(absolutePath));
            return true;
        }
        return false;
    }
}
