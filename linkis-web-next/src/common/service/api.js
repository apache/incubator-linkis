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


/**
 * Manipulate APIs(操作Api)
 */
import axios from 'axios';
import { Message } from 'iview';
import util from '@/common/util';
import cache from './apiCache';
import i18n from '../../common/i18n';

// what an array is used to store the cancel function and id for each request(什么一个数组用于存储每个请求的取消函数和标识)
const pending = [];
let cancelConfig = null;
const {CancelToken} = axios;
const removePending = (config) => {
    for (let p = 0; p < pending.length; p++) {
        const params = JSON.stringify(config.params);
        // Cancel if it exists(如果存在则执行取消操作)
        if (pending[p].u === `${config.url  }&${  config.method  }&${  params}`) {
            // pending[p].f();// perform cancellation(执行取消操作)
            pending.splice(p, 1);// remove record(移除记录)
        }
    }
};

const cutReq = (config) => {
    for (let p = 0; p < pending.length; p++) {
        const params = JSON.stringify(config.params);
        if (pending[p].u === `${config.url  }&${  config.method  }&${  params}`) {
            return true;
        }
    }
};

const instance = axios.create({
    baseURL: process.env.VUE_APP_MN_CONFIG_PREFIX || `${window.location.protocol}//${window.location.host}/api/rest_j/v1/`,
    timeout: 600000,
    withCredentials: true,
    headers: { 'Content-Type': 'application/json;charset=UTF-8' },
});

instance.interceptors.request.use((config) => {
    // Add internationalization parameters(增加国际化参数)
    config.headers['Content-language'] = localStorage.getItem('locale') || 'zh-CN';
    const flag = cutReq(config);
    // The second same request cannot be made when the last same request is not completed(当上一次相同请求未完成时，无法进行第二次相同请求)
    if (flag === true) {
        removePending(config);
        return config;
    } 
    const params = JSON.stringify(config.params);
    // It is used to remove when there is an error in the normal request(用于正常请求出现错误时移除)
    cancelConfig = config;
    config.cancelToken = new CancelToken((c) => {
        // Add identity and cancel functions(添加标识和取消函数)
        pending.push({
            u: `${config.url  }&${  config.method  }&${  params}`,
            f: c,
        });
    });
    return config;
  
}, (error) => {
    Promise.reject(error);
});

instance.interceptors.response.use((response) => {
    // Perform the cancellation operation after an ajax response is successful, and remove the completed request from the pending(在一个ajax响应成功后再执行取消操作，把已完成的请求从pending中移除)
    removePending(response.config);
    return response;
}, (error) => {
    // Judgment when an interface exception or timeout occurs(出现接口异常或者超时时的判断)
    if ((error.message && error.message.indexOf('timeout') >= 0) || (error.request && error.request.status !== 200)) {
        for (const p in pending) {
            if (pending[p].u === `${cancelConfig.url  }&${  cancelConfig.method  }&${  JSON.stringify(cancelConfig.params)}`) {
                pending.splice(p, 1);// remove record(移除记录)
            }
        }
        // The error information returned by the background is returned first, followed by the interface return(优先返回后台返回的错误信息，其次是接口返回)
        return error.response || error;
    } if (axios.Cancel) {
    // If it is in pengding state, a prompt will pop up!(如果是pengding状态，弹出提示！)
        return {
            // data: { message: 'Interface requesting! please wait……' }(data: { message: '接口请求中！请稍后……' })
        };
    } 
    return error;
  
});

const api = {
    instance,
    error: {
        '-1': function (res) {
            if (res.data && res.data.enableSSO && res.data.SSOURL) {
                return window.location.replace(res.data.SSOURL);
            }

            const isLoginPath = window.location.hash == '#/login';
            if (!isLoginPath) {
                window.location.hash = '#/login';
                throw new Error('您尚未登录，请先登录!');
            }
        },
    },
    constructionOfResponse: {
        codePath: 'status',
        successCode: '0',
        messagePath: 'message',
        resultPath: 'data'
    },
};

const getData = function (data) {
    const _arr = ['codePath', 'messagePath', 'resultPath'];
    const res = {};
    _arr.forEach((item) => {
        const pathArray = api.constructionOfResponse[item].split('.');
        let result = pathArray.length === 1 && pathArray[0] === '*' ? data : data[pathArray[0]];
        for (let j = 1; j < pathArray.length; j++) {
            result = result[pathArray[j]];
            if (!result) {
                if (j < pathArray.length - 1) {
                    window.console.error(`【FEX】ConstructionOfResponse配置错误：${item}拿到的值是undefined，请检查配置`);
                }
                break;
            }
        }
        res[item] = result;
    });
    return res;
};

