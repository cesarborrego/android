package sferea.todo2day.helpers;

import sferea.todo2day.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesHelperFinal {
	
	Context thisContext;
	private static final String SHARED_PREFERENCES_NAME = "YIEPPA_PREFERENCES";

	private String [] clavesCategoria;
	
	private String[] categorias;
	
	public SharedPreferencesHelperFinal (Context c){
		this.thisContext = c;
	}
	
	public void creaArchivoShared(){
		Log.d(null, "Creando Archivo...");
		SharedPreferences prefsCategorias = thisContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);	
		SharedPreferences.Editor editor = prefsCategorias.edit();
		
		getArrayCategorias();
		getArrayClavesCategorias();
		
		for(int i = 0; i < getArrayCategorias().length; i++){
			editor.putString(categorias[i], clavesCategoria[i]);
		}
		
		editor.commit();
	}
	
	private String[] getArrayCategorias() {
		this.categorias = thisContext.getResources().getStringArray(R.array.categorias);
		return categorias;
	}
	
	private String[] getArrayClavesCategorias() {
		this.clavesCategoria = thisContext.getResources().getStringArray(R.array.clavesCategoria);
		return clavesCategoria;
	}
	
	public String obtieneCategoriasPreferences(){
		SharedPreferences prefs = thisContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		int coma =0;
		String claveCategoria = new String();
		
		getArrayCategorias();
		getArrayClavesCategorias();
		
		//SOn 13 categorias
		for (int x = 0; x < getArrayCategorias().length; x++) {
			if (prefs.contains(categorias[x])) {
				if (coma != 0) {
					claveCategoria += "," + prefs.getString(categorias[x], null);
				} else {
					claveCategoria += prefs.getString(categorias[x], null);
				}
				coma++;
			}
		}
		return claveCategoria;
	}
}
