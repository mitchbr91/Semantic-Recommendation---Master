package twitter.tracker.hibernate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import inference.InferenceManager;
import inference.Metrics;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.SWRLBuiltInsVocabulary;

import persistence.dao.hibernate.HashtagDao;
import persistence.dao.hibernate.UserDao;
import persistence.entities.hibernate.Hashtag;
import persistence.entities.hibernate.HibernateUtil;
import persistence.entities.hibernate.RegularRecommendation;
import persistence.entities.hibernate.SemanticRecommendation;
import persistence.entities.hibernate.UserAccount;
import persistence.entities.hibernate.UserInference;
import similarity.SimilarityManager;
import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class Main {

	public static void main(String args[]){
		
		OWLOntology ontology = null;   
	    OWLOntologyManager manager;
	    OWLDataFactory factory;
	    PrefixManager pm;
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology#";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
        pm = new DefaultPrefixManager(null, null,
                ontologyIRI);

        DataSetExtractor extractor = new DataSetExtractor();      
		DataSetAnnotator annotator = new DataSetAnnotator(ontologyIRI);
//		
//		annotator.createData();
//		
		InferenceManager inference = new InferenceManager();
		SimilarityManager similarityManager = new SimilarityManager();
//		
//		similarityManager.calculateSimilarity(inference.extractUsersWithNoInteractions(inference.infer()));
        
        Twitter twitter = new TwitterFactory().getInstance();        
		        
        Paging page = new Paging(200);
             
        List<User> users = new ArrayList<User>();
        
        try {			
//			users.add(twitter.showUser("earlbittencourt"));
//			users.add(twitter.showUser("gbrlamota"));
//			users.add(twitter.showUser("DiegoH2222"));
//			users.add(twitter.showUser("freddurao"));
        	users.add(twitter.showUser("exp_mestrado1"));
        	//users.add(twitter.showUser("exp_mestrado2"));
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //extractor.extractTweets();
        //extractor.extractData(users);
        //extractor.extractTweets();
        //annotator.createData();
        
        double cosineS = 0.4757457;
        UserDao daoUser = new UserDao();
        HashtagDao daoHashtag = new HashtagDao();
         
        Map<String, List<TwitterAccount>> inferedUsers = null;
        Map<String, List<UserAccount>> usersWithNoInteractions = null;
        Map<String, java.util.List<TwitterAccount>> usersToBeUnfollowed = null;
        //inferedUsers = inference.infer();   
//        inference.extractUsersWithNoInteractions();
//        
        Metrics metrics = new Metrics();
        //daoUser.readUnfollows();
        
        System.out.println("Semantic: ");
        metrics.calculateMetrics(0, true);
        System.out.println("Regular: ");
        metrics.calculateMetrics(0, false);

//        for(UserAccount user: daoUser.listUsers(true, false)){
//        	System.out.println("User: " + user.getScreenName());
//        	
//        	for(SemanticRecommendation sr: user.getSemanticRecommendations()){
//        		System.out.println("Recommendation: " + sr.getRecommendation().getScreenName());
//        	}
//        }
               
		HibernateUtil.closeSessionFactory();

   
	}
}
