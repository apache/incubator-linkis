package com.webank.wedatasphere.linkis.cs.persistence.persistence.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wedatasphere.linkis.cs.common.entity.source.ContextID;
import com.webank.wedatasphere.linkis.cs.common.exception.CSErrorException;
import com.webank.wedatasphere.linkis.cs.common.listener.ContextIDListener;
import com.webank.wedatasphere.linkis.cs.persistence.dao.ContextIDMapper;
import com.webank.wedatasphere.linkis.cs.persistence.entity.ExtraFieldClass;
import com.webank.wedatasphere.linkis.cs.persistence.entity.PersistenceContextID;
import com.webank.wedatasphere.linkis.cs.persistence.persistence.ContextIDPersistence;
import com.webank.wedatasphere.linkis.cs.persistence.util.PersistenceUtils;
import com.webank.wedatasphere.linkis.server.BDPJettyServerHelper;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by patinousward on 2020/2/12.
 */
@Component
public class ContextIDPersistenceImpl implements ContextIDPersistence {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContextIDMapper contextIDMapper;

    private Class<PersistenceContextID> pClass = PersistenceContextID.class;

    private ObjectMapper json = BDPJettyServerHelper.jacksonJson();

    @Override
    public ContextID createContextID(ContextID contextID) throws CSErrorException {
        try {
            Pair<PersistenceContextID, ExtraFieldClass> pContextID = PersistenceUtils.transfer(contextID, pClass);
            pContextID.getFirst().setSource(json.writeValueAsString(pContextID.getSecond()));
            contextIDMapper.createContextID(pContextID.getFirst());
            contextID.setContextId(pContextID.getFirst().getContextId());
            return contextID;
        } catch (JsonProcessingException e) {
            logger.error("writeAsJson failed:", e);
            throw new CSErrorException(97000, e.getMessage());
        }
    }

    @Override
    public void deleteContextID(String contextId) {
        contextIDMapper.deleteContextID(contextId);
    }

    @Override
    public void updateContextID(ContextID contextID) throws CSErrorException {
        //contextId和source没有设置更新点
        Pair<PersistenceContextID, ExtraFieldClass> pContextID = PersistenceUtils.transfer(contextID, pClass);
        contextIDMapper.updateContextID(pContextID.getFirst());
    }

    @Override
    public ContextID getContextID(String contextId) throws CSErrorException {
        try {
            PersistenceContextID pContextID = contextIDMapper.getContextID(contextId);
            if(pContextID == null) return null;
            ExtraFieldClass extraFieldClass = json.readValue(pContextID.getSource(), ExtraFieldClass.class);
            ContextID contextID = PersistenceUtils.transfer(extraFieldClass, pContextID);
            return contextID;
        } catch (IOException e) {
            logger.error("readJson failed:", e);
            throw new CSErrorException(97000, e.getMessage());
        }
    }


}
