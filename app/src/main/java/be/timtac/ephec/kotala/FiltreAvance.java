package be.timtac.ephec.kotala;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import be.timtac.ephec.R;

public class FiltreAvance extends Activity {
	private static String scharge = "p";
	private static String sparking = "p";
	private static String sanimal = "p";
	private static String smenage = "p";
	private static String sinternet = "p";
	private static String sjardin = "p";
	private static String sfumeur = "p";	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filtre_avance);
		
		//tout les boutons du filtre avanc�s
		final RadioButton chargeoui = (RadioButton)findViewById(R.id.chargeoui);
		final RadioButton chargenon = (RadioButton)findViewById(R.id.chargenon);
		final RadioButton parkingoui = (RadioButton)findViewById(R.id.parkingoui);
		final RadioButton parkingnon = (RadioButton)findViewById(R.id.parkingnon);
		final RadioButton animaloui = (RadioButton)findViewById(R.id.animaloui);
		final RadioButton animalnon = (RadioButton)findViewById(R.id.animalnon);
		final RadioButton menageoui = (RadioButton)findViewById(R.id.menageoui);
		final RadioButton menagenon = (RadioButton)findViewById(R.id.menagenon);
		final RadioButton internetoui = (RadioButton)findViewById(R.id.internetoui);
		final RadioButton internetnon = (RadioButton)findViewById(R.id.internetnon);
		final RadioButton jardinoui = (RadioButton)findViewById(R.id.jardinoui);
		final RadioButton jardinnon = (RadioButton)findViewById(R.id.jardinnon);
		final RadioButton fumeuroui = (RadioButton)findViewById(R.id.fumeuroui);
		final RadioButton fumeurnon = (RadioButton)findViewById(R.id.fumeurnon);
		final Button boutonvalider = (Button)findViewById(R.id.boutonvalider);
		final Button boutonretour = (Button)findViewById(R.id.boutonretour);
		
		//*****On v�rifie les valeurs pour qu'elles soient identiques � celle que le user a voulu
		if(scharge.equals("v")) chargeoui.setChecked(true);
		else if(scharge.equals("f")) chargenon.setChecked(true);
		
		if(sparking.equals("v")) parkingoui.setChecked(true);
		else if(sparking.equals("f")) parkingnon.setChecked(true);
		
		if(sanimal.equals("v")) animaloui.setChecked(true);
		else if(sanimal.equals("f")) animalnon.setChecked(true);
		
		if(smenage.equals("v")) menageoui.setChecked(true);
		else if(smenage.equals("f")) menagenon.setChecked(true);
		
		if(sinternet.equals("v")) internetoui.setChecked(true);
		else if(sinternet.equals("f")) internetnon.setChecked(true);
		
		if(sjardin.equals("v")) jardinoui.setChecked(true);
		else if(sjardin.equals("f")) jardinnon.setChecked(true);
		
		if(sfumeur.equals("v")) fumeuroui.setChecked(true);
		else if(sfumeur.equals("f")) fumeurnon.setChecked(true);
		
		
		//*********si on clique sur le bouton valider
    	boutonvalider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	if(chargeoui.isChecked()) scharge = "v";
            	else if (chargenon.isChecked()) scharge = "f";
            	else scharge = "p";
            	
            	if(parkingoui.isChecked()) sparking = "v";
            	else if(parkingnon.isChecked()) sparking = "f";
            	else sparking = "p";
            	
            	if(animaloui.isChecked()) sanimal = "v";
            	else if(animalnon.isChecked()) sanimal = "f";
            	else sanimal = "p";
            	
            	if(menageoui.isChecked()) smenage = "v";
            	else if(menagenon.isChecked()) smenage = "f";
            	else smenage = "p";
            	
            	if(internetoui.isChecked()) sinternet = "v";
            	else if(internetnon.isChecked()) sinternet = "f";
            	else sinternet = "p";
            	
            	if(jardinoui.isChecked()) sjardin = "v";
            	else if(jardinnon.isChecked()) sjardin = "f";
            	else sjardin = "p";
            	
            	if(fumeuroui.isChecked()) sfumeur = "v";
            	else if(fumeurnon.isChecked()) sfumeur = "f";
            	else sfumeur = "p";
            		
            	
            	Intent intent = new Intent(FiltreAvance.this, Filtre.class);
            	startActivity(intent);
            }
        });
    	
    	
    	//*********si on clique sur le bouton retour
    	boutonretour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(FiltreAvance.this, Filtre.class);
            	startActivity(intent);
            }
        });
		
	}


	public static String getScharge() {
		return scharge;
	}


	public static void setScharge(String scharge) {
		FiltreAvance.scharge = scharge;
	}


	public static String getSparking() {
		return sparking;
	}


	public static void setSparking(String sparking) {
		FiltreAvance.sparking = sparking;
	}


	public static String getSanimal() {
		return sanimal;
	}


	public static void setSanimal(String sanimal) {
		FiltreAvance.sanimal = sanimal;
	}


	public static String getSmenage() {
		return smenage;
	}


	public static void setSmenage(String smenage) {
		FiltreAvance.smenage = smenage;
	}


	public static String getSinternet() {
		return sinternet;
	}


	public static void setSinternet(String sinternet) {
		FiltreAvance.sinternet = sinternet;
	}


	public static String getSjardin() {
		return sjardin;
	}


	public static void setSjardin(String sjardin) {
		FiltreAvance.sjardin = sjardin;
	}


	public static String getSfumeur() {
		return sfumeur;
	}


	public static void setSfumeur(String sfumeur) {
		FiltreAvance.sfumeur = sfumeur;
	}
	
	
}
