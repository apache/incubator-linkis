#!/bin/bash

export SERVER_NAME=linkis-mg-gateway

export SERVER_CONF_PATH=/opt/linkis/linkis-mg-gateway/conf
export SERVER_LIB=/opt/linkis/linkis-mg-gateway/lib
export LINKIS_LOG_DIR=/opt/linkis/linkis-mg-gateway/logs

if [ ! -w "$LINKIS_LOG_DIR" ] ; then
  mkdir -p "$LINKIS_LOG_DIR"
fi

export SERVER_JAVA_OPTS=" -DserviceName=$SERVER_NAME -Xmx2G -XX:+UseG1GC -Xloggc:$LINKIS_LOG_DIR/${SERVER_NAME}-gc.log $DEBUG_CMD"

export SERVER_CLASS=org.apache.linkis.gateway.springcloud.LinkisGatewayApplication

export SERVER_CLASS_PATH=$SERVER_CONF_PATH:$SERVER_LIB/*

exec java $SERVER_JAVA_OPTS -cp $SERVER_CLASS_PATH $SERVER_CLASS