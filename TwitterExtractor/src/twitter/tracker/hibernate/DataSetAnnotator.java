package twitter.tracker.hibernate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
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
import persistence.entities.hibernate.Hashtag;
import persistence.entities.hibernate.Tweet;
import persistence.entities.hibernate.URL;
import persistence.entities.hibernate.UserAccount;

public class DataSetAnnotator {

	private OWLOntology ontology;   
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private PrefixManager pm;
    private String ontologyURI;
    private File ontologyFile;
    private UserDao daoUser;
	
	public DataSetAnnotator(String ontologyURI){		
		
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();     
        this.ontologyURI = ontologyURI;
        daoUser = new UserDao();
        pm = new DefaultPrefixManager(null, null,
                ontologyURI);              
	
		ontologyFile = new File("TwitterOntology.owl");
		
		
	}
	
	public void createData(){
		
		int i = 1;
		File file;
		
		List<UserAccount> followees = new ArrayList<UserAccount>();
		
				
		for(UserAccount targetUser: daoUser.listUsers(true, true)){
			
			try {
				ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);				
			} catch (OWLOntologyCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Setting followees targetUser flag to false because some target users can be as well followees
			for(UserAccount followee: targetUser.getFollowees()){
				followee.setIsTargetUser(false);
				followees.add(followee);
			}
			
			followees = targetUser.getFollowees();		
			
			System.out.println(i++ + " - Target user: " + targetUser.getScreenName());	
			//System.out.println(i++ + " - " + targetUser.getScreenName());			
			createData(targetUser);
			
			for(UserAccount followee: followees){
				
				System.out.println(i++ + " - " + followee.getScreenName() + " - Target user: " + followee.isTargetUser());
				createData(followee);
			}			
			
			
			file = new File("TwitterOntology-" + targetUser.getScreenName() + ".owl");
	        try {
				manager.saveOntology(ontology, IRI.create(file.toURI()));
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        manager.removeOntology(ontology);
			
		}
	
	}
	
	private void createData(UserAccount user){			
		
		//Creating User
		createUser(user);
		
		//Creating Tweets
		createTweets(user.getTweets());
		
		if(user.isTargetUser()){
			
			//Creating Retweets
			createRetweets(user.getScreenName(), user.getRetweets());
			
			//Creating Replies
			createReplies(user.getScreenName(), user.getReplies());
			
			//Creating Favorites
			createFavorites(user.getScreenName(), user.getFavorites());
			
			//Creating Lists
			createLists(user.getScreenName(), user.getLists());
			
			//Creating List members
			createListMembers(user.getLists());
		}	
	}
	
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
          
		
	}
	
	public void createTweets(List<Tweet> tweets){
				
		for(Tweet tweet: tweets)
			createTweet(tweet);
				 
	}

	private void createTweet(Tweet t){
		
		long id;		
		String text, createdAt;   
				
		
		OWLNamedIndividual tweet, twitterAccount;
		OWLObjectProperty posts;
        OWLDataProperty tex, cAt;
        OWLClass tweetClass; 
		OWLClassAssertionAxiom tweetClassAssertion;
		OWLObjectPropertyAssertionAxiom postsAssertion;
		OWLDataPropertyAssertionAxiom textAssertion, creationAssertion;
		
		id = t.getTweetID();
		
		if(t.getText() != null){
			text = t.getText();
		}else{
			text = "";
		}
		
		
		if(t.getCreatedAt() != null){
			createdAt = t.getCreatedAt().toString();
		}else{
			createdAt = "";
		}				
		
		
		//---------------------------------- Tweet's creation------------------------
        
        tweetClass = factory.getOWLClass(":Tweet", pm);                                
        tweet = factory.getOWLNamedIndividual(":"+id, pm);
        tweetClassAssertion = factory.getOWLClassAssertionAxiom(tweetClass, tweet);
        
        tex = factory.getOWLDataProperty(":text", pm);               
        posts = factory.getOWLObjectProperty(":posts", pm);            
        twitterAccount = factory.getOWLNamedIndividual(IRI.create(ontologyURI + t.getTweetPublisher().getScreenName()));
        cAt = factory.getOWLDataProperty("createdAt", pm);
                        
        textAssertion = factory.getOWLDataPropertyAssertionAxiom(tex, tweet,
                text);
                
        creationAssertion = factory.getOWLDataPropertyAssertionAxiom(cAt, tweet, createdAt);
        postsAssertion = factory.getOWLObjectPropertyAssertionAxiom(posts, twitterAccount,
                tweet);              
        
        manager.addAxiom(ontology, tweetClassAssertion);                
        manager.addAxiom(ontology, textAssertion);
        manager.addAxiom(ontology, postsAssertion);
        manager.addAxiom(ontology, creationAssertion);
        
        //---------------------------------- Hashtags creation------------------------
        
        if(t.getHashtags().size() > 0)       	  
    	    for(Hashtag h: t.getHashtags())
    		    createHashtag(tweet, h.getHashtag());
    	
        //---------------------------------- URLs creation------------------------
        
        if(t.getURLs().size() > 0){
        	for(URL u: t.getURLs()){
        		createURL(tweet, u.getUrl());
        	}
        }
        
        //---------------------------------- Mentions creation------------------------
		
        if(t.getMentions().size() > 0)
        	for(UserAccount user: t.getMentions())                 		
        		createMention(tweet, user);
        
	}
	
