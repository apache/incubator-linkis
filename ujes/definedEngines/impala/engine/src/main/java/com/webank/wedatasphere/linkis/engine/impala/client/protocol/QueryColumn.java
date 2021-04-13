/*
 * Copyright 2019 WeBank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.wedatasphere.linkis.engine.impala.client.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * Created by dingqihuang on Sep 20, 2019
 *
 */
public class QueryColumn {
	private String label;
	private int index;

	/**
	 * @param label
	 * @param index
	 */
	public QueryColumn(String label, int index) {
		super();
		this.label = label;
		this.index = index;
	}

	public String getLabel() {
		return label;
	}

	public int getIndex() {
		return index;
	}
}
