package sferea.todo2day.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sferea.todo2day.Application;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class JsonHelper {
	
	Context thisCOntext;
	Activity thisActivity;
	
	public JsonHelper(Context c, Activity a){
		this.thisCOntext = c;
		this.thisActivity = a;
	}	
	
	public boolean connectionMongo_Json(String param){
		boolean result = false;
		if(thisActivity!=null){
			InputStream inputStream = null;

			URL url = null;
			HttpURLConnection con = null;

			try {
				url = new URL(param);
				con = (HttpURLConnection) url.openConnection();
				inputStream = con.getInputStream();
				Log.e(null,param);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(inputStream!=null){
				result = readStreamPrimerJson(inputStream);
			}								
		}
		
		return result;
	}
	
	public boolean readStreamPrimerJson(InputStream in) {
		boolean result = false;
		BufferedReader reader = null;
		ParseJson_AddDB parser = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				
				parser = new ParseJson_AddDB(Application.getInstance(), thisActivity);
				result = parser.parseFirstJson_AddDB(line);
			}

			
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
		
		return result;
	}
}
