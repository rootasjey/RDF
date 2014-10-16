package main;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.VCARD;

public class Test {
	public static Model Modelization() {
		// definitions
		String personURI = "http://somewhere/JonhSmith";
		String givenName = "Jonh";
		String familyName = "Smith";
		String fullName 	= givenName + " " + familyName;
		
		// create an empty model
		Model model = ModelFactory.createDefaultModel();
		
		// create the resource
		Resource jonhSmith = model.createResource(personURI)
				.addProperty(VCARD.FN, fullName)
				.addProperty(VCARD.N, 
						model.createResource()
							 .addProperty(VCARD.Given, givenName)
							 .addProperty(VCARD.Family, familyName));
		
		return model;
		// add he property
		// jonhSmith.addProperty(VCARD.FN, fullName);
	}
	
	public static void Show(Model model){
		// list the statements in the Model
		StmtIterator iter = model.listStatements();
		
		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
			Statement stmt				= iter.nextStatement(); // get next statement
			Resource subject 	= stmt.getSubject();	// get the subject
			Property predicate			= stmt.getPredicate();	// get the predicate
			RDFNode object				= stmt.getObject();		// get the object
			
			System.out.print(subject.toString());
			System.out.print(" " + predicate.toString() + " ");
			
			if (object instanceof Resource){
				System.out.print(object.toString());
			} else {
				// object as a literal
				System.out.print(" \"" + object.toString() + " \"");
			}
			
			System.out.println(" .");
		}
	}
	
	public static void ShowAsXML(Model model){
		model.write(System.out, "RDF/XML-ABBREV");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Model m = ModelFactory.createDefaultModel();
//		String NS = "http://example.com/test/";
//		
//		Resource r = m.createResource(NS + "r");
//		Property p = m.createProperty(NS + "p");
//		
//		r.addProperty(p, "hello world", XSDDatatype.XSDstring);
//		
//		m.write(System.out, "Turtle");
		
		Model model = Modelization();
		Show(model);
		ShowAsXML(model);
	}
}
