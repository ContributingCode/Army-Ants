package com.vmware.armyants;


import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Repository
public class DocStore {
	
	static final Logger Logger = LoggerFactory.getLogger(DocStore.class);
	public static String CIVIC_COMMONS_COLLECTION="civic-commons";
	public static String RFP_COLLECTION="rfp";
	public static String USERS_INFO="users-info";
	
	@Autowired
	MongoTemplate mongoTemplate;

	public void addDocsToStore(String collection, String name, String body) {
		Logger.info("inserting " + name);
		mongoTemplate.insert(new DocType(name, body), collection);
	}

	public void createDocStore(String collection) {
		Logger.info("Creating DocStore ");
		mongoTemplate.createCollection(collection);
	}

	public void deleteDocStore(String collection) {
		Logger.info("deleting DocStore ");
		mongoTemplate.dropCollection(collection);
	}

	public Iterator<DocType> retrieveDocs(String collection) {
		List<DocType> allData = mongoTemplate.findAll(DocType.class, collection);
		return allData.iterator();
	}
}
