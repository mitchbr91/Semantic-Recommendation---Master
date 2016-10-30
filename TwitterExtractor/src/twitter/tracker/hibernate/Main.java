package twitter.tracker.hibernate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import inference.InferenceManager;

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

import persistence.dao.hibernate.UserDao;
import persistence.entities.hibernate.HibernateUtil;
import persistence.entities.hibernate.UserAccount;
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
			users.add(twitter.showUser("earlbittencourt"));
			users.add(twitter.showUser("gbrlamota"));
			users.add(twitter.showUser("DiegoH2222"));
			users.add(twitter.showUser("freddurao"));
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        //extractor.extractTweets();
        //annotator.createData();      
        
        Map<String, List<TwitterAccount>> inferedUsers = null;
        Map<String, List<TwitterAccount>> usersWithNoInteractions = null;
        Map<String, java.util.List<TwitterAccount>> usersToBeUnfollowed = null;
        inferedUsers = inference.infer();        
        usersWithNoInteractions = inference.extractUsersWithNoInteractions(inferedUsers);
        usersToBeUnfollowed = similarityManager.calculateSimilarity(usersWithNoInteractions, 15);
        
        List<TwitterAccount> accounts = null;
        BufferedWriter buffWrite = null;	      
        File file;
        UserDao daoUser = new UserDao();
        UserAccount targetUser;
        for(String key: usersToBeUnfollowed.keySet()){
           					
        	accounts = usersToBeUnfollowed.get(key);
			targetUser = daoUser.getUserByScreenname(key, false);			
			
			for(TwitterAccount followee: accounts){									
				daoUser.insertSemanticRecommendation(targetUser.getIDUser(),
						daoUser.getUserByScreenname(followee.getName(), false));
				
			}		
			
        }
        
        
        System.out.println("Semantic Recommendation");           
     
        for(String key: usersToBeUnfollowed.keySet()){
        	
        	try {
        		file = new File("S-"+key+".txt");
				buffWrite = new BufferedWriter(new FileWriter(file));				
	        	accounts = usersToBeUnfollowed.get(key);
				System.out.println("Target user: " + key + "Size: " + accounts.size());
				
				for(TwitterAccount followee: accounts){
					System.out.println("Unfollow: " + followee.getName() + "Similarity: " + followee.getCosineSimilarity());					
					buffWrite.write(followee.getName() + " - " + followee.getCosineSimilarity());
					buffWrite.newLine();					
				}
				
				buffWrite.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        Map<String, List<TwitterAccount>> network = inference.getUsersNetwork();
        usersToBeUnfollowed = similarityManager.calculateSimilarity(network, 15);        
        
        System.out.println("Regular Recommendation");
        
        UserAccount user = null;
        for(String key: usersToBeUnfollowed.keySet()){
				
        	accounts = usersToBeUnfollowed.get(key);
			targetUser = daoUser.getUserByScreenname(key, false);			
			
			for(TwitterAccount followee: accounts){
				
				user = daoUser.getUserByScreenname(followee.getName(), false);
				
				System.out.println(targetUser + " - " + user.getScreenName());
				daoUser.insertRegularRecommendation(targetUser.getIDUser(),
						user);
				
			}		
			
        }	
        
        accounts = null;
        for(String key: usersToBeUnfollowed.keySet()){
        	
        	try {
        		file = new File("R-"+key+".txt");
				buffWrite = new BufferedWriter(new FileWriter(file));				
	        	accounts = usersToBeUnfollowed.get(key);
				System.out.println("Target user: " + key + "Size: " + accounts.size());
				
				for(TwitterAccount followee: accounts){
					System.out.println("Unfollow: " + followee.getName() + "Similarity: " + followee.getCosineSimilarity());					
					buffWrite.write(followee.getName() + " - " + followee.getCosineSimilarity());
					buffWrite.newLine();					
				}
				
				buffWrite.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        System.out.println(new java.util.Date());
		HibernateUtil.closeSessionFactory();
		
//		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
//		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
//		SWRLVariable t =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t"));
//		SWRLVariable texto =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?texto"));
//		
//		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
//		
//		OWLObjectProperty retweets = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#retweets"));
//        OWLObjectProperty posts = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#posts"));
//        OWLObjectProperty retweetsRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#retweetsRule"));
//        OWLDataProperty tweetText = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#text"));
//        
//		
//        SWRLDataPropertyAtom textAtom = factory.getSWRLDataPropertyAtom(tweetText, t, texto);
//        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(retweets, u1, t);
//        SWRLObjectPropertyAtom propAtom2 = factory.getSWRLObjectPropertyAtom(posts, u2, t);
//        SWRLObjectPropertyAtom retweetsRuleAtom = factory.getSWRLObjectPropertyAtom(retweetsRule, u1, u2);
//        
//        List<SWRLDArgument> ags = new ArrayList<SWRLDArgument>();
//        
//        ags.add(textAtom.getSecondArgument());
//        ags.add(textAtom.getSecondArgument());
//        
//        SWRLBuiltInAtom builtinAton = factory.getSWRLBuiltInAtom(SWRLBuiltInsVocabulary.CONTAINS_IGNORE_CASE.getIRI(), ags);
//        
//        preconditions.add(textAtom);
//        preconditions.add(propAtom);
//        preconditions.add(propAtom2);
//        preconditions.add(builtinAton);
//        
//        SWRLRule rule2 = factory.getSWRLRule(preconditions,
//                Collections.singleton(retweetsRuleAtom));
//
//        manager.applyChange(new AddAxiom(ontology, rule2));        
//       
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}   
   
	}
}
