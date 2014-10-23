package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;
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

@SuppressWarnings("serial")
public class AddSource extends JDialog{

	private JPanel panelPrincipal=null;
	private JPanel north=null;
	private JPanel central=null;
	private JFileChooser jfc=null;
	private JTextField link=null;
	private String path=null;
	private JButton btn=null;
	private JTextPane jTextPane = null;
	private JScrollPane jScrollPane1 = null;

	
	public AddSource(){
		super();
		initialize();
	}
	
	
	private JPanel getPrincipale(){
		
		if(panelPrincipal==null){
			panelPrincipal=new JPanel(new BorderLayout());
			panelPrincipal.add(getNorth(),BorderLayout.NORTH);
			panelPrincipal.add(getJScrollPane(),BorderLayout.CENTER);
			
		}
		
		return panelPrincipal;
	}
	
	
	
	
	
	private void initialize(){
		
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		   int hauteur = (int)(tailleEcran.getHeight()*0.5);
		   int largeur = (int)(tailleEcran.getWidth()*0.6);
		   
		   this.setTitle("Moteur de recherches");
		   this.setContentPane(getPrincipale());
		   
	       this.setSize(largeur, hauteur);
	       this.setVisible(true);
	       this.setLocationRelativeTo(null);
	       this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	       this.setAlwaysOnTop(true);
	       
	       
	}
	
	private JPanel getNorth(){
		
		if(north==null){
			north=new JPanel();
			north.add(getLink());
			north.add(getBoutonParcour());
			
			
		}
		return north;
	}
	
	
	
	private JTextField getLink(){
		if(link==null){
			link=new JTextField(50);
			link.setEditable(false);
			
			
		}
		
		return link;
	}
	
	private JButton getBoutonParcour(){
		if(btn==null){
			btn=new JButton("Parcourir");
			btn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					getJfc();
					
				}
			});
			
		}
		return btn;
	}
	
	private String getJfc()
	{

		if	(jfc	==	null){
			jfc = new JFileChooser();
			
			FileFilter nt 		= new FiltreSimple("N-TRIPLE",".nt");
			FileFilter xml 	= new FiltreSimple("XML", ".xml");
			FileFilter rdf 	= new FiltreSimple("RDF", ".rdf");
			FileFilter ttl 	= new FiltreSimple("TURTLE", ".ttl");
			FileFilter n3 	= new FiltreSimple("N3", ".txt");
			
			jfc.addChoosableFileFilter(nt);
			jfc.addChoosableFileFilter(xml);
			jfc.addChoosableFileFilter(rdf);
			jfc.addChoosableFileFilter(ttl);
			jfc.addChoosableFileFilter(n3);
			jfc.setAcceptAllFileFilterUsed(false);


			
			int retour = jfc.showOpenDialog(this);
			if (retour == JFileChooser.APPROVE_OPTION) {

				 path=jfc.getSelectedFile().getAbsolutePath();// chemin absolu du fichier choisi 			 
				 link.setText(path);
				 
				 readRDFFile(jTextPane,path);
				 
	
			} else ; // pas de fichier choisi
			jfc=null;
			
		}
		 
		return path;
	}
  
	

	private  JTextPane jTextPane() {
		
		if (jTextPane == null) {
			jTextPane = new JTextPane();
			jTextPane.setEditable(false);
			
		}
		return jTextPane;
	}


	private JScrollPane getJScrollPane() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(jTextPane());
		}
		return jScrollPane1;
	}
	
	
	
	public void readRDFFile(JTextPane editeur,String path1){
		
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
        StyledDocument Doc = (StyledDocument)jTextPane.getDocument();
        Style defaut = editeur.getStyle("default");
        Style style1 = editeur.addStyle("style1", defaut);
        StyleConstants.setFontFamily(style1, "Comic sans MS");
        Style style2 = editeur.addStyle("style2", style1);
        StyleConstants.setForeground(style2, Color.RED);
        StyleConstants.setFontSize(style2, 22);
        
        Style style3 = editeur.addStyle("style3", style1);
        StyleConstants.setForeground(style3, Color.BLUE);
        StyleConstants.setFontSize(style3, 22);
        
        
        int i=0;
        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();         // get next statement
            Resource  subject   = stmt.getSubject();   // get the subject
            Property  predicate = stmt.getPredicate(); // get the predicate
            RDFNode   object    = stmt.getObject();    // get the object
            i++;
            try{
            	
            	Doc.insertString(Doc.getLength(), i+"=>\n", style3);
            	 Doc.insertString(Doc.getLength(),subject.toString()+"\n", style2);
            	 Doc.insertString(Doc.getLength(), predicate.toString()+"\n", style1);
            	 
            	 if (object instanceof Resource) {

                     Doc.insertString(Doc.getLength(),object.toString()+"\n\n", defaut);
                 } else {
                     // object is a literal
                     Doc.insertString(Doc.getLength()," \"" + object.toString() + "\""+"\n\n", defaut);
                 }
            	
            }
            catch(BadLocationException e){
            	e.printStackTrace();
            	
            }

        
        }
        
		
		
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new AddSource();

	}
	
	
	
	public class FiltreSimple extends FileFilter{
		   //Description et extension acceptée par le filtre
		   private String description;
		   private String extension;
		   //Constructeur à partir de la description et de l'extension acceptée
		   public FiltreSimple(String description, String extension){
		      if(description == null || extension ==null){
		         throw new NullPointerException("La description (ou extension) ne peut être null.");
		      }
		      this.description = description;
		      this.extension = extension;
		   }
		   //Implémentation de FileFilter
		   public boolean accept(File file){
		      if(file.isDirectory()) { 
		         return true; 
		      } 
		      String nomFichier = file.getName().toLowerCase(); 
		 
		      return nomFichier.endsWith(extension);
		   }
		      public String getDescription(){
		      return description;
		   }
		}

}
