package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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

import data.struct.Indexer;


@SuppressWarnings("serial")
public class Main extends JFrame {
	
	private JMenuBar jJMenuBar = null;
	private JMenu parametre = null;
	private JMenu quitter = null;
	private JMenu propos = null;
	private JMenuItem addSource=null;
	private JPanel panelPrincipal=null;
	private JPanel panelNord=null;
	private JPanel panelNord1=null;
	private JPanel panelNord2=null;
	private JPanel panelCentre=null;
	private JTextField  recherche=null;
	private JButton  bRecherche=null;
	private JFrame fen=null;
	//private JScrollPane jScrollPane = null;
	
	
	
	
	private JMenuBar getMenu(){
		
		if(jJMenuBar==null){
			jJMenuBar=new JMenuBar();
			jJMenuBar.add(getParametre());
			jJMenuBar.add(getQuitter());
			jJMenuBar.add(getApropos());
			
		}
			
		return jJMenuBar;
	}
	
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
	
	private JMenu getParametre(){
		
		if(parametre==null){
			
			parametre=new JMenu("Parametre");
            parametre.add(getAddSouce());
			
		}
			
		return parametre;
	}
	
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
	
	private JMenuItem getAddSouce(){
		
		    if(addSource==null){
		    	addSource=new JMenuItem("ajouter source");
		    	addSource.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						new AddSource();
					}
				});
		    }
		
		return addSource;
	}
	
	
	
	
	private void initialize(){
		
	   Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	   int hauteur = (int)(tailleEcran.getHeight()*0.9);
	   int largeur = (int)(tailleEcran.getWidth()*0.8);
	   
	   this.setTitle("Moteur de recherches");
	   this.setJMenuBar(getMenu());
	   this.setContentPane(getPrincipale());
	   
       this.setSize(largeur, hauteur);
       this.setVisible(true);
       this.setLocationRelativeTo(null);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
       fen=this;
       
       
       //this.setResizable(false);
       

	}
	
	private JPanel getPrincipale(){
		
		if(panelPrincipal==null){
			panelPrincipal=new JPanel(new BorderLayout());
			
			panelPrincipal.add(getNordPanel(),BorderLayout.NORTH);
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
	
	private JPanel getPanelNord2(){
		if(panelNord2==null){
			panelNord2=new JPanel();

			panelNord2.add(getRecherche());
			panelNord2.add(getBRechercher());
		}
		return panelNord2;
	}
	
	private JTextField getRecherche(){
		
		if(recherche==null){
			recherche=new JTextField(50);
			recherche.setPreferredSize(new Dimension(50,30));
		}
		
		return recherche;
	}
	
	
	private JButton getBRechercher(){
		
		if(bRecherche==null){
			bRecherche=new JButton("Recherche");
		}
		
		ActionListener monActionListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String text = recherche.getText();
		        System.out.println(text);
		        
		        Indexer indexer = new Indexer();
		        indexer.SearchWithIndex(text);
		    }
		};
		bRecherche.addActionListener(monActionListener);
		
		return bRecherche;
	}
	
	
	public Main(){
		super();
		initialize();
	}
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			new Main();
	}

}
