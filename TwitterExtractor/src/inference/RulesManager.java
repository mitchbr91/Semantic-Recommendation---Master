package inference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.vocab.SWRLBuiltInsVocabulary;

public class RulesManager {
	
	
	public static Map<String, SWRLRule> getRules(){
		
		Map<String, SWRLRule> rules = new HashMap<String, SWRLRule>();
		
		rules.put("retweetsRule", retweetsRule());		
		rules.put("repliesRule", repliesRule());
		rules.put("mentionsRule", mentionsRule());
		//rules.put("equalHashtagsRule", equalHashtagsRule());
		//rules.put("hashtagContainedInTweetRule", hashtagContainedInTweetRule());
		rules.put("listContainsFollowee", listContainsFolloweeRule());
		rules.put("hashtagSubstringListDescriptionRule", hashtagSubstringListDescriptionRule());
		rules.put("hashtagSubstringListNameRule", hashtagSubstringListNameRule());
		//rules.put("equalURLsRule",equalURLsRule());
		rules.put("favoritesRule", favoritesRule());
		
		return rules;
	}
	
	private static SWRLRule retweetsRule(){
				
	    OWLOntologyManager manager;
	    OWLDataFactory factory;	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
      
		//Rule: posts(?u2, ?t), retweets(?u1, ?t) -> retweetsRule(?u1, ?u2)
		
		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
		SWRLVariable t =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t"));
		
		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
		
		OWLObjectProperty retweets = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#retweets"));
        OWLObjectProperty posts = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#posts"));
        OWLObjectProperty retweetsRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#retweetsRule"));
       
        
        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(retweets, u1, t);
        SWRLObjectPropertyAtom propAtom2 = factory.getSWRLObjectPropertyAtom(posts, u2, t);
        SWRLObjectPropertyAtom retweetsRuleAtom = factory.getSWRLObjectPropertyAtom(retweetsRule, u1, u2);
        
      
        //SWRLBuiltInAtom builtinAton = factory.getSWRLBuiltInAtom(SWRLBuiltInsVocabulary.CONTAINS_IGNORE_CASE.getIRI(), ags);
      
        preconditions.add(propAtom);
        preconditions.add(propAtom2);
        //preconditions.add(builtinAton);
        
        SWRLRule rule = factory.getSWRLRule(preconditions,
                Collections.singleton(retweetsRuleAtom));

       // manager.applyChange(new AddAxiom(ontology, rule));        
       
        
//        ontologyFile = new File("ontologies/retweetsRuleOntology.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}   
        
        return rule;
	}
	
	private static SWRLRule favoritesRule(){
				  
	    OWLOntologyManager manager;
	    OWLDataFactory factory;	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
        	
		
		//Rule: posts(?u2, ?t), favorites(?u1, ?t) -> favoritesRule(?u1, ?u2)
		
		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
		SWRLVariable t =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t"));
		
		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
		
		OWLObjectProperty favorites = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#favorites"));
        OWLObjectProperty posts = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#posts"));
        OWLObjectProperty favoritesRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#favoritesRule"));
       
        
        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(favorites, u1, t);
        SWRLObjectPropertyAtom propAtom2 = factory.getSWRLObjectPropertyAtom(posts, u2, t);
        SWRLObjectPropertyAtom favoritesRuleAtom = factory.getSWRLObjectPropertyAtom(favoritesRule, u1, u2);
         
      
        preconditions.add(propAtom);
        preconditions.add(propAtom2);        
        
        SWRLRule rule = factory.getSWRLRule(preconditions,
                Collections.singleton(favoritesRuleAtom));

        //manager.applyChange(new AddAxiom(ontology, rule));
				
//        ontologyFile = new File("ontologies/favoritesRuleOntology.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        return rule;
	}
	
	private static SWRLRule repliesRule(){
		  
	    OWLOntologyManager manager;
	    OWLDataFactory factory;	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
        		
		//Rule: posts(?u2, ?t), replies(?u1, ?t) -> repliesRule(?u1, ?u2)
		
		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
		SWRLVariable t =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t"));
		
		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
		
		OWLObjectProperty replies = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#replies"));
        OWLObjectProperty posts = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#posts"));
        OWLObjectProperty repliesRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#repliesRule"));
       
        
        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(replies, u1, t);
        SWRLObjectPropertyAtom propAtom2 = factory.getSWRLObjectPropertyAtom(posts, u2, t);
        SWRLObjectPropertyAtom repliesRuleAtom = factory.getSWRLObjectPropertyAtom(repliesRule, u1, u2);
         
      
        preconditions.add(propAtom);
        preconditions.add(propAtom2);        
        
        SWRLRule rule = factory.getSWRLRule(preconditions,
                Collections.singleton(repliesRuleAtom));

//        manager.applyChange(new AddAxiom(ontology, rule));
//				
//        ontologyFile = new File("ontologies/repliesRuleOntology.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}   
        
        return rule;
		
	}
	
