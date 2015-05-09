package be.timtac.ephec.kotala;

import android.media.Image;
import java.util.ArrayList;

public class Bien {

	
	private static int id = 0;
	private String descriptif;
	private String adresse;
	private int num;
	private int nbVues = 0;
	private ArrayList<Image> photos;
	private int loyer;
	private int surface;
	
	public Bien (String descriptif, String adresse, int num, int loyer, int surface) {
		
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Bien.id = id;
	}

	public String getDescriptif() {
		return descriptif;
	}

	public void setDescriptif(String descriptif) {
		this.descriptif = descriptif;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getNbVues() {
		return nbVues;
	}

	public void setNbVues(int nbVues) {
		this.nbVues = nbVues;
	}

	public ArrayList<Image> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<Image> photos) {
		this.photos = photos;
	}

	public int getLoyer() {
		return loyer;
	}

	public void setLoyer(int loyer) {
		this.loyer = loyer;
	}

	public int getSurface() {
		return surface;
	}

	public void setSurface(int surface) {
		this.surface = surface;
	}
	
}
