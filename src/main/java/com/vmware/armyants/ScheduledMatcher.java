package com.vmware.armyants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledMatcher {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledMatcher.class);
	
	@Scheduled(fixedDelay=5000)
	public void repeatableTask() {
		logger.info("Running scheduled task");
	}
}
