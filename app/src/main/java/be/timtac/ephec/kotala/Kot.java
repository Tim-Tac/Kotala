package be.timtac.ephec.kotala;

public class Kot extends Bien{

	private boolean animaux = false;
	private boolean charge = false;
	private boolean parking =false;
	private boolean jardin =false;
	private boolean internet =false;
	private boolean fumeur =false;
	private boolean menage =false;
	private int nbChambres;
	private int nbColocs;
	
	public Kot(String descriptif, String adresse, int num, int loyer,
			int surface, boolean animaux, boolean charge,  boolean parking,  boolean jardin,
			boolean internet,boolean fumeur, boolean menage, int nbChambres, int nbColocs) {
		
		
		
		super(descriptif, adresse, num, loyer, surface);
		this.animaux = animaux;
		this.charge = charge;
		this.fumeur = fumeur;
		this.internet = internet;
		this.jardin = jardin;
		this.menage = menage;
		this.nbChambres = nbChambres;
		this.nbColocs = nbColocs;
		// TODO Auto-generated constructor stub
	}

	public boolean isAnimaux() {
		return animaux;
	}

	public void setAnimaux(boolean animaux) {
		this.animaux = animaux;
	}

	public boolean isCharge() {
		return charge;
	}

	public void setCharge(boolean charge) {
		this.charge = charge;
	}

	public boolean isParking() {
		return parking;
	}

	public void setParking(boolean parking) {
		this.parking = parking;
	}

	public boolean isJardin() {
		return jardin;
	}

	public void setJardin(boolean jardin) {
		this.jardin = jardin;
	}

	public boolean isInternet() {
		return internet;
	}

	public void setInternet(boolean internet) {
		this.internet = internet;
	}

	public boolean isFumeur() {
		return fumeur;
	}

	public void setFumeur(boolean fumeur) {
		this.fumeur = fumeur;
	}

	public boolean isMenage() {
		return menage;
	}

	public void setMenage(boolean menage) {
		this.menage = menage;
	}

	public int getNbChambres() {
		return nbChambres;
	}

	public void setNbChambres(int nbChambres) {
		this.nbChambres = nbChambres;
	}

	public int getNbColocs() {
		return nbColocs;
	}

	public void setNbColocs(int nbColocs) {
		this.nbColocs = nbColocs;
	}

}
