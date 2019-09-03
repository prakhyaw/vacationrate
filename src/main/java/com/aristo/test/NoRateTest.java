package com.aristo.test;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.aristo.services.EligibilityOptions;
import com.aristo.services.ScheduleVacation;
import com.aristo.services.VacationHistory;
import com.aristo.util.TestAccountsProvider;

public class NoRateTest {
	
	Logger logger = Logger.getLogger(NoRateTest.class);
	
	@Test(groups = {"NO_RATE"}, invocationCount = 2)
	public void ScheduleVacationWithNoRate() {
		
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
			String startDate = "2019-09-04";
			String stopDate = "2019-09-05"; 
			String comments = "remove digi access";
			String option = "NO_RATE";
			
			EligibilityOptions eligibilitycheck = new EligibilityOptions();
			String schedulingOptions = eligibilitycheck.EligibilityEngineTest(accountId);
			
			if(schedulingOptions.equalsIgnoreCase("REMOVE_DIGITAL_ACCCESS"))
			{
				System.out.println("Account eligible for VR");
				ScheduleVacation schedulevacation = new ScheduleVacation();
				String resolutionOption = schedulevacation.scheduleVacationTest("SUG", accountId, startDate, stopDate, comments, false, "C", option);
				
				if(resolutionOption.equalsIgnoreCase("REMOVE_DIGITAL_ACCCESS"))
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
