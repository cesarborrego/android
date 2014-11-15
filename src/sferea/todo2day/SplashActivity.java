package sferea.todo2day;

import java.util.Timer;
import java.util.TimerTask;

import sferea.todo2day.Helpers.CheckInternetConnection;
import sferea.todo2day.Helpers.JsonHelper;
import sferea.todo2day.Helpers.JsonParserHelper;
import sferea.todo2day.Helpers.LocationHelper;
import sferea.todo2day.Helpers.ReadTableDB;
import sferea.todo2day.Helpers.SharedPreferencesHelperFinal;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

/**
 * Actividad que controla el tiempo de espera en el splashScreen y la accion a
 * realizar al termino de este tiempo
 * 
 * @author Mauro Bocanegra
 * @version 1.0
 * */
public class SplashActivity extends Activity {
//	double latOrigin = 19.355582;
//	double lonOrigin = -99.186726;

	double latOrigin;
	double lonOrigin;

	boolean isGpsActive = false;
	boolean isWirelessActive = false;

	public static int distanciaEvento = 5;

	String categorias = "";

	boolean creaArchivoShared = true;
	SharedPreferences prefsCategorias;

	/**
	 * contiene la cantidad de tiempo en milisegundos que se mostrara la imagen
	 * del splash
	 */
	private long splashDelay = 3000; // 3 segundos

	JsonHelper jsonHelper;
	SharedPreferencesHelperFinal sharedPreferencesHelper;
	ReadTableDB readTableDB;
	CheckInternetConnection checkInternetConnection;
	LocationHelper locationHelper;

	public static boolean leeJSONCache = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		jsonHelper = new JsonHelper(getApplicationContext());
		sharedPreferencesHelper = new SharedPreferencesHelperFinal(
				getApplicationContext());
		readTableDB = new ReadTableDB(getApplicationContext());
		checkInternetConnection = new CheckInternetConnection(
				getApplicationContext(), this);

		if (checkInternetConnection.isConnectedToInternet()) {
			locationHelper = new LocationHelper(getApplicationContext());
			
			latOrigin = locationHelper.getLatitude();
			lonOrigin = locationHelper.getLongitude();
			
			TimerTask task = new TimerTask() {
				@Override
				public void run() {

					prefsCategorias = getSharedPreferences("Categorias",
							Context.MODE_PRIVATE);
					for (int x = 0; x < 13; x++) {
						if (prefsCategorias.getString("Categories " + x, null) != null) {
							creaArchivoShared = false;
							break;
						}
					}

					if (creaArchivoShared) {
						sharedPreferencesHelper.creaArchivoShared();
						downloadJSON(latOrigin, lonOrigin);
					} else {
						sharedPreferencesHelper.obtieneCategoriasPreferences();
						downloadJSON(latOrigin, lonOrigin);
					}
				}
			};

			Timer timer = new Timer();
			timer.schedule(task, splashDelay);// Pasado los X segundos dispara la tarea
		}
		else{
			
			new Handler().postDelayed(new Runnable(){
	            @Override 
	            public void run() { 
	                /* Create an Intent that will start the Menu-Activity. */ 
	            	Intent mainIntent = new Intent().setClass(SplashActivity.this,
	    					MainActivity.class);
	    			startActivity(mainIntent);
	    			finish();
	            } 
	        }, splashDelay);

			
		}
	}

	public void downloadJSON(final double lat, final double lon) {
		Log.d(null, "Comienza la descarga del JSON...");

		new AsyncTask<String, Void, Void>() {

			protected void onPreExecute() {

			};

			@Override
			protected Void doInBackground(String... params) {
				String result = jsonHelper.connectionMongo_Json(params[0]);
				JsonParserHelper helper = new JsonParserHelper(Application.getInstance());
				helper.addEventsToDB(result);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				Intent mainIntent = new Intent().setClass(SplashActivity.this,
						MainActivity.class);
				startActivity(mainIntent);
				finish();// Destruimos esta activity para prevenir que el
							// usuario retorne aqui presionando el boton Atras.
			};

			@Override
			protected void onCancelled() {
				super.onCancelled();
			};

		}.execute("http://yapi.sferea.com/?latitud=" + latOrigin + ""
				+ "&longitud=" + lonOrigin + "" + "&radio="
				+ SplashActivity.distanciaEvento + "" + "&categoria="
				+ categorias + "" + "&numEventos=0&idEvento=0&fecha=0");
	}

	static {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
}
