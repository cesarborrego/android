package sferea.todo2day;

import java.util.Timer;
import java.util.TimerTask;

import sferea.todo2day.config.DataBaseSQLiteManagerEvents;
import sferea.todo2day.helpers.CheckInternetConnection;
import sferea.todo2day.helpers.JsonHelper;
import sferea.todo2day.helpers.JsonParserHelper;
import sferea.todo2day.helpers.LocationHelper;
import sferea.todo2day.helpers.ReadTableDB;
import sferea.todo2day.helpers.SharedPreferencesHelperFinal;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Actividad que controla el tiempo de espera en el splashScreen y la accion a
 * realizar al termino de este tiempo
 * 
 * @author Mauro Bocanegra
 * @version 1.0
 * */
public class SplashActivity extends FragmentActivity {

	boolean isGpsActive = false;
	boolean isWirelessActive = false;

	/**
	 * contiene la cantidad de tiempo en milisegundos que se mostrara la imagen
	 * del splash
	 */
	private static final int SPLASH_DELAY = 3000; // 3 segundos
	private static final String KEY_INTENT_EXTRA = "IsInitialLoad";
	private SharedPreferences preferences;
	private DataBaseSQLiteManagerEvents dbManagerEvents;
	private JsonHelper jsonHelper;
	private JsonParserHelper jsonParserHelper;
	private static final int LOCATION_SETTINGS_DIALOG = 1;
	private static final int WIRELESS_SETTINGS_DIALOG = 2;
	private static final String SHARED_PREFS_NAME = "YIEPPA_PREFERENCES";

	ReadTableDB readTableDB;
	CheckInternetConnection checkInternetConnection;
	LocationHelper locationHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		checkInternetConnection = new CheckInternetConnection(this);

		readTableDB = new ReadTableDB(this);

		locationHelper = new LocationHelper(this);
		
		preferences = getSharedPreferences(SHARED_PREFS_NAME,
				Context.MODE_PRIVATE);
	}

	@Override
	protected void onResume() {
		/*
		 * Si hay eventos almacenados en BD ir directamente a Timeline.  
		 */
		if (readTableDB.getEventsDBCount() == 0) {
			if (!checkInternetConnection.isConnectedToInternet()) {
				showAlertDialog(WIRELESS_SETTINGS_DIALOG);
			}
			
			else if (!locationHelper.canGetLocation()) {
				showAlertDialog(LOCATION_SETTINGS_DIALOG);
			}
			
			makeEventsRequest();
		}
		else{
			/*
			 * Hay eventos en caché, pasa a timeline.
			 */
			showMainActivity(false);
		}
		
		super.onResume();
	}
	
	private void makeEventsRequest() {
		jsonHelper = new JsonHelper(this);
		dbManagerEvents = new DataBaseSQLiteManagerEvents(this);
		jsonParserHelper = new JsonParserHelper(this);
		locationHelper.getLocation();

		double latOrigin = locationHelper.getLatitude();
		double lonOrigin = locationHelper.getLongitude();
		
		String requestUrl = getRequestUrl(latOrigin, lonOrigin);
		
		EventsRequestTask task = new EventsRequestTask(jsonHelper, dbManagerEvents, jsonParserHelper);
		task.execute(requestUrl);
		
	}
	
	private String getRequestUrl(double latitude, double longitud) {
		
		int distancia = preferences.getInt("Distancia", 5);
		
		return "http://yapidev.sferea.com/?" + "latitud=" + latitude
				+ "" + "&longitud=" + longitud + "" + "&radio="
				+ distancia + "&categoria=&numEventos=0&idEvento=0&fecha=0";
	}

	private void showMainActivity(final boolean isInitialLoad) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				/* Create an Intent that will start the Menu-Activity. */
				Intent mainIntent = new Intent().setClass(SplashActivity.this,
						MainActivity.class);
				mainIntent.putExtra(KEY_INTENT_EXTRA, isInitialLoad);
				startActivity(mainIntent);
				finish();
			}
		}, SPLASH_DELAY);
	}

	private void showAlertDialog(int dialogType) {
		
		final Intent intent = new Intent();
		String mensaje = "";
		
		switch(dialogType){
		case LOCATION_SETTINGS_DIALOG:
			intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			mensaje = getResources().getString(R.string.gpsDesactivado);
			break;
		case WIRELESS_SETTINGS_DIALOG:
			intent.setAction(Settings.ACTION_SETTINGS);
			mensaje = getResources().getString(R.string.conexionRedDesactivada);
			break;
		}
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(mensaje)
				.setPositiveButton(R.string.btnTextAceptar,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(intent);
							}

						})
				.setNegativeButton(R.string.btnTextCancelar,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								finish();
							}
						});
		// Create the AlertDialog object and return it
		builder.show();
	}

	static {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
	
	private class EventsRequestTask extends AsyncTask<String, Void, Void> {
		
		JsonHelper jsonHelper;
		DataBaseSQLiteManagerEvents eventsManager;
		JsonParserHelper jsonParser;
		
		public EventsRequestTask(JsonHelper jsonHelper, DataBaseSQLiteManagerEvents eventsManager, JsonParserHelper jsonParser){
			this.jsonHelper = jsonHelper;
			this.eventsManager = eventsManager;
			this.jsonParser = jsonParser;
		}
		
		@Override
		protected Void doInBackground(String... params) {
			Log.d("Mongo", params[0]);
			String result = jsonHelper.connectionMongo_Json(params[0]);

			if (!result.isEmpty()) {
				this.eventsManager.eliminarAllItems();
				jsonParser = new JsonParserHelper(getApplicationContext());
				jsonParser.addEventsToDB(result);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!this.isCancelled()) {
				
				showMainActivity(true);
				
			}

		}
		
	}
}
