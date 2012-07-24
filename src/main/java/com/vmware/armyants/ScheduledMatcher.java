package com.vmware.armyants;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledMatcher {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledMatcher.class);
	@Autowired
	private DocStore repo;
	
	@Scheduled(fixedDelay=10000)
	public void repeatableTask() throws ParseException, IOException {
		logger.info("Running scheduled task");
		/*
		 * Get JSON from URL
		 * Parse it
		 * convert to DocTye and put in CIVIC_COMMONS_COLLECTION for all values
		 * call LuceneIndexer index method
		 */
		// Create 2 entries in mongoDB
		repo.addDocsToStore(DocStore.CIVIC_COMMONS_COLLECTION,"test1", "Lucene in action");
		repo.addDocsToStore(DocStore.CIVIC_COMMONS_COLLECTION,"test2", "Eclipse is for java");
	}
}
