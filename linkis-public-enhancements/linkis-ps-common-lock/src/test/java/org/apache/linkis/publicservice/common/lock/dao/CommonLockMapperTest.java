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

package org.apache.linkis.publicservice.common.lock.dao;

import org.apache.linkis.publicservice.common.lock.entity.CommonLock;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommonLockMapperTest extends BaseDaoTest {

  @Autowired private CommonLockMapper commonLockMapper;

  @Test
  @DisplayName("getAll")
  public void getAllTest() {
    List<CommonLock> locks = commonLockMapper.getAll();
    Assertions.assertTrue(locks.size() == 1);
  }

  @Test
  @DisplayName("unlockTest")
  public void unlockTest() {
    String lockObject = "hadoop-warehouse";
    commonLockMapper.unlock(lockObject);

    List<CommonLock> locks = commonLockMapper.getAll();
    Assertions.assertTrue(locks.size() == 0);
  }

  @Test
  @DisplayName("lockTest")
  public void lockTest() {
    String lockObject = "hadoop-warehouse2";
    Long timeOut = 10000L;
    commonLockMapper.lock(lockObject, timeOut);
    List<CommonLock> locks = commonLockMapper.getAll();
    Assertions.assertTrue(locks.size() == 2);
  }
}
