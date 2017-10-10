package com.testlink.vo;

public class InputVo 
{
	private String firstpath;
	
	private String secondpath;
	
	private String thirdpath;
	
	private String name;
	
	private String externalid;
	
	private String value;
	
	private String preconditions;
	
	private String actions;

	private String importance;
	
	private String expectedresults;

	private String executiontype;

	public String getFirstpath() {
		return firstpath;
	}

	public void setFirstpath(String firstpath) {
		this.firstpath = firstpath;
	}

	public String getSecondpath() {
		return secondpath;
	}

	public void setSecondpath(String secondpath) {
		this.secondpath = secondpath;
	}

	public String getThirdpath() {
		return thirdpath;
	}

	public void setThirdpath(String thirdpath) {
		this.thirdpath = thirdpath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExternalid() {
		return externalid;
	}

	public void setExternalid(String externalid) {
		this.externalid = externalid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(String preconditions) {
		this.preconditions = preconditions;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getExpectedresults() {
		return expectedresults;
	}

	public void setExpectedresults(String expectedresults) {
		this.expectedresults = expectedresults;
	}

	public String getImportance() {return importance;}

	public void setImportance(String importance) {this.importance = importance; }

	public String getExecutiontype() {
		return executiontype;
	}

	public void setExecutiontype(String executiontype) {
		this.executiontype = executiontype;
	}
	public String toString()
	{
		String result = "{testcase: externalid="+this.getExternalid()+
				",firstpath=" +this.getFirstpath()+
				",secondpath="+this.getSecondpath()+
				",thirdpath="+this.getThirdpath()+
				",name="+this.getName()+
				",preconditions="+this.getPreconditions()+
				",actions="+this.getActions()+
				",expectedresults="+this.getExpectedresults()+
				",importance="+this.getImportance()+
				",executiontype="+this.getExecutiontype()+"}";

//		String result = "{testcase: firstpath=" +this.getFirstpath()+
//				",secondpath="+this.getSecondpath()+
////				",thirdpath="+this.getThirdpath()+
//				",name="+this.getName()+
//				",externalid="+this.getExternalid()+
//				",importance="+this.getImportance()+
//				",preconditions="+this.getPreconditions()+
//				",actions="+this.getActions()+
//				",expectedresults="+this.getExpectedresults()+
//				",executiontype="+this.getExecutiontype()+"}";
		
		return result;
	}
}
