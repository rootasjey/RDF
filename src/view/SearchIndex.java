package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;

import org.apache.lucene.queryparser.classic.ParseException;

import view.AddSource.SimpleFilter;
import data.struct.Indexer;
import data.struct.LoadData;

public class SearchIndex  extends JDialog {
	private JPanel mainPane			=	null;
	private JPanel northPane			=	null;
	private JPanel centralPanel		=	null;
	private String path					=	null;
	private JButton btnBrowse			=	null;
	private JTextPane jTextPane 		=	null;
	private JScrollPane jScrollPane1 = null;
	private JFileChooser fileChooser	=	null;
	private JTextField textFieldLink	=	null;
	
	public SearchIndex() {
		super();
		Initialize();
	}
	
	private JPanel getMain() {
		
		if (mainPane==null) {
			mainPane=new JPanel(new BorderLayout());
			mainPane.add(getNorth(), BorderLayout.NORTH);
			mainPane.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return mainPane;
	}
	
	private void Initialize() {
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int hauteur = (int)(tailleEcran.getHeight()*0.4);
		int largeur = (int)(tailleEcran.getWidth()*0.4);
		   
	   this.setTitle("Moteur de recherches");
	   this.setContentPane(getMain());
		   
       this.setSize(largeur, hauteur);
       this.setVisible(true);
       this.setLocationRelativeTo(null);
       this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//       this.setAlwaysOnTop(true);
	}
	

	private JPanel getNorth() {
		if(northPane==null) {
			northPane=new JPanel();
			northPane.add(getLink());
			northPane.add(getBtnBrowse());
		}
		return northPane;
	}
	
	
	private JTextField getLink() {
		if(textFieldLink == null) {
			textFieldLink = new JTextField(50);
//			textFieldLink.setEditable(false);
		}
		return textFieldLink;
	}
	
	private JButton getBtnBrowse() {
		if(btnBrowse == null){
			btnBrowse = new JButton("Recherche");
			btnBrowse.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						getJfc();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return btnBrowse;
	}
	
	private String getJfc() throws ParseException, IOException, InterruptedException {
		if	(fileChooser	==	null){
			fileChooser = new JFileChooser();
			
			FileFilter nt		= new SimpleFilter("N-TRIPLE", ".nt");
			FileFilter xml		= new SimpleFilter("XML", ".xml");
			FileFilter rdf 		= new SimpleFilter("RDF", ".rdf");
			FileFilter ttl		= new SimpleFilter("TURTLE", ".ttl");
			FileFilter n3		= new SimpleFilter("N3", ".txt");
			
			
			fileChooser.addChoosableFileFilter(rdf);
			fileChooser.addChoosableFileFilter(nt);
			fileChooser.addChoosableFileFilter(xml);
			fileChooser.addChoosableFileFilter(ttl);
			fileChooser.addChoosableFileFilter(n3);
			fileChooser.setAcceptAllFileFilterUsed(false);
			
			int retour = fileChooser.showOpenDialog(this);
			
			if (retour == JFileChooser.APPROVE_OPTION) {
				 path = fileChooser.getSelectedFile().getAbsolutePath(); // chemin absolu du fichier choisi 			 
				 textFieldLink.setText(path);
				 
				 LoadData loadData = new LoadData();
				 loadData.readRDFFile(jTextPane, path);
				 
			} else ; // pas de fichier choisi
			
			fileChooser = null;
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
	
	// Description et extension accept�e par le filtre
		public class SimpleFilter extends FileFilter{
			   private String description;
			   private String extension;
			   
			   // Constructeur � partir de la description et de l'extension accept�e
			   public SimpleFilter(String description, String extension){
			      if(description == null || extension == null){
			         throw new NullPointerException("La description (ou extension) ne peut �tre null.");
			      }
			      
			      this.description = description;
			      this.extension = extension;
			   }
			   
			   // Impl�mentation de FileFilter
			   public boolean accept(File file){
			      if (file.isDirectory()) { 
			         return true; 
			      } 
			      
			      String nomFichier = file.getName().toLowerCase(); 
			      	return nomFichier.endsWith(extension);
			   }
			      public String getDescription(){
			      return description;
			   }
		}
	
	public static void main(String[] args) {
		new SearchIndex();
	}
}

