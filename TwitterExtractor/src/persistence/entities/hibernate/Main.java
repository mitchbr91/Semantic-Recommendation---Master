package persistence.entities.hibernate;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import inference.InferenceManager;
import inference.Metrics;
import inference.PelletInference;
import inference.RulesManager;
import persistence.dao.hibernate.HashtagDao;
import persistence.dao.hibernate.ListDao;
import persistence.dao.hibernate.TweetDao;
import persistence.dao.hibernate.URLDao;
import persistence.dao.hibernate.UserDao;
import persistence.entities.hibernate.Tweet;
import similarity.SimilarityManager;
import twitter.tracker.hibernate.DataSetAnnotator;
import twitter.tracker.hibernate.DataSetExtractor;
import twitter.tracker.hibernate.TwitterAccount;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserMentionEntity;

public class Main {
	
	public static void main(String args[]){
		     
        UserDao daoUser = new UserDao();
        ListDao daoList = new ListDao();
        TweetDao daoTweet = new TweetDao();
       URLDao daoURL = new URLDao();
       HashtagDao daoHashtag = new HashtagDao();
       
    
        
        Twitter twitter = new TwitterFactory().getInstance();
        
        twitter.addRateLimitStatusListener(new RateLimitStatusListener() {
    	    @Override
    	    public void onRateLimitStatus(RateLimitStatusEvent event ) {    	       
    	    }

    	    @Override
    	    public void onRateLimitReached(RateLimitStatusEvent event ) {

    	        try {
   	        		System.out.println ("Hora de dormir");
   	        		Thread.sleep(event.getRateLimitStatus().getSecondsUntilReset()*1000); } 
   	       		catch (InterruptedException ex) {  
   	       			System.out.println ("Puxa, estava dormindo! Você me acordou");    	       		
   	       		}
    	    }
    	} );
        
        java.util.List<User> twitterList = new ArrayList<User>();
        DataSetExtractor extractor = new DataSetExtractor();
        RateLimitStatus rateStatus;
        
        Paging page = new Paging();
        page.setCount(200);
        
       
        try {
        	
        	//twitterList.add(twitter.showUser("ray_fashion68"));        	    	
        	        	
			twitterList.add(twitter.showUser("Jukelmer"));
			twitterList.add(twitter.showUser("kamila__sg"));
			twitterList.add(twitter.showUser("valsilva111"));
			twitterList.add(twitter.showUser("marcelolinz"));
			twitterList.add(twitter.showUser("SimoneGhetti"));
			
			//extractor.extractUsersAndFollowees(twitterList);
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
               
        //extractor.extractTweets();


        java.util.List<String> users = new ArrayList<String>();
        
        
        for(UserAccount user: daoUser.listUsers(true, false)){
        	
        	if(!users.contains(user.getScreenName()))
        		users.add(user.getScreenName());
        		        	
//        	for(UserAccount followee: user.getFollowees()){
//        		
//        		if(!users.contains(followee))
//        			users.add(followee);
//        	}
       	
        	
        }        
       
        
        System.out.println("Users list size: " + users.size());
        
        String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology#";
        
      
        DataSetAnnotator annotator = new DataSetAnnotator(ontologyIRI);

   
        //annotator.createMentionOntology();
//        Map<String, List<TwitterAccount>> targetUserSet;
//        InferenceManager manager = new InferenceManager();
//        
//        targetUserSet = manager.infer(users);
//        
//        System.out.println("Inference: " + targetUserSet);
        
        Metrics metrics = new Metrics();
        //metrics.calculateMetrics(7, targetUserSet);
        
        SimilarityManager simManager = new SimilarityManager();
        //simManager.calculateSimilarity();
        //metrics.calculateMetrics(7, simManager.calculateSimilarity());
        
      
        File ontologyFile = new File("TwitterOntology-Populated.owl");

        
//        PelletInference pi = new PelletInference(ontologyIRI);
//        
//        pi.run();
        
      HibernateUtil.closeSessionFactory(); 
       
	}

}
