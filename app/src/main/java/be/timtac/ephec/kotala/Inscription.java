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

public class Inscription extends Activity {
	private String ligne;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inscription);
		
		final EditText pseudo = (EditText) findViewById(R.id.pseudo);
		final EditText mdp1 = (EditText) findViewById(R.id.mdp1); 
		final EditText mdp2 = (EditText) findViewById(R.id.mdp2); 
		final EditText email = (EditText) findViewById(R.id.email); 
		final EditText tel = (EditText) findViewById(R.id.tel); 
		final Button valider = (Button) findViewById(R.id.valider);
		
		//*********si on clique sur le bouton valider
    	valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String spseudo = pseudo.getText().toString();
            	String smdp1 = mdp1.getText().toString();
            	String smdp2 = mdp2.getText().toString();
            	String semail = email.getText().toString();
            	String stel = tel.getText().toString();
            	
            	if((!spseudo.matches("")) && (!smdp1.matches("")) && (!smdp2.matches("")) && (!semail.matches("")) && (!stel.matches("")))
            	{
            		if(smdp1.equals(smdp2))
            		{
            			String url = "http://kotala.be/scripts/script_inscription.php";
	            		try {
							new Script().execute(url,spseudo,smdp1,semail,stel).get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            			
            			if(ligne != null)
            			{
            				if(ligne.length() > 30)
            				{
            					Toast.makeText(getApplicationContext(), "Un email de confirmation cliquable vous a �t� envoy�" , Toast.LENGTH_LONG).show();
            					Intent intent = new Intent(Inscription.this, MainActivity.class);
                            	startActivity(intent);
            				}
            				else Toast.makeText(getApplicationContext(), "Le pseudo et/ou adress email choisi est d�j� utilis�" , Toast.LENGTH_LONG).show();
            			}
	            		else Toast.makeText(getApplicationContext(), "Erreur lors de la connexion, v�rifiez si vous disposez d'une connexion internet valide" , Toast.LENGTH_LONG).show();
            		}
            		else Toast.makeText(getApplicationContext(), "Les mots de passe ne correspondent pas" , Toast.LENGTH_SHORT).show();   
            	}
            	else Toast.makeText(getApplicationContext(), "Vous devez remplir tout les champs" , Toast.LENGTH_SHORT).show();
            }
        });  
	}
	
	//************************Classe thread qui int�ragit avec le serveur
		public class Script extends AsyncTask <String, Void, String> {

			@Override
			protected String doInBackground(String... params) {
				
				try{
					
				  	HttpClient httpclient = new DefaultHttpClient();
			        HttpPost request = new HttpPost(params[0]);   		        
			        
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			        nameValuePairs.add(new BasicNameValuePair("pseudo", params[1]));
			        nameValuePairs.add(new BasicNameValuePair("mdp1", params[2]));
			        nameValuePairs.add(new BasicNameValuePair("email", params[3]));
			        nameValuePairs.add(new BasicNameValuePair("tel", params[4]));
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
