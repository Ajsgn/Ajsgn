/*
 * Copyright (c) 2019, Ajsgn 杨光 (Ajsgn@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ajsgn.common.util.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.annotation.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>http请求工具类</p>
 * @ClassName: HttpUtil
 * @Description: http请求工具类
 * @author Ajsgn@foxmail.com
 * @date 2019年1月8日 下午6:44:10
 */
@ThreadSafe
public final class HttpUtil {
	
	/**
	 * 日志记录
	 */
	private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);
	/**
	 * 默认连接超时时间
	 */
	private static final int DEFAULT_TIMEOUT_MILLISECOND = 65000;
	/**
	 * 线程请求cookies
	 */
	private static ThreadLocal<Map<String, String>> cookiesThreadLocal = new ThreadLocal<Map<String, String>>();
	/**
	 * 线程请求headers
	 */
	private static ThreadLocal<Map<String, String>> headersThreadLocal = new ThreadLocal<Map<String, String>>();
	
	/**
	 * <p>使用get方法请求<p>
	 * <p>超时时长65秒<p>
	 * @Title: get
	 * @Description: 使用get方法请求
	 * @param url 目标url链接
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午6:47:40
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String get(String url) throws IOException {
		return getMethod(url, DEFAULT_TIMEOUT_MILLISECOND);
	}

	/**
	 * <p>使用get方法请求<p>
	 * @Title: get
	 * @Description: 使用get方法请求
	 * @param url 目标url链接
	 * @param timeoutMillisecond 链接超时时间，单位毫秒
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午6:48:52
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String get(String url, int timeoutMillisecond) throws IOException {
		return getMethod(url, timeoutMillisecond);
	}
	
	/**
	 * <p>使用post方法请求<p>
	 * <p>contentType=application/x-www-form-urlencoded;charset=utf-8<p>
	 * <p>charset=utf-8<p>
	 * <p>超时时长65秒<p>
	 * @Title: post
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param params 请求参数
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午6:49:39
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String post(String url, Map<String, String> params) throws IOException {
		String result = post(url, "application/x-www-form-urlencoded;charset=utf-8", "utf-8", params, DEFAULT_TIMEOUT_MILLISECOND);
		return result;
	}

	/**
	 * <p>使用post方法请求<p>
	 * <p>contentType=application/x-www-form-urlencoded;charset=utf-8<p>
	 * <p>charset=utf-8<p>
	 * @Title: post
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param params 请求参数
	 * @param timeoutMillisecond 请求超时时间，单位毫秒
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午7:01:31
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String post(String url, Map<String, String> params, int timeoutMillisecond) throws IOException {
		String result = postMethod(url, "application/x-www-form-urlencoded;charset=utf-8", "utf-8", params, timeoutMillisecond);
		return result;
	}

	/**
	 * <p>使用post方法请求<p>
	 * <p>超时时长65秒<p>
	 * @Title: post
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param contentType 请求类型
	 * @param charset 请求字符集
	 * @param params 请求参数
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午6:50:42
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String post(String url, String contentType, String charset, Map<String, String> params) throws IOException {
		String result = postMethod(url, contentType, charset, params, DEFAULT_TIMEOUT_MILLISECOND);
		return result;
	}

	/**
	 * <p>使用post方法请求<p>
	 * @Title: post
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param contentType 请求类型
	 * @param charset 请求字符集
	 * @param params 请求参数
	 * @param timeoutMillisecond 请求超时时间，单位毫秒
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午6:51:30
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String post(String url, String contentType, String charset, Map<String, String> params, int timeoutMillisecond) throws IOException {
		String result = postMethod(url, contentType, charset, params, timeoutMillisecond);
		return result;
	}
	
	/**
	 * <p>使用post方法请求<p>
	 * <p>通过流的方式传输请求参数<p>
	 * <p>contentType=application/text<p>
	 * <p>charset=utf-8<p>
	 * <p>超时时长65秒<p>
	 * @Title: postText
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param content 请求体
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午6:56:11
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String postText(String url, String content) throws IOException {
		String result = postText(url, "utf-8", content);
		return result;
	}
	
	/**
	 * <p>使用post方法请求<p>
	 * <p>通过流的方式传输请求参数<p>
	 * <p>contentType=application/text<p>
	 * <p>charset=utf-8<p>
	 * @Title: postText
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param content 请求体
	 * @param timeoutMillisecond 请求超时时间，单位毫秒
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午7:07:42
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String postText(String url, String content, int timeoutMillisecond) throws IOException {
		String result = postText(url, "utf-8", content, timeoutMillisecond);
		return result;
	}

	/**
	 * <p>使用post方法请求<p>
	 * <p>通过流的方式传输请求参数<p>
	 * <p>contentType=application/text<p>
	 * <p>超时时长65秒<p>
	 * @Title: postText
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param charset 字符集
	 * @param content 请求体
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午6:59:09
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String postText(String url, String charset, String content) throws IOException {
		String result = postText(url, charset, content, DEFAULT_TIMEOUT_MILLISECOND);
		return result;
	}

	/**
	 * <p>使用post方法请求<p>
	 * <p>通过流的方式传输请求参数<p>
	 * <p>contentType=application/text<p>
	 * @Title: postText
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param charset 字符集
	 * @param content 请求体
	 * @param timeoutMillisecond 请求超时时间，单位毫秒
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午7:13:52
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String postText(String url, String charset, String content, int timeoutMillisecond) throws IOException {
		String result = postContent(url, "application/text", charset, content, timeoutMillisecond);
		return result;
	}

	/**
	 * <p>使用post方法请求<p>
	 * <p>通过流的方式传输请求参数<p>
	 * <p>contentType=application/json<p>
	 * <p>超时时长65秒<p>
	 * @Title: postJson
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param content 请求体
	 * @date 2019年1月8日 下午7:15:56
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String postJson(String url, String content) throws IOException {
		String result = postJson(url, "utf-8", content);
		return result;
	}
	
	/**
	 * <p>使用post方法请求<p>
	 * <p>通过流的方式传输请求参数<p>
	 * <p>contentType=application/json<p>
	 * @Title: postJson
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param content 请求体
	 * @param timeoutMillisecond 请求超时时间，单位毫秒
	 * @date 2019年1月8日 下午7:16:41
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String postJson(String url, String content, int timeoutMillisecond) throws IOException {
		String result = postJson(url, "utf-8", content);
		return result;
	}

	/**
	 * <p>使用post方法请求<p>
	 * <p>通过流的方式传输请求参数<p>
	 * <p>contentType=application/json<p>
	 * @Title: postJson
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param charset 字符集
	 * @param content 请求体
	 * @date 2019年1月8日 下午7:17:50
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String postJson(String url, String charset, String content) throws IOException {
		String result = postJson(url, charset, content, DEFAULT_TIMEOUT_MILLISECOND);
		return result;
	}

	/**
	 * <p>使用post方法请求<p>
	 * <p>通过流的方式传输请求参数<p>
	 * @Title: postJson
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param charset 字符集
	 * @param content 请求体
	 * @param timeoutMillisecond 请求超时时间，单位毫秒
	 * @date 2019年1月8日 下午7:19:12
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String postJson(String url, String charset, String content, int timeoutMillisecond) throws IOException {
		String result = postContent(url, "application/json", charset, content,timeoutMillisecond);
		return result;
	}

	/**
	 * <p>使用post方法请求<p>
	 * <p>通过流的方式传输请求参数<p>
	 * @Title: postContent
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param contentType 请求类型
	 * @param charset 字符集
	 * @param content 请求体
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午7:22:54
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String postContent(String url, String contentType, String charset, String content) throws IOException {
		String result = postContent(url, contentType, charset, content, DEFAULT_TIMEOUT_MILLISECOND);
		return result;
	}

	/**
	 * <p>使用post方法请求<p>
	 * <p>通过流的方式传输请求参数<p>
	 * @Title: postContent
	 * @Description: 使用post方法请求
	 * @param url 目标url链接
	 * @param contentType 请求类型
	 * @param charset 字符集
	 * @param content 请求体
	 * @param timeoutMillisecond 请求超时时间，单位毫秒
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午7:23:49
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
	public static String postContent(String url, String contentType, String charset, String content, int timeoutMillisecond) throws IOException {
		String result = postContentMethod(url, contentType, charset, content, timeoutMillisecond);
		return result;
	}

	/**
	 * <p>get方式请求对目标发起请求<p>
	 * @Title: getMethod
	 * @Description: get方式请求对目标发起请求
	 * @param url 目标url链接
	 * @param timeoutMillisecond 请求超时时间，单位毫秒
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午7:24:26
	 * @return String 响应内容体
	 * @throws IOException 请求异常
	 */
    private static String getMethod(String url, int timeoutMillisecond) throws IOException {
    	LOG.debug("【GET 请求】 : {}, timeoutMillisecond： {}", url, timeoutMillisecond);
    	String result = "";
    	try {
    		Protocol https = new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443);
    		Protocol.registerProtocol("https", https);
    		GetMethod get = new GetMethod(url);
    		HttpClient client = new HttpClient();
    		setLocalVariable(get);
    		client.getHttpConnectionManager().getParams().setConnectionTimeout(timeoutMillisecond);
    		client.executeMethod(get);
    		result = get.getResponseBodyAsString();
    		Protocol.unregisterProtocol("https");
    		LOG.debug("【GET 响应】 {} : 响应结果: {}", url, result);
    	} finally {
    		threadLocalClear();
    	}
        return result;
    }
    
    /**
     * <p>post方式请求对目标发起请求<p>
     * @Title: postMethod
     * @Description: post方式请求对目标发起请求
     * @param url 目标url链接
     * @param contentType 请求类型
     * @param charset 字符集
     * @param params 请求参数体
     * @param timeoutMillisecond 请求超时时间，单位毫秒
     * @author Ajsgn@foxmail.com
     * @date 2019年1月8日 下午7:25:25
	 * @return String 响应内容体
	 * @throws IOException 请求异常
     */
    private static String postMethod(String url, String contentType, String charset, Map<String, String> params, int timeoutMillisecond) throws IOException {
    	LOG.debug("【POST 请求】 : {}, 参数列表： {}, contentType: {}, charset: {}, timeoutMillisecond: {}", url, String.valueOf(params), contentType, charset, timeoutMillisecond);
		String result = "";
		try {
			Protocol https = new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", https);
			PostMethod post = new PostMethod(url);
			HttpClient client = new HttpClient();
    		setLocalVariable(post);
			HttpClientParams clientParams = new HttpClientParams();
			client.setParams(clientParams);
			post.setRequestHeader("Content-Type", buildContentType(contentType, charset));
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					list.add(new NameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			post.setRequestBody(list.toArray(new NameValuePair[]{}));
			client.executeMethod(post);
			result = post.getResponseBodyAsString();
			Protocol.unregisterProtocol("https");
			LOG.debug("【POST 响应】: {}, 参数列表： {}, 响应结果: {}", url, String.valueOf(params), result);
		} finally {
    		threadLocalClear();
    	}
        return result;
	}
	
    /**
     * <p>post方式请求对目标发起请求<p>
     * @Title: postContentMethod
     * @Description: post方式请求对目标发起请求
     * @param url 目标url链接
     * @param contentType 请求类型
     * @param charset 字符集
     * @param content 请求体
     * @param timeoutMillisecond 请求超时时间，单位毫秒
     * @author Ajsgn@foxmail.com
     * @date 2019年1月8日 下午7:26:22
	 * @return String 响应内容体
	 * @throws IOException 请求异常
     */
	private static String postContentMethod(String url, String contentType, String charset, String content, int timeoutMillisecond) throws IOException {
    	LOG.debug("【POST 请求】 : {}, 参数列表： {}, contentType: {}, charset: {}, timeoutMillisecond: {}", url, String.valueOf(content), contentType, charset, timeoutMillisecond);
		String result = "";
		try {
			Protocol https = new Protocol("https", new HTTPSSecureProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", https);
			PostMethod post = new PostMethod(url);
			HttpClient client = new HttpClient();
    		setLocalVariable(post);
			HttpClientParams clientParams = new HttpClientParams();
			client.setParams(clientParams);
			post.setRequestHeader("Content-Type", buildContentType(contentType, charset));
			RequestEntity requestEntity = new StringRequestEntity(content, contentType, charset);
			post.setRequestEntity(requestEntity);
			client.executeMethod(post);
			result = post.getResponseBodyAsString();
			Protocol.unregisterProtocol("https");
			LOG.debug("【POST 响应】: {}, 参数列表： {}, 响应结果: {}", url, String.valueOf(content), result);
		} finally {
    		threadLocalClear();
		}
		
		return result;
	}
	
	/**
	 * <p>给请求方法设置请求参数<p>
	 * @Title: setLocalVariable
	 * @Description: 给请求方法设置请求参数
	 * @param httpMethod 请求方法
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午7:27:35
	 */
	private static void setLocalVariable(HttpMethod httpMethod) {
		// cookie set
		Map<String, String> cookieVars = cookiesThreadLocal.get();
		if(null != cookieVars && false == cookieVars.isEmpty()) {
			StringBuilder cookiesStr = new StringBuilder();
			for(Map.Entry<String, String> cookie : cookieVars.entrySet()) {
				cookiesStr.append(cookie.getKey()).append("=").append(cookie.getValue()).append("; ");
			}
			Header header = new Header("cookie", String.valueOf(cookiesStr));
			httpMethod.setRequestHeader(header);
		}
		
		// header set
		Map<String, String> headerVars = headersThreadLocal.get();
		if(null != headerVars && false == headerVars.isEmpty()) {
			for(Map.Entry<String, String> headerVar : headerVars.entrySet()) {
				Header header = new Header(headerVar.getKey(), headerVar.getValue());
				httpMethod.setRequestHeader(header);
			}
		}
	}
	
	/**
	 * <p>给请求添加cookie<p>
	 * @Title: addCookie
	 * @Description: 给请求添加cookie
	 * @param name cookie name
	 * @param value cookie value
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午7:28:31
	 */
	public static void addCookie(String name, String value) {
		Map<String, String> cookies = cookiesThreadLocal.get();
		if(null == cookies) {
			cookies = new HashMap<String, String>();
			cookiesThreadLocal.set(cookies);
		}
		cookies.put(name, value);
	}
	
	/**
	 * <p>给请求添加请求头信息<p>
	 * @Title: addRequestHeader
	 * @Description: 给请求添加请求头信息
	 * @param name header name
	 * @param value header value
	 * @author Ajsgn@foxmail.com
	 * @date 2019年1月8日 下午7:29:11
	 */
	public static void addRequestHeader(String name, String value) {
		Map<String, String> headers = headersThreadLocal.get();
		if(null == headers) {
			headers = new HashMap<String, String>();
			headersThreadLocal.set(headers);
		}
		headers.put(name, value);
	}
	
	/**
	 * 清空当前线程中ThreadLocal中的变量
	 */
	private static void threadLocalClear() {
		headersThreadLocal.remove();
		cookiesThreadLocal.remove();
	}
	
	/**
	 * 构建请求类型
	 */
	private static String buildContentType(String contentType, String charset) {
		String result = "";
		if(true == StringUtils.isBlank(contentType)) {
			result = contentType;
		} else {
			if(true == StringUtils.containsIgnoreCase(contentType, "charset")) {
				result = contentType;
			} else {
				if(true == StringUtils.endsWith(contentType.trim(), ";")) {
					result = contentType + "charset=" + charset;
				} else {
					result = contentType + ";charset=" + charset;
				}
			}
		}
		return result;
	}
	
}
