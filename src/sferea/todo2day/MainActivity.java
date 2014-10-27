package sferea.todo2day;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sferea.todo2day.Helpers.SharedPreferencesHelperFinal;
import sferea.todo2day.adapters.ArrayAdapterSettings;
import sferea.todo2day.adapters.DrawerItemRow;
import sferea.todo2day.config.Constants_Settings;
import sferea.todo2day.config.SharedPreferencesHelper;
import sferea.todo2day.subfragments.Page_Favorites;
import sferea.todo2day.subfragments.Page_TimeLine;
import sferea.todo2day.subfragments.SubF_Categories;
import sferea.todo2day.subfragments.SubF_Events;
import sferea.todo2day.subfragments.SubF_Settings;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
 * ACTIVIDAD PRINCIPAL 
 * Contiene los twicks del navigation drawer
 * 
 * @author maw
 *
 */
public class MainActivity extends ActionBarActivity {
	
	/** contiene la instancia del navDrawer */
	private DrawerLayout drawerLayout;
	private ListView listViewDrawer;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private List<DrawerItemRow> listaSettings;
	private ArrayAdapterSettings arrayAdapterSettings;
	
	private static final int EVENTS = 1;
	private static final int CATEGORIES = 2;
	private static final int SETTINGS = 3;
	
	private boolean atHome = true;
	
	double latOrigin= 19.355582;
	double lonOrigin =-99.186726;
	SharedPreferencesHelperFinal sharedPreferencesHelperFinal;
	
//	double latOrigin;
//	double lonOrigin;
	
	boolean isGpsActive = false;
	boolean isWirelessActive = false;
	
	String categorias ="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_responsive);
		
//		gps();
		
		listViewDrawer = (ListView)findViewById(R.id.listViewDrawer);
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		
		
		View header = getLayoutInflater().inflate(R.layout.drawer_header, null);
		listViewDrawer.addHeaderView(header);
		
		listaSettings = new ArrayList<DrawerItemRow>();
		listaSettings.add(new DrawerItemRow(getResources().getString(R.string.eventos), R.drawable.ic_action_go_to_today));
		listaSettings.add(new DrawerItemRow(getResources().getString(R.string.categorias), R.drawable.ic_action_overflow_light));
		listaSettings.add(new DrawerItemRow(getResources().getString(R.string.ajustes), R.drawable.ic_action_settings_light));
		arrayAdapterSettings = new ArrayAdapterSettings(getApplicationContext(), R.layout.row_setting, R.id.listViewDrawer, listaSettings);
		
		listViewDrawer.setAdapter(arrayAdapterSettings);
		
		listViewDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
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
		
		//Listener para el toggle del nav drawer
		mDrawerToggle = new ActionBarDrawerToggle(
				this, 					//referencia
				drawerLayout, 			//el nav drawer
				R.drawable.ic_drawer, 	//icono para el drawer
				R.string.app_name, 		//String para el actionBar cuando el drawer esta abierto
				R.string.app_name		//String para el actionBar cuando el drawer esta abierto
		){
			
			public void onDrawerClosed(View view){
				Log.d("Cerrado completo","!!");
				//Accion para cuando termine de cerrar
			}
			
			public void onDrawerOpened(View drawerView){
				checkAndFillUserData();
			}
			
		};
		
		drawerLayout.setDrawerListener(mDrawerToggle);	//Establecer el listener
		ActionBar actionBar = getSupportActionBar();	//Obtiene el ActionBar para <Android4.0
		actionBar.setDisplayHomeAsUpEnabled(true);		//Habilitar el boton superior
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(R.string.titleColors);
		actionBar.setIcon(R.drawable.ic_action_go_to_today_dark);
		actionBar.setBackgroundDrawable(new ColorDrawable(0xFFF78326));
		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		if(actionBarTitleId > 0 ){
			TextView title = (TextView)findViewById(actionBarTitleId);
			if(title!=null){title.setTextColor(Color.WHITE);}
		}
		
		Constants_Settings constants = new Constants_Settings();
		SharedPreferencesHelper shrpref = new SharedPreferencesHelper(constants.SHARED_PREF_NAME, this);
		
