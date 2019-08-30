package com.aristo.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aristo.services.EligibilityOptions;
import com.aristo.services.ScheduleVacation;
import com.aristo.services.VacationHistory;
import com.aristo.util.TestAccountsProvider;
import org.apache.log4j.Logger;

public class AddRateTest {
	
	Logger logger = Logger.getLogger(AddRateTest.class);
	
	@SuppressWarnings("null")
	@Test(groups = {"ADD_RATE"}, invocationCount = 1)
	public void ScheduleVacationWithAddRate() {
		
		try
		{
			logger.info("Test started");
			
			TestAccountsProvider testdata = new TestAccountsProvider();
			String accountId = testdata.getAccount("HD");
						
			int l = accountId.length();
			if(accountId.length() < 9)
			{
				for(int i = 0; i < (9-l); i++)
				{
					accountId = 0+accountId;
				}
			}
			
			logger.info("Account Number selected for vacation is ==\t"+accountId);
			
			EligibilityOptions eligibilitycheck = new EligibilityOptions();
			String schedulingOptions = eligibilitycheck.EligibilityEngineTest(accountId);
			
			if(schedulingOptions.equalsIgnoreCase("VACATION_RATE"))
			{
				System.out.println("Account eligible for VR");
				ScheduleVacation schedulevacation = new ScheduleVacation();
				String resolutionOption = schedulevacation.scheduleVacationTest(accountId);
				
				if(resolutionOption.equalsIgnoreCase("VACATION_RATE"))
				{
					System.out.println("Vacation Created with VR");
					VacationHistory vacationhistory = new VacationHistory();
					vacationhistory.VacationHistoryTest(accountId);
				}
			}			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
