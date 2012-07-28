package com.vmware.armyants;

import java.util.ArrayList;
import java.util.Map;

public class UserInfoType {

	private int id=123;
	private Map<String, ArrayList<Integer> > userToRFPMapping;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<String, ArrayList<Integer>> getUserToRFPMapping() {
		return userToRFPMapping;
	}
	public void setUserToRFPMapping(Map<String, ArrayList<Integer>> userToRFPMapping) {
		this.userToRFPMapping = userToRFPMapping;
	}
}
