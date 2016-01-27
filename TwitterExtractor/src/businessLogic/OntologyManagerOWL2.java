package businessLogic;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
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
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import com.clarkparsia.owlapi.explanation.DefaultExplanationGenerator;
import com.clarkparsia.owlapi.explanation.util.SilentExplanationProgressMonitor;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;


public class OntologyManagerOWL2 {
	
	private String line;
    private BufferedReader br = null;
    private StringTokenizer st;
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private PrefixManager pm;
    private OWLOntology ontology;
    private String ontologyURI;
    
    
	public OntologyManagerOWL2(String ontologyURI){
		
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
		
		
				
//		createPages();
//		createTweets();		
//		createList();
//		createListMembers();
	}

	public void createData(){
		createPages();
		createTweets();
		createFavorites();
		createList();
		createListMembers();		
	}
	
	private void createPages(){
		
		String pageFollowing = "", screenname = "", pagename = "",
                urlpage = "", location = "", numFollowing = "", numFollowers = "";		
			
	
        OWLObjectProperty follows = null;
        OWLDataProperty sName,urlP, loc, nFollowing, nFollowers, pName;
		OWLNamedIndividual pFollowing = null, newPage;
		OWLDataPropertyAssertionAxiom pagenameAssertion, URLPageAssertion, 
		numFollowingAssertion, numFollowersAssertion, screennameAssertion, locationAssertion;
		OWLObjectPropertyAssertionAxiom followsAssertion;
		OWLClass twitterAccount; 
		OWLClassAssertionAxiom twiterAccountClassAssertion;
		
		 try {
			br = new BufferedReader(new FileReader("dataset-twitter"));
			line = br.readLine();
			
			int i = 1;
		    int totalTokens = 0;
		    while (line != null) {
		    
		    	    st = new StringTokenizer(line, "¬");
		    	    twitterAccount = factory.getOWLClass(":TwitterAccount", pm);
	               
	                screenname = (String) st.nextElement();
	                
	                pagename = (String) st.nextElement();	                
	               
	                urlpage = (String) st.nextElement();
	                location = (String) st.nextElement();
	                numFollowing =  (String) st.nextElement();
	                numFollowers = (String) st.nextElement();
	                         
	                
	                newPage = factory.getOWLNamedIndividual(":"+screenname, pm);
	                sName = factory.getOWLDataProperty(":screenname", pm);
	               	                 
	                urlP = factory.getOWLDataProperty(":urlpage", pm);
	                nFollowing = factory.getOWLDataProperty(":numPagesFollowing", pm);
	                nFollowers = factory.getOWLDataProperty(":numFollowers", pm);
	             	                
	                twiterAccountClassAssertion = factory.getOWLClassAssertionAxiom(twitterAccount, newPage);
	                
	                screennameAssertion = factory.getOWLDataPropertyAssertionAxiom(sName, newPage, screenname);
	                URLPageAssertion = factory.getOWLDataPropertyAssertionAxiom(urlP, newPage,
                            urlpage);
	                numFollowingAssertion = factory.getOWLDataPropertyAssertionAxiom(nFollowing, newPage,
                            numFollowing);
	                numFollowersAssertion = factory.getOWLDataPropertyAssertionAxiom(nFollowers, newPage,
                            numFollowers);	                
	                                
	                manager.addAxiom(ontology, twiterAccountClassAssertion);               
	                manager.addAxiom(ontology, screennameAssertion);
	                manager.addAxiom(ontology, URLPageAssertion);
	                manager.addAxiom(ontology, numFollowingAssertion);
	                manager.addAxiom(ontology, numFollowersAssertion);
	                	
	                
	                if(!pagename.equalsIgnoreCase("null")){
	                	 pName = factory.getOWLDataProperty(":pagename", pm);
	                	 pagenameAssertion = factory.getOWLDataPropertyAssertionAxiom(pName, newPage,
	                             pagename);
	                	 manager.addAxiom(ontology, pagenameAssertion);
	                }
	                
	                if(!location.equals("null")){	                	
	                    loc = factory.getOWLDataProperty(":location", pm);
	                    locationAssertion = factory.getOWLDataPropertyAssertionAxiom(loc, newPage,
	                            location);
	                    manager.addAxiom(ontology, locationAssertion);
	                }   
	                 
	                
	                line = br.readLine();
		    }
		    
		    File file = new File("TwitterOntology-Populated.owl");
	        manager.saveOntology(ontology, IRI.create(file.toURI()));
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
      
		
	}
	
	
	private void createTweets(){
		
		String id, screenname, text, createdAt;     
		OWLNamedIndividual tweet, twitterAccount1, twitterAccount2;
		OWLObjectProperty posts, retweets, replies, mentions, favorites;
        OWLDataProperty tex, cAt, hashtag, url;
        OWLClass tweetClass; 
		OWLClassAssertionAxiom tweetClassAssertion;
		OWLObjectPropertyAssertionAxiom postsAssertion, retweetAssertion,repliesAssertion, mentionsAssertion, favoritesAssertion;
		OWLDataPropertyAssertionAxiom textAssertion, creationAssertion, hashtagAssertion, urlAssertion;
	   
		
        int hashtagsCounter, mentionsCounter, urlsCounter = 0;
        String propertyType, user;
        try {
        	
            br = new BufferedReader(new FileReader("tweets"));          
            line = br.readLine();
          
            int j = 1;
            while(line != null){            
            
                st = new StringTokenizer(line, "¬");
                System.out.println("Tokens: " + st.countTokens());
                
                id = (String) st.nextElement();
                user = (String) st.nextElement();                               
                propertyType = (String) st.nextElement();
                                
                screenname = (String) st.nextElement();
                text = (String) st.nextElement();
                                  
                
                
                //---------------------------------- Tweet's creation------------------------
                
                tweetClass = factory.getOWLClass(":Tweet", pm);                                
                tweet = factory.getOWLNamedIndividual(":tweet:"+id, pm);
                tweetClassAssertion = factory.getOWLClassAssertionAxiom(tweetClass, tweet);
                tex = factory.getOWLDataProperty(":text", pm);               
                posts = factory.getOWLObjectProperty(":posts", pm);
                                
                textAssertion = factory.getOWLDataPropertyAssertionAxiom(tex, tweet,
                        text);
                            
                                
                
                manager.addAxiom(ontology, tweetClassAssertion);                
                manager.addAxiom(ontology, textAssertion);
                            
                //------------------------------------ Retweet's or Reply's creation
                
                                                
                if(!user.equalsIgnoreCase("null")){

                	twitterAccount1 = factory.getOWLNamedIndividual(IRI.create(ontologyURI + user)); 
                	twitterAccount2 = factory.getOWLNamedIndividual(IRI.create(ontologyURI + screenname));
                	
                	if(propertyType.equalsIgnoreCase("replies")){
                		 replies = factory.getOWLObjectProperty(":"+propertyType, pm);
                		 repliesAssertion = factory.getOWLObjectPropertyAssertionAxiom(replies, twitterAccount1,
                                 twitterAccount2);
                		 manager.addAxiom(ontology, repliesAssertion);
                		 
                		 //--------------------------------------------------------
                		 
                		 postsAssertion = factory.getOWLObjectPropertyAssertionAxiom(posts, twitterAccount1,
                                 tweet);
                         manager.addAxiom(ontology, postsAssertion);
                		 
                	}
                	
                	if(propertyType.equalsIgnoreCase("favorites")){
                		favorites = factory.getOWLObjectProperty(":"+propertyType, pm);
                		favoritesAssertion = factory.getOWLObjectPropertyAssertionAxiom(favorites, twitterAccount1,
                                tweet);
                		manager.addAxiom(ontology, favoritesAssertion);
                		
                		//---------------------------------------------------------
                		
                		 postsAssertion = factory.getOWLObjectPropertyAssertionAxiom(posts, twitterAccount2,
                                 tweet);
                         manager.addAxiom(ontology, postsAssertion);
                		
                	}
                	
                	if(propertyType.equalsIgnoreCase("retweets")){
                		 retweets = factory.getOWLObjectProperty(":"+propertyType, pm);
                		 retweetAssertion = factory.getOWLObjectPropertyAssertionAxiom(retweets, twitterAccount1,
                                 tweet);
                		 manager.addAxiom(ontology, retweetAssertion);
                		 
                		 //--------------------------------------------------------
                		 
                		 postsAssertion = factory.getOWLObjectPropertyAssertionAxiom(posts, twitterAccount2,
                                 tweet);
                         manager.addAxiom(ontology, postsAssertion);
                	}
                }else{                	
                	  twitterAccount2 = factory.getOWLNamedIndividual(IRI.create(ontologyURI + screenname));                	
                	  postsAssertion = factory.getOWLObjectPropertyAssertionAxiom(posts, twitterAccount2,
                              tweet);
                      manager.addAxiom(ontology, postsAssertion);
                }              
               
                
              
              
                hashtagsCounter = Integer.parseInt((String) st.nextElement());
                if(hashtagsCounter > 0){
                    hashtag = factory.getOWLDataProperty(":hashtag", pm);
                      
                    for(int i = 0; i < hashtagsCounter; i++){
                    	hashtagAssertion = factory.getOWLDataPropertyAssertionAxiom(hashtag, tweet,
                        		  (String) st.nextElement());
                      	manager.addAxiom(ontology, hashtagAssertion);
                    }
                  
                }   
                
                urlsCounter = Integer.parseInt((String) st.nextElement());
                if(urlsCounter > 0){
                	url = factory.getOWLDataProperty(":url", pm);
                	
                	 for(int i = 0; i < urlsCounter; i++){
                     	urlAssertion = factory.getOWLDataPropertyAssertionAxiom(url, tweet,
                         		  (String) st.nextElement());
                       	manager.addAxiom(ontology, urlAssertion);
                     }
                }
                
                mentionsCounter = Integer.parseInt((String) st.nextElement()); 
                if(mentionsCounter > 0){
                	 mentions = factory.getOWLObjectProperty(":mentions", pm);
                	 
                	  for(int i = 0; i < mentionsCounter; i++){
                		twitterAccount1 = factory.getOWLNamedIndividual(":"+ (String) st.nextElement(), pm);  
                      	mentionsAssertion = factory.getOWLObjectPropertyAssertionAxiom(mentions, tweet,
                          		  twitterAccount1);
                        	manager.addAxiom(ontology, mentionsAssertion);
                      }
                }
                                 
                line = br.readLine();
            	
            }
            
            File file = new File("TwitterOntology-Populated.owl");
	        manager.saveOntology(ontology, IRI.create(file.toURI()));
        }catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	e.printStackTrace();
        } catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	private void createFavorites(){
		
		   
		OWLNamedIndividual tweet, twitterAccount;
		OWLObjectProperty liked;
        OWLClass tweetClass;
        OWLClassAssertionAxiom tweetClassAssertion;
		OWLObjectPropertyAssertionAxiom likedAssertion;
		
        String screenname = "", id = "";
        try {
            br = new BufferedReader(new FileReader("favorites"));
            line = br.readLine();
             
            while(line != null){
                st = new StringTokenizer(line, "¬");
                 
                screenname = (String) st.nextElement();
                id = (String) st.nextElement();
                
                tweetClass = factory.getOWLClass(":Tweet", pm);
                tweet = factory.getOWLNamedIndividual(":"+id, pm);
                twitterAccount = factory.getOWLNamedIndividual(":" + screenname, pm);
                liked = factory.getOWLObjectProperty(":liked", pm);
                
                tweetClassAssertion = factory.getOWLClassAssertionAxiom(tweetClass, tweet);
                likedAssertion = factory.getOWLObjectPropertyAssertionAxiom(liked, twitterAccount,
                        tweet);
                
                manager.addAxiom(ontology, tweetClassAssertion);
                manager.addAxiom(ontology, likedAssertion);
                
                line = br.readLine();
            }
            br.close();
            
            File file = new File("TwitterOntology-Populated.owl");
	        manager.saveOntology(ontology, IRI.create(file.toURI()));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
          
                 
	}
	

	public void createList(){
		
		OWLNamedIndividual list, twitterAccount;
		OWLObjectProperty owns;		
		OWLDataProperty name, description;
		OWLClass listClass;
        
		OWLClassAssertionAxiom listClassAssertion;
		OWLObjectPropertyAssertionAxiom ownsAssertion;
		OWLDataPropertyAssertionAxiom nameAssertion, descriptionAssertion;
		
        String screenname = "", id = "", listName, listDescription = null;
        try {
            br = new BufferedReader(new FileReader("lists"));
            line = br.readLine();
             
            while(line != null){
                st = new StringTokenizer(line, "¬");
                 
                screenname = (String) st.nextElement();
                id = (String) st.nextElement();
                listName = (String) st.nextElement();
                listDescription = (String) st.nextElement();
                               
                listClass = factory.getOWLClass(":List", pm);
                list = factory.getOWLNamedIndividual(":list:"+id, pm);
                twitterAccount = factory.getOWLNamedIndividual(IRI.create(ontologyURI + screenname));
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
                
                line = br.readLine();
            }
            br.close();
             
            File file = new File("TwitterOntology-Populated.owl");
	        manager.saveOntology(ontology, IRI.create(file.toURI()));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

	public void createListMembers(){
		OWLNamedIndividual list, twitterAccount;
		OWLObjectProperty hasMember;
        
		OWLObjectPropertyAssertionAxiom hasMemberAssertion;
		
        String screenname = "", id;
        
        try {
            br = new BufferedReader(new FileReader("list-members"));
            line = br.readLine();
             
            while(line != null){
                st = new StringTokenizer(line, "¬");
                                
                id = (String) st.nextElement();
                screenname = (String) st.nextElement();
                
                list = factory.getOWLNamedIndividual(IRI.create(ontologyURI + "list:" + id));
                twitterAccount = factory.getOWLNamedIndividual(IRI.create(ontologyURI + screenname));
                hasMember = factory.getOWLObjectProperty(":hasMember", pm);
                
                hasMemberAssertion = factory.getOWLObjectPropertyAssertionAxiom(hasMember, list,
                        twitterAccount);
                manager.addAxiom(ontology, hasMemberAssertion);
                
                line = br.readLine();
            }
            br.close();
            
            File file = new File("TwitterOntology-Populated.owl");
	        manager.saveOntology(ontology, IRI.create(file.toURI()));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	
	

}
