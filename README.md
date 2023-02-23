<h2 align="center">
  Apache Linkis
</h2>

<p align="center">
  <strong> Linkis builds a computation middleware layer to facilitate connection, 
    governance and orchestration between the upper applications and the underlying data engines. </strong>
</p>
<p align="center">
  <a href="https://linkis.apache.org/">https://linkis.apache.org/</a>
</p>

<p align="center">
  <a href="https://linkis.apache.org/docs/latest/introduction/" >
    <img src="https://img.shields.io/badge/document-English-blue.svg" alt="EN docs" />
  </a>
  <a href="https://linkis.apache.org/zh-CN/docs/latest/introduction/">
    <img src="https://img.shields.io/badge/文档-简体中文-blue.svg" alt="简体中文文档" />
  </a>
</p>

<p align="center">
    <a target="_blank" href="https://search.maven.org/search?q=g:org.apache.linkis%20AND%20a:linkis">
        <img src="https://img.shields.io/maven-central/v/org.apache.linkis/linkis.svg?label=maven%20central" />
    </a>
    <a target="_blank" href="https://github.com/apache/linkis/blob/master/LICENSE">
        <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license" />
    </a>
    <a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img src="https://img.shields.io/badge/JDK-8-green.svg" />
    </a>
    <a target="_blank" href="https://github.com/apache/linkis/actions">
        <img src="https://github.com/apache/linkis/actions/workflows//build-backend.yml/badge.svg" />
    </a>

   <a target="_blank" href='https://github.com/apache/linkis'>
        <img src="https://img.shields.io/github/forks/apache/linkis.svg" alt="github forks"/>
   </a>
   <a target="_blank" href='https://github.com/apache/linkis'>
        <img src="https://img.shields.io/github/stars/apache/linkis.svg" alt="github stars"/>
   </a>
   <a target="_blank" href='https://github.com/apache/linkis'>
        <img src="https://img.shields.io/github/contributors/apache/linkis.svg" alt="github contributors"/>
   </a>
  <a target="_blank" href="https://badges.toozhao.com/stats/01G7TRNN1PH9PMSCYWDF3EK4QT">
       <img src="https://badges.toozhao.com/badges/01G7TRNN1PH9PMSCYWDF3EK4QT/green.svg" />
  </a>
  
</p>
<br/>

---
[English](README.md) | [中文](README_CN.md)

# Introduction

 Linkis builds a layer of computation middleware between upper applications and underlying engines. By using standard interfaces such as REST/WS/JDBC provided by Linkis, the upper applications can easily access the underlying engines such as MySQL/Spark/Hive/Presto/Flink, etc., and achieve the intercommunication of user resources like unified variables, scripts, UDFs, functions and resource files at the same time.

As a computation middleware, Linkis provides powerful connectivity, reuse, orchestration, expansion, and governance capabilities. By decoupling the application layer and the engine layer, it simplifies the complex network call relationship, and thus reduces the overall complexity and saves the development and maintenance costs as well.

Since the first release of Linkis in 2019, it has accumulated more than **700** trial companies and **1000+** sandbox trial users, which involving diverse industries, from finance, banking, tele-communication, to manufactory, internet companies and so on. Lots of companies have already used Linkis as a unified entrance for the underlying computation and storage engines of the big data platform.