	public void createRetweets(String screenname, List<Tweet> retweets){
		
		OWLObjectProperty rt;
		OWLNamedIndividual retweet, twitterAccount;
		OWLObjectPropertyAssertionAxiom retweetAssertion;
		
		rt = factory.getOWLObjectProperty(":retweets", pm);		
		twitterAccount = factory.getOWLNamedIndividual(IRI.create(ontologyURI + screenname));
		
		
		for(Tweet tweet: retweets){
			
			if(!ontology.containsIndividualInSignature(IRI.create(ontologyURI + tweet.getTweetPublisher().getScreenName())))
				createUser(tweet.getTweetPublisher());
			
			createTweet(tweet);
			
			retweet = factory.getOWLNamedIndividual(IRI.create(ontologyURI + tweet.getTweetID()));
			retweetAssertion = factory.getOWLObjectPropertyAssertionAxiom(rt, twitterAccount, retweet);
			
			manager.addAxiom(ontology, retweetAssertion);
		}			
		
		
	}
	
	public void createReplies(String screenname, List<Tweet> replies){
		
		OWLObjectProperty rp;
		OWLNamedIndividual twitterAccount, tweet;
		OWLObjectPropertyAssertionAxiom repliesAssertion;
		
		rp = factory.getOWLObjectProperty(":replies", pm);
		twitterAccount = factory.getOWLNamedIndividual(IRI.create(ontologyURI + screenname));	
		
			
		for(Tweet reply: replies){
			
			if(!ontology.containsIndividualInSignature(IRI.create(ontologyURI + reply.getTweetPublisher().getScreenName())))
				createUser(reply.getTweetPublisher());
			
			createTweet(reply);
			
			//tweet = factory.getOWLNamedIndividual(":" + reply.getTweetID(), pm);
			tweet = factory.getOWLNamedIndividual(IRI.create(ontologyURI + reply.getTweetID()));
   		 	repliesAssertion = factory.getOWLObjectPropertyAssertionAxiom(rp, twitterAccount,
                    tweet);
   		 	manager.addAxiom(ontology, repliesAssertion);	
		}
		
	}
	
	public void createFavorites(String screenname, List<Tweet> favorites){
		
		OWLObjectProperty fav;
		OWLNamedIndividual tweet, twitterAccount;
		OWLObjectPropertyAssertionAxiom favoriteAssertion;
		
		twitterAccount = factory.getOWLNamedIndividual(IRI.create(ontologyURI + screenname));
		
		
		for(Tweet favorite: favorites){
		
			if(!ontology.containsIndividualInSignature(IRI.create(ontologyURI + favorite.getTweetPublisher().getScreenName())))
				createUser(favorite.getTweetPublisher());
			
			createTweet(favorite);
			
			fav = factory.getOWLObjectProperty(":favorites", pm);
			tweet = factory.getOWLNamedIndividual(IRI.create(ontologyURI + favorite.getTweetID()));			
			favoriteAssertion = factory.getOWLObjectPropertyAssertionAxiom(fav, twitterAccount, tweet);
			
			manager.addAxiom(ontology, favoriteAssertion);
			
		}
		
	}
	
	public void createLists(String screenname, List<persistence.entities.hibernate.List> lists){		
		
		for(persistence.entities.hibernate.List list: lists)
			createList(screenname,list);
		  
	}
	
