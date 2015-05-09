package be.timtac.ephec.kotala;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import be.timtac.ephec.R;

public class ModifKot extends Activity {
	private static String ligne;
	private static String ligne3;
	private String idkot;
	Bitmap pic = null;
	ImageView monimage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modif_kot);
		
		final EditText prix = (EditText)findViewById(R.id.prix);
		final EditText surface = (EditText)findViewById(R.id.surface);
		final EditText bail = (EditText)findViewById(R.id.bail);
		final EditText nbchambres = (EditText)findViewById(R.id.nbchambres);
		final EditText nbcolocs = (EditText)findViewById(R.id.nbcolocs);
		final CheckBox parking = (CheckBox) findViewById(R.id.parking);
		final CheckBox jardin = (CheckBox) findViewById(R.id.jardin);
		final CheckBox charge = (CheckBox) findViewById(R.id.charge);
		final CheckBox internet = (CheckBox) findViewById(R.id.internet);
		final CheckBox fumeur = (CheckBox) findViewById(R.id.fumeur);
		final CheckBox menage = (CheckBox) findViewById(R.id.menage);
		final CheckBox animaux = (CheckBox) findViewById(R.id.animaux);	
		final EditText supp = (EditText)findViewById(R.id.supp);
		final Button valider = (Button) findViewById(R.id.valider);
		final Button modimage = (Button) findViewById(R.id.modimage);
		final Button modphoto = (Button) findViewById(R.id.modphoto);
		final CheckBox visible = (CheckBox)findViewById(R.id.visible);
		final Button majvis = (Button)findViewById(R.id.majvis);
		final Button majimage = (Button)findViewById(R.id.majimage);
		monimage = (ImageView)findViewById(R.id.image);
		
		Intent intent = getIntent();
		idkot = intent.getStringExtra("id");
		
		new DownloadImageTask((ImageView) findViewById(R.id.image)).execute("http://kotala.be/img/"+idkot+"_thumb.jpg");
			
		
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

					prix.setText(json_data.getString("loyer"));
					surface.setText(json_data.getString("surface"));
					bail.setText(json_data.getString("bail"));
					nbchambres.setText(json_data.getString("nbChambre"));
					nbcolocs.setText(json_data.getString("nbColoc"));
					supp.setText(json_data.getString("description"));
					
					
					if(json_data.getInt("parking") == 1) parking.setChecked(true);
					if(json_data.getInt("jardin") == 1) jardin.setChecked(true);
					if(json_data.getInt("charge") == 1) charge.setChecked(true);
					if(json_data.getInt("internet") == 1) internet.setChecked(true);
					if(json_data.getInt("menage") == 1) menage.setChecked(true);
					if(json_data.getInt("fumeur") == 1) fumeur.setChecked(true);
					if(json_data.getInt("animaux") == 1) animaux.setChecked(true);
					if(json_data.getInt("visibility") == 1)visible.setChecked(true);
				}				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else Toast.makeText(getApplicationContext(), "Erreur lors de la connexion, v�rifiez si vous disposez d'une connexion internet valide." , Toast.LENGTH_LONG).show();

		
		
		//****************si on clique sur le bouton valider
    	valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String sparking, sjardin, scharge, sinternet, sfumeur, smenage, sanimaux;
            	String sprix = prix.getText().toString();
            	String ssurface = surface.getText().toString();
            	String sbail = bail.getText().toString();
            	String snbchambres = nbchambres.getText().toString();
            	String snbcolocs = nbcolocs.getText().toString();
            	String ssupp = supp.getText().toString();
            	
            	if(parking.isChecked()) sparking = "v";
            	else sparking = "f";
            	if(jardin.isChecked() == true) sjardin = "v";
            	else sjardin = "f";
            	if(charge.isChecked() == true) scharge = "v";
            	else scharge = "f";         	
            	if(internet.isChecked() == true) sinternet = "v";
            	else sinternet = "f";
            	if(fumeur.isChecked() == true) sfumeur = "v";
            	else sfumeur = "f";
            	if(menage.isChecked() == true) smenage = "v";
            	else smenage = "f";
            	if(animaux.isChecked() == true) sanimaux = "v";
            	else sanimaux = "f"; 
            	
            	
            	
            	if((!ssurface.matches("")) && (!sbail.matches("")) && (!snbchambres.matches("")) && (!snbcolocs.matches("")) && (!sprix.matches("")))
            	{          		
            		String url = "http://kotala.be/scripts/script_updateKot.php";
            		try {
						  new Script2().execute(url,idkot,sprix,ssurface,sbail,snbchambres,snbcolocs,sparking,sjardin,scharge,sinternet,sfumeur,smenage,sanimaux,ssupp).get();
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
            				Toast.makeText(getApplicationContext(), "Kot mis � jour", Toast.LENGTH_LONG).show();
            				Intent intent = new Intent(ModifKot.this, MainActivity.class);
                        	startActivity(intent);
            			}
            			else
            			{
            				Toast.makeText(getApplicationContext(), "Une erreur s'est produite lors de la mise � jour. Veuillez recommencer" , Toast.LENGTH_LONG).show();
            			}	
            		}	
            	}
            	else
            	{
            		Toast.makeText(getApplicationContext(), "Vous devez remplir tout les champs du dessus" , Toast.LENGTH_LONG).show();
            	}
            }
        }); 
    	
    	//*********si on clique sur modifimage
    	modimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent();
        		intent.setType("image/*");
        		intent.setAction(Intent.ACTION_GET_CONTENT);
        		startActivityForResult(Intent.createChooser(intent, "Choisissez votre image"), 1);
            	
            }
        });
    	
    	//*********si on clique sur modifphoto
    	modphoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
            	startActivityForResult(intent2, 0);
            	
            }
        });
    	
    	
    	//*********si on clique sur mise � jour image
    	majimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String image = null;
        		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        		
        		//si l'utilisateur a choisi une image
        		if(pic != null)
        		{
            		pic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            		byte[] byte_arr = stream.toByteArray();
                    image = Base64.encodeToString(byte_arr, 0);	
        		}
        		else
        		{
        			pic = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
        			pic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            		byte[] byte_arr = stream.toByteArray();
                    image = Base64.encodeToString(byte_arr, 0);
        		}
        		
        		
        		String url = "http://kotala.be/scripts/script_updatePhoto.php";
        		try {
        			new Script3().execute(url,idkot,image).get();
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (ExecutionException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		
        		if(ligne3 != null)
        		{
        			if(ligne3.length() > 10)
        			{
        				Toast.makeText(getApplicationContext(), "La nouvelle image sera visible dans quelques minutes", Toast.LENGTH_LONG).show();
        				Intent intent = new Intent(ModifKot.this, MainActivity.class);
                    	startActivity(intent);
        			}
        			else
        			{
        				Toast.makeText(getApplicationContext(), "Une erreur s'est produite lors de la mise � jour. Veuillez recommencer" , Toast.LENGTH_LONG).show();
        			}	
        		}
        		else Toast.makeText(getApplicationContext(), "Erreur lors de la connexion au r�seau" , Toast.LENGTH_LONG).show();
        		
            	
            }
        });
    	
    	
    	
    	//*********si on clique sur mise � jour visibilit�
    	majvis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String url = "http://kotala.be/scripts/script_visibilitykot.php";
        		try {
        			new Script1().execute(url,idkot).get();
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (ExecutionException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		
        		if(visible.isChecked() == true)
        		{
        			visible.setChecked(false);
        			Toast.makeText(getApplicationContext(), "Votre kot n'est plus visible" , Toast.LENGTH_LONG).show();
        		}
        		else
        		{
        			visible.setChecked(true);
        			Toast.makeText(getApplicationContext(), "Votre kot est d�sormais visible" , Toast.LENGTH_LONG).show();
        		}
        		
            }
        });
    	
	}
	
	//************************Classe thread qui int�ragit avec le serveur
		public class Script2 extends AsyncTask <String, Void, String> {

			@Override
			protected String doInBackground(String... params) {
				
				try{
					
				  	HttpClient httpclient = new DefaultHttpClient();
			        HttpPost request = new HttpPost(params[0]);   		        
			        
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(15);
			        nameValuePairs.add(new BasicNameValuePair("idkot", params[1]));
			        nameValuePairs.add(new BasicNameValuePair("sprix", params[2]));
			        nameValuePairs.add(new BasicNameValuePair("ssurface", params[3]));
			        nameValuePairs.add(new BasicNameValuePair("sbail", params[4]));
			        nameValuePairs.add(new BasicNameValuePair("snbchambres", params[5]));
			        nameValuePairs.add(new BasicNameValuePair("snbcolocs", params[6]));
			        nameValuePairs.add(new BasicNameValuePair("sparking", params[7]));
			        nameValuePairs.add(new BasicNameValuePair("sjardin", params[8]));
			        nameValuePairs.add(new BasicNameValuePair("scharge", params[9]));
			        nameValuePairs.add(new BasicNameValuePair("sinternet", params[10]));
			        nameValuePairs.add(new BasicNameValuePair("sfumeur", params[11]));
			        nameValuePairs.add(new BasicNameValuePair("smenage", params[12]));
			        nameValuePairs.add(new BasicNameValuePair("sanimaux", params[13]));
			        nameValuePairs.add(new BasicNameValuePair("supp", params[14]));
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
		
		public class Script3 extends AsyncTask <String, Void, String> {

			@Override
			protected String doInBackground(String... params) {
				
				try{
					
				  	HttpClient httpclient = new DefaultHttpClient();
			        HttpPost request = new HttpPost(params[0]);   		        
			        
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        nameValuePairs.add(new BasicNameValuePair("idkot", params[1]));
			        nameValuePairs.add(new BasicNameValuePair("image", params[2]));
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
		
		public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
			  ImageView bmImage;

			  public DownloadImageTask(ImageView bmImage) {
			      this.bmImage = bmImage;
			  }

			  protected Bitmap doInBackground(String... urls) {
			      String urldisplay = urls[0];
			      Bitmap mIcon11 = null;
			      try {
			        InputStream in = new java.net.URL(urldisplay).openStream();
			        mIcon11 = BitmapFactory.decodeStream(in);
			      } catch (Exception e) {
			          Log.e("Error", e.getMessage());
			          e.printStackTrace();
			      }
			      return mIcon11;
			  }

			  protected void onPostExecute(Bitmap result) {
			      bmImage.setImageBitmap(result);
			  }
			}
		
		//*******************************************Classe pour r�cup�rer l'image
				protected void onActivityResult(int requestCode, int resultCode, Intent data) {  

					if(requestCode == 0)
					{
						Bundle extras = data.getExtras();
				         pic = (Bitmap) extras.get("data");
				         
				         monimage.getLayoutParams().height = 500;
				         monimage.getLayoutParams().width = 500;
				         monimage.setImageBitmap(pic);
					}
					else
					{
						super.onActivityResult(requestCode, resultCode, data); 

					     if (requestCode == 1 && resultCode == RESULT_OK && null != data)
					     {
			
					         	Uri selectedImage = data.getData();
					            String[] filePathColumn = { MediaStore.Images.Media.DATA };
					            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
					            cursor.moveToFirst();
					            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					            String picturePath = cursor.getString(columnIndex);
					            cursor.close();
					            pic = (BitmapFactory.decodeFile(picturePath));
					    	  
					            monimage.getLayoutParams().height = 500;
					            monimage.getLayoutParams().width = 500;
					            monimage.setImageBitmap(pic);
					             
					     }
					}
				}

}
