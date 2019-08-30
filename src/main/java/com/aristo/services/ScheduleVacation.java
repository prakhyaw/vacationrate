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
import com.aristo.requests.ScheduleVacationRequest;
import com.aristo.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class ScheduleVacation extends TestBase{
	
	Logger logger = Logger.getLogger(ScheduleVacation.class);
	
	TestBase testbase;
	String apiurl;
	String scheduleVacationURL;
	String url;
	RestClient restClient;
	CloseableHttpResponse httpresponse;
	
	@BeforeMethod
	public void setUp(){
		testbase = new TestBase();
		apiurl = prop.getProperty("URL");
		scheduleVacationURL = prop.getProperty("scheduleVacationURL");
		
		url = apiurl + scheduleVacationURL;
		System.out.println("URL is "+url);
	}
	
	@Test
	public String scheduleVacationTest(String accountId) throws ClientProtocolException, IOException {
		setUp();
		restClient = new RestClient();
		
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("x-api-key", "AIzaSyBdziJBDcDkTzhlAfQYoBYzUySvcrlqSRc");
		headerMap.put("Content-Type", "application/json");
		
		ObjectMapper mapper = new ObjectMapper();
		ScheduleVacationRequest data = new ScheduleVacationRequest("SUG", accountId, "2019-08-31", "2019-09-01", "ADD VR", false, "C", "VACATION_RATE");
		
		String filePath = System.getProperty("user.dir")+"/src/main/java/com/aristo/requests/schedulevacation.json";
		
		//object to json file
		mapper.writeValue(new File(filePath), data);
		
		String dataString = mapper.writeValueAsString(data);
		logger.info(dataString);
		
		httpresponse = restClient.postService(url, dataString, headerMap);
		
		int statusCode = httpresponse.getStatusLine().getStatusCode();
		logger.info("Status code ----->"+ statusCode);
		//Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		
		String responseString = EntityUtils.toString(httpresponse.getEntity(),"UTF-8");
		
		JSONObject responseJson = new JSONObject(responseString);
		logger.info("Response json from api -->" +responseJson);
				
		//get the value of JSONArray
		String resolutionOption = TestUtil.getValueByJPath(responseJson, "/resolutionOption");
		logger.info("Vacatioon created opted for : "+resolutionOption);
		//Assert.assertEquals("VACATION_RATE", resolutionOption);
		
		String statusCode1 = TestUtil.getValueByJPath(responseJson, "/aristoVacationSchedulingResponse/statusCode");
		logger.info("status code: "+statusCode1);
		//Assert.assertEquals("00S", statusMessage);
		
		return resolutionOption;
		
		
		
		
		
	}

}
