package sferea.todo2day;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sferea.todo2day.adapters.ArrayAdapterSettings;
import sferea.todo2day.adapters.DrawerItemRow;
import sferea.todo2day.config.Constants_Settings;
import sferea.todo2day.config.DataBaseSQLiteManagerEvents;
import sferea.todo2day.config.SharedPreferencesHelper;
import sferea.todo2day.fragments.SubF_Categories;
import sferea.todo2day.fragments.SubF_Events;
import sferea.todo2day.fragments.SubF_Settings;
import sferea.todo2day.helpers.CheckInternetConnection;
import sferea.todo2day.helpers.JsonHelper;
import sferea.todo2day.helpers.JsonParserHelper;
import sferea.todo2day.helpers.LocationHelper;
import sferea.todo2day.helpers.SharedPreferencesHelperFinal;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ACTIVIDAD PRINCIPAL Contiene los twicks del navigation drawer
 * 
 * @author maw
 *
 */
public class MainActivity extends ActionBarActivity {

	/** contiene la instancia del navDrawer */
	private DrawerLayout drawerLayout;
	private ListView listViewDrawer;
	private ActionBarDrawerToggle mDrawerToggle;
	private static final String BROADCAST_INTENT_NAME = "actualiza_timeline";
	private static final String SHARED_PREFS_NAME = "YIEPPA_PREFERENCES";

	private List<DrawerItemRow> listaSettings;
	private ArrayAdapterSettings arrayAdapterSettings;

	private static final int EVENTS = 1;
	private static final int CATEGORIES = 2;
	private static final int SETTINGS = 3;
	
	private EventsRequestTask task;
	private boolean atHome = true;
	
	SharedPreferences preferences;
	SharedPreferencesHelperFinal sharedPreferencesHelperFinal;
	JsonHelper jsonHelper;
	JsonParserHelper jsonParser;
	DataBaseSQLiteManagerEvents dataBaseSQLiteManagerEvents;
	LocationHelper locationHelper;
	CheckInternetConnection internetConnectionCheck;
	ProgressDialog pDialog;

	double latOrigin;
	double lonOrigin;

	boolean isGpsActive = false;
	boolean isWirelessActive = false;

	String categorias = "";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listViewDrawer = (ListView) findViewById(R.id.listViewDrawer);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		jsonParser = new JsonParserHelper(Application.getInstance());

		View header = getLayoutInflater().inflate(R.layout.drawer_header, null);
		listViewDrawer.addHeaderView(header);

		listaSettings = new ArrayList<DrawerItemRow>();
		listaSettings.add(new DrawerItemRow(getResources().getString(
				R.string.eventos), R.drawable.ic_action_go_to_today));
		listaSettings.add(new DrawerItemRow(getResources().getString(
				R.string.categorias), R.drawable.ic_action_overflow_light));
		listaSettings.add(new DrawerItemRow(getResources().getString(
				R.string.ajustes), R.drawable.ic_action_settings_light));
		arrayAdapterSettings = new ArrayAdapterSettings(
				getApplicationContext(), R.layout.row_setting,
				R.id.listViewDrawer, listaSettings);
		locationHelper = new LocationHelper(this);
		internetConnectionCheck = new CheckInternetConnection(this);
		
		preferences = getSharedPreferences(SHARED_PREFS_NAME,
				Context.MODE_PRIVATE);

		listViewDrawer.setAdapter(arrayAdapterSettings);

