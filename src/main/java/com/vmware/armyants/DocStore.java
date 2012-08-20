package com.vmware.armyants;


import java.util.ArrayList;
import java.util.List;

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
		mongoTemplate.insert(document, collection);
	}
	public void addAppsToStore(ArrayList<AppType> list) {
		mongoTemplate.insert(list, CIVIC_COMMONS_COLLECTION);
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
	
	public RFPCollectionType fetchRFPbyId(String rfpName) {
		Query query = new Query(Criteria.where("rfpName").is(rfpName));
		return mongoTemplate.findAndRemove(query, RFPCollectionType.class, RFP_COLLECTION);
	}
	
	public AppType fetchAppById(String rfpName) {
		Query query = new Query(Criteria.where("name").is(rfpName));
		return mongoTemplate.findOne(query, AppType.class, CIVIC_COMMONS_COLLECTION);
	}
	
	public List<RFPCollectionType> getAllRFPsForUser(String userName) {
		Query query = new Query(Criteria.where("userName").is(userName));
		return mongoTemplate.find(query, RFPCollectionType.class, RFP_COLLECTION);
	}
	
	public ArrayList<AppType> getAppsForRFPbyId(String rfpName) {
		// fetch removes the RFP from store
		RFPCollectionType rfp = fetchRFPbyId(rfpName);
		ArrayList<String> appsId = rfp.getAppList();
		ArrayList<AppType> results = new ArrayList<AppType>();
		for (String eachAppId : appsId) {
			results.add(fetchAppById(eachAppId));
		}
		// put it back
		addDocsToStore(RFP_COLLECTION, rfp);
		return results;
	}
}
