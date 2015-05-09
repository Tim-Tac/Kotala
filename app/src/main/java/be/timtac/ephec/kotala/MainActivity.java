package be.timtac.ephec.kotala;

import java.io.BufferedReader;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import be.timtac.ephec.R;

public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, 
																									LocationListener, android.location.LocationListener {
	public LocationClient mLocationClient;
	public Location mCurrentLocation;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected double lat;
	protected double lon;
	private static int typeVue = 4;
	private String ligne;
	ProgressDialog ringProgressDialog;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        //****************D�claration des variables pour g�olocalisation****************
        mLocationClient = new LocationClient(this, this, this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);      
        mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        lat = mCurrentLocation.getLatitude();
        lon = mCurrentLocation.getLongitude();
        LatLng pos = new LatLng(lat,lon);
    
        
        //*****************************R�cup�ration des ids des vues********************
        final Button vues = (Button)findViewById(R.id.vues);
    	final Button filtre = (Button)findViewById(R.id.filtre);
     	final Button ajout = (Button)findViewById(R.id.ajout);
     	final Button connexion = (Button)findViewById(R.id.connexion);
     	
     	
     	//*****************************Cr�ation de la map*******************************
     	final GoogleMap map = ((MapFragment) getFragmentManager()
     	.findFragmentById(R.id.map)).getMap();
     	map.setMyLocationEnabled(true);
     	if(typeVue == 4) map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
     	else map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
     	map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15)); 
	
     	
     	//****************************Ajout des marqueurs sur la map********************
     	Geocoder geo = new Geocoder(getApplicationContext());
     	String url = "http://kotala.be/scripts/script_receivekot.php";
     	
     	String prixmin = Filtre.getSprixmin();
     	String prixmax = Filtre.getSprixmax();
     	String surface = Filtre.getSsurface();
     	String bail = Filtre.getSbail();
     	String nbcolocsmin = Filtre.getSnbcolocsmin();
     	String nbcolocsmax = Filtre.getSnbcolocsmax();
     	String nbchambres = Filtre.getSnbchambres();
     	String parking = FiltreAvance.getSparking();
     	String animal = FiltreAvance.getSanimal();
     	String charge = FiltreAvance.getScharge();
     	String jardin = FiltreAvance.getSjardin();
     	String fumeur = FiltreAvance.getSfumeur();
     	String menage = FiltreAvance.getSmenage();
     	String internet = FiltreAvance.getSinternet();
     	
     	try {
			new Script().execute(url,prixmin,prixmax,surface,bail,nbcolocsmin,nbcolocsmax,nbchambres,parking,animal,charge,jardin,fumeur,menage,internet).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	
     	//*****Si on obtient des kots******
     	if(ligne != null)
     	{
     		if(ligne.length() > 100)
     		{			
	     		try {
					JSONArray jarray = new JSONArray(ligne);
					for(int i=0;i<jarray.length();i++)
					{
						JSONObject json_data = jarray.getJSONObject(i);
						String rue = json_data.getString("adresse");
						String cp = json_data.getString("codePostal");
						String idkot = json_data.getString("id");
						int visible = json_data.getInt("visibility");
						String addr = rue + " " + cp;			
						
						List<Address> addresses = null;
				     	try {
							addresses = geo.getFromLocationName(addr, 10);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				     	Address ad = addresses.get(0); 	 
				     	
				     	Location lockot = new Location("");
				     	lockot.setLatitude(ad.getLatitude());
				     	lockot.setLongitude(ad.getLongitude());
				     	
				     	if((mCurrentLocation.distanceTo(lockot) < Filtre.getDistancechoisie()) && (visible == 1))
				     	{
				     		LatLng kot = new LatLng(ad.getLatitude(), ad.getLongitude());    	
	
				     		map.addMarker(new MarkerOptions()
					        .title(addr)
					        .snippet(idkot)
					        .position(kot)
					        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointeur)));
				     	}
					}
					
					map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
						
						@Override
						public void onInfoWindowClick(Marker arg0) {
							String i = arg0.getSnippet();
							Intent intent = new Intent(MainActivity.this, AfficheKot.class);
							intent.putExtra("idkot", i);	
							startActivity(intent);
						}
					});

	     		} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
     		}
     		else
     		{
         		Toast.makeText(getApplicationContext(), "Aucuns kots ne correspond � vos crit�res" , Toast.LENGTH_LONG).show();

     		}
     	}
     	else
     	{
     		Toast.makeText(getApplicationContext(), "Erreur lors de connexion, v�rifiez si vous disposez d'une connexion internet valide et si vos identifiants sont corrects." , Toast.LENGTH_LONG).show();
     	}
     	
        
        //********************Divers conditions qui influent sur l'affichage********
        if(Connexion.isConnected() == true) connexion.setText("Profil");   

    	/**********************************************************************************************************************************/
        //**********si on clique sur le bouton des vues*********
    	vues.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(typeVue == 4)
            	{
            		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            		typeVue = 1;
            		vues.setBackgroundResource(R.drawable.satnon);
            	}
            	else
            	{
            		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            		typeVue = 4;
            		vues.setBackgroundResource(R.drawable.satoui);
            	}
            }
        });

    	//*********si on clique sur le bouton filtre
    	filtre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MainActivity.this, Filtre.class);
            	startActivity(intent);
            }
        });
    	
    	//********si on clique sur le bouton ajouter un kot*********
    	ajout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(Connexion.isConnected() == false)
            	{		
             		Toast.makeText(getApplicationContext(), "Vous devez vous connecter pour ajouter un kot" , Toast.LENGTH_SHORT).show();
            	}
            	else 
            	{
            		Intent intent = new Intent(MainActivity.this, AjoutKot.class);
            		startActivity(intent);
            	}
            }
        });
    	
    	
    	//********si on clique sur le bouton connexion (ou d�co)*********
    	connexion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(Connexion.isConnected() == false)
            	{		
            		Intent intent = new Intent(MainActivity.this, Connexion.class);
            		startActivity(intent);
            	}
            	else 
            	{
            		Intent intent = new Intent(MainActivity.this, Profil.class);
            		startActivity(intent);
            	}
            }
        });	 
    }

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		//Toast.makeText(this, "error while connecting.",Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onConnected(Bundle arg0) {
		//Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

	}
	@Override
	public void onDisconnected() {
		//Toast.makeText(this, "Disconnected. Please re-connect.",Toast.LENGTH_SHORT).show();
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
	
    
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
    
    public void onProviderDisabled(String provider) {
    	Log.d("Latitude","disable");
    	}

    public void onProviderEnabled(String provider) {
    Log.d("Latitude","enable");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    Log.d("Latitude","status");
    }
    
    @Override
    public void onLocationChanged(Location location) {
    	lat = location.getLatitude();
    	lon = location.getLongitude();
    }
    
    
	public static int getTypeVue() {
		return typeVue;
	}

	public static void setTypeVue(int typeVue) {
		MainActivity.typeVue = typeVue;
	}
	
	//************************Classe thread qui int�ragit avec le serveur et qui recoit les kots
		public class Script extends AsyncTask <String, Void, String> {

			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				ringProgressDialog = ProgressDialog.show(MainActivity.this, "Chargement", "Patientez s'il vous plait", true);
			}
			
			
			@Override
			protected String doInBackground(String... params) {
				
				try{
					
				  	HttpClient httpclient = new DefaultHttpClient();
			        HttpPost request = new HttpPost(params[0]);   		        
			        
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(14);
			        nameValuePairs.add(new BasicNameValuePair("sprixmin", params[1]));
			        nameValuePairs.add(new BasicNameValuePair("sprixmax", params[2]));
			        nameValuePairs.add(new BasicNameValuePair("ssurface", params[3]));
			        nameValuePairs.add(new BasicNameValuePair("sbail", params[4]));
			        nameValuePairs.add(new BasicNameValuePair("snbcolocsmin", params[5]));
			        nameValuePairs.add(new BasicNameValuePair("snbcolocsmax", params[6]));
			        nameValuePairs.add(new BasicNameValuePair("snbchambres", params[7]));
			        nameValuePairs.add(new BasicNameValuePair("sparking", params[8]));
			        nameValuePairs.add(new BasicNameValuePair("sanimal", params[9]));
			        nameValuePairs.add(new BasicNameValuePair("scharge", params[10]));
			        nameValuePairs.add(new BasicNameValuePair("sjardin", params[11]));
			        nameValuePairs.add(new BasicNameValuePair("sfumeur", params[12]));
			        nameValuePairs.add(new BasicNameValuePair("smenage", params[13]));
			        nameValuePairs.add(new BasicNameValuePair("sinternet", params[14]));
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
				Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			        	 ringProgressDialog.dismiss();
			         } 
			    }, 1000);     
			}
		}
	
	
}