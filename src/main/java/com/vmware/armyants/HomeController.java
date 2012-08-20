package com.vmware.armyants;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private LuceneIndexer searchEngine;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is "+ locale.toString());
		String environmentName = (System.getenv("VCAP_APPLICATION") != null) ? "Cloud" : "Local";
		model.addAttribute("environmentName", environmentName);
		return "home";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void loginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	    Twitter twitter = new TwitterFactory().getInstance();
        request.getSession().setAttribute("twitter", twitter);
        try {
            StringBuffer callbackURL = request.getRequestURL();
            int index = callbackURL.lastIndexOf("/");
            callbackURL.replace(index, callbackURL.length(), "").append("/callback");

            RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
            request.getSession().setAttribute("requestToken", requestToken);
            response.sendRedirect(requestToken.getAuthenticationURL());
            logger.info("redirecting" + requestToken.getAuthenticationURL());
        } catch (TwitterException e) {
        	logger.info(e.toString());
            throw new ServletException(e);
        } catch (IOException e) {
			logger.info(e.toString());
		}
	}
	
	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	public void callbackPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        
        try {
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
            String userName = twitter.verifyCredentials().getName();
            request.getSession().removeAttribute("requestToken");
            logger.info(userName);
            response.setHeader("name", userName);
            response.sendRedirect(request.getContextPath() + "/dashboard");	
            return;
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String dashboardPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws IllegalStateException, TwitterException {
		// Get username
		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		String userName = twitter.getScreenName();
		
		// Collect all RFPs this user has uploaded and display them
		List<RFPCollectionType> rfpsForUser = searchEngine.getAllRFPsForUser(userName);
		
		model.put("rfpcollection",rfpsForUser);
		logger.info(userName);
		return "dashboard";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		response.sendRedirect(request.getContextPath()+ "/");
		return "home";
	}

	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
    public void attachReceipt(HttpServletRequest request, 
    		@RequestParam("upload_file") MultipartFile file,HttpServletResponse response) throws IOException, IllegalStateException, TwitterException {
		// Get user name for upload
		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		String userName = twitter.getScreenName();
		// Get file details
		String filename = file.getOriginalFilename();
		String body = new String(file.getBytes());
		logger.info(body);
		// Store to mongodb here
		searchEngine.addRFPToUser(userName, new RFPCollectionType(userName, filename, body));
        response.sendRedirect(request.getContextPath()+ "/dashboard");
    }
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws IllegalStateException, TwitterException, ParseException, IOException, URISyntaxException {
		// Get username
		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		String userName = twitter.getScreenName();

		searchEngine.indexDocs(DocStore.CIVIC_COMMONS_COLLECTION);
		// Collect all RFPs this user has uploaded and display them
		List<RFPCollectionType> rfpsForUser = searchEngine.getAllRFPsForUser(userName);
		
		ArrayList<AppType> results = new ArrayList<AppType>();
		// Collect all searchresults for user
		for (RFPCollectionType rfp : rfpsForUser) {
			for( AppType app : searchEngine.getAppsForRFPbyId(userName, rfp.getRfpName(), true)) {
				results.add(app);
			}
		}
		model.put("search_results",	results);
		return "search";
	}
}
