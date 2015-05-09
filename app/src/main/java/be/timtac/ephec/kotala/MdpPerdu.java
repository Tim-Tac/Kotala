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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.timtac.ephec.R;

public class MdpPerdu extends Activity {
	private String ligne;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mdp_perdu);
				
		final EditText identifiants = (EditText) findViewById(R.id.identifiants);
		final Button valider = (Button) findViewById(R.id.validerinfos);
		
		
		//*********si on clique sur le bouton valider
    	valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String infos = identifiants.getText().toString();
            	
            	if(!infos.matches(""))
            	{
            		String url = "http://kotala.be/scripts/script_mdpperdu.php";
            		try {
						new Script().execute(url,infos).get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		
            		if(ligne != null)
            		{
            			if(ligne.length() > 10)
            			{
            				Toast.makeText(getApplicationContext(), "Un email pour changer votre mot de passe vous a �t� envoy�" , Toast.LENGTH_LONG).show();
            			}
            			else Toast.makeText(getApplicationContext(), "L'adresse email ou login n'existe pas" , Toast.LENGTH_LONG).show();
            		}
            		else Toast.makeText(getApplicationContext(), "Erreur lors de la connexion, v�rifiez si vous disposez d'une connexion internet valide" , Toast.LENGTH_LONG).show();
            	}
            	else Toast.makeText(getApplicationContext(), "Le champs est vide" , Toast.LENGTH_SHORT).show();
            	
            	
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
				        
				        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				        nameValuePairs.add(new BasicNameValuePair("info", params[1]));
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