	private void createList(String listOwner, persistence.entities.hibernate.List l){
		
		OWLNamedIndividual list, twitterAccount;
		OWLObjectProperty owns;		
		OWLDataProperty name, description;
		OWLClass listClass;
        
		OWLClassAssertionAxiom listClassAssertion;
		OWLObjectPropertyAssertionAxiom ownsAssertion;
		OWLDataPropertyAssertionAxiom nameAssertion, descriptionAssertion;
		
		long id;
        String listName, listDescription;
        
        id = l.getIDList();
        listName = l.getName();
        listDescription = l.getDescription();
        
        
        listClass = factory.getOWLClass(":List", pm);
        list = factory.getOWLNamedIndividual(":"+id, pm);
        twitterAccount = factory.getOWLNamedIndividual(":"+ listOwner, pm);
        owns = factory.getOWLObjectProperty(":owns", pm);
        name = factory.getOWLDataProperty(":name", pm);
        
        listClassAssertion = factory.getOWLClassAssertionAxiom(listClass, list);
        nameAssertion = factory.getOWLDataPropertyAssertionAxiom(name, list, listName);                
        ownsAssertion = factory.getOWLObjectPropertyAssertionAxiom(owns, twitterAccount,
                list);           
        
        manager.addAxiom(ontology, nameAssertion);                
        manager.addAxiom(ontology, ownsAssertion);
        manager.addAxiom(ontology, listClassAssertion);
        
        if(!listDescription.equalsIgnoreCase("null")){                	
        	description = factory.getOWLDataProperty(":description", pm);
        	descriptionAssertion = factory.getOWLDataPropertyAssertionAxiom(description, list, listDescription);
        	manager.addAxiom(ontology, descriptionAssertion);
        }
		
		
	}
	
	public void createListMembers(List<persistence.entities.hibernate.List> lists){
		
		OWLNamedIndividual list, twitterAccount;
		OWLObjectProperty hasMember;
        
		OWLObjectPropertyAssertionAxiom hasMemberAssertion;
		
        long id;
        
        hasMember = factory.getOWLObjectProperty(":hasMember", pm);
		for(persistence.entities.hibernate.List l: lists){
			
			id = l.getIDList();
			list = factory.getOWLNamedIndividual(":" + id, pm);
			for(UserAccount member: l.getListMembers()){
				
	            twitterAccount = factory.getOWLNamedIndividual(":" + member.getScreenName(), pm);
	            hasMemberAssertion = factory.getOWLObjectPropertyAssertionAxiom(hasMember, list,
	                    twitterAccount);
	            manager.addAxiom(ontology, hasMemberAssertion);
			}			
			
		}		
		
		
		
	}
	
	private void createHashtag(OWLNamedIndividual tweet, String hashtagname){
		
		OWLDataProperty hashtag;
		OWLDataPropertyAssertionAxiom hashtagAssertion;
		
		hashtag = factory.getOWLDataProperty(":hashtag", pm);		  
		hashtagAssertion = factory.getOWLDataPropertyAssertionAxiom(hashtag, tweet, hashtagname);    	
		
		manager.addAxiom(ontology, hashtagAssertion);
	}
	
	private void createURL(OWLNamedIndividual tweet, String urlname){
		
		OWLDataProperty url;
		OWLDataPropertyAssertionAxiom urlAssertion;
		
		url = factory.getOWLDataProperty(":url", pm);
		urlAssertion = factory.getOWLDataPropertyAssertionAxiom(url, tweet, urlname);
		
     	manager.addAxiom(ontology, urlAssertion);
		
	}
	
	private void createMention(OWLNamedIndividual tweet, UserAccount user){
		
		OWLNamedIndividual twitterAccount;
		OWLObjectProperty mentions;
		OWLObjectPropertyAssertionAxiom mentionsAssertion;
		
		if(!ontology.containsIndividualInSignature(IRI.create(ontologyURI + user.getScreenName())))
			createUser(user);
		
		twitterAccount = factory.getOWLNamedIndividual(IRI.create(ontologyURI + user.getScreenName()));
		mentions = factory.getOWLObjectProperty(":mentions", pm);		
		mentionsAssertion = factory.getOWLObjectPropertyAssertionAxiom(mentions,tweet,twitterAccount);
		
      	manager.addAxiom(ontology, mentionsAssertion);
		
		
	}

	
}
