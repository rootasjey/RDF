/*
 * 	La calsse est imcompléte encore .... 
 *	En cours : La séparation entre l'indexation est la lecture du fichier RDF
 */

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

public class Indexer {
	static final File INDEX_DIR = new File("c:\\Temp\\index_test");
	Directory dir;// = FSDirectory.open(INDEX_DIR);
	
	StandardAnalyzer analyzer;// = new StandardAnalyzer(Version.LUCENE_44);
	IndexWriterConfig config;// = new IndexWriterConfig(Version.LUCENE_44, analyzer);
	 
	IndexWriter w;// = new IndexWriter(dir, config);
	
	Query q;
	IndexReader reader;
	IndexSearcher searcher;// = new IndexSearcher(reader);
	
	public Indexer()throws IOException{
		dir = FSDirectory.open(INDEX_DIR);
		analyzer= new StandardAnalyzer(Version.LUCENE_44);
		config= new IndexWriterConfig(Version.LUCENE_44, analyzer);
		w = new IndexWriter(dir, config);
		
	}
	
	public void Indexer_Doc(Property prp, Resource ress) throws IOException{
		try {
			
			addDoc(w, prp.toString(), ress.toString()); // la proprite et sa valeur seront mises dans Document
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		w.close();
		
	}
	
	public void SearchWithIndex(String querystr){
		try {
			q = new QueryParser(Version.LUCENE_44, "propriete", analyzer).parse(querystr);
			
			int hitsPerPage = 2000;

		    reader = DirectoryReader.open(dir);
		    
		    searcher = new IndexSearcher(reader);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void addDoc(IndexWriter w, String title, String isbn) throws IOException 
	{
		  Document doc = new Document();
		  // A text field will be tokenized
		  doc.add(new TextField("propriete", title, Field.Store.YES));
		  // We use a string field for isbn because we don\'t want it tokenized
		  doc.add(new StringField("valeur", isbn, Field.Store.YES));
		  w.addDocument(doc);
	}
}