![linkis-intro-01](https://user-images.githubusercontent.com/7869972/148767375-aeb11b93-16ca-46d7-a30e-92fbefe2bd5e.png)

![linkis-intro-03](https://user-images.githubusercontent.com/7869972/148767380-c34f44b2-9320-4633-9ec8-662701f41d15.png)

# Features

- **Support for diverse underlying computation storage engines**：  
  Currently supported computation/storage engines: Spark, Hive, Python, Presto, ElasticSearch, MLSQL, TiSpark,Trino, SeaTunnel, JAVA , Shell, etc;      
  Computation/storage engines to be supported: Flink(Supported in version >=1.0.2), Impala, etc;      
  Supported scripting languages: SparkSQL, HiveQL, Python, Shell, Pyspark, R, Scala and JDBC, etc.
- **Powerful task/request governance capabilities**： With services such as Orchestrator, Label Manager and customized Spring Cloud Gateway, Linkis is able to provide multi-level labels based, cross-cluster/cross-IDC fine-grained routing, load balance, multi-tenancy, traffic control, resource control, and orchestration strategies like dual-active, active-standby, etc.
- **Support full stack computation/storage engine**： As a computation middleware, it will receive, execute and manage tasks and requests for various computation storage engines, including batch tasks, interactive query tasks, real-time streaming tasks and storage tasks;
- **Resource management capabilities**：  ResourceManager is not only capable of managing resources for Yarn and Linkis EngineManger as in Linkis 0.X, but also able to provide label-based multi-level resource allocation and recycling, allowing itself to have powerful resource management capabilities across multiple Yarn clusters and multiple computation resource types.
- **Unified Context Service**： Generate Context ID for each **task**/request,  associate and manage user and system resource files (JAR, ZIP, Properties, etc.), result set, parameter variable, function, etc., across user, system, and computing engine. Set in one place, automatic reference everywhere.
- **Unified materials**： System and user-level unified material management, which can be shared and transferred across users and systems.
- **Unified Data Source Manage**： Provides functions such as adding, deleting, checking, and modifying data sources of hive, es, mysql, and kafka types, version control, and connection testing.
- **Unified MetaData Query**： Provides database, table, and partition queries for hive, es, mysql, and kafka metadata.

# Supported Engine Types

| **Engine** | **Supported Version** | **Linkis Version Requirements**| **Included in Release Package By Default** | **Description** |
|:---- |:---- |:---- |:---- |:---- |
|Flink |1.12.2|\>=dev-0.12.0, PR #703 not merged yet.|>=1.0.2|	Flink EngineConn. Supports FlinkSQL code, and also supports Flink Jar to Linkis Manager to start a new Yarn application.|
|Impala|\>=3.2.0, CDH >=6.3.0"|\>=dev-0.12.0, PR #703 not merged yet.|ongoing|Impala EngineConn. Supports Impala SQL.|
|Presto|\>= 0.180|\>=0.11.0|ongoing|Presto EngineConn. Supports Presto SQL.|
|ElasticSearch|\>=6.0|\>=0.11.0|ongoing|ElasticSearch EngineConn. Supports SQL and DSL code.|
|Shell|Bash >=2.0|\>=0.9.3|\>=1.0.0_rc1|Shell EngineConn. Supports shell code.|
|MLSQL|\>=1.1.0|\>=0.9.1|ongoing|MLSQL EngineConn. Supports MLSQL code.|
|JDBC|MySQL >=5.0, Hive >=1.2.1|\>=0.9.0|\>=1.0.0_rc1|JDBC EngineConn. Supports MySQL and HiveQL code.|
|Spark|Apache 2.0.0~2.4.7, CDH >=5.4.0|\>=0.5.0|\>=1.0.0_rc1|Spark EngineConn. Supports SQL, Scala, Pyspark and R code.|
|Hive|Apache >=1.0.0, CDH >=5.4.0|\>=0.5.0|\>=1.0.0_rc1|Hive EngineConn. Supports HiveQL code.|
|Hadoop|Apache >=2.6.0, CDH >=5.4.0|\>=0.5.0|ongoing|Hadoop EngineConn. Supports Hadoop MR/YARN application.|
|Python|\>=2.6|\>=0.5.0|\>=1.0.0_rc1|Python EngineConn. Supports python code.|
|TiSpark|1.1|\>=0.5.0|ongoing|TiSpark EngineConn. Support querying TiDB data by SparkSQL.|
|Trino | 371 | >=1.3.1 | 否 |   Trino EngineConn， Support Trino SQL code |
|Seatunnel | 2.1.2 | >=1.3.1 | 否 | Seatunnel EngineConn， Support Seatunnel SQL code |

# Download

Please go to the [Linkis Releases Page](https://linkis.apache.org/download/main) to download a compiled distribution or a source code package of Linkis.

# Compile and Deploy

> For more detailed guidance see:
>- [[Backend Compile]](https://linkis.apache.org/docs/latest/development/build)
>- [[Management Console Build]](https://linkis.apache.org/docs/latest/development/build-console)

```shell

Note: If you want use `-Dlinkis.build.web=true` to build  linkis-web image, you need to compile linkis-web first.

## compile backend
### Mac OS/Linux

# 1. When compiling for the first time, execute the following command first
./mvnw -N install

# 2. make the linkis distribution package
# - Option 1: make the linkis distribution package only
./mvnw clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true

# - Option 2: make the linkis distribution package and docker image
#   - Option 2.1: image without mysql jdbc jars
./mvnw clean install -Pdocker -Dmaven.javadoc.skip=true -Dmaven.test.skip=true
#   - Option 2.2: image with mysql jdbc jars
./mvnw clean install -Pdocker -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dlinkis.build.with.jdbc=true

# - Option 3: linkis distribution package and docker image (included web)
./mvnw clean install -Pdocker -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dlinkis.build.web=true

# - Option 4: linkis distribution package and docker image (included web and ldh (hadoop all in one for test))
./mvnw clean install -Pdocker -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dlinkis.build.web=true -Dlinkis.build.ldh=true -Dlinkis.build.with.jdbc=true

### Windows
mvnw.cmd -N install
mvnw.cmd clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true

## compile web
cd linkis/linkis-web
npm install
npm run build
```

### Bundled with MySQL JDBC Driver
Due to the MySQL licensing restrictions, the MySQL Java Database Connectivity (JDBC) driver is not bundled with the 
official released linkis image by default. However, at current stage, linkis still relies on this library to work properly.
To solve this problem, we provide a script which can help to creating a custom image with mysql jdbc from the official 
linkis image by yourself, the image created by this tool will be tagged as `linkis:with-jdbc` by default.

```shell
$> LINKIS_IMAGE=linkis:1.3.1 
$> ./linkis-dist/docker/scripts/make-linikis-image-with-mysql-jdbc.sh
```


Please refer to [Quick Deployment](https://linkis.apache.org/docs/latest/deployment/deploy-quick/) to do the deployment.

# Examples and Guidance
- [User Manual](https://linkis.apache.org/docs/latest/user-guide/how-to-use)
- [Engine Usage Documents](https://linkis.apache.org/docs/latest/engine-usage/overview) 
- [API Documents](https://linkis.apache.org/docs/latest/api/overview)

# Documentation & Vedio

- The documentation of linkis is in [Linkis-Website Git Repository](https://github.com/apache/linkis-website)
- Meetup videos on [Bilibili](https://space.bilibili.com/598542776?from=search&seid=14344213924133040656)

# Architecture
Linkis services could be divided into three categories: computation governance services, public enhancement services and microservice governance services
- The computation governance services, support the 3 major stages of processing a task/request: submission -> preparation -> execution
- The public enhancement services, including the material library service, context service, and data source service
- The microservice governance services, including Spring Cloud Gateway, Eureka and Open Feign

Below is the Linkis architecture diagram. You can find more detailed architecture docs in [Linkis-Doc/Architecture](https://linkis.apache.org/docs/latest/architecture/overview).
![architecture](https://user-images.githubusercontent.com/7869972/148767383-f87e84ba-5baa-4125-8b6e-d0aa4f7d3a66.png)

# Contributing

Contributions are always welcomed, we need more contributors to build Linkis together. either code, or doc, or other supports that could help the community.  
For code and documentation contributions, please follow the [contribution guide](https://linkis.apache.org/community/how-to-contribute).

# Contact Us


- Any questions or suggestions please kindly submit an [issue](https://github.com/apache/linkis/issues).  
- By mail [dev@linkis.apache.org](mailto:dev@linkis.apache.org)
- You can scan the QR code below to join our WeChat group to get more immediate response

![wechatgroup](https://linkis.apache.org/Images/wedatasphere_contact_01.png)


# Who is Using Linkis

We opened an issue [[Who is Using Linkis]](https://github.com/apache/linkis/issues/23) for users to feedback and record who is using Linkis.  
Since the first release of Linkis in 2019, it has accumulated more than **700** trial companies and **1000+** sandbox trial users, which involving diverse industries, from finance, banking, tele-communication, to manufactory, internet companies and so on.