	private static SWRLRule mentionsRule(){
		
	    OWLOntologyManager manager;
	    OWLDataFactory factory;	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
       
		//Rule: mentions(?t, ?u2), posts(?u1, ?t) -> mentionsRule(?u1, ?u2)
		
		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
		SWRLVariable t =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t"));
		
		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
		
		OWLObjectProperty mentions = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#mentions"));
        OWLObjectProperty posts = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#posts"));
        OWLObjectProperty mentionsRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#mentionsRule"));
       
        
        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(mentions, t, u2);
        SWRLObjectPropertyAtom propAtom2 = factory.getSWRLObjectPropertyAtom(posts, u1, t);
        SWRLObjectPropertyAtom mentionsRuleAtom = factory.getSWRLObjectPropertyAtom(mentionsRule, u1, u2);
         
      
        preconditions.add(propAtom);
        preconditions.add(propAtom2);        
        
        SWRLRule rule = factory.getSWRLRule(preconditions,
                Collections.singleton(mentionsRuleAtom));

        //manager.applyChange(new AddAxiom(ontology, rule));
        
//        ontologyFile = new File("ontologies/mentionsRuleOntology.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        return rule;
	}
	
	private static SWRLRule equalHashtagsRule(){
		
	    OWLOntologyManager manager;
	    OWLDataFactory factory;	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
        				
		//Rule: posts(?u1, ?t1), posts(?u2, ?t2), hashtag(?t1, ?h), hashtag(?t2, ?h) -> equalHashtagsRule(?u1, ?u2)
		
		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
		SWRLVariable t1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t1"));
		SWRLVariable t2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t2"));
		SWRLVariable h =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?h"));
		
		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
		
		OWLDataProperty hashtag = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#hashtag"));
        OWLObjectProperty posts = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#posts"));
        OWLObjectProperty equalHashtagsRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#equalHashtagsRule"));
       
        
        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(posts, u1, t1);
        SWRLObjectPropertyAtom propAtom2 = factory.getSWRLObjectPropertyAtom(posts, u2, t2);
        SWRLDataPropertyAtom propAtom3 = factory.getSWRLDataPropertyAtom(hashtag, t1, h);
        SWRLDataPropertyAtom propAtom4 = factory.getSWRLDataPropertyAtom(hashtag, t2, h);
        SWRLObjectPropertyAtom equalHashtagsRuleAtom = factory.getSWRLObjectPropertyAtom(equalHashtagsRule, u1, u2);
         
      
        preconditions.add(propAtom);
        preconditions.add(propAtom2);
        preconditions.add(propAtom3);
        preconditions.add(propAtom4);
        
        SWRLRule rule = factory.getSWRLRule(preconditions,
                Collections.singleton(equalHashtagsRuleAtom));

//        manager.applyChange(new AddAxiom(ontology, rule));
//				
//        ontologyFile = new File("ontologies/equalHashtagsRuleOntology.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        return rule;
	}
	
	private static SWRLRule hashtagContainedInTweetRule(){
		 
	    OWLOntologyManager manager;
	    OWLDataFactory factory;	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
       		
		//Rule: posts(?u1, ?t1), hashtag(?t1, ?h), posts(?u2, ?t2), text(?t2, ?t), containsIgnoreCase(?t, ?h) -> hashtagContainedInTweetRule(?u1, ?u2)
		
		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
		SWRLVariable t =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t"));
		SWRLVariable t1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t1"));
		SWRLVariable t2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t2"));
		SWRLVariable h =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?h"));
		
		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
		
		OWLDataProperty hashtag = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#hashtag"));
        OWLObjectProperty posts = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#posts"));
        OWLDataProperty text = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#text"));
        OWLObjectProperty hashtagContainedInTweetRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hashtagContainedInTweetRule"));
       
        
        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(posts, u1, t1);
        SWRLDataPropertyAtom propAtom2 = factory.getSWRLDataPropertyAtom(hashtag, t1, h);
        SWRLObjectPropertyAtom propAtom3 = factory.getSWRLObjectPropertyAtom(posts, u2, t2);
        SWRLDataPropertyAtom propAtom4 = factory.getSWRLDataPropertyAtom(text, t2, t);
        
        List<SWRLDArgument> ags = new ArrayList<SWRLDArgument>();
        
        ags.add(propAtom4.getSecondArgument());
        ags.add(propAtom2.getSecondArgument());
        
        SWRLBuiltInAtom builtinAtom = factory.getSWRLBuiltInAtom(SWRLBuiltInsVocabulary.CONTAINS_IGNORE_CASE.getIRI(), ags);
        SWRLObjectPropertyAtom hashtagContainedInTweetRuleAtom = factory.getSWRLObjectPropertyAtom(hashtagContainedInTweetRule, u1, u2);
             
        preconditions.add(propAtom);
        preconditions.add(propAtom2);
        preconditions.add(propAtom3);
        preconditions.add(propAtom4);
        preconditions.add(builtinAtom);
        
        SWRLRule rule = factory.getSWRLRule(preconditions,
                Collections.singleton(hashtagContainedInTweetRuleAtom));

//        manager.applyChange(new AddAxiom(ontology, rule));
//				
//        ontologyFile = new File("ontologies/hashtagContainedInTweetRuleOntology.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        return rule;
	}

