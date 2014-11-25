package sferea.todo2day;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.fragments.Page_TimeLine;
import sferea.todo2day.helpers.LocationHelper;
import sferea.todo2day.utils.TypefaceSpan;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends ActionBarActivity {
	
	GoogleMap mMap;
	EventoObjeto eventoObjeto;
	Marker marcadorNuevoPunto;
	LocationHelper locationHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
//		ActionBar actionBar = getSupportActionBar();	//Obtiene el ActionBar para < Android 4.0
//		//actionBar.setDisplayHomeAsUpEnabled(true);		//Habilitar el boton superior
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setTitle(R.string.titleColors);
//		actionBar.setIcon(R.drawable.ic_action_go_to_today_dark);
//		actionBar.setBackgroundDrawable(new ColorDrawable(0xFFF78326));
//		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
//		if(actionBarTitleId > 0 ){
//			TextView title = (TextView)findViewById(actionBarTitleId);
//			if(title!=null){title.setTextColor(Color.WHITE);}
//		}
		
		SpannableString appName = new SpannableString(getResources().getString(R.string.titleColors));
	    appName.setSpan(new TypefaceSpan(this, "BubblegumSans-Regular.ttf"), 0, appName.length(),
	            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		ActionBar actionBar = getSupportActionBar(); // Obtiene el ActionBar
														// para <Android4.0
		actionBar.setDisplayHomeAsUpEnabled(true); // Habilitar el boton
													// superior
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(appName);
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
		
		locationHelper = new LocationHelper(getApplicationContext());
		locationHelper.getLocation();
		
		setUpMapIfNeeded();
	}
	
	private void setUpMapIfNeeded() {
        mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            // The Map is verified. It is now safe to manipulate the map.
        	SharedPreferences prefs = getSharedPreferences("latlong",Context.MODE_PRIVATE);	
        	final String lat = prefs.getString("lat", null);
        	final String lon = prefs.getString("lon", null);
        	Log.d(null, lat);
        	Log.d(null, lon);
        	
        	SharedPreferences prefsMap = getSharedPreferences("map",Context.MODE_PRIVATE);
			String activa = prefsMap.getString("ActivaClicMapa", null);
    		
//        	Log.d(null, activaClic);
        	if(Page_TimeLine.activaUbicate.equals("si")&Page_TimeLine.activaRuta.equals("no")){
        		//Creamos marcador para que indique la posicion
        		final Marker marcadorOrigen = mMap.addMarker(new MarkerOptions()
//		        .position(new LatLng(Page_TimeLine.latOrigin, Page_TimeLine.lonOrigin))
        		.position(new LatLng(locationHelper.getLatitude(), locationHelper.getLongitude()))
		        .title("Tu posición"));
        		marcadorOrigen.showInfoWindow();
        		
        		Toast.makeText(getApplicationContext(), "Presiona sobre el nuevo lugar", Toast.LENGTH_LONG).show();
        		//Debe enfocar la posicion actual        		
//        		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Page_TimeLine.latOrigin, Page_TimeLine.lonOrigin), 14));
        		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationHelper.getLatitude(), locationHelper.getLongitude()), 14));
        		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
    				
    				@Override
    				public void onMapLongClick(final LatLng point) {
    					Page_TimeLine.latOrigin = point.latitude;
						Page_TimeLine.lonOrigin = point.longitude;
    					if(marcadorNuevoPunto==null){  
    						//Al darle clic borramos el antiguo marcador
    						marcadorOrigen.remove();
        					marcadorNuevoPunto = mMap.addMarker(new MarkerOptions()
        			        .position(new LatLng(point.latitude, point.longitude))
        			        .title("Nuevo Punto!"));
        					marcadorNuevoPunto.showInfoWindow();
    					}else {
    						//Removemos el marcador que hayamos creado antes al darle clic al mapa
    						//y presentamos el nuevo
    						marcadorNuevoPunto.remove();
        					marcadorNuevoPunto = mMap.addMarker(new MarkerOptions()
        			        .position(new LatLng(point.latitude, point.longitude))
        			        .title("Nuevo Punto!"));
        					marcadorNuevoPunto.showInfoWindow();
    					}
    				}
    			});
        	} else if ((Page_TimeLine.activaUbicate.equals("no")&Page_TimeLine.activaRuta.equals("si"))|
        			(Page_TimeLine.activaUbicate.equals("si")&Page_TimeLine.activaRuta.equals("si"))){        		
        		setUpMap(Double.parseDouble(lat), Double.parseDouble(lon));
        	} 
        }
	}
	
	/*private void alertMap(final LatLng point, final String lat, final String lon){
		AlertDialog.Builder alertbuiBuilder = new AlertDialog.Builder(getApplicationContext());
		alertbuiBuilder.setMessage("���Deseas que este sea el punto de inicio?")
		.setCancelable(false)
		.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				latOrigin = point.latitude;
				lonOrigin = point.longitude;
				setUpMap(Double.parseDouble(lon), Double.parseDouble(lat));
				Toast.makeText(getApplicationContext(), "Clicl largo", Toast.LENGTH_SHORT).show();
				Marker marcadorEvento = mMap.addMarker(new MarkerOptions()
		        .position(new LatLng(point.latitude, point.longitude))
		        .title("Nuevo Punto!"));
			}
		});
		AlertDialog alert = alertbuiBuilder.create();
        alert.show();
	}*/
	
	private void setUpMap(final double latDestination, final double lonDestination) {
		mMap.setMyLocationEnabled(true);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latDestination, lonDestination), 14));
		
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost
							("https://maps.googleapis.com/maps/api/directions/json?origin="+Page_TimeLine.latOrigin+","+Page_TimeLine.lonOrigin+"&destination="+latDestination+","+lonDestination);
					Log.d(null, "https://maps.googleapis.com/maps/api/directions/json?origin="+Page_TimeLine.latOrigin+","+Page_TimeLine.lonOrigin+"&destination="+latDestination+","+lonDestination);
					HttpResponse response = httpclient.execute(httppost);
			        return EntityUtils.toString(response.getEntity());
			        
				} catch (ClientProtocolException e) {
	            	Log.e(null,"Client Protocol Exception");
	                return "Client protocol Error";
	            } catch (IOException e) {
	            	Log.e(null,"IOException");
	                return "I/O Error";
	            }
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				JSONObject jsonObject;
				try{
					jsonObject = new JSONObject(result); //jsonObject contiene: status, routes
					Log.i(null,"tam json = "+jsonObject.length());
					
					Log.d(null,jsonObject.get("status").toString());
					JSONArray jsonArray = (JSONArray) jsonObject.get("routes");
					JSONObject routes = (JSONObject) jsonArray.get(0); 
					//routes contiene: waypoint_order, summary, bounds, legs, warnings, overview_polyline, copyrights
					JSONObject overview_polyline = routes.getJSONObject("overview_polyline");
					String points = overview_polyline.getString("points");
					Log.d("points",points);
					
					
					
					List<LatLng> listaCoordenadasRuta = decode(points);
					PolylineOptions polylineOptions = new PolylineOptions();
					polylineOptions.color(Color.argb(150,0,181,247)).width(25);
					
					Log.d(null,"tam listaCoordenadasRuta = "+listaCoordenadasRuta.size());
					
					for(LatLng latLng : listaCoordenadasRuta){
						//Log.d(null,""+latLng.latitude+","+latLng.longitude);
						polylineOptions.add(latLng);
					}
					mMap.addPolyline(polylineOptions);
					
					Marker marcadorEvento = mMap.addMarker(new MarkerOptions()
			        .position(new LatLng(latDestination, lonDestination))
			        .title("Evento"));
					
					Marker marcadorOrigen = mMap.addMarker(new MarkerOptions()
			        .position(new LatLng(Page_TimeLine.latOrigin, Page_TimeLine.lonOrigin))
			        .title("Origen de tu camino!"));
					
					marcadorEvento.showInfoWindow();

					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}.execute();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        Log.d(null,"pressed home");
	        onBackPressed();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	
	
	
	public static List<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }
}
