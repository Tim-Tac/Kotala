package be.timtac.ephec.kotala;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import be.timtac.ephec.R;

public class AfficheKot extends Activity {
	private String idkot;
	private static String ligne;
	private static String ligne2;
	private static String ligne3;
	private static int zoomed = 0;
	private String email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affiche_kot);
				
		final TextView nbvues = (TextView)findViewById(R.id.nbvues);
		final TextView loyer = (TextView)findViewById(R.id.loyer);
		final TextView surface = (TextView)findViewById(R.id.surface);
		final TextView bail = (TextView)findViewById(R.id.bail);
		final TextView nbchambres = (TextView)findViewById(R.id.nbchambres);
		final TextView nbcollocs = (TextView)findViewById(R.id.nbcollocs);
		final CheckBox parking = (CheckBox) findViewById(R.id.parking);
		final CheckBox jardin = (CheckBox) findViewById(R.id.jardin);
		final CheckBox charge = (CheckBox) findViewById(R.id.charge);
		final CheckBox internet = (CheckBox) findViewById(R.id.internet);
		final CheckBox fumeur = (CheckBox) findViewById(R.id.fumeur);
		final CheckBox menage = (CheckBox) findViewById(R.id.menage);
		final CheckBox animaux = (CheckBox) findViewById(R.id.animaux);	
		final TextView supp = (TextView)findViewById(R.id.supp);
		final Button mailadmin = (Button) findViewById(R.id.mailadmin);
		final Button mailproprio = (Button) findViewById(R.id.mailproprio);
		final Button gotomap = (Button)findViewById(R.id.gotomap);
		final ImageView kot = (ImageView)findViewById(R.id.kot);
		final TextView changeIm = (TextView)findViewById(R.id.changeim);
		
		Intent intent = getIntent();
		idkot = intent.getStringExtra("idkot");
		zoomed = 0;
		kot.getLayoutParams().height = 400;
		kot.getLayoutParams().width = 450;
		if(getZoomed() == 1) changeIm.setText("toucher pour diminuer");
		
		new ModifKot.DownloadImageTask((ImageView) findViewById(R.id.kot)).execute("http://kotala.be/img/"+idkot+"_thumb.jpg");
		
		String url2 = "http://kotala.be/scripts/script_AddView.php";
		try {
			new Script2().execute(url2,idkot).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String url = "http://kotala.be/scripts/script_receiveMyKot.php";
		try {
			new Script1().execute(url,idkot).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(ligne != null)
		{
			try {
				JSONArray jarray = new JSONArray(ligne);
				for(int i=0;i<jarray.length();i++)
				{
					JSONObject json_data = jarray.getJSONObject(i);
					
					nbvues.setText(json_data.getString("nbVue") + " vues");
					loyer.setText("Loyer : " + json_data.getString("loyer") + " �");
					surface.setText("Surface : " + json_data.getString("surface") + " m�");
					bail.setText("Bail : " + json_data.getString("bail") + " mois");
					nbchambres.setText("Nombre de chambres dispo : " + json_data.getString("nbChambre"));
					nbcollocs.setText("Nombre de collocs : " + json_data.getString("nbColoc"));
					supp.setText(json_data.getString("description"));				
					
					if(json_data.getInt("parking") == 1) parking.setChecked(true);
					if(json_data.getInt("jardin") == 1) jardin.setChecked(true);
					if(json_data.getInt("charge") == 1) charge.setChecked(true);
					if(json_data.getInt("internet") == 1) internet.setChecked(true);
					if(json_data.getInt("menage") == 1) menage.setChecked(true);
					if(json_data.getInt("fumeur") == 1) fumeur.setChecked(true);
					if(json_data.getInt("animaux") == 1) animaux.setChecked(true);
					
					parking.setEnabled(false);
					jardin.setEnabled(false);
					charge.setEnabled(false);
					internet.setEnabled(false);
					menage.setEnabled(false);
					fumeur.setEnabled(false);
					animaux.setEnabled(false);
				}				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else Toast.makeText(getApplicationContext(), "Erreur lors de la connexion, v�rifiez si vous disposez d'une connexion internet valide." , Toast.LENGTH_LONG).show();
		
		//*********si on clique sur le bouton contact admin
    	mailadmin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(Connexion.isConnected() == true)
            	{
	            	Intent intent = new Intent(AfficheKot.this, SendMail.class);
	            	intent.putExtra("idkot", idkot);
	            	startActivity(intent);
            	}
            	else
            	{
    				Toast.makeText(getApplicationContext(), "Vous devez vous connecter" , Toast.LENGTH_SHORT).show();
            	}
            }
        });
    	
    	//*********si on clique sur le bouton proprio
    	mailproprio.setOnClickListener(new View.OnClickListener() {	
    		public void onClick(View v) {
    			if(Connexion.isConnected() == true)
    			{
	            	String url = "http://kotala.be/scripts/script_getemail.php";
	        		try {
	        			new Script3().execute(url,idkot).get();
	        		} catch (InterruptedException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		} catch (ExecutionException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	        		
	        		if(ligne3 != null)
	        		{
	            		if(ligne3.length() < 10)
	    		    	{
	    		    		Toast.makeText(getApplicationContext(), "Erreur lors de la connexion" , Toast.LENGTH_LONG).show();
	    		    	}
	    		    	else
	    		    	{
	    		    		try {
								JSONArray jarray = new JSONArray(ligne3);
								for(int i=0;i<jarray.length();i++)
								{
									JSONObject json_data = jarray.getJSONObject(0);
									email = json_data.getString("email");
								}				
							} catch (JSONException e) {
								e.printStackTrace();
							}
	    		    	}
	        		}
	        		else Toast.makeText(getApplicationContext(), "Erreur lors de la connexion, v�rifiez si vous disposez d'une connexion internet valide et si vos identifiants sont corrects." , Toast.LENGTH_LONG).show();

	            	Intent intent = new Intent(AfficheKot.this, SendMail.class);
	            	intent.putExtra("email", email);
	            	intent.putExtra("idkot", idkot);
	            	startActivity(intent);
    			}
    			else
    			{
    				Toast.makeText(getApplicationContext(), "Vous devez vous connecter" , Toast.LENGTH_SHORT).show();
    			}
            }
        });
    	
    	//*********si on clique sur le bouton gotomap
    	gotomap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(AfficheKot.this, MainActivity.class);
            	startActivity(intent);
            }
        });
    	
    	//*********si on clique sur l'image
    	kot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(zoomed == 0)
            	{
            		Display display = getWindowManager().getDefaultDisplay();
            		Point size = new Point();
            		display.getSize(size);
            		int width = size.x;
            		int height = size.y;
            		new ModifKot.DownloadImageTask((ImageView) findViewById(R.id.kot)).execute("http://kotala.be/img/"+idkot+".jpg");
            		kot.getLayoutParams().height = height/2;
            		kot.getLayoutParams().width = width;
            		setZoomed(1);
            	}
            	else
            	{
            		new ModifKot.DownloadImageTask((ImageView) findViewById(R.id.kot)).execute("http://kotala.be/img/"+idkot+"_thumb.jpg");
            		kot.getLayoutParams().height = 400;
            		kot.getLayoutParams().width = 450;
            		setZoomed(0);
            	}
            	
            }
        });
    	
	}
	
	//classe pour recevoir les infos du kot
	public class Script1 extends AsyncTask <String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			
			try{
				
			  	HttpClient httpclient = new DefaultHttpClient();
		        HttpPost request = new HttpPost(params[0]);   		        
		        
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("idkot", params[1]));
		        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
		        
		        HttpResponse response = httpclient.execute(request);     
		        
		        BufferedReader in = new BufferedReader
		                (new InputStreamReader(response.getEntity().getContent()));     
		        ligne = in.readLine();
		        in.close();
		        
		       }catch(Exception e){
		           Log.e("log_tag", "Error in http connection "+e.toString());
		       }
			return ligne;
		}
		
		@Override
		protected void onPostExecute(String ligne){
			
		}
	}
	
	//************************Classe thread qui int�ragit avec le serveur
		public class Script2 extends AsyncTask <String, Void, String> {
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected String doInBackground(String... params) {
				
				try{
					
				  	HttpClient httpclient = new DefaultHttpClient();
			        HttpPost request = new HttpPost(params[0]);   		        
			        
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("idkot", params[1]));
			        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
			        
			        HttpResponse response = httpclient.execute(request);     
			        
			        BufferedReader in = new BufferedReader
			                (new InputStreamReader(response.getEntity().getContent()));     
			        ligne2 = in.readLine();
			        in.close();
			        
			       }catch(Exception e){
			           Log.e("log_tag", "Error in http connection "+e.toString());
			       }
				return ligne2;
			}
			
			@Override
			protected void onPostExecute(String ligne){
			}
		}
		
		
		//classe pour recevoir l'email du type
		public class Script3 extends AsyncTask <String, Void, String> {

			@Override
			protected String doInBackground(String... params) {
				
				try{
					
				  	HttpClient httpclient = new DefaultHttpClient();
			        HttpPost request = new HttpPost(params[0]);   		        
			        
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			        nameValuePairs.add(new BasicNameValuePair("idkot", params[1]));
			        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
			        
			        HttpResponse response = httpclient.execute(request);     
			        
			        BufferedReader in = new BufferedReader
			                (new InputStreamReader(response.getEntity().getContent()));     
			        ligne3 = in.readLine();
			        in.close();
			        
			       }catch(Exception e){
			           Log.e("log_tag", "Error in http connection "+e.toString());
			       }
				return ligne3;
			}
			
			@Override
			protected void onPostExecute(String ligne){
				
			}
		}
		

		public static int getZoomed() {
			return zoomed;
		}

		public static void setZoomed(int zoomed) {
			AfficheKot.zoomed = zoomed;
		}
		
		
	
}