package sferea.todo2day.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesHelperFinal {
	
	Context thisContext;
	
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
		SharedPreferences prefsCategorias = thisContext.getSharedPreferences("Categorias",Context.MODE_PRIVATE);	
		SharedPreferences.Editor editorCategoriasString = prefsCategorias.edit();

		for(int i=0; i<categoriasNoDeseadas.length; i++){
			editorCategoriasString.putString("Categories "+i, "Desactivada");
		}		   
		editorCategoriasString.commit();

		SharedPreferences prefsCategoriasBoolean = thisContext.getSharedPreferences("CategoriasBoolean",Context.MODE_PRIVATE);	
		SharedPreferences.Editor editorCategoriasBoolean = prefsCategoriasBoolean.edit();

		for(int i=0; i<activaCategorias.length; i++){
			for(int j =0; j<activaCategorias[i].length; j++){
				editorCategoriasBoolean.putString("Activa_Categoria "+i+""+j, "true");
			}
		}		   
		editorCategoriasBoolean.commit();
		categorias ="";
	}
	
	public String obtieneCategoriasPreferences(){
		SharedPreferences prefsCategorias = thisContext.getSharedPreferences("Categorias",Context.MODE_PRIVATE);
		int coma =0;
		categorias ="";
		//SOn 13 categorias
		for (int x = 0; x < 13; x++) {
			if (!prefsCategorias.getString("Categories " + x, "Desactivada")
					.equals("Desactivada")
					& !prefsCategorias.getString("Categories " + x,
							"Desactivada").equals("")) {
				if (coma != 0) {
					categorias += ","
							+ prefsCategorias
							.getString("Categories " + x, null);
				} else {
					categorias += prefsCategorias.getString("Categories " + x,
							null);
				}
				coma++;
			}
		}
		return categorias;
	}
}
