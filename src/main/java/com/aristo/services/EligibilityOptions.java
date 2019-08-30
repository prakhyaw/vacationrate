package com.aristo.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aristo.base.TestBase;
import com.aristo.client.RestClient;
import com.aristo.requests.EligibilityRequest;
import com.aristo.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EligibilityOptions extends TestBase{
	
	TestBase testbase;
	String apiurl;
	String eligibilityURL;
	String url;
	RestClient restClient;
	CloseableHttpResponse httpresponse;
	
	@BeforeMethod
	public void setUp(){
		testbase = new TestBase();
		apiurl = prop.getProperty("URL");
		eligibilityURL = prop.getProperty("eligibilityURL");
		
		url = apiurl + eligibilityURL;
		System.out.println("URL is "+url);
	}
	
	@Test
	public String EligibilityEngineTest(String accountId) throws ClientProtocolException, IOException {
		setUp();		
		restClient = new RestClient();
		
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("x-api-key", "AIzaSyBdziJBDcDkTzhlAfQYoBYzUySvcrlqSRc");
		headerMap.put("Content-Type", "application/json");
		//Jackson API
		ObjectMapper mapper = new ObjectMapper();
		EligibilityRequest data = new EligibilityRequest("SUG", accountId);
		
		//object to json file
		mapper.writeValue(new File("C:/Users/nagaprakhya_w/finalworkspacewithconnection/vacationrateservices/src/main/java/com/aristo/requests/eligibility.json"), data);
		
		//object to json string
		String dataString = mapper.writeValueAsString(data);
		
		//execute
		httpresponse = restClient.postService(url, dataString, headerMap);
		
		int statusCode = httpresponse.getStatusLine().getStatusCode();
		System.out.println("Status code ----->"+ statusCode);
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		
		//Json String
		String responseString = EntityUtils.toString(httpresponse.getEntity(),"UTF-8");		
				
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response json from api -->" +responseJson);
				
		//get the value of JSONArray
		String schedulingOptions = TestUtil.getValueByJPath(responseJson, "/schedulingOptions[2]");
		System.out.println("Scheduling Options are: "+schedulingOptions);
		//Assert.assertEquals("VACATION_RATE", schedulingOptions);
		
		return schedulingOptions;
		
	}
}
