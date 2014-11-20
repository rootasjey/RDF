package data.struct;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import jenaadapter.GraphStreamJena;
import jenaadapter.JenaUtils;

import org.apache.lucene.queryparser.classic.ParseException;
import org.graphstream.graph.Graph;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
//import org.apache.lucene.store.RAMDirectory;


public class LoadData {

	Indexer indx = new Indexer();
	
	public void readRDFFile(JTextPane editeur,String path1) throws ParseException, IOException, InterruptedException{
		
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
        
        
        GraphStreamJena jenaAdapter=new GraphStreamJena(model);
        Graph graph=jenaAdapter.buildGraph();
       
        JenaUtils.setLastFile(inputFileName);
    
        
	
			try {
	        	
				 int j=0;
			        // print out the predicate, subject and object of each statement
			        while (iter.hasNext()) {
			            Statement stmt      = iter.nextStatement();         // get next statement
			            Resource  subject   = stmt.getSubject();   // get the subject
			            Property  predicate = stmt.getPredicate(); // get the predicate
			            RDFNode   object    = stmt.getObject();    // get the object
			            j++;
			            try{
			            	/***********************************************************/
			        		/*				Partie INDEXATION						   */
			        		
			            			//indx.Indexer_Doc(predicate, subject,object);
			            	indx.Indexer_Doc(predicate, subject,object);
			            			
			            			
			            	/*				Fin d'INDEXATION						   */
			            	/***********************************************************/
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
			        graph.display();
				    indx.closeWriter();
				//   jenaAdapter.viewResultNode(new Indexer().SearchWithIndex("Smith"));
			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
      
	}

}
