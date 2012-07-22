package com.vmware.armyants;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private DocStore repo;
	@Autowired
	private LuceneIndexer searchEngine;
	private ScheduledMatcher sm;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is "+ locale.toString());
		
		if (repo.mongoTemplate != null) {
			model.addAttribute("dbinit", "yes");
		} else {
			model.addAttribute("dbinit", "no");
		}
		
		// Create 2 entries in mongoDB
		repo.deleteDocStore();
		repo.createDocStore();
		repo.addDocsToStore("test1", "Lucene in action");
		repo.addDocsToStore("test2", "Eclipse is for java");
		
		try {
			searchEngine.indexDocs();
			String[] results = searchEngine.search("eclipse");
			for(String result : results) {
				model.addAttribute("result", result);
			}
			searchEngine.addRFPToStore(new DocType("rfp1","dummy body"));
		} catch (Exception e) {
			logger.info("Exception in Lucene" + e);
		}
		String environmentName = (System.getenv("VCAP_APPLICATION") != null) ? "Cloud" : "Local";
		model.addAttribute("environmentName", environmentName);
		return "home";
	}
	
}
