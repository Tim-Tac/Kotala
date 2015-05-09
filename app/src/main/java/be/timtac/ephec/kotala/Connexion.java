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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import be.timtac.ephec.R;

public class Connexion extends Activity {
	private String ligne;
	private static boolean connected = false;
	private static String pseudo = null;
	private static String email = null;
	private static String tel = null;
	private static String password = null;
	private static int id;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connexion);
		
		 final EditText login = (EditText)findViewById(R.id.login);
		 final EditText pwd = (EditText)findViewById(R.id.pwd);
		 final TextView mdpperdu = (TextView)findViewById(R.id.mdpperdu);
		 final Button connexion = (Button)findViewById(R.id.connexion);
		 final TextView inscription = (TextView)findViewById(R.id.inscription);
	
		
		 //*********si on clique sur le bouton connexion
	    	connexion.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	String slogin = login.getText().toString();
            		String spwd = pwd.getText().toString();
	            	if((!slogin.matches("")) && (!spwd.matches("")))
	            	{
	            		String url = "http://kotala.be/scripts/script_login.php";
	            		try {
							new Script().execute(url,slogin,spwd).get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
	            		
	            		if(ligne != null)
	            		{
		            		if(ligne.length() < 10)
		    		    	{
		    		    		Toast.makeText(getApplicationContext(), "Erreur lors de la connexion" , Toast.LENGTH_LONG).show();
		    		    	}
		    		    	else
		    		    	{
		    		    		try {
									JSONArray jarray = new JSONArray(ligne);
									for(int i=0;i<jarray.length();i++)
									{
										JSONObject json_data = jarray.getJSONObject(i);
										pseudo = json_data.getString("login");
										email = json_data.getString("email");
										tel = json_data.getString("phone");
										password = json_data.getString("pwd");
										id = json_data.getInt("id");
									}				
								} catch (JSONException e) {
									e.printStackTrace();
								}
		    		    		
		    		    		connected = true;
		    		    		Toast.makeText(getApplicationContext(), "Vous �tes maintenant connect�" , Toast.LENGTH_LONG).show();
		    		    		Intent intent = new Intent(Connexion.this, MainActivity.class);
 		    	            	startActivity(intent);
		    		    	}
	            		}
	            		else Toast.makeText(getApplicationContext(), "Erreur lors de la connexion, v�rifiez si vous disposez d'une connexion internet valide et si vos identifiants sont corrects." , Toast.LENGTH_LONG).show();

	            	}
	            	else
	            	{
	            		Toast.makeText(getApplicationContext(), "Au moins un des 2 champs est vide" , Toast.LENGTH_SHORT).show();
	            	}
	            }
	        });
	    	

	    	//*********si on clique sur le texte mdpperdu
	    	mdpperdu.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent intent = new Intent(Connexion.this, MdpPerdu.class);
	            	startActivity(intent);
	            }
	        });   	
	    	
	    	//*********si on clique sur le texte pas encore inscrit
	    	inscription.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent intent = new Intent(Connexion.this, Inscription.class);
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
		        
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("username", params[1]));
		        nameValuePairs.add(new BasicNameValuePair("password", params[2]));
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
	
	public static boolean isConnected() {
		return connected;
	}

	public static void setConnected(boolean connected) {
		Connexion.connected = connected;
	}
	
	public static String getPseudo() {
		return pseudo;
	}

	public static String getTel() {
		return tel;
	}

	public static void setTel(String tel) {
		Connexion.tel = tel;
	}

	public void setPseudo(String pseudo) {
		Connexion.pseudo = pseudo;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		Connexion.email = email;
	}

	public static String getPassword() {
		return password;
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Connexion.id = id;
	}

	public static void setPassword(String password) {
		Connexion.password = password;
	}
	
	
	
}
