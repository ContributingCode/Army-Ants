/**
 * 
 */
package com.vmware.armyants;

import java.net.URI;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author rukmani
 *
 */
@Document
public class AppType {
	
	private String name;
	private String body;
	private String url;
	
	public AppType(String name, String body, String url) {
		this.name = name;
		this.body = body;
		//this.Id = url.hashCode();
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

}
