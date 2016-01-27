package inference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mindswap.pellet.PelletOptions;
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

import com.clarkparsia.owlapi.explanation.DefaultExplanationGenerator;
import com.clarkparsia.owlapi.explanation.util.SilentExplanationProgressMonitor;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

import twitter.tracker.hibernate.TwitterAccount;

public class InferenceManager {
	
	private OWLReasonerFactory reasonerFactory;
	private OWLReasoner reasoner;
	private OWLObjectRenderer renderer; 
	private Map<String, Integer> rulesWeights;
	private OWLOntology ontology;   
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private PrefixManager pm;
	private Map<String, List<TwitterAccount>> inferedFollowees;
	
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
	    
	    File ontologyFile = new File("TwitterOntology-Populated.owl");
		try {
			ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);				
			System.out.println("Ontology: " + ontology);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pm = new DefaultPrefixManager(null, null,
				ontology.getOntologyID().getOntologyIRI().get().toString()+"#");
	        
	    
	}
	
	public Map<String, List<TwitterAccount>> infer(List<String> targetUserSet){
		
		
		OWLNamedIndividual targetUser;
	    OWLObjectProperty inferedProperty;
	    OWLObjectPropertyAssertionAxiom axiomToExplain;
	    Set<Set<OWLAxiom>> explanation;
	    List<TwitterAccount> twitterAccountList = null;
	    int ruleWeight;	    
	    DefaultExplanationGenerator explanationGenerator;
	    String inferedUserName;
        int inferedPoints;
        TwitterAccount user = null;
                
        
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
	        
	       
	        
	        int i = 1;
	        for(String targetU: targetUserSet){
	        	
	        	System.out.println(i++ + " - " + targetU);
	        
	        	targetUser = factory.getOWLNamedIndividual(":"+targetU, pm);
	        	inferedProperty = factory.getOWLObjectProperty(":"+rule, pm);
	        	
	        		
              	 twitterAccountList = new ArrayList<TwitterAccount>();
	        	 for (OWLNamedIndividual inferedUser : reasoner.getObjectPropertyValues(targetUser, inferedProperty).getFlattened()) { 
	        	         	        		
	        		 inferedUserName = renderer.render(inferedUser); 
	        		 user = new TwitterAccount(inferedUserName);		        		 
	        		 	        		
//	        		 axiomToExplain = factory.getOWLObjectPropertyAssertionAxiom(inferedProperty, targetUser, inferedUser); 
//	        		 explanation = explanationGenerator.getExplanations(axiomToExplain);
	        		 
	        		 ruleWeight = rulesWeights.get(rule);
	        		 inferedPoints = 1/*ruleWeight*explanation.size()*/;
	        		 user.setInferedPoints(inferedPoints);
	        			 
	        		 twitterAccountList.add(user);        
	        		 System.out.println("Some inference: " + inferedUserName);
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
	        }
	        
	        //Remove rule
	        manager.removeAxiom(ontology, swrlRule);	       
	        reasoner.dispose();	        
	        System.out.println("Rule removed!");
	    }
       
        return inferedFollowees;

	}

	
}
