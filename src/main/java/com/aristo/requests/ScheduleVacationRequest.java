package com.aristo.requests;

public class ScheduleVacationRequest {
	
	String operatorId;
	String accountId;
	String vacStartDt;
	String vacEndDt;
	String stopComments;
	Boolean validationFlag;
	String stopBy;
	String resolutionOption;
	
	public ScheduleVacationRequest(String operatorId, String accountId, String vacStartDt, String vacEndDt, String stopComments, Boolean validationFlag, String stopBy, String resolutionOption)
	{
		this.operatorId = operatorId;
		this.accountId = accountId;
		this.vacStartDt = vacStartDt;
		this.vacEndDt = vacEndDt;
		this.stopComments = stopComments;
		this.validationFlag = validationFlag;
		this.stopBy = stopBy;
		this.resolutionOption = resolutionOption;		
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getVacStartDt() {
		return vacStartDt;
	}

	public void setVacStartDt(String vacStartDt) {
		this.vacStartDt = vacStartDt;
	}

	public String getVacEndDt() {
		return vacEndDt;
	}

	public void setVacEndDt(String vacEndDt) {
		this.vacEndDt = vacEndDt;
	}

	public String getStopComments() {
		return stopComments;
	}

	public void setStopComments(String stopComments) {
		this.stopComments = stopComments;
	}

	public Boolean getValidationFlag() {
		return validationFlag;
	}

	public void setValidationFlag(Boolean validationFlag) {
		this.validationFlag = validationFlag;
	}

	public String getStopBy() {
		return stopBy;
	}

	public void setStopBy(String stopBy) {
		this.stopBy = stopBy;
	}

	public String getResolutionOption() {
		return resolutionOption;
	}

	public void setResolutionOption(String resolutionOption) {
		this.resolutionOption = resolutionOption;
	}

}
