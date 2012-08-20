/**
 * This is the core class that wraps Lucene and provides an interface for the application to 
 * issue commands to the search engine
 */
package com.vmware.armyants;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author rukmani
 */
public class LuceneIndexer {
	
	private static final Logger logger = LoggerFactory.getLogger(LuceneIndexer.class);
	// The place from where you fetch the documents
	private DocStore repo;
	// The directory where Lucene will store its indexes - could be jdbc/inmemory/any db
	private Directory dir;
	private Analyzer analyzer;
	
	
	public LuceneIndexer(DocStore repo) {
		this.repo = repo;
		this.repo.deleteDocStore(DocStore.CIVIC_COMMONS_COLLECTION);
		this.repo.deleteDocStore(DocStore.RFP_COLLECTION);
		this.repo.createDocStore(DocStore.CIVIC_COMMONS_COLLECTION);
		this.repo.createDocStore(DocStore.RFP_COLLECTION);
		analyzer = new StandardAnalyzer(Version.LUCENE_40);
	}

	/**
	 * Takes docs from the repository and indexes them. This has to be done
	 * only once
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 */
	public void indexDocs(String collection) throws CorruptIndexException, LockObtainFailedException, IOException {
		dir = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		IndexWriter writer = new IndexWriter(dir, config);
		
		for (DBCursor it = repo.retrieveDocs(collection); it.hasNext();) {
			logger.info("added doc");
			addDoc(writer, it.next());
		}
		writer.close();
	}
	@Scheduled(fixedDelay = 300000)
	public void indexDocs() throws CorruptIndexException, LockObtainFailedException, IOException {
		String collection = DocStore.CIVIC_COMMONS_COLLECTION;
		dir = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
		IndexWriter writer = new IndexWriter(dir, config);
		
		DBCursor it = repo.retrieveDocs(collection);
		while (it.hasNext()) {
			logger.info("added doc from scheduler");
			addDoc(writer, it.next());
		}
		writer.close();
	}
	private void addDoc(IndexWriter writer, DBObject document) throws IOException {
		Document luceneDoc = new Document();
		FieldType type = new FieldType();
		type.setIndexed(true);
		luceneDoc.add(new Field("name", (String) document.get("name"), Field.Store.YES, Field.Index.ANALYZED));
		luceneDoc.add(new Field("body", (String) document.get("body"), Field.Store.YES, Field.Index.ANALYZED));
		writer.addDocument(luceneDoc);
	}
	
	/**
	 * The main search method
	 * @param query
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public List<AppType> search(String query) throws ParseException, IOException, URISyntaxException {
		Query q = new QueryParser(Version.LUCENE_40, "body", analyzer).parse(query);
		logger.info(q.toString());
		int hitsPerPage = 10;
	    DirectoryReader reader = DirectoryReader.open(dir);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    
	    // Got results here
	    logger.info("# score docs = " + hits.length);
	    
	    ArrayList<AppType> results = new ArrayList<AppType>();
	    for(int i = 0; i<hits.length; ++i) {
	        int docId = hits[i].doc;
	        Document d = searcher.doc(docId);
	        results.add(new AppType(d.get("name"), d.get("body"), d.get("url")));
	        logger.info("collecting result");
	      }
	    return results;
	}
	
	public void addRFPToStore(RFPCollectionType doc) {
		repo.addDocsToStore(DocStore.RFP_COLLECTION,doc);
		logger.info("added RFP");
	}
	
	public void addRFPToUser(String userName, RFPCollectionType rfp) {
		// add this RFP to mongodb
		rfp.setUserName(userName);
		addRFPToStore(rfp);
	}
	
	
	public List<RFPCollectionType> getAllRFPsForUser(String userName) {
		return repo.getAllRFPsForUser(userName);
	}
	
	public ArrayList<AppType> getAppsForRFPbyId(String userName, String rfpName, boolean searchNow) throws ParseException, IOException, URISyntaxException {
		// If search now, then initiate search for this rfp id
		if (searchNow) {
			populateSearchResults(userName, rfpName);
		}
		return repo.getAppsForRFPbyId(rfpName);
	}
	
	/*
	 * Get the body of the RFP
	 * Make that the query
	 * Query the civic commons database using the 'search' method
	 * Get the results i.e the apps
	 */
	public boolean populateSearchResults(String userName, String rfpName) throws ParseException, IOException, URISyntaxException {
		// Fetch RFP from docstore
		RFPCollectionType rfp = repo.fetchRFPbyId(rfpName);
		// Search using that rfp as query in lucene indexer
		List<AppType> searchAppResults = search(rfp.getRfpBody());
		// Update RFP appList in docstore
		ArrayList<String> appList = new ArrayList<String>();
		for (AppType app : searchAppResults) {
			appList.add(app.getName());
		}
		rfp.setAppList(appList);
		// Put the updated RFP with the searchresults back into docstore
		addRFPToUser(userName, rfp);
		return true;
	}
}
