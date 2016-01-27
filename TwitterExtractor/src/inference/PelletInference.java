package inference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class PelletInference {
	
	private PrefixManager pm;
	private OWLDataFactory factory;
	
	public PelletInference(String ontologyURI){
		pm = new DefaultPrefixManager(null, null,
                ontologyURI);
	}
	
	public void run(){
		
		try {
		    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		    File ontologyFile = new File("TwitterOntology-Populated-OneRule.owl");
		    OWLOntology inferredOntology = manager.loadOntologyFromOntologyDocument(ontologyFile);

		    System.out.println("Before inference");
		    PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(inferredOntology);
		    reasoner.getKB().realize();
		    
		    factory = manager.getOWLDataFactory();
		    OWLObjectProperty inferedProperty = factory.getOWLObjectProperty(":favoritesRule", pm);
		    
		    OWLNamedIndividual targetUser = factory.getOWLNamedIndividual(":kamila__sg", pm);
		    //reasoner.getObjectPropertyValues(targetUser, inferedProperty);
		    
		    System.out.println("After inference");

//		    List<InferredAxiomGenerator<? extends OWLAxiom>> axiomGenerators = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
//		    axiomGenerators.add(new InferredPropertyAssertionGenerator() );
//
//		    InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner,axiomGenerators);		    
//		    iog.fillOntology((OWLDataFactory) manager, inferredOntology);
//
//		    // Save the new ontology
//		    OutputStream owlOutputStream = new ByteArrayOutputStream();
//		    manager.saveOntology(inferredOntology, owlOutputStream);
//		    String inferredData = owlOutputStream.toString();
//		    
//		    System.out.println("Inference: " + inferredData);
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}

		
	}

}
