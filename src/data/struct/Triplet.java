package data.struct;

public class Triplet {

	private String ressource;
	private String propriete;
	private String objet;
	
	
	

	public Triplet() {
		
	}
	
	/**
	 * @param objet
	 */
	public Triplet(String objet) {

		this.objet = objet;
	}

	public  String getRessource() {
		return ressource;
	}
	public  void setRessource(String ressource) {
		this.ressource = ressource;
	}
	public  String getPropriete() {
		return propriete;
	}
	public  void setPropriete(String propriete) {
		this.propriete = propriete;
	}
	public  String getObjet() {
		return objet;
	}
	public  void setObjet(String objet) {
		this.objet = objet;
	}
	
	
}
