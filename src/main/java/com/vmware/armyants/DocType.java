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
public class DocType {
	
	@Id
	private String Id;
	private String name;
	private String body;
	private URI url;
	
	public DocType(String name, String body) {
		this.name = name;
		this.body = body;
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
