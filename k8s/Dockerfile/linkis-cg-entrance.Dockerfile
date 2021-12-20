FROM linkis-base:1.2.0

WORKDIR /opt/linkis

COPY lib/linkis-commons/public-module /opt/linkis/public-module
COPY lib/linkis-computation-governance/linkis-cg-entrance/ /opt/linkis/linkis-cg-entrance/lib/
COPY sbin/k8s/linkis-cg-entrance.sh /opt/linkis/linkis-cg-entrance/bin/startup.sh

ENTRYPOINT ["linkis-cg-entrance/bin/startup.sh"]