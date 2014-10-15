package sferea.todo2day.config;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationHelper implements LocationListener {
	
	private final Context context;
	Location location;
	boolean gpsActivo;
	LocationManager locationManager;
	double latitud;
	double longitud;
	
	public LocationHelper( Context c){
		super();
		this.context = c;
		getLocation();
	}
	
	public void setLatLong(double latitud, double longitud){

		
		this.longitud = longitud;
	}

	public void getLocation(){
		Log.d("GPS", "Entra a funcion GPS");
		try {
			locationManager = (LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);
			gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception e) {
			if (gpsActivo){
				Log.d("GPS", "Esta Activado");
				locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,
						1000*60,
						5,
						this);
				location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
				latitud = location.getLatitude();
				longitud = location.getLongitude();
				
				Log.d(null, "Latitud"+latitud);
				Log.d(null, "Longitud"+longitud);
			}
		}
	}
	
	@Override
	public void onLocationChanged(Location location) {
		latitud = location.getLatitude();
		longitud = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
	

}
