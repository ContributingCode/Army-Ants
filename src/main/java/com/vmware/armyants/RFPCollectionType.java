package com.vmware.armyants;

import java.util.ArrayList;

public class RFPCollectionType{
    private int id;
    private String userName;
    private String rfpName;
    private String rfpBody;
    private ArrayList<Integer> appList;
    
    
    public RFPCollectionType(String userName, String rfpName, String rfpBody){
        this.id = rfpName.hashCode();
        this.userName = userName;
        this.rfpName = rfpName;
        this.rfpBody = rfpBody;
    }
    
    public ArrayList<Integer> getAppList() {
		return appList;
	}

	public void setAppList(ArrayList<Integer> appList) {
		this.appList = appList;
	}

	public String getUserName()    {
        return this.userName;
    }
    public void setUserName(String username){
        this.userName = username;
    }
    
    public String getRfpName()    {
        return this.rfpName;
    }
    public void setRfpName(String rfpName){
        this.rfpName = rfpName;
    }
    
    public String getRfpBody()    {
        return this.rfpBody;
    }
    public void setRfpBody(String rfpBody) {
        this.rfpBody = rfpBody;
    }
}
