package com.vmware.armyants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.queryparser.classic.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledMatcher {

	private static final Logger logger = LoggerFactory
			.getLogger(ScheduledMatcher.class);
	@Autowired
	private DocStore repo;
	@Autowired
	private LuceneIndexer searchEngine;

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	@Scheduled(fixedDelay = 300000)
	public void repeatableTask() throws ParseException, IOException,
	JSONException, URISyntaxException {
		logger.info("Running scheduled task");
		ArrayList<AppType> allApps = new ArrayList<AppType>();
		/*
		 * Get JSON from URL Parse it convert to DocTye and put in
		 * CIVIC_COMMONS_COLLECTION for all values call LuceneIndexer index
		 * method
		 */
		// URL url = new
		// URL("https://api.scraperwiki.com/api/1.0/datastore/sqlite?format=jsondict&name=civic_commons_extracting_links_of_all_apps&query=select%20*%20from%20%60army_ants_civic_commons_apps%60%20");
		URL civic_commons_datastore_url = new URL(
				"https://api.scraperwiki.com/api/1.0/datastore/sqlite?format=jsondict&name=civic_commons_extracting_links_of_all_apps&query=select%20*%20from%20%60army_ants_civic_commons_apps%60%20");
		BufferedReader br_reader = new BufferedReader(
				new InputStreamReader(
						civic_commons_datastore_url.openStream()));
		String jsonText = readAll(br_reader);
		JSONArray json_datastore_array = new JSONArray(jsonText);
		for (int i = 0; i < json_datastore_array.length() - 2; i++) {
			JSONObject civic_app_item = json_datastore_array
					.getJSONObject(i);
			String app_url = (String) civic_app_item.get("url");
			String app_name = civic_app_item.get("name").toString();
			String app_description = civic_app_item.get("description")
					.toString();

			AppType dt = new AppType(app_name, app_description, app_url);
			allApps.add(dt);
			logger.info("Added a civic commons app" + dt.toString());
		}
		
		repo.addAppsToStore(allApps);
		br_reader.close();
		searchEngine.indexDocs();
	}
}
