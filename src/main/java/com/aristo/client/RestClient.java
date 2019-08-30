package com.aristo.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.aristo.base.TestBase;

public class RestClient extends TestBase{
	
	
	//GET method
	public CloseableHttpResponse getService(String url, HashMap<String, String> headerMap) throws ClientProtocolException, IOException
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		
		/*new TestBase();
		httpget.addHeader(prop.getProperty("headerkey1"), prop.getProperty("headervalue1"));
		httpget.addHeader(prop.getProperty("headerkey2"), prop.getProperty("headervalue2"));*/
		
		for(Map.Entry<String, String> entry : headerMap.entrySet())
		{
			httpget.addHeader(entry.getKey(), entry.getValue());
		}
		CloseableHttpResponse httpresponse = httpClient.execute(httpget);
		
		return httpresponse;
	}
	
	
	//POST method
	public CloseableHttpResponse postService(String url, String entityString, HashMap<String, String> headerMap) throws ClientProtocolException, IOException
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(new StringEntity(entityString));
		for(Map.Entry<String, String> entry : headerMap.entrySet())
		{
			httppost.addHeader(entry.getKey(), entry.getValue());
		}
		CloseableHttpResponse httpresponse = httpClient.execute(httppost);
			
		return httpresponse;		
	}

}
