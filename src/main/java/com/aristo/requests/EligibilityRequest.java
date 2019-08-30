package com.aristo.requests;

public class EligibilityRequest {
	
	String operatorId;
	String accountId;
	
	public EligibilityRequest(String operatorId, String accountId) {
		this.operatorId = operatorId;
		this.accountId = accountId;
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
}
