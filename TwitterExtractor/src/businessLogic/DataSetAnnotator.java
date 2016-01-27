package businessLogic;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import persistence.dao.HashtagDao;
import persistence.dao.ListDao;
import persistence.dao.TweetDao;
import persistence.dao.URLDao;
import persistence.dao.UserDao;
import persistence.entities.Hashtag;
import persistence.entities.Tweet;
import persistence.entities.URL;
import persistence.entities.UserAccount;

public class DataSetAnnotator {
	
	private TweetDao daoTweet;
	private UserDao daoUser;
	private HashtagDao daoHashtag;
	private URLDao daoURL;	
	private ListDao daoList;	
	private OWLOntology ontology;   
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private PrefixManager pm;
    private String ontologyURI;
	
	public DataSetAnnotator(String ontologyURI){
		daoTweet = new TweetDao(ontologyURI);
		daoUser = new UserDao(ontologyURI);
		daoHashtag = new HashtagDao(ontologyURI);
		daoURL = new URLDao(ontologyURI);	
		daoList = new ListDao(ontologyURI);
		
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();   
        this.ontologyURI = ontologyURI;
        pm = new DefaultPrefixManager(null, null,
                ontologyURI);
               
	
		File ontologyFile = new File("TwitterOntology.owl");
		try {
			ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public void createData(List<UserAccount> users){
//		
//		for(UserAccount user: users){	
//						
//			//Creating User
//			createUser(user);
//			
//			//Creating Tweets
//			createTweets(user.getTweets());
//			
//			//Creating Retweets
//			createRetweets(user.getScreenName(), user.getRetweets());
//			
//			//Creating Replies
//			createReplies(user.getScreenName(), user.getReplies());
//			
//			//Creating Favorites
//			createFavorites(user.getScreenName(), user.getFavorites());
//			
//			//Creating Lists
//			createLists(user.getScreenName(), user.getLists());
//			
//			//Creating List members
//			createListMembers(user.getLists());
//		}
//	}
	
	public void createUser(UserAccount user){		
			
        OWLDataProperty sName,urlP, loc, nFollowing, nFollowers, pName;
		OWLNamedIndividual newPage;
		OWLDataPropertyAssertionAxiom pagenameAssertion, URLPageAssertion, 
		numFollowingAssertion, numFollowersAssertion, screennameAssertion, locationAssertion;		
		OWLClass twitterAccount; 
		OWLClassAssertionAxiom twiterAccountClassAssertion;
		
		String screenname = "", pagename = "", urlpage = "", location = "";
        int numFollowing, numFollowers;		
        
        twitterAccount = factory.getOWLClass(":TwitterAccount", pm);
        screenname = user.getScreenName();
        pagename = user.getName();
        urlpage = user.getUrl();
        location = user.getLocation();
        numFollowing = user.getFriendsCount();
        numFollowers = user.getFollowersCount();
        
        newPage = factory.getOWLNamedIndividual(":"+screenname, pm);
        sName = factory.getOWLDataProperty(":screenname", pm);       	                 
        
        nFollowing = factory.getOWLDataProperty(":numPagesFollowing", pm);
        nFollowers = factory.getOWLDataProperty(":numFollowers", pm);
		
        twiterAccountClassAssertion = factory.getOWLClassAssertionAxiom(twitterAccount, newPage);
        
        // Getting properties from ontology
        screennameAssertion = factory.getOWLDataPropertyAssertionAxiom(sName, newPage, screenname);
        
        numFollowingAssertion = factory.getOWLDataPropertyAssertionAxiom(nFollowing, newPage,
                numFollowing);
        numFollowersAssertion = factory.getOWLDataPropertyAssertionAxiom(nFollowers, newPage,
                numFollowers);
		
        manager.addAxiom(ontology, twiterAccountClassAssertion);               
        manager.addAxiom(ontology, screennameAssertion);       
        manager.addAxiom(ontology, numFollowingAssertion);
        manager.addAxiom(ontology, numFollowersAssertion);
        
        if(pagename != null){
       	 pName = factory.getOWLDataProperty(":pagename", pm);
       	 pagenameAssertion = factory.getOWLDataPropertyAssertionAxiom(pName, newPage,
                    pagename);
       	 manager.addAxiom(ontology, pagenameAssertion);
       }
        
       if(urlpage != null){
    	   urlP = factory.getOWLDataProperty(":urlpage", pm);
    	   URLPageAssertion = factory.getOWLDataPropertyAssertionAxiom(urlP, newPage,
                   urlpage);
    	   manager.addAxiom(ontology, URLPageAssertion);
       }
       
       if(location != null){	                	
           loc = factory.getOWLDataProperty(":location", pm);
           locationAssertion = factory.getOWLDataPropertyAssertionAxiom(loc, newPage,
                   location);
           manager.addAxiom(ontology, locationAssertion);
       }   
		
        
        //Saving ontology
        File file = new File("TwitterOntology-Populated.owl");
        try {
			manager.saveOntology(ontology, IRI.create(file.toURI()));
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	public void createTweets(List<Tweet> tweets){
//				
//		for(Tweet tweet: tweets)
//			createTweet(tweet);
//		
//		File file = new File("TwitterOntology-Populated.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(file.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}     
//	}

//	private void createTweet(Tweet t){
//		
//		long id;		
//		String text, createdAt;   
//		
//		
//		UserAccount tweetPublisher;
//		OWLNamedIndividual tweet, twitterAccount;
//		OWLObjectProperty posts;
//        OWLDataProperty tex, cAt;
//        OWLClass tweetClass; 
//		OWLClassAssertionAxiom tweetClassAssertion;
//		OWLObjectPropertyAssertionAxiom postsAssertion;
//		OWLDataPropertyAssertionAxiom textAssertion, creationAssertion;
//		
//		id = t.getTweetID();
//		text = t.getText();
//		createdAt = t.getCreatedAt().toString();			
//		tweetPublisher = t.getTweetPublisher();
//		
//		//---------------------------------- Tweet's creation------------------------
//        
//        tweetClass = factory.getOWLClass(":Tweet", pm);                                
//        tweet = factory.getOWLNamedIndividual(":"+id, pm);
//        tweetClassAssertion = factory.getOWLClassAssertionAxiom(tweetClass, tweet);
//        tex = factory.getOWLDataProperty(":text", pm);               
//        posts = factory.getOWLObjectProperty(":posts", pm);            
//        twitterAccount = factory.getOWLNamedIndividual(":" + tweetPublisher.getScreenName(), pm);
//        cAt = factory.getOWLDataProperty("createdAt", pm);
//                        
//        textAssertion = factory.getOWLDataPropertyAssertionAxiom(tex, tweet,
//                text);
//        postsAssertion = factory.getOWLObjectPropertyAssertionAxiom(posts, twitterAccount,
//                tweet);          
//        creationAssertion = factory.getOWLDataPropertyAssertionAxiom(cAt, tweet, createdAt);
//                    
//        
//        manager.addAxiom(ontology, tweetClassAssertion);                
//        manager.addAxiom(ontology, textAssertion);
//        manager.addAxiom(ontology, postsAssertion);
//        manager.addAxiom(ontology, creationAssertion);
//        
//        //---------------------------------- Hashtags creation------------------------
//        
//        if(t.getHashtags().size() > 0)       	  
//    	    for(Hashtag h: t.getHashtags())
//    		    createHashtag(tweet, h.getHashtag());
//    	
//        //---------------------------------- URLs creation------------------------
//        
//        if(t.getURLs().size() > 0){
//        	for(URL u: t.getURLs()){
//        		createURL(tweet, u.getUrl());
//        	}
//        }
//        
//        //---------------------------------- Mentions creation------------------------
//		
//        if(t.getMentions().size() > 0)
//        	for(UserAccount user: t.getMentions())                 		
//        		createMention(tweet, user.getScreenName());
//        
//	}
//	
//	public void createRetweets(String screenname, List<Tweet> retweets){
//		
//		OWLObjectProperty rt;
//		OWLNamedIndividual retweet, twitterAccount;
//		OWLObjectPropertyAssertionAxiom retweetAssertion;
//		
//		rt = factory.getOWLObjectProperty(":retweets", pm);
//		twitterAccount = factory.getOWLNamedIndividual(":" + screenname, pm);
//		for(Tweet tweet: retweets){
//			retweet = factory.getOWLNamedIndividual(":" + tweet.getTweetID(), pm);
//			retweetAssertion = factory.getOWLObjectPropertyAssertionAxiom(rt, twitterAccount, retweet);
//			
//			manager.addAxiom(ontology, retweetAssertion);
//		}			
//		
//		File file = new File("TwitterOntology-Populated.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(file.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}     
//		
//		
//	}
//	
//	public void createReplies(String screenname, List<UserAccount> replies){
//		
//		OWLObjectProperty rp;
//		OWLNamedIndividual twitterAccount1, twitterAccount2;
//		OWLObjectPropertyAssertionAxiom repliesAssertion;
//		
//		rp = factory.getOWLObjectProperty(":replies", pm);
//		twitterAccount1 = factory.getOWLNamedIndividual(":" + screenname, pm);		
//		
//		for(UserAccount user: replies){
//			
//			twitterAccount2 = factory.getOWLNamedIndividual(":" + user.getScreenName(), pm);
//   		 	repliesAssertion = factory.getOWLObjectPropertyAssertionAxiom(rp, twitterAccount1,
//                    twitterAccount2);
//   		 	manager.addAxiom(ontology, repliesAssertion);	
//		}
//		
//		File file = new File("TwitterOntology-Populated.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(file.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		
//	}
//	
//	public void createFavorites(String screenname, List<Tweet> favorites){
//		
//		OWLObjectProperty fav;
//		OWLNamedIndividual tweet, twitterAccount;
//		OWLObjectPropertyAssertionAxiom favoriteAssertion;
//		
//		for(Tweet favorite: favorites){
//			createTweet(favorite);
//			
//			fav = factory.getOWLObjectProperty(":favorites", pm);
//			tweet = factory.getOWLNamedIndividual(":" + favorite.getTweetID(), pm);
//			twitterAccount = factory.getOWLNamedIndividual(":" + screenname, pm);
//			favoriteAssertion = factory.getOWLObjectPropertyAssertionAxiom(fav, twitterAccount, tweet);
//			
//			manager.addAxiom(ontology, favoriteAssertion);
//			
//		}
//		
//		File file = new File("TwitterOntology-Populated.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(file.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}     
//	}
//	
//	public void createLists(String screenname, List<persistence.entities.List> lists){		
//		
//		for(persistence.entities.List list: lists)
//			createList(screenname,list);
//		
//		File file = new File("TwitterOntology-Populated.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(file.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}     
//	}
//	
//	private void createList(String listOwner, persistence.entities.List l){
//		
//		OWLNamedIndividual list, twitterAccount;
//		OWLObjectProperty owns;		
//		OWLDataProperty name, description;
//		OWLClass listClass;
//        
//		OWLClassAssertionAxiom listClassAssertion;
//		OWLObjectPropertyAssertionAxiom ownsAssertion;
//		OWLDataPropertyAssertionAxiom nameAssertion, descriptionAssertion;
//		
//		long id;
//        String listName, listDescription;
//        
//        id = l.getIDList();
//        listName = l.getName();
//        listDescription = l.getDescription();
//        
//        
//        listClass = factory.getOWLClass(":List", pm);
//        list = factory.getOWLNamedIndividual(":"+id, pm);
//        twitterAccount = factory.getOWLNamedIndividual(":"+ listOwner, pm);
//        owns = factory.getOWLObjectProperty(":owns", pm);
//        name = factory.getOWLDataProperty(":name", pm);
//        
//        listClassAssertion = factory.getOWLClassAssertionAxiom(listClass, list);
//        nameAssertion = factory.getOWLDataPropertyAssertionAxiom(name, list, listName);                
//        ownsAssertion = factory.getOWLObjectPropertyAssertionAxiom(owns, twitterAccount,
//                list);           
//        
//        manager.addAxiom(ontology, nameAssertion);                
//        manager.addAxiom(ontology, ownsAssertion);
//        manager.addAxiom(ontology, listClassAssertion);
//        
//        if(!listDescription.equalsIgnoreCase("null")){                	
//        	description = factory.getOWLDataProperty(":description", pm);
//        	descriptionAssertion = factory.getOWLDataPropertyAssertionAxiom(description, list, listDescription);
//        	manager.addAxiom(ontology, descriptionAssertion);
//        }
//		
//		
//	}
//	
//	public void createListMembers(List<persistence.entities.List> lists){
//		
//		OWLNamedIndividual list, twitterAccount;
//		OWLObjectProperty hasMember;
//        
//		OWLObjectPropertyAssertionAxiom hasMemberAssertion;
//		
//        long id;
//        
//        hasMember = factory.getOWLObjectProperty(":hasMember", pm);
//		for(persistence.entities.List l: lists){
//			
//			id = l.getIDList();
//			list = factory.getOWLNamedIndividual(":" + id, pm);
//			for(UserAccount member: l.getListMembers()){
//				
//	            twitterAccount = factory.getOWLNamedIndividual(":" + member.getScreenName(), pm);
//	            hasMemberAssertion = factory.getOWLObjectPropertyAssertionAxiom(hasMember, list,
//	                    twitterAccount);
//	            manager.addAxiom(ontology, hasMemberAssertion);
//			}			
//			
//		}		
//		
//		File file = new File("TwitterOntology-Populated.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(file.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}    
//		
//	}
//	
//	private void createHashtag(OWLNamedIndividual tweet, String hashtagname){
//		
//		OWLDataProperty hashtag;
//		OWLDataPropertyAssertionAxiom hashtagAssertion;
//		
//		hashtag = factory.getOWLDataProperty(":hashtag", pm);		  
//		hashtagAssertion = factory.getOWLDataPropertyAssertionAxiom(hashtag, tweet, hashtagname);    	
//		
//		manager.addAxiom(ontology, hashtagAssertion);
//	}
//	
//	private void createURL(OWLNamedIndividual tweet, String urlname){
//		
//		OWLDataProperty url;
//		OWLDataPropertyAssertionAxiom urlAssertion;
//		
//		url = factory.getOWLDataProperty(":url", pm);
//		urlAssertion = factory.getOWLDataPropertyAssertionAxiom(url, tweet, urlname);
//		
//     	manager.addAxiom(ontology, urlAssertion);
//		
//	}
//	
//	private void createMention(OWLNamedIndividual tweet, String user){
//		
//		OWLNamedIndividual twitterAccount;
//		OWLObjectProperty mentions;
//		OWLObjectPropertyAssertionAxiom mentionsAssertion;
//		
//		twitterAccount = factory.getOWLNamedIndividual(":" + user, pm);
//		mentions = factory.getOWLObjectProperty(":mentions", pm);		
//		mentionsAssertion = factory.getOWLObjectPropertyAssertionAxiom(mentions,tweet,twitterAccount);
//		
//      	manager.addAxiom(ontology, mentionsAssertion);
//		
//		
//	}
//	
	

}
