package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jenaadapter.GraphStreamJena;
import jenaadapter.JenaUtils;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

import data.struct.Indexer;
import data.struct.Triplet;


@SuppressWarnings("serial")
public class Main extends JFrame implements ComponentListener {
	
	private JMenuBar jJMenuBar = null;
	private JMenu parametre = null;
	private JMenu quitter = null;
	private JMenu propos = null;
	private JMenuItem addSourceItem =null;
	private JPanel panelPrincipal=null;
	private JPanel panelNord=null;
	private JPanel panelNord1=null;
	private JPanel panelNord2=null;
	private JPanel panelCentre=null;
	private JPanel panelWest=null;
	private JTextField  recherche=null;
	private JButton  bRecherche=null;
	private JFrame fen=null;
	private Viewer viewer=null;
	private View view=null;
	private JMenuItem searchIndexItem	= null;
	//private JScrollPane jScrollPane = null;
	
	//	Panels filles
	private AddSource addSourcePanel 	=null;
	private SearchIndex searchIndexPanel= null;
	
	// Objet indexer global
//	public static Indexer indexer 				= new Indexer();
	
	// Construit le menu de la fenêtre principale
	private JMenuBar getMenu(){
		if(jJMenuBar==null){
			jJMenuBar=new JMenuBar();
			jJMenuBar.add(getParametre());
			jJMenuBar.add(getQuitter());
			jJMenuBar.add(getApropos());
		}
		return jJMenuBar;
	}
	
