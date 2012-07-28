package com.vmware.armyants;


import java.util.Iterator;
import java.util.List;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;


@Repository
public class DocStore {
	
	static final Logger Logger = LoggerFactory.getLogger(DocStore.class);
	public static String CIVIC_COMMONS_COLLECTION="civic-commons";
	public static String RFP_COLLECTION="rfp";
	public static String USERS_INFO="users-info";
	
	@Autowired
	MongoTemplate mongoTemplate;

	public void addDocsToStore(String collection, Object document) {
		Logger.info("inserting ");
		mongoTemplate.insert(document, collection);
	}

	public void createDocStore(String collection) {
		Logger.info("Creating DocStore ");
		mongoTemplate.createCollection(collection);
	}

	public void deleteDocStore(String collection) {
		Logger.info("deleting DocStore ");
		mongoTemplate.dropCollection(collection);
	}

	public DBCursor retrieveDocs(String collection) {
		return mongoTemplate.getCollection(collection).find();
	}
}
