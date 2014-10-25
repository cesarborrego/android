package sferea.todo2day.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
	
	public void connectionMongo_Json(String param){
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
				readStreamPrimerJson(inputStream);
			}								
		}
	}
	
	public void connectionMongo_JsonFake(String param){
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
//				readStreamPrimerJson(inputStream);
			}								
		}
	}
	@SuppressWarnings("unused")
	public String leerJsonCache() {
    	BufferedReader bufferedReader=null;
    	String jsonCache = null;
    	try {
    		BufferedReader fin =
    		        new BufferedReader(
    		            new InputStreamReader(
    		                thisCOntext.openFileInput("JsonCache.txt")));
    		
    		while ((jsonCache = fin.readLine()) != null) {
    	    	fin.close();
    		}
    		
		} catch (Exception e) {}
    	if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return jsonCache;
    }
	
	@SuppressWarnings("unused")
	public String leerPrimerJson() {
    	BufferedReader bufferedReader=null;
    	String jsonCache = null;
    	try {
    		BufferedReader fin =
    		        new BufferedReader(
    		            new InputStreamReader(
    		                thisCOntext.openFileInput("Json.txt")));
    		
    		while ((jsonCache = fin.readLine()) != null) {
    	    	fin.close();
    		}
    		
		} catch (Exception e) {}
    	if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return jsonCache;
    }
	
	public void readStreamPrimerJson(InputStream in) {
		BufferedReader reader = null;
		OutputStreamWriter outputStreamWriter = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				outputStreamWriter = new OutputStreamWriter(thisCOntext.openFileOutput("Json.txt", Context.MODE_PRIVATE));
				outputStreamWriter.write(line);	    					
				outputStreamWriter.flush();
				outputStreamWriter.close();				
			}
			Log.d(null, "Primer Json creado!");	

			
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
}