		if(getResources().getBoolean(R.bool.doublePane)){
			shrpref.Write_String(constants.DUAL_PANE, "true");
		}
		else{
			shrpref.Write_String(constants.DUAL_PANE, "false");
		}
		
	}
	
	/**
	 * Gestiona la cadena de usuario y avatar dependiendo de estos casos:
	 * SI conexion, SI datosExistentes = cambiar cadena y avatar
	 * NO conexion, SI datosExistentes = cambia cadena TOAST de conectar para mostrar avatar
	 * SI/NO conexion, NO cadena = cambia a cadena y avatar predeterminado
	 */
	private void checkAndFillUserData(){
		Constants_Settings constants = new Constants_Settings();
		SharedPreferencesHelper shrpref = new SharedPreferencesHelper(constants.SHARED_PREF_NAME, getApplicationContext());
		String userName = shrpref.Get_stringfrom_shprf(constants.USER_NAME);
		final String avatarURL = shrpref.Get_stringfrom_shprf(constants.AVATAR_URL);
		
		if(userName!=null){
			((TextView)findViewById(R.id.textUsuario)).setText(userName);
		}else{
			((TextView)findViewById(R.id.textUsuario)).setText(getResources().getString(R.string.defaultUser));
		}
		
		if(isConnectedToInternet(getApplicationContext()) && avatarURL!=null){
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
					((ImageView)findViewById(R.id.avatarHeaderTwitter)).setImageBitmap(result);
					((ImageView)findViewById(R.id.avatarHeaderTwitter)).setVisibility(View.VISIBLE);
					((ImageView)findViewById(R.id.avatarHeaderDefault)).setVisibility(View.INVISIBLE);
					super.onPostExecute(result);
				}
				
			}.execute();
		}else if(!isConnectedToInternet(getApplicationContext()) && avatarURL!=null ){
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.checkInternetAvatar), Toast.LENGTH_SHORT).show();
		}else if(avatarURL==null){
			((ImageView)findViewById(R.id.avatarHeaderTwitter)).setVisibility(View.INVISIBLE);
			((ImageView)findViewById(R.id.avatarHeaderDefault)).setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * sustituye los fragmentos dependiendo del parametro que reciba
	 * @param position = eventos, categorias o ajustes
	 */
	private void ShowFragment(final int position){
		
		//inicializamos la varible
		Fragment fragment = null;
		
		//elegimos que opcion se escogido como parametro, entre categorias, ajustes, o timeline
		switch (position) {
		
		case EVENTS:{
			if(!atHome){
				downloadJSON(19.355582, -99.186726);
//				atHome=true;
			}else{
				atHome=true;
				fragment = new SubF_Events();
			}
			
//			fragment = new SubF_Events();
			break;
		}
		
		case CATEGORIES:{
			fragment = new SubF_Categories();
			atHome=false;
			break;
		}
		
		case SETTINGS:{
			fragment = new SubF_Settings();
			atHome=false;
			break;
		}

		//Si es una opcion diferente nos manda al TimeLine
		/*
		default:
			Toast.makeText(getApplicationContext(), "Non existent: "+position, Toast.LENGTH_SHORT).show();
			fragment = new SubF_Events();
			position=EVENTS;
			break;
			*/
		}
		
		if(fragment!=null){
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			
			//Actualizamos el nav drawer segun la opcion elegida
			listViewDrawer.setItemChecked(position, true);
			listViewDrawer.setSelection(position);
			//Cerramos el nav drawer
		}
	}
	
	/**
	  * Si se manda como parametro el homebutton del ActionBar y se devuelve true responde hacia la pulsacion del mismo
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
	     //mDrawerToggle.syncState();
	 }

	 /**
	  * Sincroniza el estado de configuracion del drawer y el actionBar
	  */
	 @Override
	 public void onConfigurationChanged(Configuration newConfig) {
	     super.onConfigurationChanged(newConfig);
	     mDrawerToggle.onConfigurationChanged(newConfig);
	 }
	 
	 public boolean isConnectedToInternet(Context _context){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
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
	 public void onBackPressed(){
		 if(!atHome){
			 downloadJSON(19.355582, -99.186726);
			 
			}else{
				finish();		 
		 }
	 }
	 
	 @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	 
	 private void refreshFavoritesFragment(){
		 //inicializamos la varible
		 Fragment fragment = null;
		 fragment = new Page_Favorites();

		 if(fragment!=null){
			 FragmentManager fragmentManager = getSupportFragmentManager();
			 fragmentManager.beginTransaction().replace(R.id.content_Favorites, fragment).commit();
		 }		 
	 }
	 
	 private void refreshPage_TimelLineFragment(){
		 //inicializamos la varible
		 Fragment fragment = null;
		 fragment = new SubF_Events();

		 if(fragment!=null){
			 FragmentManager fragmentManager = getSupportFragmentManager();
			 fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		 }		 
	 }
	 
	//Consulta mongo
		public void downloadJSON(final double lat, final double lon) {	
			
			final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
	        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pDialog.setMessage("Procesando nueva búsqueda...");
	        pDialog.setCancelable(true);
	        pDialog.setMax(100);
	        
	        sharedPreferencesHelperFinal = new SharedPreferencesHelperFinal(getApplicationContext());
			
			
			
	    	new AsyncTask<String, Void, InputStream>(){
	    		
	    		protected void onPreExecute() {
	    			pDialog.show();
	    		};

				@Override
				protected InputStream doInBackground(String... params) {
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
							readStream(inputStream);
//							leerJson();
						}
					}
					
					
			        
					return inputStream;
				}
				
				@Override
				protected void onPostExecute(InputStream result) {
//					if(result!=null){
//						readStream(result);
////						leerJson();
//					}
//	    			refreshFavoritesFragment();
	    			SplashActivity.leeJSONCache = false;
	    			Page_TimeLine.arrayAdapterEvents.notifyDataSetChanged();
	    			pDialog.dismiss();
//	    			ShowFragment(EVENTS);
	    			atHome=true;
	    			refreshPage_TimelLineFragment();
	    			
				};
				
				@Override
				protected void onCancelled() {
					super.onCancelled();
				};
	    		
	    	}.execute("http://yapi.sferea.com/?" +
	    			"latitud="+latOrigin+"" +
	    			"&longitud="+lonOrigin+"" +
	    			"&radio="+SplashActivity.distanciaEvento+"" +
	    			"&categoria="+sharedPreferencesHelperFinal.obtieneCategoriasPreferences()+"" +
	    			"&numEventos=0&idEvento=0&fecha=0");
	    }
	   
	   private void readStream(InputStream in) {
	    	BufferedReader reader = null;
	    	OutputStreamWriter outputStreamWriter = null;
	    	try {
	    		reader = new BufferedReader(new InputStreamReader(in));
	    		String line = "";
	    		while ((line = reader.readLine()) != null) {
	    			outputStreamWriter = new OutputStreamWriter(openFileOutput("Json.txt", Context.MODE_PRIVATE));
	    			outputStreamWriter.write(line);	    					
	    			outputStreamWriter.flush();
	    			outputStreamWriter.close();
	    		}
	    		Log.d(null, "Json Creado!");
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	} finally {
	    		if (reader != null) {
	    			try {
	    				reader.close();
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}	    		
	    	}
	    }
	   
	   public void gps(){
			LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			
			isGpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isWirelessActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
//			//Verificamos que este el gps activado, si no entonces toma la ubicacion por red
//			if(isGpsActive){
//				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//				latOrigin = location.getLatitude();
//				lonOrigin = location.getLongitude();
//				Log.d("GPS", "Latitud "+latOrigin);
//				Log.d("GPS", "Longitud "+lonOrigin);
//			}else{
//				Toast.makeText(getApplicationContext(), "Yieppa funciona mejor cuando activas el GPS :)", Toast.LENGTH_LONG).show();
//				//Asumimos que esta prendido el wireless ya que si no lo fuera, no entraría a la app
//				if(isWirelessActive){
//					Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//					latOrigin = location.getLatitude();
//					lonOrigin = location.getLongitude();
//					Log.d("WIFI", "Latitud "+latOrigin);
//					Log.d("WIFI", "Longitud "+lonOrigin);
//				}
//				
////				finish();
//			}
//			if(isWirelessActive){
//				Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//				latOrigin = location.getLatitude();
//				lonOrigin = location.getLongitude();
//				Log.d("WIFI", "Latitud "+latOrigin);
//				Log.d("WIFI", "Longitud "+lonOrigin);
//			}

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
//			if(isGpsActive){
//				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);			
//			}else{
//				if(isWirelessActive){
//					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, locationListener);
//				}
//			}
			if(isWirelessActive){
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, locationListener);
			}
		}
	   
	   static{
		     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();  
		     StrictMode.setThreadPolicy(policy);  
		}

}
