package sferea.todo2day.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternetConnection {
	Context thisContext;

	public CheckInternetConnection(Context c){
		this.thisContext = c;
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
		return false;
	}

}
