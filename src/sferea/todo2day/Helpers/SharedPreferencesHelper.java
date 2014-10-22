package sferea.todo2day.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesHelper {
	
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
	
	public SharedPreferencesHelper (Context c){
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
}
