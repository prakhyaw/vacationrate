package com.aristo.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aristo.base.TestBase;
import com.aristo.client.RestClient;
import com.aristo.requests.EligibilityRequest;
import com.aristo.test.AddRateTest;
import com.aristo.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EligibilityOptions extends TestBase{
	
	Logger logger = Logger.getLogger(EligibilityOptions.class);
	
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
		
		String filePath = System.getProperty("user.dir")+"/src/main/java/com/aristo/requests/eligibility.json";
		
		//object to json file
		mapper.writeValue(new File(filePath), data);
		
		//object to json string
		String dataString = mapper.writeValueAsString(data);
		logger.info(dataString);
		
		//execute
		httpresponse = restClient.postService(url, dataString, headerMap);
		
		int statusCode = httpresponse.getStatusLine().getStatusCode();
		logger.info("Status code ----->"+ statusCode);
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		
		//Json String
		String responseString = EntityUtils.toString(httpresponse.getEntity(),"UTF-8");		
				
		JSONObject responseJson = new JSONObject(responseString);
		logger.info("Response json from api -->" +responseJson);
				
		//get the value of JSONArray

		String schedulingOptions = TestUtil.getValueByJPath(responseJson, "/schedulingOptions[0]");
		if(schedulingOptions.equalsIgnoreCase("REMOVE_DIGITAL_ACCCESS"))
		{
			logger.info("Scheduling Options are: "+schedulingOptions);
			logger.info(accountId + ": Account eligible for VR");
			//Assert.assertEquals("VACATION_RATE", schedulingOptions);
		}
		
		else
		{
			logger.info("Scheduling Options are: "+schedulingOptions);
			logger.info(accountId + ": Account not eligible for VR");
		}
		
		
		
		return schedulingOptions;
		
	}
}
