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
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.timtac.ephec.R;

public class SendMail extends Activity {
	private static String email = null;
	private static String ligne;
	private static String idkot = null;
	private static String txt = null;
	private static String id = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_mail);
		
		final EditText texte = (EditText)findViewById(R.id.texte);
		final Button envoyer = (Button)findViewById(R.id.envoyer);
		
		Intent intent = getIntent();
		email = intent.getStringExtra("email");
		idkot = intent.getStringExtra("idkot");
		id = String.valueOf(Connexion.getId());
		
		//*********si on clique sur le bouton envoyer
    	envoyer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	txt = texte.getText().toString();
            	if(txt.length() > 1)
            	{
            		String url = "http://kotala.be/scripts/script_sendmail.php";
            		try {
						new Script().execute(url,txt,email,id,idkot).get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
            		
            		if(ligne != null)
            		{
	            		if(ligne.length() < 10)
	    		    	{
	    		    		Toast.makeText(getApplicationContext(), "Erreur lors de l'envoi du mail, veuillez recommencer" , Toast.LENGTH_LONG).show();
	    		    	}
	    		    	else
	    		    	{
	    		    		Toast.makeText(getApplicationContext(), "Mail envoy�" , Toast.LENGTH_LONG).show();
	    		    	}
            		}
            		else Toast.makeText(getApplicationContext(), "Erreur lors de la connexion, v�rifiez si vous disposez d'une connexion internet valide et si vos identifiants sont corrects." , Toast.LENGTH_LONG).show();

            	}
            	else
            	{
            		Toast.makeText(getApplicationContext(), "Le champs est vide" , Toast.LENGTH_SHORT).show();
            	}
            	
            	Intent intent = new Intent(SendMail.this, MainActivity.class);
            	startActivity(intent);
            }
        });  
		

	}
	
	//************************Classe thread qui int�ragit avec le serveur
		public class Script extends AsyncTask <String, Void, String> {
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected String doInBackground(String... params) {
				
				try{
					
				  	HttpClient httpclient = new DefaultHttpClient();
			        HttpPost request = new HttpPost(params[0]);   		        
			        
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			        nameValuePairs.add(new BasicNameValuePair("info", params[1]));
			        nameValuePairs.add(new BasicNameValuePair("email", params[2]));
			        nameValuePairs.add(new BasicNameValuePair("id", params[3]));
			        nameValuePairs.add(new BasicNameValuePair("idkot", params[4]));
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
}
