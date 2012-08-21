/********************* ARMY ANTS********************************************************************/
/********************* CONTRIBUTING CODE - CLOUD FOUNDRY    ****************************/
/********************* CIVIC COMMONS REPOSITORY*******************************************/
/********************************************************************************************************/

URL: http://army-ants.cloudfoundry.com/
Authors: 	RaviSundaram Rukmani,
				Sharan Munyal,
				Thirumurthi Pragadheeshwaran

Backend Processing	: Java, JSP
Frontend Interface		: Javascript, CSS, Twitter Bootstrap
Scraperwiki Script		: Python
Framework				: Spring MVC

----------------------------------------------------------------------------------------------------------------------------------
INTRODUCTION

This application allows user to upload RFP files for software the user is looking for. 
The application searches the Civic Commons repository for applications mathcing to the user's RFP
The user gets a list of mathcing software for the uploaded RFP

The applicaiton allows user to log in through the user's Twitter id

----------------------------------------------------------------------------------------------------------------------------------
PROJECT DESCRIPTION

SCRAPER WIKI SCRIPT: 
	It scrapes the civic commons web site for obtaining all the applications in civic commons repository
	The values obtained from the scraper wiki script are added to a data store
	The script runs periodically to update the civic commons data store
	
BACK END PROCESSING
	Three mongoDB data stores are maintained:
		1. Apptype:  <ID, AppName, AppDescription, AppURL>
			Holds the list of all Software in Civic commons repository along with its Description and URL
			ID  - hashcode(AppURL)
		2. RFPCollectionType: <ID, UserName, RFPName, RFPBody>
			Contains list of users logged into the applicaiton with a corresponding list of RFPs the user has uploaded
			ID - hashcode<UserName>
	The civic commons data is obtained as a JSON object. 
	The JSON object is parsed and the data obtained from civic commons web site are populated in Mongo DB.
	
	LUCENE indexer intelligently performs match between the description of the RFP with the description of the Softwares in Civic Commons repository. It makes use of TF*IDF to perform searches intelligently
	The Lucene indexer runs periodically to perform indexing and adds all the documents to Mongo DB docstore.
		
USER INTERFACE:
	The home pages requests user to log in through a twitter account
	Once the user logs the list of RFPs the user has previously uploaded gets listed.
	The user can upload an RFP file through the upload dialog box
	The uploaded RFP is parsed by the Lucene indexer and it is matched against the description of applications got from Civic commons data store
	The matching softwares are given to the user as output.
----------------------------------------------------------------------------------------------------------------------------------	