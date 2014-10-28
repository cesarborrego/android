package sferea.todo2day.Helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckInternetConnection {
	Context thisContext;
	Activity thisActivity;
	public CheckInternetConnection(Context c, Activity a){
		this.thisContext = c;
		this.thisActivity = a;
	}
	
	public boolean isConnectedToInternetSplash() {
		ConnectivityManager connectivity = (ConnectivityManager) thisContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		Toast.makeText(thisContext,
				"Verificar la conexión a internet", Toast.LENGTH_SHORT).show();
		thisActivity.finish();
		return false;
	}
	
	public boolean isConnectedToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) thisContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
//		Toast.makeText(thisContext,
//				"Verificar la conexión a internet", Toast.LENGTH_SHORT).show();
		return false;
	}

}
