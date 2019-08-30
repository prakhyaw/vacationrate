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
import com.aristo.requests.ScheduleVacationRequest;
import com.aristo.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class ScheduleVacation extends TestBase{
	
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
		ScheduleVacationRequest data = new ScheduleVacationRequest("SUG", accountId, "2019-12-30", "2019-12-31", "ADD VR", false, "C", "VACATION_RATE");
		
		mapper.writeValue(new File("C:/Users/nagaprakhya_w/finalworkspacewithconnection/vacationrateservices/src/main/java/com/aristo/requests/schedulevacation.json"), data);
		
		String dataString = mapper.writeValueAsString(data);
		httpresponse = restClient.postService(url, dataString, headerMap);
		
		int statusCode = httpresponse.getStatusLine().getStatusCode();
		System.out.println("Status code ----->"+ statusCode);
		//Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not 200");
		
		String responseString = EntityUtils.toString(httpresponse.getEntity(),"UTF-8");
		
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response json from api -->" +responseJson);
				
		//get the value of JSONArray
		String resolutionOption = TestUtil.getValueByJPath(responseJson, "/resolutionOption");
		System.out.println("Scheduling Options are: "+resolutionOption);
		//Assert.assertEquals("VACATION_RATE", resolutionOption);
		
		String statusMessage = TestUtil.getValueByJPath(responseJson, "/aristoVacationSchedulingResponse/statusCode");
		System.out.println("Scheduling Options are: "+statusMessage);
		//Assert.assertEquals("00S", statusMessage);
		
		return resolutionOption;
		
		
		
		
		
	}

}
