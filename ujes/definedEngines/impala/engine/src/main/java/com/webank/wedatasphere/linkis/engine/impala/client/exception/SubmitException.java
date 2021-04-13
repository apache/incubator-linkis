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
package com.webank.wedatasphere.linkis.engine.impala.client.exception;

/**
 *
 * Created by dingqihuang on Sep 20, 2019
 *
 */
public class SubmitException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5045965784519089392L;

	public SubmitException(String message) {
		super(message);
	}

	public SubmitException(Exception exception) {
		super(exception);
	}

	public static SubmitException of(ExceptionCode code) {
		return new SubmitException(code.getMessage());
	}
	
	public static SubmitException of(ExceptionCode code, String massage) {
		return new SubmitException(String.format("%s, %s", code.getMessage(), massage));
	}
	
	public static SubmitException of(Exception exception) {
		return new SubmitException(exception);
	}
}
