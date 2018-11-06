package io.sdb.common.utils;

import com.jfinal.kit.LogKit;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpRequest {
	
	public static String addUrl(String head, String tail) {
		if (head.endsWith("/")) {
			if (tail.startsWith("/")) {
				return head.substring(0, head.length() - 1) + tail;
			} else {
				return head + tail;
			}
		} else {
			if (tail.startsWith("/")) {
				return head + tail;
			} else {
				return head + "/" + tail;
			}
		}
	}

	public synchronized static String postData(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(url);
		//拼接参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        NameValuePair[] nameValuePairArray = assembleRequestParams(params);
        for (NameValuePair value:nameValuePairArray
             ) {
            nvps.add(value);
        }

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		CloseableHttpResponse response = httpclient.execute(httpPost);
        String result = "";
        try {
            StatusLine statusLine = response.getStatusLine();
			HttpEntity entity = response.getEntity();
            // do something useful with the response body
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }else{
                LogKit.error("httpRequest postData1 error entity = null code = "+statusLine.getStatusCode());
            }
			// and ensure it is fully consumed
			//消耗掉response
			EntityUtils.consume(entity);
		} finally {
			response.close();
		}

		return result;
	}

	public synchronized static String postData(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        String result = "";
        try {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }else{
                LogKit.error("httpRequest postData2 error entity = null code = "+statusLine.getStatusCode());
            }
            // and ensure it is fully consumed
            //消耗掉response
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        return result;
	}

	/**
	 * 组装http请求参数
	 * @return
	 */
	private synchronized static NameValuePair[] assembleRequestParams(Map<String, String> data) {
		final List<NameValuePair> nameValueList = new ArrayList<NameValuePair>();

		Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			nameValueList.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
		}

		return nameValueList.toArray(new NameValuePair[nameValueList.size()]);
	}

}
