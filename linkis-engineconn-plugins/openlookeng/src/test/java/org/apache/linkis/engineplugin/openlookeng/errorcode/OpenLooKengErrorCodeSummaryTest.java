/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.linkis.engineplugin.openlookeng.errorcode;

import org.junit.jupiter.api.Test;

import static org.apache.linkis.engineplugin.openlookeng.errorcode.OpenLooKengErrorCodeSummary.OPENLOOKENG_CLIENT_ERROR;
import static org.apache.linkis.engineplugin.openlookeng.errorcode.OpenLooKengErrorCodeSummary.OPENLOOKENG_STATUS_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenLooKengErrorCodeSummaryTest {
  @Test
  void testGetErrorCode() {
    assertEquals(26030, OPENLOOKENG_CLIENT_ERROR.getErrorCode());
    assertEquals(26031, OPENLOOKENG_STATUS_ERROR.getErrorCode());
  }

  @Test
  void testSetErrorCode() {
    OPENLOOKENG_CLIENT_ERROR.setErrorCode(1);
    assertEquals(1, OPENLOOKENG_CLIENT_ERROR.getErrorCode());
    OPENLOOKENG_CLIENT_ERROR.setErrorCode(26030);
    assertEquals(26030, OPENLOOKENG_CLIENT_ERROR.getErrorCode());

    OPENLOOKENG_STATUS_ERROR.setErrorCode(1);
    assertEquals(1, OPENLOOKENG_STATUS_ERROR.getErrorCode());
    OPENLOOKENG_STATUS_ERROR.setErrorCode(26031);
    assertEquals(26031, OPENLOOKENG_STATUS_ERROR.getErrorCode());
  }

  @Test
  void testGetErrorDesc() {
    assertEquals(
        "openlookeng client error(openlookeng客户端异常)", OPENLOOKENG_CLIENT_ERROR.getErrorDesc());
    assertEquals(
        "openlookeng status error,Statement is not finished(openlookeng状态异常, 查询语句未完成)",
        OPENLOOKENG_STATUS_ERROR.getErrorDesc());
  }

  @Test
  void testSetErrorDesc() {
    OPENLOOKENG_CLIENT_ERROR.setErrorDesc("test");
    assertEquals("test", OPENLOOKENG_CLIENT_ERROR.getErrorDesc());
    OPENLOOKENG_CLIENT_ERROR.setErrorDesc("openlookeng client error(openlookeng客户端异常)");
    assertEquals(
        "openlookeng client error(openlookeng客户端异常)", OPENLOOKENG_CLIENT_ERROR.getErrorDesc());

    OPENLOOKENG_STATUS_ERROR.setErrorDesc("test");
    assertEquals("test", OPENLOOKENG_STATUS_ERROR.getErrorDesc());
    OPENLOOKENG_STATUS_ERROR.setErrorDesc(
        "openlookeng status error,Statement is not finished(openlookeng状态异常, 查询语句未完成)");
    assertEquals(
        "openlookeng status error,Statement is not finished(openlookeng状态异常, 查询语句未完成)",
        OPENLOOKENG_STATUS_ERROR.getErrorDesc());
  }

  @Test
  void testGetComment() {
    assertEquals(
        "openlookeng client is abnormal due to some circumstances(openlookeng client由于某些情况异常)",
        OPENLOOKENG_CLIENT_ERROR.getComment());
    assertEquals(
        "The status of openlookeng is abnormal, and the query statement cannot be executed and ended(openlookeng状态出现异常，查询语句无法执行结束)",
        OPENLOOKENG_STATUS_ERROR.getComment());
  }

  @Test
  void testSetComment() {
    OPENLOOKENG_CLIENT_ERROR.setComment("test");
    assertEquals("test", OPENLOOKENG_CLIENT_ERROR.getComment());
    OPENLOOKENG_CLIENT_ERROR.setComment(
        "openlookeng client is abnormal due to some circumstances(openlookeng client由于某些情况异常)");
    assertEquals(
        "openlookeng client is abnormal due to some circumstances(openlookeng client由于某些情况异常)",
        OPENLOOKENG_CLIENT_ERROR.getComment());

    OPENLOOKENG_STATUS_ERROR.setComment("test");
    assertEquals("test", OPENLOOKENG_STATUS_ERROR.getComment());
    OPENLOOKENG_STATUS_ERROR.setComment(
        "The status of openlookeng is abnormal, and the query statement cannot be executed and ended(openlookeng状态出现异常，查询语句无法执行结束)");
    assertEquals(
        "The status of openlookeng is abnormal, and the query statement cannot be executed and ended(openlookeng状态出现异常，查询语句无法执行结束)",
        OPENLOOKENG_STATUS_ERROR.getComment());
  }

  @Test
  void testGetModule() {
    assertEquals("jdbcEngineConnExecutor", OPENLOOKENG_CLIENT_ERROR.getModule());
    assertEquals("jdbcEngineConnExecutor", OPENLOOKENG_STATUS_ERROR.getModule());
  }

  @Test
  void testSetModule() {
    OPENLOOKENG_CLIENT_ERROR.setModule("test");
    assertEquals("test", OPENLOOKENG_CLIENT_ERROR.getModule());
    OPENLOOKENG_CLIENT_ERROR.setModule("jdbcEngineConnExecutor");
    assertEquals("jdbcEngineConnExecutor", OPENLOOKENG_CLIENT_ERROR.getModule());

    OPENLOOKENG_STATUS_ERROR.setModule("test");
    assertEquals("test", OPENLOOKENG_STATUS_ERROR.getModule());
    OPENLOOKENG_STATUS_ERROR.setModule("jdbcEngineConnExecutor");
    assertEquals("jdbcEngineConnExecutor", OPENLOOKENG_STATUS_ERROR.getModule());
  }
}