	private static SWRLRule listContainsFolloweeRule(){
				  
	    OWLOntologyManager manager;
	    OWLDataFactory factory;	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
       		
		//Rule: hasMember(?l, ?u2), owns(?u1, ?l) -> listContainsFollowee(?u1, ?u2)
		
		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
		SWRLVariable l =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?l"));
		
		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
		
		OWLObjectProperty owns = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#owns"));
        OWLObjectProperty hasMember = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasMember"));
        OWLObjectProperty listContainsFolloweeRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#listContainsFollowee"));
       
        
        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(hasMember, l, u2);
        SWRLObjectPropertyAtom propAtom2 = factory.getSWRLObjectPropertyAtom(owns, u1, l);
        SWRLObjectPropertyAtom listContainsFolloweeAtom = factory.getSWRLObjectPropertyAtom(listContainsFolloweeRule, u1, u2);
        
      
        preconditions.add(propAtom);
        preconditions.add(propAtom2);
                
        SWRLRule rule = factory.getSWRLRule(preconditions,
                Collections.singleton(listContainsFolloweeAtom));

//        manager.applyChange(new AddAxiom(ontology, rule));        
//       
//        
//        ontologyFile = new File("ontologies/listContainsFolloweeOntology.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
        
        return rule;
	}

	private static SWRLRule hashtagSubstringListDescriptionRule(){
		   
	    OWLOntologyManager manager;
	    OWLDataFactory factory;	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
        		
		//Rule: posts(?u2, ?t), hashtag(?t, ?h), owns(?u1, ?l), description(?l, ?d),
		//containsIgnoreCase(?d, ?h) -> hashtagSubstringListDescriptionRule(?u1, ?u2)
		
		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
		SWRLVariable t =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t"));
		SWRLVariable l =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?l"));
		SWRLVariable d =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?d"));
		SWRLVariable h =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?h"));
		
		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
		
		OWLDataProperty hashtag = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#hashtag"));
        OWLObjectProperty posts = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#posts"));
        OWLObjectProperty owns = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#owns"));
        OWLDataProperty description = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#description"));
        OWLObjectProperty hashtagSubstringListDescriptionRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hashtagSubstringListDescriptionRule"));
       
        
        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(posts, u2, t);
        SWRLDataPropertyAtom propAtom2 = factory.getSWRLDataPropertyAtom(hashtag, t, h);
        SWRLObjectPropertyAtom propAtom3 = factory.getSWRLObjectPropertyAtom(owns, u1, l);
        SWRLDataPropertyAtom propAtom4 = factory.getSWRLDataPropertyAtom(description, l, d);
        
        List<SWRLDArgument> ags = new ArrayList<SWRLDArgument>();
        
        ags.add(propAtom4.getSecondArgument());
        ags.add(propAtom2.getSecondArgument());
        
        SWRLBuiltInAtom builtinAtom = factory.getSWRLBuiltInAtom(SWRLBuiltInsVocabulary.CONTAINS_IGNORE_CASE.getIRI(), ags);
        SWRLObjectPropertyAtom hashtagSubstringListDescriptionRuleAtom = factory.getSWRLObjectPropertyAtom(hashtagSubstringListDescriptionRule, u1, u2);
             
        preconditions.add(propAtom);
        preconditions.add(propAtom2);
        preconditions.add(propAtom3);
        preconditions.add(propAtom4);
        preconditions.add(builtinAtom);
        
        SWRLRule rule = factory.getSWRLRule(preconditions,
                Collections.singleton(hashtagSubstringListDescriptionRuleAtom));

        
//        manager.applyChange(new AddAxiom(ontology, rule));
//				
//        ontologyFile = new File("ontologies/hashtagSubstringListDescriptionRuleOntology.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        return rule;
	}

