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
	long splashDelay = 3000; // 3 segundos
	
	private static final int LOCATION_SETTINGS_DIALOG = 1;
	private static final int WIRELESS_SETTINGS_DIALOG = 2;

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
		}
		else{
			showMainActivity();
		}

		super.onResume();
	}

	private void showMainActivity() {
		new Handler().postDelayed(new Runnable() {
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
}
