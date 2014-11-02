package data.struct;

import java.awt.Color;
import java.io.File;
import java.io.InputStream;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

import java.io.IOException;

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


public class LoadData {
	static final File INDEX_DIR = new File("c:\\Temp\\index_test");
	
	public void readRDFFile(JTextPane editeur,String path1) throws ParseException{
		
		Model model = ModelFactory.createDefaultModel();
		String inputFileName =path1;
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
		
		//model.write(System.out);
        // list the statements in the graph
        StmtIterator iter = model.listStatements();
        
        editeur.setText("");
        StyledDocument Doc = (StyledDocument)editeur.getDocument();
        Style defaut = editeur.getStyle("default");
        Style style1 = editeur.addStyle("style1", defaut);
        StyleConstants.setFontFamily(style1, "Comic sans MS");
        Style style2 = editeur.addStyle("style2", style1);
        StyleConstants.setForeground(style2, Color.RED);
        StyleConstants.setFontSize(style2, 22);
        
        Style style3 = editeur.addStyle("style3", style1);
        StyleConstants.setForeground(style3, Color.BLUE);
        StyleConstants.setFontSize(style3, 22);
        
        /***********************************************************/
		/*				Partie INDEXATION						   */
		/***********************************************************/
        try {
			Directory dir = FSDirectory.open(INDEX_DIR);
		
			try {
	       
		        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
		        
		        //Directory index = new RAMDirectory();
		        
		        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
			
				IndexWriter w = new IndexWriter(dir, config);
				
				
				 int j=0;
			        // print out the predicate, subject and object of each statement
			        while (iter.hasNext()) {
			            Statement stmt      = iter.nextStatement();         // get next statement
			            Resource  subject   = stmt.getSubject();   // get the subject
			            Property  predicate = stmt.getPredicate(); // get the predicate
			            RDFNode   object    = stmt.getObject();    // get the object
			            j++;
			            try{
			            	//INDEXATION
			            	addDoc(w, predicate.toString(), subject.toString()); // la proprite et sa valeur seront mises dans Document
			            	
			            	 Doc.insertString(Doc.getLength(), j+"=>\n", style3);
			            	 Doc.insertString(Doc.getLength(),subject.toString()+" ==> La Ressource \n", style2);
			            	 Doc.insertString(Doc.getLength(), predicate.toString()+" ==> Propriete \n", style1);
			            	 
			            	 if (object instanceof Resource) {
			            		 //addDoc(w, predicate.toString(), object.toString());
			                     Doc.insertString(Doc.getLength(),object.toString()+" ==> Ressource de Ressource\n\n", defaut);
			                 } else {
			                     // object is a literal
			                     Doc.insertString(Doc.getLength()," \"" + object.toString() + "\""+" ==> Le litérale\n\n", defaut);
			                 }
			            	
			            }
			            catch(BadLocationException e){
			            	e.printStackTrace();
			            	
			            }
			        }
				w.close();
				/***********************************************************/
				/*				Partie Recherche						   */
				/***********************************************************/
				// Text to search
				String querystr = "nom";
				
				//	The \"title\" arg specifies the default field to use when no field is explicitly specified in the query
				Query q = new QueryParser(Version.LUCENE_44, "propriete", analyzer).parse(querystr);
				
				// Searching code
							int hitsPerPage = 2000;
							//IndexReader ir=IndexReader.open(FSDirectory.open(new File(INDEX_DIR)));
						    IndexReader reader = DirectoryReader.open(dir);
						    IndexSearcher searcher = new IndexSearcher(reader);
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
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        
        } catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
        
        
        
       
	}
	
	private static void addDoc(IndexWriter w, String s1, String s2) throws IOException 
	{
		  Document doc = new Document();
		  // A text field will be tokenized
		  doc.add(new TextField("propriete", s1, Field.Store.YES));
		  // We use a string field for isbn because we don\'t want it tokenized
		  doc.add(new StringField("valeur", s2, Field.Store.YES));
		  w.addDocument(doc);
	}
}