	// Construit la fenetre d'informations
	private JMenu getApropos(){
		if(propos==null){
			propos=new JMenu("A propos");
			
			propos.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					//lance la fenêtre à propos
					new Help();
				}
			});
		}
		return propos;
	}
	
	// Ajoute des items au menu "Paramètres"
	private JMenu getParametre(){
		if(parametre==null){
			parametre=new JMenu("Parametre");
            parametre.add(getAddSouce());
            parametre.add(getSearchIndex());
		}
		return parametre;
	}
	
	// Construit le menu pour quitter (et gère l'évènement de click)
	private JMenu getQuitter(){
		if(quitter==null){
			quitter =new JMenu("Quitter");
			quitter.addMouseListener(new MouseAdapter() {
				
				public void mouseClicked(MouseEvent e) {
					//fermer la fenetre
                  JOptionPane pOption=new JOptionPane();
                  
                  int opt=pOption.showConfirmDialog(null, "voulez vous quitter l'application ?", "confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  
                  if(opt==JOptionPane.YES_OPTION){
                	  fen.dispose();
                  }
				}
			});
			
		}
		
		return quitter;
	}
	
	// Ajoute le menu d'ajout d'une source (ainsi que l'évènement de click)
	private JMenuItem getAddSouce(){
		    if(addSourceItem == null){
		    	addSourceItem = new JMenuItem("ajouter source");
		    	
		    	// Evénement de click
		    	addSourceItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (addSourcePanel == null) {
							// Crée une nouvelle fenêtre
							addSourcePanel = new AddSource();
							addSourcePanel.requestFocus();
						}
						else {
							// Affiche la fenêtre et met le focus
							addSourcePanel.setVisible(true);
							addSourcePanel.requestFocus();
						}
					}
				});
		    }
		
		return addSourceItem;
	}
	
	// Ajoute le menu de recherche sur l'index (ainsi que l'évènement de click)
	private JMenuItem getSearchIndex(){
	    if (searchIndexItem == null){
	    	searchIndexItem	= new JMenuItem("recherche index");
	    	
	    	// Evénement de click
	    	searchIndexItem.addActionListener (new ActionListener() {
				@Override
				public void actionPerformed (ActionEvent e) {
					if (searchIndexPanel == null) {
						// Crée une nouvelle fenêtre
						searchIndexPanel = new SearchIndex();
						searchIndexPanel.requestFocus();
					}
					else {
						// Affiche la fenêtre et met le focus
						searchIndexPanel.setVisible(true);
						searchIndexPanel.requestFocus();
					}
				}
			});
	    }
	
	return searchIndexItem;
}

	// Initialise la fenêtre principale
	// Initialise la fenêtre principale
	private void initialize(){
		
	   Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	   int hauteur = (int)(tailleEcran.getHeight()*0.7);
	   int largeur = (int)(tailleEcran.getWidth()*0.6);
	   
	   this.setTitle("Moteur de recherches");
	   this.setJMenuBar(getMenu());
	   this.setContentPane(getPrincipale());
	   
       this.setSize(largeur, hauteur);
       this.setVisible(true);
       this.setLocationRelativeTo(null);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.addComponentListener(this);
       
       fen=this;
       fen.addComponentListener(this);
       
       
       //this.setResizable(false);
       

	}
	
	// Construit la fenêtre principale
	private JPanel getPrincipale(){
		if(panelPrincipal==null){
			// Crée un nouveau panel
			panelPrincipal=new JPanel(new BorderLayout());
			panelPrincipal.add(getNordPanel(),BorderLayout.NORTH);
			panelPrincipal.add(getWestPanel(),BorderLayout.WEST);
			panelPrincipal.add(getcentrePanel(),BorderLayout.CENTER);
		//	panelPrincipal.setBackground(Color.WHITE);
		}
		return panelPrincipal;
	}
	
	private JPanel getNordPanel(){
		if(panelNord==null){
			panelNord=new JPanel(new GridBagLayout());
			GridBagConstraints bgc=new GridBagConstraints();
			
			bgc.gridx=0;
			bgc.gridy=GridBagConstraints.RELATIVE;
		//	bgc.fill=GridBagConstraints.HORIZONTAL;
			
			bgc.insets= new Insets(0, 0, 10, 10);
			
			panelNord.add(getPanelNord1(),bgc);
			panelNord.add(getPanelNord2(),bgc);
			panelNord.setBackground(Color.WHITE);
		}
		return panelNord;
	}
	
	// Construit le panel contenant l'image (de la fenêtre principale)
	private JPanel getPanelNord1(){
		if(panelNord1==null){
			panelNord1=new JPanel();
			//JLabel text=new JLabel("RDF RESOURCES FINDER");
			//text.setFont(new Font("Serif", Font.PLAIN, 46));
			//panelNord1.add(text);
			
			
			//// TAILLE DE L'IMAGE
			Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			   int hauteur = (int)(tailleEcran.getHeight()*0.16);
			  
			
			ImageIcon icon = new ImageIcon("img/uvsq.jpg"); 
			Image img = icon.getImage(); 
			BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB); 
			Graphics g = bi.createGraphics(); 
			g.drawImage(img, 0, 0, img.getWidth(null), hauteur, null); 
			ImageIcon newIcon = new ImageIcon(bi); 
			JLabel textImage=new JLabel();
			textImage.setIcon(newIcon);
            panelNord1.setPreferredSize(new Dimension(img.getWidth(null),hauteur));
			panelNord1.add(textImage);
			panelNord1.setBackground(Color.WHITE);
			
		}
		return panelNord1;
	}
	
	// Construit le panel contenant la recherche
	private JPanel getPanelNord2(){
		if(panelNord2==null){
			panelNord2=new JPanel();

			panelNord2.add(getRecherche());
			panelNord2.add(getBRechercher());
		}
		return panelNord2;
	}
	
	
	
	private JPanel getWestPanel(){
		
		if(panelWest==null){
			panelWest=new JPanel();
			
			  Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			   int hauteur = (int)(tailleEcran.getHeight()*0.9*0.7);
			   int largeur = (int)(tailleEcran.getWidth()*0.8*0.4);
			panelWest.setPreferredSize(new Dimension(largeur, hauteur));
			
	
			
			
		}
		return panelWest;
	}
	
	
	
	private JPanel getcentrePanel(){
		
		if(panelCentre==null){
			panelCentre=new JPanel();
			
			  Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			   int hauteur = (int)(tailleEcran.getHeight()*0.9*0.7);
			   int largeur = (int)(tailleEcran.getWidth()*0.8*0.6);
			   panelCentre.setPreferredSize(new Dimension(largeur, hauteur));
			
			
		}
		return panelCentre;
	}
	
	private JTextField getRecherche(){
		if(recherche==null){
			recherche=new JTextField(50);
			recherche.setPreferredSize(new Dimension(50,30));
		}
		return recherche;
	}
	
	// Construit le bouton de recherche
	private JButton getBRechercher(){
		
		if(bRecherche==null){
			bRecherche=new JButton("Recherche");
			
		}
		
		ActionListener monActionListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String text = recherche.getText();
		        System.out.println(text);
		        Graph graph=null;
		        GraphStreamJena jenaAdapter=new GraphStreamJena(JenaUtils.loadJenaModel());
		        try {
                     graph=jenaAdapter.buildGraph();
				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        getWestPanel().removeAll();
		       // viewer = graph.display();
		        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		        viewer.enableAutoLayout();
				view = viewer.addDefaultView(false);
	
				view.setPreferredSize(new Dimension(getWestPanel().getWidth(), getWestPanel().getHeight()));				
				view.resizeFrame(getWestPanel().getWidth(), getWestPanel().getHeight());

				
				getWestPanel().add(view);
				getWestPanel().validate();
		        Indexer indexer = new Indexer();
		        List<Triplet> list;
		        list=indexer.SearchWithIndex(text);
		        jenaAdapter.viewResultNode( list);
		        if(list.size()>1)
		        jenaAdapter.printShortPath(list);
		    }
		};
		bRecherche.addActionListener(monActionListener);
		
		return bRecherche;
	}
	

	public Main(){
		super();
		initialize(); // initialise la fenêtre principale
	}
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			new Main();
	}

	@Override
	public void componentResized(ComponentEvent e) {

		
           int largeur=(int) (fen.getWidth()*0.6);
           int hauteur=(int)(fen.getHeight()*0.7);
           int largeur2=(int) (fen.getWidth()*0.4);
		   getWestPanel().setPreferredSize(new Dimension(largeur2, hauteur));
		   getcentrePanel().setPreferredSize(new Dimension(largeur, hauteur));
		   
		   if(view!=null)
		   {
		   view.setPreferredSize(new Dimension(largeur2, hauteur));
		   view.resizeFrame(largeur2, hauteur);
		   }
		   fen.validate();

		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

}
