package be.timtac.ephec.kotala;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import android.app.ProgressDialog;
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

public class Profil extends Activity {
	private String ligne;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profil);
		
		final TextView pseudo = (TextView)findViewById(R.id.pseudo);
		final EditText email = (EditText)findViewById(R.id.email);
		final EditText tel = (EditText)findViewById(R.id.tel);
		final EditText pwd = (EditText)findViewById(R.id.pwd);
		final EditText pwd2 = (EditText)findViewById(R.id.pwd2);
		final EditText pwdainfos = (EditText)findViewById(R.id.pwdainfos);
		final EditText pwdapwd = (EditText)findViewById(R.id.pwdapwd);
		final Button majinfos = (Button)findViewById(R.id.majinfos);
		final Button majpwd = (Button)findViewById(R.id.majpwd);
		final Button deco = (Button)findViewById(R.id.deconnexion);
		final Button kots = (Button) findViewById(R.id.kots);
				
		
		pseudo.setText(Connexion.getPseudo());
		email.setText(Connexion.getEmail());
		tel.setText(Connexion.getTel());
		
		
		//************************si on clique sur le bouton de d�connexion
		deco.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final ProgressDialog ringProgressDialog = ProgressDialog.show(Profil.this, "D�connexion", "Patientez s'il vous plait", true);
            	Connexion.setConnected(false);
         		Toast.makeText(getApplicationContext(), "Vous �tes d�connect�" , Toast.LENGTH_LONG).show();

            	Intent intent = new Intent(Profil.this, MainActivity.class);
            	startActivity(intent);
            }
        });
		
		//************************si on clique sur le bouton voir mes kots
				kots.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		            	Intent intent = new Intent(Profil.this, MesKots.class);
		            	startActivity(intent);
		            }
		        });
		
		//************************si on clique sur le bouton de mise � jour des infos
		majinfos.setOnClickListener(new View.OnClickListener() {
		     public void onClick(View v) {
		    	 String url = "http://kotala.be/scripts/script_updateprofil_infos.php";
		    	 
		    	 String nemail = email.getText().toString();
		    	 String ntel = tel.getText().toString();
		    	 String npwdainfos = pwdainfos.getText().toString();	
		    	 String id = Integer.toString(Connexion.getId());
		    	 
		    	 
		    		 try {
		    			 	if(Connexion.getPassword().equals(sha1Hash(npwdainfos)))
		    			 	{
			    			 	new ScriptInfo().execute(url,nemail,ntel,id).get();
			    			 	Toast.makeText(getApplicationContext(), "Infos mises � jour" , Toast.LENGTH_LONG).show();
			    			 	Connexion.setTel(ntel);
			    			 	Connexion.setEmail(nemail);	
			    			 	Intent intent = new Intent(Profil.this, MainActivity.class);
		    	            	startActivity(intent);
		    			 	}
		    			 	else Toast.makeText(getApplicationContext(), "Mot de passe incorrect" , Toast.LENGTH_LONG).show();
		    			 	
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 	
		     }
		});
		
		//************************si on clique sur le bouton de mise � jour
				majpwd.setOnClickListener(new View.OnClickListener() {
				     public void onClick(View v) {
				    	 String url = "http://kotala.be/scripts/script_updateprofil_pwd.php";
				    	 
				    	 String npwd = pwd.getText().toString();
				    	 String npwd2 = pwd2.getText().toString();
				    	 String npwdapwd = pwdapwd.getText().toString();	
				    	 String id = Integer.toString(Connexion.getId());
				    	 
				    	 if((npwd.equals(npwd2)))
				    	 {
				    		 try {
				    			 	if(Connexion.getPassword().equals(sha1Hash(npwdapwd)))
				    			 	{
					    			 	new ScriptPwd().execute(url,npwd,id).get();
					    			 	Toast.makeText(getApplicationContext(), "Mot de passe chang�" , Toast.LENGTH_SHORT).show();
					    			 	Intent intent = new Intent(Profil.this, MainActivity.class);
				    	            	startActivity(intent);
				    			 	}
				    			 	else Toast.makeText(getApplicationContext(), "Mot de passe incorrect" , Toast.LENGTH_SHORT).show();
				    			 	
									
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				    	 }
				    	 else
				    	 {
				    		 Toast.makeText(getApplicationContext(), "Les nouveaux mots de passes ne correspondent pas" , Toast.LENGTH_LONG).show();
				    	 }   	
				     }
				});
		
	}
		
	//-**************************Script pour changer les infos
	public class ScriptInfo extends AsyncTask <String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			
			try{
				
			  	HttpClient httpclient = new DefaultHttpClient();
		        HttpPost request = new HttpPost(params[0]);   		        
		        
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("nemail", params[1]));
		        nameValuePairs.add(new BasicNameValuePair("ntel", params[2]));
		        nameValuePairs.add(new BasicNameValuePair("id", params[3]));
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
	
	//*******************Script pour changer les password
	public class ScriptPwd extends AsyncTask <String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			
			try{
				
			  	HttpClient httpclient = new DefaultHttpClient();
		        HttpPost request = new HttpPost(params[0]);   		        
		        
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("npwd", params[1]));
		        nameValuePairs.add(new BasicNameValuePair("id", params[2]));
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
	
	String sha1Hash( String toHash )
	{
	    String hash = null;
	    try
	    {
	        MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
	        byte[] bytes = toHash.getBytes("UTF-8");
	        digest.update(bytes, 0, bytes.length);
	        bytes = digest.digest();

	        // This is ~55x faster than looping and String.formating()
	        hash = bytesToHex( bytes );
	    }
	    catch( NoSuchAlgorithmException e )
	    {
	        e.printStackTrace();
	    }
	    catch( UnsupportedEncodingException e )
	    {
	        e.printStackTrace();
	    }
	    return hash;
	}
	
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	public static String bytesToHex( byte[] bytes )
	{
	    char[] hexChars = new char[ bytes.length * 2 ];
	    for( int j = 0; j < bytes.length; j++ )
	    {
	        int v = bytes[ j ] & 0xFF;
	        hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
	        hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
	    }
	    return new String( hexChars );
	}
	

}
