package inference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.pellet.PelletOptions;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.api.ExplanationManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import persistence.dao.hibernate.UserDao;
import persistence.entities.hibernate.UserAccount;

import com.clarkparsia.owlapi.explanation.BlackBoxExplanation;
import com.clarkparsia.owlapi.explanation.DefaultExplanationGenerator;
import com.clarkparsia.owlapi.explanation.HSTExplanationGenerator;
import com.clarkparsia.owlapi.explanation.util.SilentExplanationProgressMonitor;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import twitter.tracker.hibernate.MyComparatorInferedPoints;
import twitter.tracker.hibernate.TwitterAccount;

public class InferenceManager {
	
	private OWLReasonerFactory reasonerFactory;
	private OWLReasoner reasoner;
	private OWLObjectRenderer renderer; 
	private Map<String, Integer> rulesWeights;
	private OWLOntology ontology;   
    private OWLOntologyManager manager;
    private OWLDataFactory factory;   
	private Map<String, List<TwitterAccount>> inferedFollowees;
	private PrefixManager pm;
	private UserDao daoUser;
		
	public InferenceManager(){
		
		PelletOptions.USE_CONTINUOUS_RULES = true;
		PelletOptions.IGNORE_UNSUPPORTED_AXIOMS=true;
		
		rulesWeights = new HashMap<String, Integer>();
		rulesWeights.put("favoritesRule", 1);
		rulesWeights.put("retweetsRule", 1);
		rulesWeights.put("repliesRule", 1);
		rulesWeights.put("mentionsRule", 1);
		//rulesWeights.put("equalHashtagsRule", 1);
//		rulesWeights.put("hashtagContainedInTweetRule", 1);
//		rulesWeights.put("equalURLsRule", 1);
		rulesWeights.put("listContainsFollowee", 1);
	    rulesWeights.put("hashtagSubstringListNameRule", 1);
	    rulesWeights.put("hashtagSubstringListDescriptionRule", 1);
	   	    
	   	    
	    manager = OWLManager.createOWLOntologyManager();	   
	    factory = manager.getOWLDataFactory();
	    reasonerFactory = PelletReasonerFactory.getInstance();
	    renderer = new DLSyntaxObjectRenderer();
	    inferedFollowees = new HashMap<String, List<TwitterAccount>>();
	    daoUser = new UserDao();       
	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology#";
	    pm = new DefaultPrefixManager(null, null,
	    		ontologyIRI); 
	}
	
	public Map<String, List<TwitterAccount>> infer(){
		
		
		OWLNamedIndividual targetUser;
	    OWLObjectProperty inferedProperty;
	    OWLAxiom axiomToExplain;
	    Set<Explanation<OWLAxiom>> explanation = null;
	    List<TwitterAccount> twitterAccountList = null;
	    int ruleWeight;	    
	    DefaultExplanationGenerator explanationGenerator;
	    String inferedUserName;
        int inferedPoints;
        TwitterAccount user = null;       
        List<String> targetUserSet = new ArrayList<String>();
        
        List<String> ontologies = new ArrayList<String>();
        
        //Get the name of all ontologies 
        for(UserAccount u: daoUser.listUsers(true, false)){
        	targetUserSet.add(u.getScreenName());
        	ontologies.add("TwitterOntology-" + u.getScreenName() + ".owl");        
        }
        
        System.out.println("Ontologies size: " + ontologies.size());
        ExplanationGeneratorFactory<OWLAxiom> genFac =
    			ExplanationManager.createExplanationGeneratorFactory(reasonerFactory);
        
      
        
        String targetU;
        int inf = 0;
        for(String ontologyName: ontologies){
        	
        	File ontologyFile = new File(ontologyName);
     		
        	try {
     			ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);				
     			System.out.println("Ontology: " + ontologyName);
     		} catch (OWLOntologyCreationException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
        	
        	
        	System.out.println("Ontology IRI: " + ontology.getOntologyID().getOntologyIRI());
        	
        	
        	for(String rule: RulesManager.getRules().keySet()){
    	    	
        		
    	    	SWRLRule swrlRule = RulesManager.getRules().get(rule);    	    
    	    	
    	    	//Add rule to ontology
    	    	manager.applyChange(new AddAxiom(ontology, swrlRule));
    	    	
    	    	System.out.println("Rule: " + rule);
    	    	
    			reasoner = reasonerFactory.createNonBufferingReasoner(ontology);			
    			//reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());        
    	        System.out.println("Reasoner: " + reasoner); 
    	        
    	        explanationGenerator = 
    	                new DefaultExplanationGenerator( 
    	                        manager, reasonerFactory, ontology, reasoner, new SilentExplanationProgressMonitor());
    	        
    	       
    	        int i = 0;
    	        targetU = ontologyName.replace("TwitterOntology-", "").replace(".owl", "");
    	        
    	        System.out.println(i++ + " - " + targetU);
    	        
	        	targetUser = factory.getOWLNamedIndividual(":"+targetU, pm);
	        	inferedProperty = factory.getOWLObjectProperty(":"+rule, pm);
	        	
	        	ExplanationGenerator<OWLAxiom> gen = genFac.createExplanationGenerator(ontology);	
              	twitterAccountList = new ArrayList<TwitterAccount>();
	        	for (OWLNamedIndividual inferedUser : reasoner.getObjectPropertyValues(targetUser, inferedProperty).getFlattened()) { 
	        	    	        		
	        		inferedUserName = renderer.render(inferedUser); 
	        		user = new TwitterAccount(inferedUserName);		        		 
	        		
	        		
	        		axiomToExplain = factory.getOWLObjectPropertyAssertionAxiom(inferedProperty, targetUser, inferedUser);	        	
	        		
	        		if (rule.equalsIgnoreCase("listContainsFollowee") 
	        				|| rule.equalsIgnoreCase("hashtagSubstringListNameRule")
	        				|| rule.equalsIgnoreCase("hashtagSubstringListDescriptionRule")
	        						){
	        			
	        			inferedPoints = 1;
		        		System.out.println("Ich bin hier - IF");
	        		}else{	        				        		
		        				        		
		        		System.out.println("Na und - ELSE");
	        			ruleWeight = rulesWeights.get(rule);
	        			explanation = gen.getExplanations(axiomToExplain, 10);	        		
		        		inferedPoints = ruleWeight*explanation.size();
	        		}
	        		
	        		//inferedPoints = 1;
	        		
	        		//inf += ruleWeight*explanation.size();	        				
	        		user.setInferedPoints(inferedPoints);
	        		
	        		//inf++;
	        			 
	        		twitterAccountList.add(user);        
	        		System.out.println("Some inference: " + inferedUserName + " - Points: " + inferedPoints);
	            }
	        	 
	        	if(!inferedFollowees.containsKey(targetU))	        	
	        		inferedFollowees.put(targetU, twitterAccountList);
	        	else{
	        		 
	        		List<TwitterAccount> normalizedTwitterList = new ArrayList<TwitterAccount>();
	        		for(TwitterAccount ta: twitterAccountList){
	        			if(inferedFollowees.get(targetU).contains(ta)){
	        				 
	        				for(TwitterAccount account: inferedFollowees.get(targetU)){
	        					if(account.getName().equals(ta.getName())){
	        						ta.setInferedPoints(account.getInferedPoints());
	        						normalizedTwitterList.add(ta);	        						 
	        						break;
	        					}	        					 
	        					 
	        				}
	        			 }else{
	        				 normalizedTwitterList.add(ta);      				 
	        			 }
	        				 
	        		 }
	        		 
	        		 for(TwitterAccount account: inferedFollowees.get(targetU)){
	        			 if(!normalizedTwitterList.contains(account))
	        	        		normalizedTwitterList.add(account);
	        		 }
	        		 
	        		 inferedFollowees.put(targetU, normalizedTwitterList);
	        		 
	        	 }
    	        
    	        //Remove rule
    	        manager.removeAxiom(ontology, swrlRule);	       
    	        reasoner.dispose();	        
    	        System.out.println("Rule removed!");
    	    }
        	
        	manager.removeOntology(ontology);
        	
        	
        }
        
        //System.out.println("Total inferido: " + inf);
        return inferedFollowees;

	}
	
