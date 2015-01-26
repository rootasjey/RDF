package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.SystemException;

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
import com.hp.hpl.jena.sparql.pfunction.library.listIndex;
import com.hp.hpl.jena.vocabulary.VCARD;

import data.struct.Triplet;

public class Sparqlquery {
	public static String searchTerms; 													// contiendra les termes de la recherche
	public static ArrayList<Model>models = new ArrayList<Model>(); // contiendra les modèles du sous-graphe
	public static ArrayList<String> arrayTerms = new ArrayList<String>();
	
	static String[] keywords = {"country", "name", "length", "releaseDate"};
	
	// Méthode principale
	public static void basicStuff(List<Triplet> list) {
		createModel(list);
		getAllTerms();
		buildQuery();
		generateQuery();
	}
	
	public static void getAllTerms(){
		String[] terms = searchTerms.split("\\s");
		
		for (int i = 0; i < terms.length; i++) {
			arrayTerms.add(terms[i]);
			System.out.println(arrayTerms.get(i));
		}
	}
	
	public static void buildQuery() {
		String initialQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#> " +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
				"PREFIX movie: <http://www.example.org/Movie/> " +
				"PREFIX dbpedia: <http://es.dbpedia.org/page/> " +
				"SELECT ?whatever ?director " +
				"WHERE {" +
				"      ?resource foaf:name ?whatever . " +
				"      ?resource vcard:N ?director . " +
				"      FILTER regex(?whatever, \"" + arrayTerms.get(0) +"\", \"i\") . " +
				"      }";
		
		String where = "";
		String additionnalQuery = null;
		ArrayList<String> localKeywords = null;
		
		for (int i = 0; i < arrayTerms.size(); i++) {
			String term = arrayTerms.get(i);
			
			for (int j = 0; j < keywords.length; j++) {
				if (term.contains(keywords[j])) {
					System.out.println("toto");
					localKeywords.add(arrayTerms.get(i));
					where += "      ?resource foaf:name . ";
					where += arrayTerms.get(i+1);
					
				}
			}
		}
		
		System.out.println("where : " + where);
	}
	
	// Génère une requête SPARQL
	public static void generateQuery() {
		// Create a new query
		// String queryString = "SELECT ?x WHERE { ?x <http://www.w3.org/2001/vcard-rdf/3.0#LABEL> ?whatever }";
		
		// Create a new query
		String queryString = 
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
			"PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#> " +
			"SELECT ?whatever ?director " +
			"WHERE {" +
			"      ?resource vcard:LABEL ?whatever . " +
			"      ?resource vcard:N ?director . " +
			"      FILTER regex(?whatever, \"" + searchTerms +"\", \"i\") . " +
			"      }";
		
//		String select = "SELECT";
//		String whereBegin = "WHERE {";
//		String wehreEnd = "";
		
		// Union
//		for (int i = 0; i < arrayTerms.size(); i++) {
//			select += " ?" + arrayTerms.get(i);
//			queryString += "UNION { [] vcard:FN ? }";
//		}
		
		Query query = QueryFactory.create(queryString);

		for (int i = 0; i < models.size(); i++) {
			Model model = models.get(i);
			// model.write(System.out);
			
			// Execute the query and obtain results
			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet results = qe.execSelect();

			// Output query results	
			ResultSetFormatter.out(System.out, results, query);

			// Important - free up resources used running the query
			qe.close();
		}
		
	}
	
	// Crée un modèle
	public static void createModel(List<Triplet> list) {
		for (int i = 0; i < list.size(); i++) {
			Triplet triplet = list.get(i);
			String tripletResult = triplet.getObjet();
			String tripletProp = triplet.getPropriete();
			String tripletResource = triplet.getRessource();
			
//			 System.out.println("object : " + tripletResult);
//			 System.out.println("prop : " + tripletProp);
//			 System.out.println("ress : " + tripletResource);
			
			// Crée un modèle vide
			Model model = ModelFactory.createDefaultModel();
			
			if (tripletProp == null) {
				// On a un résultat contenant qu'une ressource
				// Crée la resource
				Resource resource = model.createResource(tripletResult).addProperty(VCARD.LABEL, tripletResult);
			}
			else {
				// Crée la resource
				Resource resource = model.createResource(tripletResource)
														.addProperty(VCARD.LABEL, tripletResult)
														.addProperty(VCARD.N, tripletResource);
			}
			
			models.add(model);			// ajoute un modèle à la liste
//			model.write(System.out);		// test affichage
		}
	}
	
	// Méthode de test
	public static void initialize(List<Triplet> list) throws IOException {		
		// Open the bloggers RDF graph from the filesystem
		InputStream in = new FileInputStream(new File("C:\\Users\\Jeremie\\Documents\\dev\\java\\Resources\\sparkl\\vc-db-1.rdf"));

		// Create an empty in-memory model and populate it from the graph
		Model model = ModelFactory.createMemModelMaker().createModel("test");
		model.read(in,null); // null base URI, since model URIs are absolute
//		model.write(System.out);
		in.close();
		
		// Create a new query
		String queryString = "SELECT ?x WHERE { ?x <http://www.w3.org/2001/vcard-rdf/3.0#FN> ?whatever }";
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
