package sferea.todo2day;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.impl.conn.Wire;

import sferea.todo2day.Helpers.JsonHelper;
import sferea.todo2day.Helpers.SharedPreferencesHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

/** 
 * Actividad que controla el tiempo de espera en el splashScreen y la accion a realizar al termino de este tiempo
 * 
 * @author Mauro Bocanegra
 * @version 1.0
 * */
public class SplashActivity extends Activity {
		double latOrigin= 19.355582;
		double lonOrigin =-99.186726;

//	double latOrigin;
//	double lonOrigin;

	boolean isGpsActive = false;
	boolean isWirelessActive = false;
	
	public static int distanciaEvento=5;

	String categorias ="";
	
	boolean creaArchivoShared = true;
	SharedPreferences prefsCategorias;

	/** contiene la cantidad de tiempo en milisegundos que se mostrara la imagen del splash */
	private long splashDelay = 3000; //3 segundos
	
	JsonHelper jsonHelper;
	SharedPreferencesHelper sharedPreferencesHelper;
	
	public static boolean leeJSONCache = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		jsonHelper = new JsonHelper(getApplicationContext());
		sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
		
		if(jsonHelper.leerJsonCache()!=null){
			leeJSONCache = true;
			Toast.makeText(getApplicationContext(), "Pasa a time", Toast.LENGTH_SHORT).show();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);
					startActivity(mainIntent);
					finish();//Destruimos esta activity para prevenir que el usuario retorne aqui presionando el boton Atras.
				}
			};

			Timer timer = new Timer();
			timer.schedule(task, splashDelay);//Pasado los X segundos dispara la tarea
		}else{
			leeJSONCache = false;
			Toast.makeText(getApplicationContext(), "No hay archivo cache", Toast.LENGTH_SHORT).show();
			if(isConnectedToInternet(getApplicationContext())){
				//		gps();

				TimerTask task = new TimerTask() {
					@Override
					public void run() {


						prefsCategorias = getSharedPreferences("Categorias",Context.MODE_PRIVATE);
						for (int x=0; x<13; x++){
							if(prefsCategorias.getString("Categories "+x, null)!=null){
								creaArchivoShared = false;
								System.out.println("No");
								break;
							}else{
								System.out.println("Si");
							}
						}

						if(creaArchivoShared){
							sharedPreferencesHelper.creaArchivoShared();
							downloadJSON(latOrigin, lonOrigin);
						} 
						else {

							int coma =0;
							//SOn 13 categorias
							for (int x=0; x<13; x++){
								if(!prefsCategorias.getString("Categories "+x, "Desactivada").equals("Desactivada")
										&!prefsCategorias.getString("Categories "+x, "Desactivada").equals("")){	
									if(coma!=0){
										categorias += ","+prefsCategorias.getString("Categories "+x, null);	
									} else {
										categorias += prefsCategorias.getString("Categories "+x, null);	
									}
									coma++;		
								}
							}
							downloadJSON(latOrigin, lonOrigin);
						}
					}
				};

				Timer timer = new Timer();
				timer.schedule(task, splashDelay);//Pasado los X segundos dispara la tarea
			}
		}
	}

	public void downloadJSON(final double lat, final double lon) {
		Log.d(null, "Comienza la descarga del JSON...");
		
		new AsyncTask<String, Void, Void>(){

			protected void onPreExecute() {

			};

			@Override
			protected Void doInBackground(String... params) {
				InputStream inputStream = null;
				URL url = null;
				HttpURLConnection con = null;
				if(!isCancelled()){					
					try {
						url = new URL(params[0]);
						Log.d(null,params[0]);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						con = (HttpURLConnection) url.openConnection();
						inputStream = con.getInputStream();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if(inputStream!=null){
						jsonHelper.readStreamPrimerJson(inputStream);
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);
				startActivity(mainIntent);
				finish();//Destruimos esta activity para prevenir que el usuario retorne aqui presionando el boton Atras.
			};

			@Override
			protected void onCancelled() {
				super.onCancelled();
			};

		}.execute("http://yapi.sferea.com/?latitud="+latOrigin+"" +
				"&longitud="+lonOrigin+"" +
				"&radio="+SplashActivity.distanciaEvento+"" +
				"&categoria="+categorias+"" +
				"&numEventos=0&idEvento=0&fecha=0");
	}

	

	

	public void gps(){
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		isGpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isWirelessActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		//Verificamos que este el gps activado, si no entonces toma la ubicacion por red
//		if(isGpsActive){
//			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//			latOrigin = location.getLatitude();
//			lonOrigin = location.getLongitude();
//			Log.d("GPS", "Latitud "+latOrigin);
//			Log.d("GPS", "Longitud "+lonOrigin);
//		}else{
//			Toast.makeText(getApplicationContext(), "Yieppa funciona mejor cuando activas el GPS :)", Toast.LENGTH_LONG).show();
//			//Asumimos que esta prendido el wireless ya que si no lo fuera, no entraría a la app
//			if(isWirelessActive){
//				Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//				latOrigin = location.getLatitude();
//				lonOrigin = location.getLongitude();
//				Log.d("WIFI", "Latitud "+latOrigin);
//				Log.d("WIFI", "Longitud "+lonOrigin);
//			}
//			
////			finish();
//		}
//		if(isWirelessActive){
//			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//			latOrigin = location.getLatitude();
//			lonOrigin = location.getLongitude();
//			Log.d("WIFI", "Latitud "+latOrigin);
//			Log.d("WIFI", "Longitud "+lonOrigin);
//		}

		LocationListener locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				//				Log.d("GPS", "Latitud"+location.getLatitude());
				//				Log.d("GPS", "Longitud"+location.getLongitude());
				latOrigin = location.getLatitude();
				lonOrigin = location.getLongitude();
			}
		};
//		if(isGpsActive){
//			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);			
//		}else{
//			if(isWirelessActive){
//				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, locationListener);
//			}
//		}
		if(isWirelessActive){
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, locationListener);
		}
	}

	public boolean isConnectedToInternet(Context _context) {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		Toast.makeText(getApplicationContext(),
				"Verificar la conexión a internet", Toast.LENGTH_SHORT).show();
		this.finish();
		return false;
	}

	static{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();  
		StrictMode.setThreadPolicy(policy);  
	}
}
