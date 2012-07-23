package com.vmware.armyants;

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
	
	@Scheduled(fixedDelay=24*60*60*1000)
	public void repeatableTask() {
		logger.info("Running scheduled task");
		// Create 2 entries in mongoDB
		repo.deleteDocStore();
		repo.createDocStore();
		repo.addDocsToStore("test1", "Lucene in action");
		repo.addDocsToStore("test2", "Eclipse is for java");
	}
}