		listViewDrawer
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {
						drawerLayout.closeDrawer(listViewDrawer);

						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								ShowFragment(position);
							}
						}, 250);
					}
				});

		ShowFragment(EVENTS);

		// Listener para el toggle del nav drawer
		mDrawerToggle = new ActionBarDrawerToggle(this, // referencia
				drawerLayout, // el nav drawer
				R.drawable.ic_drawer, // icono para el drawer
				R.string.app_name, // String para el actionBar cuando el drawer
									// esta abierto
				R.string.app_name // String para el actionBar cuando el drawer
									// esta abierto
		) {

			public void onDrawerClosed(View view) {
				Log.d("Cerrado completo", "!!");
				// Accion para cuando termine de cerrar
			}

			public void onDrawerOpened(View drawerView) {
				checkAndFillUserData();
			}

		};

		drawerLayout.setDrawerListener(mDrawerToggle); // Establecer el listener
		ActionBar actionBar = getSupportActionBar(); // Obtiene el ActionBar
														// para <Android4.0
		actionBar.setDisplayHomeAsUpEnabled(true); // Habilitar el boton
													// superior
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(R.string.titleColors);
		actionBar.setIcon(R.drawable.ic_action_go_to_today_dark);
		actionBar.setBackgroundDrawable(new ColorDrawable(0xFFF78326));
		int actionBarTitleId = Resources.getSystem().getIdentifier(
				"action_bar_title", "id", "android");
		if (actionBarTitleId > 0) {
			TextView title = (TextView) findViewById(actionBarTitleId);
			if (title != null) {
				title.setTextColor(Color.WHITE);
			}
		}

		Constants_Settings constants = new Constants_Settings();
		SharedPreferencesHelper shrpref = new SharedPreferencesHelper(
				constants.SHARED_PREF_NAME, this);

	}

	/**
	 * Gestiona la cadena de usuario y avatar dependiendo de estos casos: SI
	 * conexion, SI datosExistentes = cambiar cadena y avatar NO conexion, SI
	 * datosExistentes = cambia cadena TOAST de conectar para mostrar avatar
	 * SI/NO conexion, NO cadena = cambia a cadena y avatar predeterminado
	 */
	private void checkAndFillUserData() {
		Constants_Settings constants = new Constants_Settings();
		SharedPreferencesHelper shrpref = new SharedPreferencesHelper(
				constants.SHARED_PREF_NAME, getApplicationContext());
		String userName = shrpref.Get_stringfrom_shprf(constants.USER_NAME);
		final String avatarURL = shrpref
				.Get_stringfrom_shprf(constants.AVATAR_URL);

		if (userName != null) {
			((TextView) findViewById(R.id.textUsuario)).setText(userName);
		} else {
			((TextView) findViewById(R.id.textUsuario)).setText(getResources()
					.getString(R.string.defaultUser));
		}

		if (isConnectedToInternet(getApplicationContext()) && avatarURL != null) {
			new AsyncTask<Void, Void, Bitmap>() {

				@Override
				protected Bitmap doInBackground(Void... params) {
					Bitmap mIcon = null;
					InputStream in;
					try {
						in = new java.net.URL(avatarURL).openStream();
						mIcon = BitmapFactory.decodeStream(in);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return mIcon;
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					((ImageView) findViewById(R.id.avatarHeaderTwitter))
							.setImageBitmap(result);
					((ImageView) findViewById(R.id.avatarHeaderTwitter))
							.setVisibility(View.VISIBLE);
					((ImageView) findViewById(R.id.avatarHeaderDefault))
							.setVisibility(View.INVISIBLE);
					super.onPostExecute(result);
				}

			}.execute();
		} else if (!isConnectedToInternet(getApplicationContext())
				&& avatarURL != null) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.checkInternetAvatar),
					Toast.LENGTH_SHORT).show();
		} else if (avatarURL == null) {
			((ImageView) findViewById(R.id.avatarHeaderTwitter))
					.setVisibility(View.INVISIBLE);
			((ImageView) findViewById(R.id.avatarHeaderDefault))
					.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * sustituye los fragmentos dependiendo del parametro que reciba
	 * 
	 * @param position
	 *            = eventos, categorias o ajustes
	 */
	private void ShowFragment(final int position) {

		// inicializamos la varible
		Fragment fragment = null;

		// elegimos que opcion se escogido como parametro, entre categorias,
		// ajustes, o timeline
		switch (position) {

		case EVENTS:
			if (atHome) {
				fragment = new SubF_Events();
				makeFragmentTransaction(fragment);
			}

			downloadJSON();

			break;

		case CATEGORIES:
			fragment = new SubF_Categories();
			atHome = false;
			makeFragmentTransaction(fragment);
			cancelEventTask();
			break;

		case SETTINGS:
			fragment = new SubF_Settings();
			atHome = false;
			makeFragmentTransaction(fragment);
			cancelEventTask();
			break;
		}

		// Actualizamos el nav drawer segun la opcion elegida
		listViewDrawer.setItemChecked(position, true);
		listViewDrawer.setSelection(position);
		// Cerramos el nav drawer
	}

	private void makeFragmentTransaction(Fragment fragment) {
		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

		}
	}
	
	private void cancelEventTask() {
		if(task != null && task.getStatus() == AsyncTask.Status.RUNNING){
			task.cancel(true);
		}
	}

	/**
	 * Si se manda como parametro el homebutton del ActionBar y se devuelve true
	 * responde hacia la pulsacion del mismo
	 * 
	 * @params MenuItem
	 * @return true;
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Sincroniza el estado del nav drawer y el actionBar
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	/**
	 * Sincroniza el estado de configuracion del drawer y el actionBar
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
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
		return false;
	}

	@Override
	/**
	 * Sobreescribe el evnto de la tecla atras en el menu principal para regresar a home si es que no esta ahi
	 * o salir de la app si esta en home
	 */
	public void onBackPressed() {
		if (!atHome) {
			downloadJSON();

		} else {
			finish();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	// Consulta mongo
	public void downloadJSON() {
		
		sharedPreferencesHelperFinal = new SharedPreferencesHelperFinal(this);
		jsonHelper = new JsonHelper(this);
		dataBaseSQLiteManagerEvents = new DataBaseSQLiteManagerEvents(this);
		locationHelper = new LocationHelper(this);
		int distancia = preferences.getInt("Distancia", 5);
		
		if (!locationHelper.canGetLocation()) {
			Toast.makeText(this, R.string.errorUbicacion, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (!internetConnectionCheck.isConnectedToInternet()) {
			Toast.makeText(this, R.string.NoInternetToastMessage,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		for (int x = 0; x < 13; x++) {
			if (preferences.getString("Categories " + x, null) != null) {
				sharedPreferencesHelperFinal.creaArchivoShared();
				break;
			}
		}
		
		if (!atHome) {
			getProgressDialog();
			pDialog.show();
		}

		locationHelper.getLocation();

		latOrigin = locationHelper.getLatitude();
		lonOrigin = locationHelper.getLongitude();

		task = new EventsRequestTask();
		task.execute("http://yapidev.sferea.com/?" + "latitud=" + latOrigin
				+ "" + "&longitud=" + lonOrigin + "" + "&radio="
				+ distancia + "" + "&categoria="
				+ sharedPreferencesHelperFinal.obtieneCategoriasPreferences()
				+ "" + "&numEventos=0&idEvento=0&fecha=0");
	}

	private void getProgressDialog() {
		if (pDialog == null) {
			pDialog = new ProgressDialog(this);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage(getResources().getString(
					R.string.progressDialogBusqueda));
			pDialog.setCancelable(true);
			pDialog.setMax(100);
		}
	}

	private void sendMessage() {
		Intent intent = new Intent(BROADCAST_INTENT_NAME);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	static {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	private class EventsRequestTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			Log.d("Mongo", params[0]);
			String result = jsonHelper.connectionMongo_Json(params[0]);

			if (!result.isEmpty()) {
				dataBaseSQLiteManagerEvents.eliminarAllItems();
				jsonParser = new JsonParserHelper(getApplicationContext());
				jsonParser.addEventsToDB(result);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!this.isCancelled()) {
				if (pDialog != null && pDialog.isShowing()) {
					pDialog.dismiss();

				}
				
				if(!atHome){
					makeFragmentTransaction(new SubF_Events());
					atHome = true;
				}
				
				sendMessage();
			}

		};

		@Override
		protected void onCancelled() {
			super.onCancelled();
		};
	}

}
