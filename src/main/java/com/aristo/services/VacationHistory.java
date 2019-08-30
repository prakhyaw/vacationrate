package com.aristo.services;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aristo.base.TestBase;
import com.aristo.client.RestClient;
import com.aristo.util.TestUtil;

public class VacationHistory extends TestBase{
	
	TestBase testbase;
	String apiurl;
	String vacationHistoryURL;
	String url;
	RestClient restClient;
	CloseableHttpResponse httpresponse;
	
	@BeforeMethod
	public void setUp(){
		testbase = new TestBase();
		apiurl = prop.getProperty("URL");
		vacationHistoryURL = prop.getProperty("vacationHistoryURL");
		
		url = apiurl + vacationHistoryURL;
		System.out.println("URL is "+url);
	}
	
	@Test
	public void VacationHistoryTest(String accountId) throws ClientProtocolException, IOException {
		setUp();
		restClient = new RestClient();
		
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("x-api-key", "AIzaSyBdziJBDcDkTzhlAfQYoBYzUySvcrlqSRc");
		headerMap.put("Content-Type", "application/json");
		String finalUrl = url + accountId;
		httpresponse = restClient.getService(finalUrl, headerMap);
		
		//Status code
		int statusCode = httpresponse.getStatusLine().getStatusCode();
		System.out.println("Status code ----->"+ statusCode);
		
		//Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		
		//Json String
		String responseString = EntityUtils.toString(httpresponse.getEntity(),"UTF-8");		
		
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response json from api -->" +responseJson);
		
		//get the value of JSONArray
		String tranCount = TestUtil.getValueByJPath(responseJson, "/transactionRespCode/tranCount");
		System.out.println("transactions count: "+tranCount);
		
		String resolutionOption = TestUtil.getValueByJPath(responseJson, "/suspensions[0]/resolutionOption");
		System.out.println("resolutionOption: "+resolutionOption);
		//Assert.assertEquals(resolutionOption, "ADD_RATE");
				
			
	}

}
