package be.timtac.ephec.kotala;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
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

public class AjoutKot extends Activity {
	private String ligne;
	private static int RESULT_LOAD_IMAGE = 1;
	Bitmap pic = null;
	ImageView monimage = null;
	ProgressDialog ringProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajout_kot);
		
		final EditText rue = (EditText)findViewById(R.id.rue);
		final EditText cp = (EditText)findViewById(R.id.cp);
		final EditText loyer = (EditText)findViewById(R.id.loyer);
		final EditText surface = (EditText)findViewById(R.id.surface);
		final EditText bail = (EditText)findViewById(R.id.bail);
		final EditText nbchambres = (EditText)findViewById(R.id.nbchambres);
		final EditText nbcolocs = (EditText)findViewById(R.id.nbcolocs);
		final CheckBox parking = (CheckBox)findViewById(R.id.parking);
		final CheckBox jardin = (CheckBox)findViewById(R.id.jardin);
		final CheckBox charge = (CheckBox)findViewById(R.id.charge);
		final CheckBox internet = (CheckBox)findViewById(R.id.internet);
		final CheckBox fumeur = (CheckBox)findViewById(R.id.fumeur);
		final CheckBox menage = (CheckBox)findViewById(R.id.menage);
		final CheckBox animaux = (CheckBox)findViewById(R.id.animaux);
		final EditText supp = (EditText)findViewById(R.id.supp);
		final Button valider = (Button)findViewById(R.id.ajoutkot);
		final Button select = (Button)findViewById(R.id.select);
		final Button take = (Button)findViewById(R.id.take);
		monimage = (ImageView)findViewById(R.id.monimage);
		
		
		//**********************Bouton valider******************
		valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String sparking, sjardin, scharge, sinternet, sfumeur, smenage, sanimaux;
            	String srue = rue.getText().toString();
            	String scp = cp.getText().toString();
            	String sloyer = loyer.getText().toString();
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
            	
            	if((!srue.matches("")) && (!scp.matches("")) && (!sloyer.matches("")) && (!ssurface.matches("")) && (!sbail.matches("")) && (!snbchambres.matches("")) && (!snbcolocs.matches("")))
            	{          		
            		String url = "http://kotala.be/scripts/script_addkot.php";
            		String id = String.valueOf(Connexion.getId());	
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
            		
            		
            		//r�cup�ration des coordonn�es
            		String addr = srue + " " + scp;
            		Geocoder geo = new Geocoder(getApplicationContext());
            		List<Address> addresses = null;
            		try {
						addresses = geo.getFromLocationName(addr, 10);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			     	Address ad = addresses.get(0); 	
            		double lat = ad.getLatitude();
            		double lng = ad.getLongitude();
            		String slat = String.valueOf(lat);
            		String slng = String.valueOf(lng);
			     	
            		
            		try {
						  new Script().execute(url,srue,scp,sloyer,ssurface,sbail,snbchambres,snbcolocs,sparking,sjardin,scharge,sinternet,sfumeur,smenage,sanimaux,ssupp,id,image,slat,slng).get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
            		
            		if(ligne != null)
            		{
            			if(ligne.length() > 5)
            			{
            				Toast.makeText(getApplicationContext(), "Kot ajout�", Toast.LENGTH_LONG).show();
            				Intent intent = new Intent(AjoutKot.this, MainActivity.class);
                        	startActivity(intent);
            			}
            			else
            			{
            				Toast.makeText(getApplicationContext(), "Une erreur s'est produite lors de l'ajout de kot. Veuillez recommencer" , Toast.LENGTH_LONG).show();
            			}	
            		}	
            		else Toast.makeText(getApplicationContext(), "V�rifiez si vous avez une connexion internet valide" , Toast.LENGTH_LONG).show();
            	}
            	else
            	{
            		Toast.makeText(getApplicationContext(), "Vous devez remplir tout les champs, sauf la description suppl�mentaire" , Toast.LENGTH_LONG).show();
            	}

            }
        });
		
		//*********si on clique sur le bouton pour s�lectionner une image
    	select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent();
        		intent.setType("image/*");
        		intent.setAction(Intent.ACTION_GET_CONTENT);
        		startActivityForResult(Intent.createChooser(intent, "Choisissez votre image"), 1);

            }
        });
    	
    	//*********si on clique sur le bouton pour prendre une photo
    	take.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
            	startActivityForResult(intent2, 0);
            }
        });   
		
	}
		
	//************************Classe thread qui int�ragit avec le serveur
		public class Script extends AsyncTask <String, Void, String> {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				ringProgressDialog = ProgressDialog.show(AjoutKot.this, "Chargement", "Patientez s'il vous plait", true);
			}
			
			@Override
			protected String doInBackground(String... params) {
				
				try{
					
				  	HttpClient httpclient = new DefaultHttpClient();
			        HttpPost request = new HttpPost(params[0]);   
			        
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(15);
			        nameValuePairs.add(new BasicNameValuePair("rue", params[1]));
			        nameValuePairs.add(new BasicNameValuePair("cp", params[2]));
			        nameValuePairs.add(new BasicNameValuePair("loyer", params[3]));
			        nameValuePairs.add(new BasicNameValuePair("surface", params[4]));
			        nameValuePairs.add(new BasicNameValuePair("bail", params[5]));
			        nameValuePairs.add(new BasicNameValuePair("nbchambres", params[6]));
			        nameValuePairs.add(new BasicNameValuePair("nbcolocs", params[7]));
			        nameValuePairs.add(new BasicNameValuePair("parking", params[8]));
			        nameValuePairs.add(new BasicNameValuePair("jardin", params[9]));
			        nameValuePairs.add(new BasicNameValuePair("charge", params[10]));
			        nameValuePairs.add(new BasicNameValuePair("internet", params[11]));
			        nameValuePairs.add(new BasicNameValuePair("fumeur", params[12]));
			        nameValuePairs.add(new BasicNameValuePair("menage", params[13]));
			        nameValuePairs.add(new BasicNameValuePair("animaux", params[14]));
			        nameValuePairs.add(new BasicNameValuePair("supp", params[15]));
			        nameValuePairs.add(new BasicNameValuePair("id", params[16]));
			        nameValuePairs.add(new BasicNameValuePair("image", params[17]));
			        nameValuePairs.add(new BasicNameValuePair("lat", params[18]));
			        nameValuePairs.add(new BasicNameValuePair("lng", params[19]));
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

			     if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
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
