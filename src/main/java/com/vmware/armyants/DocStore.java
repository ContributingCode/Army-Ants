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
	
	@Autowired
	MongoTemplate mongoTemplate;

	public void addDocsToStore(String name, String body) {
		Logger.info("inserting " + name);
		mongoTemplate.insert(new DocType(name, body));
	}

	public void createDocStore() {
		Logger.info("Creating DocStore ");
		mongoTemplate.createCollection(DocType.class);
	}

	public void deleteDocStore() {
		Logger.info("deleting DocStore ");
		mongoTemplate.dropCollection(DocType.class);
	}

	public Iterator<DocType> retrieveDocs() {
		List<DocType> allData = mongoTemplate.findAll(DocType.class);
		return allData.iterator();
	}
}