	public Map<String, List<TwitterAccount>> extractUsersWithNoInteractions(Map<String, List<TwitterAccount>> inferedUsers){
		
		List<TwitterAccount> inferedSet;
		List<UserAccount> followees;
		boolean found = false;
		Map<String, List<TwitterAccount>> usersWithNoInteractions = new HashMap<String, List<TwitterAccount>>();
		int i = 0;		
		List<TwitterAccount> users;
	
		for(UserAccount user: daoUser.listUsers(true, false)){
			
			inferedSet = inferedUsers.get(user.getScreenName());			
			followees = user.getFollowees();
			
			if (inferedSet != null){
				users = new ArrayList<TwitterAccount>();
				for(UserAccount followee: followees){
					while(i < inferedSet.size() && !found){
						if(followee.getScreenName().equals(inferedSet.get(i).getName())){
							found = true;						
						}
						
						i++;
					}
					
					if(!found)
						users.add(new TwitterAccount(followee.getScreenName()));	
					
					
					found = false;
					i = 0;
						
				}
				
				/*
				 * The threshold is half of followees size. If the users with no is equal or greater than the 
				 * half of the followees size is ok. Otherwise we need to complete this threshold with users
				 * with low interaction punctuation. The threshold can the changed, when it's desired. 
				 */
				
				
				if(users.size() >= followees.size()/2)			
					usersWithNoInteractions.put(user.getScreenName(), users);
				else{
					
					int usersWithLowInteractionSize = followees.size()/2 - users.size();
					
					//Ordering
					inferedSet.sort(new MyComparatorInferedPoints());
					for(int j = 0; j < usersWithLowInteractionSize; j++){
						users.add(inferedSet.get(j));
					}			
					
					usersWithNoInteractions.put(user.getScreenName(), users);
				}
			}
			
		}
		
		System.out.println("extractUsersWithNoInteractions - Method");
		
		return usersWithNoInteractions;
		
	}
	
	/**
	 * Return for each target user the list of his/her followees. This set will be used to calculate similarity between a user 
	 * and his followees without analyse semantic interaction between users.
	 * @return
	 */
	public Map<String, List<TwitterAccount>> getUsersNetwork(){
		
		Map<String, List<TwitterAccount>> usersNetwork = new HashMap<String, List<TwitterAccount>>();
		
		List<TwitterAccount> followees; 
		for(UserAccount targetUser: daoUser.listUsers(true, false)){
			
			followees = new ArrayList<TwitterAccount>();
			for(UserAccount followee: targetUser.getFollowees()){
				followees.add(new TwitterAccount(followee.getScreenName()));
			}
			
			usersNetwork.put(targetUser.getScreenName(), followees);			
			
		}
		
		return usersNetwork;
	}

	
}
