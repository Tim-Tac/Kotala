package be.timtac.ephec.kotala;

public class Critere {
	private String quartier;
	private double loyerMin;
	private double loyerMax;
	private double surface;
	private boolean cercleActif;
	private boolean charges;
	private boolean parking;
	private boolean animal;
	private boolean menage;
	private boolean internet;
	private boolean jardin;
	private boolean fumeur;



	public Critere (String quartier, double loyerMin, double loyerMax, double surface, boolean cercleActif, boolean charges, boolean parking, boolean animal, boolean menage, boolean internet, boolean jardin, boolean fumeur) {

		this.quartier = quartier;
		this.loyerMin = loyerMin;
		this.loyerMax = loyerMax;
		this.surface = surface;
		this.cercleActif = cercleActif;
		this.charges = charges;
		this.parking = parking;
		this.animal = animal;
		this.menage = menage;
		this.internet = internet;
		this.jardin = jardin;
		this.fumeur = fumeur;
	}



	public String getQuartier() {
		return quartier;
	}



	public void setQuartier(String quartier) {
		this.quartier = quartier;
	}



	public double getLoyerMin() {
		return loyerMin;
	}



	public void setLoyerMin(double loyerMin) {
		this.loyerMin = loyerMin;
	}



	public double getLoyerMax() {
		return loyerMax;
	}



	public void setLoyerMax(double loyerMax) {
		this.loyerMax = loyerMax;
	}



	public double getSurface() {
		return surface;
	}



	public void setSurface(double surface) {
		this.surface = surface;
	}



	public boolean isCercleActif() {
		return cercleActif;
	}



	public void setCercleActif(boolean cercleActif) {
		this.cercleActif = cercleActif;
	}



	public boolean isCharges() {
		return charges;
	}



	public void setCharges(boolean charges) {
		this.charges = charges;
	}



	public boolean isParking() {
		return parking;
	}



	public void setParking(boolean parking) {
		this.parking = parking;
	}



	public boolean isAnimal() {
		return animal;
	}



	public void setAnimal(boolean animal) {
		this.animal = animal;
	}



	public boolean isMenage() {
		return menage;
	}



	public void setMenage(boolean menage) {
		this.menage = menage;
	}



	public boolean isInternet() {
		return internet;
	}



	public void setInternet(boolean internet) {
		this.internet = internet;
	}



	public boolean isJardin() {
		return jardin;
	}



	public void setJardin(boolean jardin) {
		this.jardin = jardin;
	}



	public boolean isFumeur() {
		return fumeur;
	}



	public void setFumeur(boolean fumeur) {
		this.fumeur = fumeur;
	}

}
