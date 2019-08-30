package com.aristo.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aristo.services.EligibilityOptions;
import com.aristo.services.ScheduleVacation;
import com.aristo.services.VacationHistory;
import com.aristo.util.TestAccountsProvider;
import com.nytimes.testng.Logger;

public class AddRateTest {
	
	@Test(groups = {"ADD_RATE"}, invocationCount = 2)
	public void ScheduleVacationWithAddRate() {
		
		try
		{
			TestAccountsProvider testdata = new TestAccountsProvider();
			String accountId = testdata.getAccount("HD");
			
			System.out.println("Account Number selected for vacation is ==\t"+accountId);
			
			int l = accountId.length();
			if(accountId.length() < 9)
			{
				for(int i = 0; i < (9-l); i++)
				{
					accountId = 0+accountId;
				}
			}
			
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
			Assert.fail("Some exception has been thrown.....Please refer log file for more information.....");
		}
	}
}
