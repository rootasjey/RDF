package graphic;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Help extends JDialog{

	private JTextPane jTextPane = null;
	private JScrollPane jScrollPane1 = null;
	private JPanel panelPrincipal=null;
	
	public Help(){
		super();
		initialize();
	}

	
	private void initialize(){
		
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		   int hauteur = (int)(tailleEcran.getHeight()*0.8);
		   int largeur = (int)(tailleEcran.getWidth()*0.5);
		   
		   this.setTitle("Moteur de recherches");
		   this.setContentPane(getPrincipale());
		   
	       this.setSize(largeur, hauteur);
	       this.setVisible(true);
	       this.setLocationRelativeTo(null);
	       this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	       this.setAlwaysOnTop(true);
	       
	       
	}
	
	private JPanel getPrincipale(){
		
		if(panelPrincipal==null){
			panelPrincipal=new JPanel(new BorderLayout());
			panelPrincipal.add(getJScrollPane(),BorderLayout.CENTER);
			
		}
		
		return panelPrincipal;
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
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
     new Help();
	}

}
