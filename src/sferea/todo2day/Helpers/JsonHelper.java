package sferea.todo2day.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

public class JsonHelper {
	
	Context thisCOntext;
	
	public JsonHelper(Context c){
		this.thisCOntext = c;
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