	private static SWRLRule hashtagSubstringListNameRule(){
		  
	    OWLOntologyManager manager;
	    OWLDataFactory factory;	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
       		
		//Rule: posts(?u2, ?t), hashtag(?t, ?h), owns(?u1, ?l), name(?l, ?n),
		//containsIgnoreCase(?n, ?h) -> hashtagSubstringListNameRule(?u1, ?u2)
		
		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
		SWRLVariable t =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t"));
		SWRLVariable l =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?l"));
		SWRLVariable n =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?n"));
		SWRLVariable h =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?h"));
		
		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
		
		OWLDataProperty hashtag = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#hashtag"));
        OWLObjectProperty posts = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#posts"));
        OWLObjectProperty owns = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#owns"));
        OWLDataProperty name = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#name"));
        OWLObjectProperty hashtagSubstringListNameRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#hashtagSubstringListNameRule"));
       
        
        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(posts, u2, t);
        SWRLDataPropertyAtom propAtom2 = factory.getSWRLDataPropertyAtom(hashtag, t, h);
        SWRLObjectPropertyAtom propAtom3 = factory.getSWRLObjectPropertyAtom(owns, u1, l);
        SWRLDataPropertyAtom propAtom4 = factory.getSWRLDataPropertyAtom(name, l, n);
        
        List<SWRLDArgument> ags = new ArrayList<SWRLDArgument>();
        
        ags.add(propAtom4.getSecondArgument());
        ags.add(propAtom2.getSecondArgument());
        
        SWRLBuiltInAtom builtinAtom = factory.getSWRLBuiltInAtom(SWRLBuiltInsVocabulary.CONTAINS_IGNORE_CASE.getIRI(), ags);
        SWRLObjectPropertyAtom hashtagSubstringListNameRuleAtom = factory.getSWRLObjectPropertyAtom(hashtagSubstringListNameRule, u1, u2);
             
        preconditions.add(propAtom);
        preconditions.add(propAtom2);
        preconditions.add(propAtom3);
        preconditions.add(propAtom4);
        preconditions.add(builtinAtom);
        
        SWRLRule rule = factory.getSWRLRule(preconditions,
                Collections.singleton(hashtagSubstringListNameRuleAtom));

//        manager.applyChange(new AddAxiom(ontology, rule));
//				
//        ontologyFile = new File("ontologies/hashtagSubstringListNameRuleOntology.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        return rule;
	}

	private static SWRLRule equalURLsRule(){
		  
	    OWLOntologyManager manager;
	    OWLDataFactory factory;	    
	    String ontologyIRI = "http://www.semanticweb.org/michel/ontologies/2014/6/TwitterOntology";
	    
		manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();          
        		
		//Rule: posts(?u1, ?t1), posts(?u2, ?t2), url(?t1, ?u), url(?t2, ?u) -> equalURLsRule(?u1, ?u2)
		
		SWRLVariable u1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u1"));
		SWRLVariable u2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u2"));
		SWRLVariable t1 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t1"));
		SWRLVariable t2 =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?t2"));
		SWRLVariable u =  factory.getSWRLVariable(IRI.create(ontologyIRI + "#?u"));
		
		Set<SWRLAtom>  preconditions = new TreeSet<SWRLAtom>();
		
		OWLDataProperty url = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#url"));
        OWLObjectProperty posts = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#posts"));
        OWLObjectProperty equalURLsRule = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#equalURLsRule"));
       
        
        SWRLObjectPropertyAtom propAtom = factory.getSWRLObjectPropertyAtom(posts, u1, t1);
        SWRLObjectPropertyAtom propAtom2 = factory.getSWRLObjectPropertyAtom(posts, u2, t2);
        SWRLDataPropertyAtom propAtom3 = factory.getSWRLDataPropertyAtom(url, t1, u);
        SWRLDataPropertyAtom propAtom4 = factory.getSWRLDataPropertyAtom(url, t2, u);
        SWRLObjectPropertyAtom equalURLsRuleAtom = factory.getSWRLObjectPropertyAtom(equalURLsRule, u1, u2);
         
      
        preconditions.add(propAtom);
        preconditions.add(propAtom2);
        preconditions.add(propAtom3);
        preconditions.add(propAtom4);
        
        SWRLRule rule = factory.getSWRLRule(preconditions,
                Collections.singleton(equalURLsRuleAtom));

//        manager.applyChange(new AddAxiom(ontology, rule));
//				
//        ontologyFile = new File("ontologies/equalURLsRuleOntology.owl");
//        try {
//			manager.saveOntology(ontology, IRI.create(ontologyFile.toURI()));
//		} catch (OWLOntologyStorageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        return rule;
	}

}
