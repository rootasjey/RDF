package jenaadapter;

import java.io.IOException;
import java.util.UUID;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class GraphStreamJena {

	private Model model;
	private Graph graph;
	
	
	

	/**
	 * @param model
	 */
	public GraphStreamJena(Model model) {
		this.model = model;
		graph=new  SingleGraph("jena");
		
	}
	
	
	public Graph buildGraph() throws IOException, InterruptedException{
		
		this.addNode();

		        		
		return graph;
		
	}
	
	
	
	public void addNode()throws IOException, InterruptedException {
		
		
		
		// list the statements in the graph
        StmtIterator iter = model.listStatements();

    
		    // print out the predicate, subject and object of each statement
		    while (iter.hasNext()) {
		        Statement stmt      = iter.nextStatement();         // get next statement
		        Resource  subject   = stmt.getSubject();   // get the subject
		        Property  predicate = stmt.getPredicate(); // get the predicate
		        RDFNode   object    = stmt.getObject();    // get the object

		        if(!existNode(subject.toString()))
		        {
		        	graph.addNode(subject.toString());
		        	graph.getNode(subject.toString()).addAttribute("ui.label", subject.toString());
		        	graph.getNode(subject.toString()).addAttribute("ui.style", "fill-color: rgb(51,51,153);");
		        }
		        

		        if(!existNode(object.toString()))
		        {
		        	graph.addNode(object.toString());
		        
		        }
		        
	            if (object instanceof Resource) {
	            	graph.getNode(object.toString()).addAttribute("ui.typeNoeud","ressource");
		        	graph.getNode(object.toString()).addAttribute("ui.label", "ressource");
		        	graph.getNode(object.toString()).addAttribute("ui.style", "fill-color: rgb(51,51,153);");
	            	
	            }
	            else{
	            	///if ressource is attribute
	            	
	            	graph.getNode(object.toString()).addAttribute("ui.typeNoeud","literal");
	            	graph.getNode(object.toString()).addAttribute("ui.label",object.toString());
	            	graph.getNode(object.toString()).addAttribute("ui.style", "fill-color: rgb(255,255,102);");
	            	
	            }
	            
	            if(graph.getEdge("arete"+subject.toString()+"=>"+object.toString()+"fin arrete")==null){
	            	
	            	graph.addEdge("arete"+subject.toString()+"=>"+object.toString()+"fin arrete", subject.toString(),object.toString());
	            }
	            
		       
		        	
		    }

	}
	
	
	public boolean existNode(String id_node){
		
		Node A =null;
		A=graph.getNode(id_node);
		
		if(A==null)
			return false;
		return true;
	}
	

	public  Graph getGraph() {
		return graph;
	}

	public  void setGraph(Graph graph) {
		this.graph = graph;
	}

	public final Model getModel() {
		return model;
	}

	public final void setModel(Model model) {
		this.model = model;
	}
	
	
	
}
