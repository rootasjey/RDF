package jenaadapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class JenaUtils {
public static Model loadJenaModel(){
		
		Model model = ModelFactory.createDefaultModel();
		String inputFileName =JenaUtils.getLastFile();
		InputStream in = FileManager.get().open(inputFileName);
		if(in == null){
			throw new IllegalArgumentException("File: "+inputFileName+" not found");
		}
 
		System.out.println(inputFileName);
		if (inputFileName.endsWith(".nt")) {
				model.read(in, "", "N-TRIPLE");
		}
		else if (inputFileName.endsWith(".xml") || inputFileName.endsWith(".rdf")) {
				model.read(in, "", "RDF/XML");
		}
		else if (inputFileName.endsWith(".ttl")) {
				model.read(in, "", "TURTLE");
		}
		else if (inputFileName.endsWith(".n3") || inputFileName.endsWith(".txt")) {
			model.read(in, "", "N3");
	}
		return model;
	}
	
	
	
	
	
	public static String getLastFile(){
		String NomFichier = "c:\\Temp\\last.txt";
		String path=null;
	    try{
	      BufferedReader in  = new BufferedReader(new FileReader(NomFichier));
	      String line;
	 
	        while ((line = in.readLine()) != null) {
	          path=line;
	      
	        }
	      in.close();
	    }
	    catch(Exception e){
	      e.printStackTrace();
	    }
		
		return path;
	}
	
	public static void setLastFile(String path){
		
		String NomFichier = "c:\\Temp\\last.txt";
	    try{
	      PrintWriter out  = new PrintWriter(new FileWriter(NomFichier));
	   
	        out.println(path);
	      out.close();
	    }
	    catch(Exception e){
	      e.printStackTrace();
	    }
	}
	
	

	
	

}
