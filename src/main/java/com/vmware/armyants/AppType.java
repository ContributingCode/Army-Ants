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
	
	@Id
	private int Id;
	private String name;
	private String body;
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
	private URI url;
	
	public AppType(String name, String body, URI url) {
		this.name = name;
		this.body = body;
		this.Id = url.hashCode();
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
