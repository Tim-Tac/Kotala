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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import be.timtac.ephec.R;

public class MesKots extends Activity {
	private String ligne;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_kots);
		
		final TextView nbkots = (TextView) findViewById(R.id.nbkots);
		final LinearLayout monlayout = (LinearLayout) findViewById(R.id.monlayout);
		
		String url = "http://kotala.be/scripts/script_meskots.php";
		String id = String.valueOf(Connexion.getId());
		try {
			new Script().execute(url,id).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(ligne.length() > 10)
		{
			try {
				JSONArray jarray = new JSONArray(ligne);
				TextView[] marray = new TextView[jarray.length()];
				final String[] idkot = new String[jarray.length()];
				final String[] cp = new String[jarray.length()];
				final String[] ad = new String[jarray.length()];
				
				for(int i=0;i<jarray.length();i++)
				{
					JSONObject json_data = jarray.getJSONObject(i);
					idkot[i] = json_data.getString("id");
					cp[i] = json_data.getString("codePostal");
					ad[i] = json_data.getString("adresse");
					
					final int n = i;
					
					marray[i] = new TextView(this);
					marray[i].setText(idkot[i] + " "+ cp[i] + " " + ad[i]);
					marray[i].setTextSize(20);
					marray[i].setOnClickListener(new View.OnClickListener() {
			            public void onClick(View v) {
			            	Intent intent = new Intent(MesKots.this, ModifKot.class);
			            	intent.putExtra("id", idkot[n]);
                        	startActivity(intent);
			            }
					});			
					
					LayoutParams textViewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					textViewLayoutParams.setMargins(5, 30, 0, 0);
					marray[i].setLayoutParams(textViewLayoutParams);
					monlayout.addView(marray[i]);

				}				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else nbkots.setText("Aucuns kot n'est � votre nom. Si vous en avez, v�rifiez votre connexion internet.");

	}
	
	//************************Classe thread qui int�ragit avec le serveur
		public class Script extends AsyncTask <String, Void, String> {

			@Override
			protected String doInBackground(String... params) {
				
				try{
					
				  	HttpClient httpclient = new DefaultHttpClient();
			        HttpPost request = new HttpPost(params[0]);   		        
			        
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("id", params[1]));
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
