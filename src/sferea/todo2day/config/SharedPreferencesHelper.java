package sferea.todo2day.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase que controla todo lo relacionado al objeto SharedPreferences
 * @author maw
 *
 */
public class SharedPreferencesHelper {
	
	SharedPreferences settings;
	Constants_Settings constants;
	
	/**
	 * Constructor que crea el objeto SharedPreferences
	 * @param sharedPrefName
	 * @param context
	 */
	public SharedPreferencesHelper(String sharedPrefName, Context context){
		//Referencia al shared preferences
		settings = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
	}
	
	/**
	 * True/False dependiendo si existe el valor
	 * @param val
	 * @return
	 */
	public boolean isExist(String val){
		String value = settings.getString(val, null);
		if(value==null){return false;}else{ return true; }
	}
	
	/**
	 * Devuelve el valor correspondiente a la llave del parametro
	 * @param val
	 * @return
	 */
	public String Get_stringfrom_shprf(String val){
		String valor = settings.getString(val, null);
		return valor;
	}
	
	/**
	 * Escribe una cadena "valor" con la clave "clave" en SharedPreferences
	 * @param clave
	 * @param valor
	 */
	public void Write_String(String clave, String valor){
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(clave, valor);
		editor.commit();
	}
	
	/**
	 * Borra el valor contenido en la clave "clave" en SharedPreferences
	 * @param clave
	 */
	public void Remove_Value(String clave){
		SharedPreferences.Editor editor=settings.edit();
		
		editor.remove(clave);
		editor.commit();
	}

}
