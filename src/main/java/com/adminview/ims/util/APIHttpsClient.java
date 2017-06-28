package com.adminview.ims.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class APIHttpsClient {

    private static final Log logger = LogFactory.getLog(APIHttpsClient.class);

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 8000;

    static {
	// 设置连接池
	connMgr = new PoolingHttpClientConnectionManager();
	// 设置连接池大小
	connMgr.setMaxTotal(100);
	connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

	RequestConfig.Builder configBuilder = RequestConfig.custom();
	// 设置连接超时
	configBuilder.setConnectTimeout(MAX_TIMEOUT);
	// 设置读取超时
	configBuilder.setSocketTimeout(MAX_TIMEOUT);
	// 设置从连接池获取连接实例的超时
	configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
	// 在提交请求之前 测试连接是否可用
	configBuilder.setStaleConnectionCheckEnabled(true);
	requestConfig = configBuilder.build();
    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     * 
     * @param url
     * @return
     */
    public static String doGet(String url) {
	return doGet(url, new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTP），字符串形式
     * 
     * @param url
     * @return
     */

    @SuppressWarnings("resource")
    public static String doGet(String url, String params) {
	String apiUrl = url;
	apiUrl += params;
	logger.debug("doget: url=" + apiUrl);
	String result = null;
	HttpClient httpclient = new DefaultHttpClient();
	try {
	    HttpGet httpPost = new HttpGet(apiUrl);
	    HttpResponse response = httpclient.execute(httpPost);
	    int statusCode = response.getStatusLine().getStatusCode();

	    logger.debug("执行状态码 : " + statusCode);

	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
		InputStream instream = entity.getContent();
		result = IOUtils.toString(instream, "UTF-8");
	    }
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	logger.debug("doget: url=" + apiUrl + "; result" + result);
	return result;
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     * 
     * @param url
     * @param params
     * @return
     */
    @SuppressWarnings("resource")
    public static String doGet(String url, Map<String, Object> params) {

	String apiUrl = url;
	StringBuffer param = new StringBuffer();
	int i = 0;
	for (String key : params.keySet()) {
	    if (i == 0)
		param.append("?");
	    else
		param.append("&");
	    param.append(key).append("=").append(params.get(key));
	    i++;
	}
	apiUrl += param;
	logger.debug("doGet: url=" + apiUrl);
	String result = null;
	HttpClient httpclient = new DefaultHttpClient();
	try {
	    HttpGet httpPost = new HttpGet(apiUrl);
	    HttpResponse response = httpclient.execute(httpPost);
	    int statusCode = response.getStatusLine().getStatusCode();

	    logger.debug("执行状态码 : " + statusCode);

	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
		InputStream instream = entity.getContent();
		result = IOUtils.toString(instream, "UTF-8");
	    }
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	logger.debug("doget: url=" + apiUrl + "; result" + result);
	return result;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     * 
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
	return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     * 
     * @param apiUrl
     *            API接口URL
     * @param params
     *            参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {
	CloseableHttpClient httpClient = HttpClients.createDefault();
	String httpStr = null;
	HttpPost httpPost = new HttpPost(apiUrl);
	CloseableHttpResponse response = null;
	logger.debug("doPost: url=" + apiUrl + "; params" + params);
	try {
	    httpPost.setConfig(requestConfig);
	    List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
	    for (Map.Entry<String, Object> entry : params.entrySet()) {
		NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
		pairList.add(pair);
	    }
	    httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
	    response = httpClient.execute(httpPost);
	    logger.debug(response.toString());
	    HttpEntity entity = response.getEntity();
	    httpStr = EntityUtils.toString(entity, "UTF-8");
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} finally {
	    if (response != null) {
		try {
		    EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	logger.debug("doPost: url=" + apiUrl + "; result" + httpStr);
	return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     * 
     * @param apiUrl
     * @param json
     *            json对象
     * @return
     */
    public static String doPostJSON(String apiUrl, Object json) {
	CloseableHttpClient httpClient = HttpClients.createDefault();
	String httpStr = null;
	HttpPost httpPost = new HttpPost(apiUrl);
	CloseableHttpResponse response = null;

	try {
	    httpPost.setConfig(requestConfig);
	    StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
	    stringEntity.setContentEncoding("UTF-8");
	    stringEntity.setContentType("application/json");
	    httpPost.setEntity(stringEntity);
	    response = httpClient.execute(httpPost);
	    HttpEntity entity = response.getEntity();
	    logger.debug(response.getStatusLine().getStatusCode());
	    httpStr = EntityUtils.toString(entity, "UTF-8");
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (response != null) {
		try {
		    EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	return httpStr;
    }

    /**
     * 发送 GET 请求（HTTPSSL），K-V形式
     * 
     * @param url
     * @param params
     * @return
     */
    public static String doGetSSL(String url, Map<String, Object> params) {
	String apiUrl = url;
	CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
		.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
	HttpGet httpGet = new HttpGet(apiUrl);
	CloseableHttpResponse response = null;

	if (null != params && params.size() > 0) {

	    StringBuffer param = new StringBuffer();
	    int i = 0;
	    for (String key : params.keySet()) {
		if (i == 0)
		    param.append("?");
		else
		    param.append("&");
		param.append(key).append("=").append(params.get(key));
		i++;
	    }
	    apiUrl += param;
	}
	logger.debug("doPost: url=" + apiUrl);
	String result = null;
	try {

	    response = httpClient.execute(httpGet);
	    int statusCode = response.getStatusLine().getStatusCode();

	    logger.debug("执行状态码 : " + statusCode);

	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
		InputStream instream = entity.getContent();
		result = IOUtils.toString(instream, "UTF-8");
	    }
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
	logger.debug("doGet-ssl: url=" + apiUrl + ";result=" + result);
	return result;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     * 
     * @param apiUrl
     *            API接口URL
     * @param params
     *            参数map
     * @return
     */
    public static String doPostSSL(String apiUrl, Map<String, Object> params) {
	CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
		.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
	HttpPost httpPost = new HttpPost(apiUrl);
	CloseableHttpResponse response = null;
	String httpStr = null;
	logger.debug("doPost-ssl: url=" + apiUrl + ";params=" + params);
	try {
	    httpPost.setConfig(requestConfig);
	    if (null != params && params.size() > 0) {
		List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
		for (Map.Entry<String, Object> entry : params.entrySet()) {
		    NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
		    pairList.add(pair);
		}
		httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
	    }

	    response = httpClient.execute(httpPost);
	    int statusCode = response.getStatusLine().getStatusCode();
	    if (statusCode != HttpStatus.SC_OK) {
		return null;
	    }
	    HttpEntity entity = response.getEntity();
	    if (entity == null) {
		return null;
	    }
	    httpStr = EntityUtils.toString(entity, "utf-8");
	} catch (Exception e) {
	    throw new RuntimeException(e);
	} finally {
	    if (response != null) {
		try {
		    EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	logger.debug("doPost-ssl: url=" + apiUrl + ";result=" + httpStr);
	return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     * 
     * @param apiUrl
     *            API接口URL
     * @param json
     *            JSON对象
     * @return
     */
    public static String doPostSSL(String apiUrl, Object json) {
	CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
		.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
	HttpPost httpPost = new HttpPost(apiUrl);
	CloseableHttpResponse response = null;
	String httpStr = null;

	try {
	    httpPost.setConfig(requestConfig);
	    StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
	    stringEntity.setContentEncoding("UTF-8");
	    stringEntity.setContentType("application/json");
	    httpPost.setEntity(stringEntity);
	    response = httpClient.execute(httpPost);
	    int statusCode = response.getStatusLine().getStatusCode();
	    if (statusCode != HttpStatus.SC_OK) {
		return null;
	    }
	    HttpEntity entity = response.getEntity();
	    if (entity == null) {
		return null;
	    }
	    httpStr = EntityUtils.toString(entity, "utf-8");
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (response != null) {
		try {
		    EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	return httpStr;
    }

    

    /**
     * 创建SSL安全连接
     * 
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
	SSLConnectionSocketFactory sslsf = null;
	try {
	    SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

		public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		    return true;
		}
	    }).build();
	    sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

		@Override
		public boolean verify(String arg0, SSLSession arg1) {
		    return true;
		}

		@Override
		public void verify(String host, SSLSocket ssl) throws IOException {
		}

		@Override
		public void verify(String host, X509Certificate cert) throws SSLException {
		}

		@Override
		public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
		}
	    });
	} catch (GeneralSecurityException e) {
	    e.printStackTrace();
	}
	return sslsf;
    }

    /**
     * https 方式访问; 如果是get方式，则 outputStr参数传null，不为空时为post访问
     * 
     * @param requestUrl
     *            访问地址
     * @param outputStr
     *            访问参数 a=1&b=2 形式的字串;
     * @return
     */
    public static JSONObject requestHTTPS(String requestUrl, Map<String, Object> params) {
	JSONObject jsonObject = null;
	try {
	    String response = null;
	    if (null == params || params.size() == 0) {// get
		response = doGetSSL(requestUrl, null);
	    } else {// post
		response = doPostSSL(requestUrl, params);
	    }
	    if (null != response) {
		jsonObject = JSONObject.fromObject(response);
	    }
	} catch (RuntimeException e) {
	    throw e;
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
	return jsonObject;
    }

   
}
