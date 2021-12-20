FROM linkis-base:1.2.0

WORKDIR /opt/linkis

COPY lib/linkis-commons/public-module /opt/linkis/public-module
COPY lib/linkis-public-enhancements/linkis-ps-publicservice/ /opt/linkis/linkis-ps-publicservice/lib/
COPY sbin/k8s/linkis-ps-publicservice.sh /opt/linkis/linkis-ps-publicservice/bin/startup.sh

ENTRYPOINT ["linkis-ps-publicservice/bin/startup.sh"]