const success = function (response) {
    if (util.isNull(api.constructionOfResponse.codePath) || util.isNull(api.constructionOfResponse.successCode) ||
        util.isNull(api.constructionOfResponse.messagePath) || util.isNull(api.constructionOfResponse.resultPath)) {
        window.console.error('【FEX】Api配置错误: 请调用setConstructionOfResponse来设置API的响应结构');
        return;
    }
    let data;
    if (response) {
        const linkis_errorMsgTip = (sessionStorage.getItem('linkis.errorMsgTip') || '').replace(/%s/g, response.config.url);
        if (util.isString(response.data)) {
            data = JSON.parse(response.data);
        } else if (util.isObject(response.data)) {
            data = response.data;
        } else {
            throw new Error(linkis_errorMsgTip || i18n.t('message.common.exceptionTips'));
        }
        const res = getData(data);
        const code = res.codePath;
        const message = res.messagePath;
        const result = res.resultPath;
        const errorMsgTip = result ? result.errorMsgTip : '';
        if (errorMsgTip) {
            sessionStorage.setItem('linkis.errorMsgTip', errorMsgTip);
        }
        if (code != api.constructionOfResponse.successCode) {
            if (api.error[code]) {
                api.error[code](response);
                throw new Error('');
            } else {
                throw new Error(message || linkis_errorMsgTip || i18n.t('message.common.exceptionTips'));
            }
        }
        if (result) {
            let len = 0;
            const hasBigData = Object.values(result).some(item => {
                if (Array.isArray(item)) {
                    len = item.length > len ? item.length : len;
                    return len > 200;
                }
            });
            if (hasBigData) {
                window.console.log(response.data, '潜在性能问题大数据量', len);
            }
        }

        return result || {};
    }
};

const fail = function (error) {
    let _message = '';
    const {response} = error;
    if (response && api.error[response.status]) {
        api.error[response.status].forEach((fn) => fn(response));
    } else {
        _message = i18n.t('message.common.exceptionTips');
        if (response && response.config) _message = (sessionStorage.getItem('linkis.errorMsgTip') || '').replace(/%s/g, response.config.url) || i18n.t('message.common.exceptionTips');
        if (response && response.data) {
            let data;
            if (util.isString(response.data)) {
                data = JSON.parse(response.data);
            } else if (util.isObject(response.data)) {
                data = response.data;
            }
            if (data) {
                const res = getData(data);
                _message = res.messagePath;
            }
        }
    }
    error.message = _message;
    throw error;
};

const param = function (url, data, option) {
    const method = 'post';
    if (util.isNull(url)) {
        return window.console.error('请传入URL');
    } if (!util.isNull(url) && util.isNull(data) && util.isNull(option)) {
        option = {
            method,
        };
    } else if (!util.isNull(url) && !util.isNull(data) && util.isNull(option)) {
        option = {
            method,
        };
        if (util.isString(data)) {
            option.method = data;
        } else if (util.isObject(data)) {
            option.data = data;
        }
    } else if (!util.isNull(url) && !util.isNull(data) && !util.isNull(option)) {
        if (!util.isObject(data)) {
            data = {};
        }
        if (util.isString(option)) {
            option = {
                method: option,
            };
        } else if (util.isObject(option)) {
            option.method = option.method || method;
        } else {
            option = {
                method,
            };
        }
        if (option.method == 'get' || option.method == 'delete' || option.method == 'head' || option.method == 'options') {
            option.params = data;
        }
        if (option.method == 'post' || option.method == 'put' || option.method == 'patch') {
            option.data = data;
        }
    }
    // cacheOptions interface data cache {time} When the time is 0, the data cached in the memory will not be cleaned up after the request(cacheOptions接口数据缓存 {time} time为0则请求之后缓存在内存里的数据不清理)
    if (option.cacheOptions) {
        option.adapter = cache(option.cacheOptions);
    }
    option.url = url;

    return instance.request(option);
};

const action = function (url, data, option) {
    return param(url, data, option)
        .then(success, fail)
        .then((response) => response)
        .catch((error) => {
            if (error.message) {
                const urlReg = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/g;
                const result = error.message.match(urlReg);
                result ? Message.error({
                    duration: 3,
                    render: h => {
                        const context = error.message.split(result[0]);
                        return h('span', [
                            context[0],
                            h('a', {
                                domProps: {
                                    href: result[0],
                                    target: '_blank'
                                },
                            }, result[0]),
                            context[1],
                        ]);
                    }
                }) : Message.error({
                    duration: 1.5,
                    content: error.message
                });
            }
            throw error;
        });
};

api.fetch = action;

api.option = function (option) {
    if (option.root) {
        instance.defaults.baseURL = option.root;
    }
    if (option.timeout && util.isNumber(option.timeout)) {
        instance.defaults.timeout = option.timeout;
    }
    if (option.config && util.isObject(option.config)) {
        Object.keys(option.config).forEach((key) => {
            instance.defaults[key] = option.config[key];
        });
    }
};

api.setError = function (option) {
    if (option && util.isObject(option)) {
        util.merge(api.error, option);
    }
};

api.setResponse = function (constructionOfResponse) {
    this.constructionOfResponse = constructionOfResponse;
};

export default api;
