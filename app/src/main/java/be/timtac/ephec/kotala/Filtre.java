package be.timtac.ephec.kotala;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.timtac.ephec.R;

public class Filtre extends Activity {
	private static int distancechoisie = 9999;
	private static String sprixmin = "0";
	private static String sprixmax = "9999";
	private static String ssurface = "1";
	private static String sbail = "1";
	private static String snbcolocsmin = "0";
	private static String snbcolocsmax = "9";
	private static String snbchambres = "0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filtre);
		
		final EditText prixmin = (EditText)findViewById(R.id.prixmin);
		final EditText prixmax = (EditText)findViewById(R.id.prixmax);
		final EditText surfacemin = (EditText)findViewById(R.id.surfacemin);
		final EditText bail = (EditText) findViewById(R.id.bail);
		final EditText nbcolocsmin = (EditText) findViewById(R.id.nbcolocsmin);
		final EditText nbcolocsmax = (EditText) findViewById(R.id.nbcolocsmax);
		final EditText nbchambres = (EditText) findViewById(R.id.nbchambres);
		final EditText distance = (EditText)findViewById(R.id.distance);
		final Button filtreavance = (Button)findViewById(R.id.filtreavance);
		final Button envoyer = (Button)findViewById(R.id.envoyer);
		
		prixmin.setText(getSprixmin());
		prixmax.setText(getSprixmax());
		surfacemin.setText(getSsurface());
		bail.setText(getSbail());
		nbcolocsmin.setText(getSnbcolocsmin());
		nbcolocsmax.setText(getSnbcolocsmax());
		nbchambres.setText(getSnbchambres());
		distance.setText(String.valueOf(getDistancechoisie()));
		
		
		//*********si on clique sur le bouton filtre avanc�
    	filtreavance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	distancechoisie = Integer.parseInt(distance.getText().toString());
            	sprixmin = prixmin.getText().toString();
        		sprixmax = prixmax.getText().toString();
        		ssurface = surfacemin.getText().toString();
        		sbail = bail.getText().toString();
        		snbcolocsmin = nbcolocsmin.getText().toString();
        		snbcolocsmax = nbcolocsmax.getText().toString();
        		snbchambres = nbchambres.getText().toString();
            	
            	Intent intent = new Intent(Filtre.this, FiltreAvance.class);
            	startActivity(intent);
            }
        });
    	
    	//*********si on clique sur le bouton envoyer
    	envoyer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {	
            	distancechoisie = Integer.parseInt(distance.getText().toString());
            	sprixmin = prixmin.getText().toString();
            	sprixmax = prixmax.getText().toString();
            	ssurface = surfacemin.getText().toString();
            	sbail = bail.getText().toString();
            	snbcolocsmin = nbcolocsmin.getText().toString();
            	snbcolocsmax = nbcolocsmax.getText().toString();
            	snbchambres = nbchambres.getText().toString();        		
            	
	            Intent intent = new Intent(Filtre.this, MainActivity.class);
	            startActivity(intent);
            	
            }
        });
    	
	}
	

	public static int getDistancechoisie() {
		return distancechoisie;
	}

	public void setDistancechoisie(int distance) {
		Filtre.distancechoisie = distance;
	}


	public static String getSprixmin() {
		return sprixmin;
	}


	public static void setSprixmin(String sprixmin) {
		Filtre.sprixmin = sprixmin;
	}


	public static String getSprixmax() {
		return sprixmax;
	}


	public static void setSprixmax(String sprixmax) {
		Filtre.sprixmax = sprixmax;
	}


	public static String getSsurface() {
		return ssurface;
	}


	public static void setSsurface(String ssurface) {
		Filtre.ssurface = ssurface;
	}


	public static String getSbail() {
		return sbail;
	}


	public static void setSbail(String sbail) {
		Filtre.sbail = sbail;
	}


	public static String getSnbcolocsmin() {
		return snbcolocsmin;
	}


	public static void setSnbcolocsmin(String snbcolocsmin) {
		Filtre.snbcolocsmin = snbcolocsmin;
	}


	public static String getSnbcolocsmax() {
		return snbcolocsmax;
	}


	public static void setSnbcolocsmax(String snbcolocsmax) {
		Filtre.snbcolocsmax = snbcolocsmax;
	}


	public static String getSnbchambres() {
		return snbchambres;
	}


	public static void setSnbchambres(String snbchambres) {
		Filtre.snbchambres = snbchambres;
	}
	
}
