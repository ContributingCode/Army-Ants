package com.vmware.armyants;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.*;


@Repository
public class DocStore {
	
	static final Logger Logger = LoggerFactory.getLogger(DocStore.class);
	public static String CIVIC_COMMONS_COLLECTION="civic-commons";
	public static String RFP_COLLECTION="rfp";
	
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
	
	public RFPCollectionType fetchRFPbyId(int id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.findAndRemove(query, RFPCollectionType.class);
	}
	
	public AppType fetchAppById(int id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, AppType.class);
	}
	
	public void addRFPToUser(String userName, String name, String body) {
		
	}
	
	public void findAppsForRFPs(){
		
	}
	
	public List<RFPCollectionType> getAllRFPsForUser(String userName) {
		Query query = new Query(Criteria.where("userName").is(userName));
		return mongoTemplate.find(query, RFPCollectionType.class, RFP_COLLECTION);
	}
	
	public ArrayList<AppType> getAppsForRFPbyId(int id) {
		ArrayList<Integer> appsId = fetchRFPbyId(id).getAppList();
		ArrayList<AppType> results = new ArrayList<AppType>();
		for (int eachAppId : appsId) {
			results.add(fetchAppById(eachAppId));
		}
		return results;
	}
}
