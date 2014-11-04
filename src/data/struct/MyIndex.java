package data.struct;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

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

public class MyIndex {
	static final File DIR = new File("c:\\Temp");
	Directory director;
	
	StandardAnalyzer analyzer;
	IndexWriterConfig myconfigur;
	 
	IndexWriter iwrit;
	
	Query query;
	IndexReader ireader;
	IndexSearcher isearcher;
	
	public MyIndex()throws IOException{
		director = FSDirectory.open(DIR);// crée le fichier sur le disque
		analyzer= new StandardAnalyzer(Version.LUCENE_44);
		myconfigur= new IndexWriterConfig(Version.LUCENE_44, analyzer);
		iwrit = new IndexWriter(director, myconfigur);
		
	}
	
	public void Indexer_Doc(Property prp, Resource ress) throws IOException{
		try {
			
			addDoc(iwrit, prp.toString(), ress.toString()); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iwrit.close();
		
	}
	
	public void SearchWithIndex(String querystr){
		try {
			query = new QueryParser(Version.LUCENE_44, "propriete", analyzer).parse(querystr);
			
			int hitsPerPage = 2000;

		    ireader = DirectoryReader.open(director);
		    
		    isearcher = new IndexSearcher(ireader);
		    isearcher = new IndexSearcher(ireader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    isearcher.search(query, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    
		    //	Code to display the results of search
		    System.out.println("Found " + hits.length + " hits.");
		    for(int i=0;i<hits.length;++i) 
		    {
		      int docId = hits[i].doc;
		      Document docu = isearcher.doc(docId);
		      System.out.println((i + 1) + ". " + docu.get("propriete") + "\t" + docu.get("valeur"));
		    }
		    
		    // reader can only be closed when there is no need to access the documents any more
		    ireader.close();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void addDoc(IndexWriter iwrit, String title, String occ) throws IOException 
	{
		  Document docum = new Document();
		  // A text field will be tokenized
		  docum.add(new TextField("propriete", title, Field.Store.YES));
		  // We use a string field for isbn because we don\'t want it tokenized
		  docum.add(new StringField("valeur", occ, Field.Store.YES));
		  iwrit.addDocument(docum);
	}
}