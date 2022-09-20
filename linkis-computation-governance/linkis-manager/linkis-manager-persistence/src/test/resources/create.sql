/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS linkis_cg_manager_label;
CREATE TABLE linkis_cg_manager_label (
  id int(20) NOT NULL AUTO_INCREMENT,
  label_key varchar(32)  NOT NULL,
  label_value varchar(255) NOT NULL,
  label_feature varchar(16) NOT NULL,
  label_value_size int(20) NOT NULL,
  update_time datetime NOT NULL,
  create_time datetime NOT NULL,
  PRIMARY KEY (id)
) ;

DROP TABLE IF EXISTS linkis_cg_ec_resource_info_record;
CREATE TABLE linkis_cg_ec_resource_info_record (
    id INT(20) NOT NULL AUTO_INCREMENT,
    label_value VARCHAR(255) NOT NULL,
    create_user VARCHAR(128) NOT NULL COMMENT 'ec create user',
    service_instance varchar(128)  DEFAULT NULL COMMENT 'ec instance info',
    ecm_instance varchar(128)  DEFAULT NULL COMMENT 'ecm instance info ',
    ticket_id VARCHAR(100) NOT NULL COMMENT 'ec ticket id',
    log_dir_suffix varchar(128)  DEFAULT NULL COMMENT 'log path',
    request_times INT(8) COMMENT 'resource request times',
    request_resource VARCHAR(1020) COMMENT 'request resource',
    used_times INT(8) COMMENT 'resource used times',
    used_resource VARCHAR(1020) COMMENT 'used resource',
    release_times INT(8) COMMENT 'resource released times',
    released_resource VARCHAR(1020)  COMMENT 'released resource',
    release_time datetime DEFAULT NULL COMMENT 'released time',
    used_time datetime DEFAULT NULL COMMENT 'used time',
    create_time datetime,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS linkis_cg_manager_label_value_relation;
CREATE TABLE linkis_cg_manager_label_value_relation (
  id int(20) NOT NULL AUTO_INCREMENT,
  label_value_key varchar(255) NOT NULL,
  label_value_content varchar(255) DEFAULT NULL,
  label_id int(20) DEFAULT NULL,
  update_time datetime,
  create_time datetime,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS linkis_cg_manager_linkis_resources;
CREATE TABLE linkis_cg_manager_linkis_resources (
  id int(11) NOT NULL AUTO_INCREMENT,
  max_resource varchar(1020) DEFAULT NULL,
  min_resource varchar(1020) DEFAULT NULL,
  used_resource varchar(1020) DEFAULT NULL,
  left_resource varchar(1020) DEFAULT NULL,
  expected_resource varchar(1020) DEFAULT NULL,
  locked_resource varchar(1020) DEFAULT NULL,
  resourceType varchar(255) DEFAULT NULL,
  ticketId varchar(255) DEFAULT NULL,
  update_time datetime ,
  create_time datetime ,
  updator varchar(255) DEFAULT NULL,
  creator varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS linkis_cg_manager_label_service_instance;
CREATE TABLE linkis_cg_manager_label_service_instance (
  id int(20) NOT NULL AUTO_INCREMENT,
  label_id int(20) DEFAULT NULL,
  service_instance varchar(128) DEFAULT NULL,
  update_time datetime ,
  create_time datetime ,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS linkis_cg_manager_label_user;
CREATE TABLE linkis_cg_manager_label_user (
  id int(20) NOT NULL AUTO_INCREMENT,
  username varchar(255) DEFAULT NULL,
  label_id int(20) DEFAULT NULL,
  update_time datetime ,
  create_time datetime ,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS linkis_cg_manager_engine_em;
CREATE TABLE linkis_cg_manager_engine_em (
  id int(20) NOT NULL AUTO_INCREMENT,
  engine_instance varchar(128) DEFAULT NULL,
  em_instance varchar(128) DEFAULT NULL,
  update_time datetime ,
  create_time datetime ,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `linkis_cg_manager_label_resource`;
CREATE TABLE `linkis_cg_manager_label_resource` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `label_id` int(20) DEFAULT NULL,
  `resource_id` int(20) DEFAULT NULL,
  `update_time` datetime,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `linkis_cg_manager_service_instance`;
CREATE TABLE `linkis_cg_manager_service_instance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `instance` varchar(128) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `owner` varchar(32) DEFAULT NULL,
  `mark` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updator` varchar(32) DEFAULT NULL,
  `creator` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `linkis_cg_manager_lock`;
CREATE TABLE `linkis_cg_manager_lock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lock_object` varchar(255) DEFAULT NULL,
  `time_out` longtext,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);