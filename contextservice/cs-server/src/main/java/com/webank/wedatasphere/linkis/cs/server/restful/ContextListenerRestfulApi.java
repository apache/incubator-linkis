package com.webank.wedatasphere.linkis.cs.server.restful;

import com.webank.wedatasphere.linkis.cs.common.entity.listener.CommonContextIDListenerDomain;
import com.webank.wedatasphere.linkis.cs.common.entity.listener.CommonContextKeyListenerDomain;
import com.webank.wedatasphere.linkis.cs.common.entity.listener.ContextIDListenerDomain;
import com.webank.wedatasphere.linkis.cs.common.entity.source.ContextID;
import com.webank.wedatasphere.linkis.cs.common.entity.source.ContextKey;
import com.webank.wedatasphere.linkis.cs.common.exception.CSErrorException;
import com.webank.wedatasphere.linkis.cs.server.enumeration.ServiceMethod;
import com.webank.wedatasphere.linkis.cs.server.enumeration.ServiceType;
import com.webank.wedatasphere.linkis.cs.server.scheduler.CsScheduler;
import com.webank.wedatasphere.linkis.cs.server.scheduler.HttpAnswerJob;
import com.webank.wedatasphere.linkis.server.Message;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by patinousward on 2020/2/18.
 */
@Component
@Path("/contextservice")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContextListenerRestfulApi implements CsRestfulParent {

    @Autowired
    private CsScheduler csScheduler;

    private ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Path("onBindIDListener")
    public Response onBindIDListener(@Context HttpServletRequest req, JsonNode jsonNode) throws InterruptedException, CSErrorException, IOException, ClassNotFoundException {
        String source = jsonNode.get("source").getTextValue();
        ContextID contextID = getContextIDFromJsonNode(jsonNode);
        ContextIDListenerDomain listener = new CommonContextIDListenerDomain();
        listener.setSource(source);
        HttpAnswerJob answerJob = submitRestJob(req, ServiceMethod.BIND, contextID, listener);
        return Message.messageToResponse(generateResponse(answerJob, ""));
    }

    @POST
    @Path("onBindKeyListener")
    public Response onBindKeyListener(@Context HttpServletRequest req, JsonNode jsonNode) throws InterruptedException, CSErrorException, IOException, ClassNotFoundException {
        String source = jsonNode.get("source").getTextValue();
        ContextID contextID = getContextIDFromJsonNode(jsonNode);
        ContextKey contextKey = getContextKeyFromJsonNode(jsonNode);
        CommonContextKeyListenerDomain listener = new CommonContextKeyListenerDomain();
        listener.setSource(source);
        HttpAnswerJob answerJob = submitRestJob(req, ServiceMethod.BIND, contextID, contextKey, listener);
        return Message.messageToResponse(generateResponse(answerJob, ""));
    }

    @POST
    @Path("heartbeat")
    public Response heartbeat(@Context HttpServletRequest req, JsonNode jsonNode) throws InterruptedException, IOException, CSErrorException {
        String source = jsonNode.get("source").getTextValue();
        HttpAnswerJob answerJob = submitRestJob(req, ServiceMethod.HEARTBEAT, source);
        return Message.messageToResponse(generateResponse(answerJob, "ContextKeyValueBean"));
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.CONTEXT_LISTENER;
    }

    @Override
    public CsScheduler getScheduler() {
        return this.csScheduler;
    }
}
