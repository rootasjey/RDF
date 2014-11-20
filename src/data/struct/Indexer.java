
package data.struct;


import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;





import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Indexer {
	static final File INDEX_DIR = new File("c:\\Temp\\index_test2");
	Directory dir;
	
	StandardAnalyzer analyzer;
	IndexWriterConfig config;
	 
	IndexWriter w;
	
	Query q;
	Query q2;
	Query q3;
	IndexReader reader;
	IndexSearcher searcher;
	
	public Indexer(){
		try {
			dir = FSDirectory.open(INDEX_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		analyzer= new StandardAnalyzer(Version.LUCENE_44);
		config= new IndexWriterConfig(Version.LUCENE_44, analyzer);
		
		try {
			w = new IndexWriter(dir, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Indexe une ressource et une propriété
	public void Indexer_Doc(Resource ress, Property prop) throws IOException{
		try {
			addDoc(w, ress.toString(), prop.toString()); // la proprite et sa valeur seront mises dans Document
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Indexe une ressource, une propriété et un littéral
	public void Indexer_Doc(Resource ress, Property prop, RDFNode litteral) throws IOException{
		try {
			addDoc(w, ress.toString(), prop.toString(), litteral.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Recherche sur l'index des propriété, et affichage dans la console
	public void SearchWithIndex(String querystr){
		try {
			w.close();
			q = new QueryParser(Version.LUCENE_44, "propriete", analyzer).parse(querystr);
			
			int hitsPerPage = 2000;

		    reader = DirectoryReader.open(dir);
		    
		    searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(q, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    
		    //	Code to display the results of search
		     System.out.println("Found " + hits.length + " hits.");
		    
		    for(int i=0;i<hits.length;++i) 
		    {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      System.out.println((i + 1) + ". " + d.get("propriete") + "\t" + d.get("valeur"));
		    }
		    
		    // reader can only be closed when there is no need to access the documents any more
		    reader.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Recherche sur l'index du triplet, et affichage dans le JTextPane (paramètre)
	public void SearchWithIndex(String querystr, JTextPane editeur){
		try {
			w.close();
			q = new QueryParser(Version.LUCENE_44, "property", analyzer).parse(querystr);
			q2 = new QueryParser(Version.LUCENE_44, "resource", analyzer).parse(querystr);
			q3 = new QueryParser(Version.LUCENE_44, "litteral", analyzer).parse(querystr);
			
			int hitsPerPage = 2000;

		    reader = DirectoryReader.open(dir);
		    
		    searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(q, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    
		    // --------Resource
		    TopScoreDocCollector collector2 = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(q2, collector2);
		    ScoreDoc[] hits2 = collector2.topDocs().scoreDocs;
		    // --------
		    
		 // --------Litteral
		    TopScoreDocCollector collector3 = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(q3, collector3);
		    ScoreDoc[] hits3 = collector3.topDocs().scoreDocs;
		    // --------
		    
		    //	Code to display the results of search
		    String textFound 	= "Found " + hits.length + " hits in propertie. \n";
		    String textFound2 	= "Found " + hits2.length + " hits in resources. \n";
		    String textFound3 	= "Found " + hits3.length + " hits in litterals. \n";
		    
		    String lines 	= "";
		    String lines2 	= "";
		    String lines3 	= "";
		    
		    for(int i=0; i<hits.length; ++i) 
		    {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      lines += (i + 1) + ". " + d.get("property") + "\t" + d.get("resource") + "\n";
		    }
		    
		    for(int i=0; i<hits2.length; ++i) 
		    {
		      int docId = hits2[i].doc;
		      Document d = searcher.doc(docId);
		      lines2 += (i + 1) + ". " + d.get("resource")+ "\n";
		    }
		    
		    for(int i=0; i<hits3.length; ++i) 
		    {
		      int docId = hits3[i].doc;
		      Document d = searcher.doc(docId);
		      lines3 += (i + 1) + ". " + d.get("litteral") + "\t" + d.get("resource") + "\n";
		    }
		    
		    // Affiche les résultats dans le JTextPane
		    editeur.setText(textFound + textFound2 + textFound3 + lines + lines2 + lines3);
		    
		    // reader can only be closed when there is no need to access the documents any more
		    reader.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Création d'un document pour l'indexation de la ressource et de la propriété
	private static void addDoc(IndexWriter w, String ress, String prop) throws IOException 
	{
		/*	Document :
		  	 Prop		Ress	
		 	-------- |------------  
		 	 XXX 	 |	YYY	
		 	---------------------- */
		
		Document doc = new Document();
		doc.add(new TextField("property", prop, Field.Store.YES));
		doc.add(new TextField("resource", ress, Field.Store.YES));
		  
		w.addDocument(doc);
	}
	
	// Création d'un document pour l'indexation de la ressource, de la propriété et du litteral
	private static void addDoc(IndexWriter w, String ress, String prop, String litteral) throws IOException 
	{		
		Document doc = new Document();
		doc.add(new TextField("property", prop, Field.Store.YES));
		doc.add(new TextField("resource", ress, Field.Store.YES));
		doc.add(new TextField("litteral", litteral, Field.Store.YES));
		
		w.addDocument(doc);
	}
}
