package data.struct;


import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;




import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Indexer {
	static final File INDEX_DIR = new File("c:\\Temp\\index_test");
	Directory dir;
	
	StandardAnalyzer analyzer;
	IndexWriterConfig config;
	 
	IndexWriter w;
	

	Query q,q2, q3;

	IndexReader reader;
	IndexSearcher searcher;
	
	List<Triplet> resultat,resultat2;
	
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
	

	public void Indexer_Doc(Property prp, Resource ress, RDFNode object) throws IOException{
		try {
			// addDoc(w, ress.toString(), prp.toString()); // la proprite et sa valeur seront mises dans Document
			

			addDoc(w, prp.toString(), ress.toString(),object.toString()); // la proprite et sa valeur seront mises dans Document
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Triplet> SearchWithIndex(String querystr){
		try {
			
			q = new QueryParser(Version.LUCENE_44, "literale", analyzer).parse(querystr);
			q2 = new QueryParser(Version.LUCENE_44, "ressource", analyzer).parse(querystr);
			
			int hitsPerPage = 2000;

		    reader = DirectoryReader.open(dir);
		    


		    
		    //tableau resultat
		    resultat=new ArrayList<Triplet>();
		    
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
//		      resultat.add(new Triplet(d.get("literale")));
		      
		      Triplet triplet = new Triplet();
		      triplet.setObjet(d.get("literale"));
		      triplet.setPropriete(d.get("propriete"));
		      triplet.setRessource(d.get("ressource"));
		      resultat.add(triplet);
		      
		      System.out.println((i + 1) + ". " + d.get("propriete") + "\t" + d.get("literale"));
		    }
		    
		    
		    ///recheche dans les ressource
		    resultat2=new ArrayList<Triplet>();
		    collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(q2, collector);
		    hits = collector.topDocs().scoreDocs;
		    
		    for(int i=0;i<hits.length;++i) 
		    {
		    	
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      resultat2.add(new Triplet(d.get("ressource")));
		     
		    }
		    
		    // reader can only be closed when there is no need to access the documents any more
		    reader.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Triplet> result=new ArrayList<Triplet>(resultat.size()+resultat2.size());
		result.addAll(resultat);
		result.addAll(resultat2);
		
		return result;
	}
	

	private static void addDoc(IndexWriter w, String prop, String ress,String lite) throws IOException 
	{
		/*	Document :
		  	 Prop		Ress	
		 	-------- |------------  
		 	 XXX 	 |	YYY	
		 	---------------------- */
		
		Document doc = new Document();

		doc.add(new TextField("ressource", ress, Field.Store.YES));
		doc.add(new TextField("propriete", prop, Field.Store.YES));
		doc.add(new TextField("literale", lite, Field.Store.YES));
		
		System.out.println(" {"+ress+" "+prop+""+ lite+" }");
		
		  
		w.addDocument(doc);
	}
	

	public void closeWriter() throws IOException{
		w.close();
	}
}
