package twitter.tracker.hibernate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

import persistence.entities.hibernate.HibernateUtil;

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
		
//		File ontologyFile = new File("TwitterOntology-Jukelmer.owl");
//		try {
//			ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);
//		} catch (OWLOntologyCreationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
               
		DataSetAnnotator annotator = new DataSetAnnotator(ontologyIRI);
		
		//annotator.createData();
		
		InferenceManager inference = new InferenceManager();
		
		inference.infer();				
			
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
