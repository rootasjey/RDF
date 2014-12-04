package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.VCARD;

import data.struct.Triplet;

public class Sparqlquery {
	public static ArrayList<String> resourcesArrayList = new ArrayList<String>();
	public ArrayList<String> propertiesArrayList = new ArrayList<String>();
	
	public void generateQuery() {
		
	}
	
	public static void createModel(List<Triplet> list) {
		for (int i = 0; i < list.size(); i++) {
			Triplet triplet = list.get(i);
			String tripletResult = triplet.getObjet();
			String tripletProp = triplet.getObjet();
			String tripletResource = triplet.getRessource();
			
//			System.out.println("object : " + triplet.getObjet());
//			System.out.println("prop : " + triplet.getPropriete());
//			System.out.println("ress : " + triplet.getRessource());
			
			// Crée un modèle vide
			Model model = ModelFactory.createDefaultModel();
			
			if (tripletProp == null) {
				// On a un résultat contenant qu'une ressource
				// Crée la resource
				Resource resource = model.createResource(tripletResult).addProperty(VCARD.FN, tripletResult);
			}
			else {
				// Crée la resource
				Resource resource = model.createResource(tripletResource).addProperty(VCARD.LABEL, tripletResult);
			}
			
			model.write(System.out);
		}
	}
	
	public static void initialize(List<Triplet> list) throws IOException {		
		// Open the bloggers RDF graph from the filesystem
		InputStream in = new FileInputStream(new File("C:\\Users\\Jeremie\\Documents\\dev\\java\\Resources\\sparkl\\vc-db-1.rdf"));

		// Create an empty in-memory model and populate it from the graph
		Model model = ModelFactory.createMemModelMaker().createModel("test");
		model.read(in,null); // null base URI, since model URIs are absolute
		in.close();
		
		// Create a new query
		String queryString = "SELECT ?x WHERE { ?x <http://www.w3.org/2001/vcard-rdf/3.0#FN> ?fname }";
//		String queryString2 = "SELECT ?x WHERE { ?x <http://www.w3.org/2001/vcard-rdf/3.0#Family> \"Smith\" }";
		
		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		// Output query results	
		ResultSetFormatter.out(System.out, results, query);

		// Important - free up resources used running the query
		qe.close();
	}
}
