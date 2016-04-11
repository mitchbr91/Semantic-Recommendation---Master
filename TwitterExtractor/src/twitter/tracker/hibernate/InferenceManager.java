package twitter.tracker.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import com.clarkparsia.owlapi.explanation.DefaultExplanationGenerator;
import com.clarkparsia.owlapi.explanation.util.SilentExplanationProgressMonitor;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class InferenceManager {
	
	private OWLReasonerFactory reasonerFactory;
	private OWLReasoner reasoner;
	private OWLObjectRenderer renderer; 
	private Map<String, Integer> rulesWeights;
	private OWLOntology ontology;   
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private PrefixManager pm;
	
	public InferenceManager(){
		rulesWeights = new HashMap<String, Integer>();
		rulesWeights.put("favoritesRule", 1);
		rulesWeights.put("retweetsRule", 1);
		rulesWeights.put("repliesRule", 1);
		rulesWeights.put("mentionsRule", 1);
		rulesWeights.put("equalHashtagsRule", 1);
		rulesWeights.put("hashtagContainedInTweetRule", 1);
		rulesWeights.put("equalURLsRule", 1);
		rulesWeights.put("listContainsFollowee", 1);
	    rulesWeights.put("hashtagSubstringListNameRule", 1);
	    rulesWeights.put("hashtagSubstringListDescriptionRule", 1);
	}
	
	public Map<String, List<TwitterAccount>> infer(List<String> targetUserSet, List<String> inferedPropertiesSet){
		
		
		OWLNamedIndividual targetUser;
	    OWLObjectProperty inferedProperty;
	    OWLObjectPropertyAssertionAxiom axiomToExplain;
	    Set<Set<OWLAxiom>> explanation;
	    List<TwitterAccount> twitterAccountList = null;
	    int ruleWeight;
	    
		reasonerFactory = PelletReasonerFactory.getInstance(); 
        reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration()); 
        renderer = new DLSyntaxObjectRenderer();
        
        Map<String, List<TwitterAccount>> inferedFollowees = new HashMap<String, List<TwitterAccount>>();
        
      
        DefaultExplanationGenerator explanationGenerator = 
                new DefaultExplanationGenerator( 
                        manager, reasonerFactory, ontology, reasoner, new SilentExplanationProgressMonitor()); 
             
       
        
        String inferedUserName;
        int inferedPoints;
        TwitterAccount user;
        
        for(String targetU: targetUserSet){
        	for(String inferedP: inferedPropertiesSet){
        		targetUser = factory.getOWLNamedIndividual(":"+targetU, pm);
        		inferedProperty = factory.getOWLObjectProperty(":"+inferedP, pm);
        		        		
        		 for (OWLNamedIndividual inferedUser : reasoner.getObjectPropertyValues(targetUser, inferedProperty).getFlattened()) { 
        	         
        			 twitterAccountList = new ArrayList<TwitterAccount>();
        			         			 
        			 inferedUserName = renderer.render(inferedUser);
        			 user = new TwitterAccount(inferedUserName);
        			 axiomToExplain = factory.getOWLObjectPropertyAssertionAxiom(inferedProperty, targetUser, inferedUser); 
        			 explanation = explanationGenerator.getExplanations(axiomToExplain);
        			 
        			 ruleWeight = rulesWeights.get(inferedP.replaceFirst("follows-", ""));
        			 inferedPoints = ruleWeight*explanation.size();
        			 user.setInferedPoints(inferedPoints);
        			 
        			 twitterAccountList.add(user);        			 
        	     }
        		
        	}
        	inferedFollowees.put(targetU, twitterAccountList);
        }                            
      
        return inferedFollowees;

	}

	
}
