package sferea.todo2day.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesHelperFinal {
	
	Context thisContext;
	private static final String SHARED_PREFERENCES_NAME = "YIEPPA_PREFERENCES";
	
	public static boolean [][] activaCategorias ={
		{true,true,true},
		{true,true,true},
		{true,true,true},
		{true,true,true},
		{true}};

	public static String [] categoriasNoDeseadas = {
		"",
		"",
		"",
		"",
		"",
		"",
		"",
		"",
		"",
		"",
		"",
		"",
		""
	};
	
	public static String categorias;
	
	public SharedPreferencesHelperFinal (Context c){
		this.thisContext = c;
	}
	
	public void creaArchivoShared(){
		Log.d(null, "Creando Archivo...");
		SharedPreferences prefsCategorias = thisContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);	
		SharedPreferences.Editor editor = prefsCategorias.edit();

		for(int i=0; i<categoriasNoDeseadas.length; i++){
			editor.putString("Categories "+i, "Desactivada");
		}		   

		for(int i=0; i<activaCategorias.length; i++){
			for(int j =0; j<activaCategorias[i].length; j++){
				editor.putString("Activa_Categoria "+i+""+j, "true");
			}
		}
		
		editor.commit();
		categorias ="";
	}
	
	
	public String obtieneCategoriasPreferences(){
		SharedPreferences prefs = thisContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		int coma =0;
		categorias ="";
		//SOn 13 categorias
		for (int x = 0; x < 13; x++) {
			if (!prefs.getString("Categories " + x, "Desactivada")
					.equals("Desactivada")
					& !prefs.getString("Categories " + x,
							"Desactivada").equals("")) {
				if (coma != 0) {
					categorias += ","
							+ prefs
							.getString("Categories " + x, null);
				} else {
					categorias += prefs.getString("Categories " + x,
							null);
				}
				coma++;
			}
		}
		return categorias;
	}
}
