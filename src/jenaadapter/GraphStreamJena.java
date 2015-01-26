package jenaadapter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.queries.function.valuesource.IfFunction;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
//import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

import com.hp.hpl.jena.rdf.model.Model;
//import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import data.struct.Triplet;

public class GraphStreamJena {

	private Model model;
	private MultiGraph graph;
	
	
	

	/**
	 * @param model
	 */
	public GraphStreamJena(Model model) {
		this.model = model;
		graph=new  MultiGraph("jena");
		
	}
	
	
	public MultiGraph buildGraph() throws IOException, InterruptedException{
		
		this.addNode();

		        		
		return graph;
		
	}
	
	
	
	public void addNode()throws IOException, InterruptedException {
		
		
		 String styleSheet= "node.important {"+
				   " fill-color: green;"+
				   " size: 40px;"+
				   "}";
		 graph.addAttribute("ui.stylesheet", styleSheet);
		// list the statements in the graph
        StmtIterator iter = model.listStatements();

    
		    // print out the predicate, subject and object of each statement
		    while (iter.hasNext()) {
		        Statement stmt      = iter.nextStatement();         // get next statement
		        Resource  subject   = stmt.getSubject();   // get the subject
		   //     Property  predicate = stmt.getPredicate(); // get the predicate
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
		        int cost=1;
		        
	            if (object instanceof Resource) {
	            	graph.getNode(object.toString()).addAttribute("ui.typeNoeud","ressource");
		        	graph.getNode(object.toString()).addAttribute("ui.label", "ressource");
		        	graph.getNode(object.toString()).addAttribute("ui.style", "fill-color: rgb(51,51,153);");
		        	graph.getNode(object.toString()).addAttribute("ui.size",40);
		        	
		        	cost=2;
	            	
	            }
	            else{
	            	///if ressource is attribute
	            	
	            	graph.getNode(object.toString()).addAttribute("ui.typeNoeud","literal");
	            	graph.getNode(object.toString()).addAttribute("ui.label",object.toString());
	            	graph.getNode(object.toString()).addAttribute("ui.style", "fill-color: rgb(255,255,102);");
	            	
	            }
	            
	            if(graph.getEdge("arete"+subject.toString()+"=>"+object.toString()+"fin arrete")==null){
	            	
	            	graph.addEdge("arete"+subject.toString()+"=>"+object.toString()+"fin arrete", subject.toString(),object.toString()).addAttribute("length", cost);;
	            }
	            
		       
		        	
		    }

	}
	
	
	public void viewResultNode(List<Triplet> resultat){
		
		Node current;
		for(Triplet triple: resultat){
			
			current=graph.getNode(triple.getObjet());
			
			if (current != null) {
				current.addAttribute("ui.class", "important");
				//current.addAttribute("ui.style", "fill-color: rgb(200,167,153);");
			}
			
		}
		
	}
	
	
	public void printShortPath(List<Triplet> list){
		
		Dijkstra dijkstra= new Dijkstra(Dijkstra.Element.EDGE,null,"length");
		dijkstra.init(graph);
		dijkstra.setSource(graph.getNode(list.get(0).getObjet()));
		dijkstra.compute();
		for(int i=1;i<list.size();i++){
			Iterator<Edge> ite = graph.getEdgeIterator();
			
//				if (list.get(i) == null) {
//					System.out.println("TTOOOTOT");
//				}
		       ite= (Iterator<Edge>) dijkstra.getPathEdgesIterator(graph.getNode(list.get(i).getObjet()));
		       while(ite.hasNext())
		       {
		    	   Edge edge=ite.next();
		    	   edge.addAttribute("ui.style", "fill-color: red;");
		    	   edge.addAttribute("ui.style", "size: 3;");
		       }
		}
		
                // edge.addAttribute("ui.style", "fill-color: red;");
		
		
		
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

	public  void setGraph(MultiGraph graph) {
		this.graph = graph;
	}

	public final Model getModel() {
		return model;
	}

	public final void setModel(Model model) {
		this.model = model;
	}
	
	
	
}
