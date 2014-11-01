package sferea.todo2day.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseSQLiteManagerEvents {
	
	public static final String DB_NAME="EVENTS";
	public static final String ID ="ID_EVENTO";
	//DATOS
	public static final String TITULO_EVENTO = "TITULO_EVENTO";
	public static final String CATEGORIA = "CATEGORIA";
	public static final String CATEGORIA_ID = "CATEGORIA_ID";
	public static final String FECHA = "FECHA";
	public static final String DESCRIPCION = "DESCRIPCION";
	public static final String FUENTE = "FUENTE";
	public static final String LUGAR = "LUGAR";
	public static final String DIRECCION = "DIRECCION";
	public static final String TELEFONO = "TELEFONO";
	public static final String BOLETO = "BOLETO";
	public static final String PRECIO = "PRECIO";
	public static final String DISTANCIA = "DISTANCIA";
	public static final String LATITUD = "LATITUD";
	public static final String LONGITUD = "LONGITUD";
	public static final String URL_IMAGEN_EVENTO = "URL_IMAGEN_EVENTO";
	public static final String POSICION = "POSICION";
	public static final String INDEX_OF_EVENT = "INDEX_OF_EVENT";
	public static final String FECHA_UNIX = "FECHA_UNIX";
	
//	//CREAR TABLA
	public static final String CREATE_TABLE = " CREATE TABLE "+DB_NAME+" " +
			"( "+ID+" integer primary key autoincrement," +
			" "+TITULO_EVENTO+" text not null," +
			" "+CATEGORIA+" text not null, " +
			" "+CATEGORIA_ID+" text not null, " +
			" "+FECHA+" text not null, " +
			" "+DESCRIPCION+" text not null, " +
			" "+FUENTE+" text, " +
			" "+LUGAR+" text not null, " +
			" "+DIRECCION+" text," +
			" "+TELEFONO+" text, " +
			" "+BOLETO+" text, " +
			" "+PRECIO+" text, " +
			" "+DISTANCIA+" text," +
			" "+LATITUD+" text not null, " +
			" "+LONGITUD+" text not null, " +
			" "+URL_IMAGEN_EVENTO+" text," +
			" "+POSICION+" integer," +
			" "+INDEX_OF_EVENT+" text not null," +
			" "+FECHA_UNIX+" text not null);";
	
	private SQLiteHelperEvents sqLiteHelperEvents;
	private SQLiteDatabase db;
	
	public DataBaseSQLiteManagerEvents (Context context){
		sqLiteHelperEvents = new SQLiteHelperEvents(context);
		db = sqLiteHelperEvents.getWritableDatabase();
	}
	
//	public ContentValues generarContentValues (String tituloEvento, String categoriEvento, String fechaEvento, 
//			String descripcionEvento, String fuenteEvento, String lugarEvento, String direccionEvento,
//			String telefonoEvento, String boletoEvento, String distanciaEvento, String latitudEvento, 
//			String longitudEvento){
	public ContentValues generarContentValues (String tituloEvento, String categoriEvento, String categoriIDEvento, String fechaEvento, 
			String descripcionEvento, String fuenteEvento, String lugarEvento, String direccionEvento,
			String telefonoEvento, String boletoEvento, String precioEvento, String distanciaEvento, String latitudEvento, 
			String longitudEvento, String urlImagen, String posicion, String indexOfEvent, String fechaUnix){	
		ContentValues valoresDB = new ContentValues();
		valoresDB.put(TITULO_EVENTO, tituloEvento);
		valoresDB.put(CATEGORIA, categoriEvento);
		valoresDB.put(CATEGORIA_ID, categoriIDEvento);
		valoresDB.put(FECHA, fechaEvento);
		valoresDB.put(DESCRIPCION, descripcionEvento);
		valoresDB.put(FUENTE, fuenteEvento);
		valoresDB.put(LUGAR, lugarEvento);
		valoresDB.put(DIRECCION, direccionEvento);
		valoresDB.put(TELEFONO, telefonoEvento);
		valoresDB.put(BOLETO, boletoEvento);
		valoresDB.put(PRECIO, precioEvento);
		valoresDB.put(DISTANCIA, distanciaEvento);
		valoresDB.put(LATITUD, latitudEvento);
		valoresDB.put(LONGITUD, longitudEvento);
		valoresDB.put(URL_IMAGEN_EVENTO, urlImagen);
		valoresDB.put(POSICION, posicion);
		valoresDB.put(INDEX_OF_EVENT, indexOfEvent);
		valoresDB.put(FECHA_UNIX, fechaUnix);
		return valoresDB;
		
	}
	
	
	public void insertar(String tituloEvento, String categoriEvento, String categoriIDEvento, String fechaEvento, 
			String descripcionEvento, String fuenteEvento, String lugarEvento, String direccionEvento,
			String telefonoEvento, String boletoEvento, String precioEvento, String distanciaEvento, String latitudEvento, 
			String longitudEvento, String urlImagen, String posicion, String indexOfEvent, String fechaUnix){	
		//db.insert(table, nullColumnHack, values)		
		db.insert(DB_NAME, null, generarContentValues(tituloEvento, categoriEvento, categoriIDEvento, fechaEvento, descripcionEvento, 
				fuenteEvento, lugarEvento, direccionEvento, telefonoEvento, boletoEvento, precioEvento, distanciaEvento, latitudEvento, longitudEvento,
				urlImagen, posicion, indexOfEvent, fechaUnix));		
	}
	
	public void eliminar(String indexOfEvent){
		//db.delete(table, whereClause, whereArgs)
		db.delete(DB_NAME, INDEX_OF_EVENT+"=?", new String[]{indexOfEvent});
	}
	
	public void eliminarAllItems(){
		Log.d("DB EVENTS", "Delete from events;");
		db.execSQL("Delete from events;");
	}
	
	public void eliminarTabla(){
		db.execSQL("Drop table events;");
	}
	
	public void crearTabla(){
		db.execSQL("CREATE TABLE EVENTS (ID_EVENTO INTEGER PRIMARY KEY AUTOINCREMENT," +
				" TITULO_EVENTO TEXT NOT NULL, CATEGORIA TEXT NOT NULL, CATEGORIA_ID TEXT NOT NULL, FECHA TEXT NOT NULL," +
				" DESCRIPCION TEXT NOT NULL, FUENTE TEXT, LUGAR TEXT NOT NULL, DIRECCION TEXT," +
				" TELEFONO TEXT, BOLETO TEXT, PRECIO TEXT, DISTANCIA TEXT, LATITUD TEXT NOT NULL, LONGITUD TEXT NOT NULL," +
				" URL_IMAGEN_EVENTO TEXT, POSICION INTEGER, INDEX_OF_EVENT TEXT NOT NULL, FECHA_UNIX TEXT NOT NULL);");
	}
	
	public void eliminarMultiple(String tituloEvento, String tituloEvento2){
		//db.delete(table, whereClause, whereArgs)
		db.delete(DB_NAME, TITULO_EVENTO+" IN (?,?)", new String[]{tituloEvento, tituloEvento2});
	}
	
	public void actualizar(String tituloEvento, String categoriEvento, String categoriIDEvento, String fechaEvento, 
			String descripcionEvento, String fuenteEvento, String lugarEvento, String direccionEvento,
			String telefonoEvento, String boletoEvento, String precioEvento, String distanciaEvento, String latitudEvento, 
			String longitudEvento, String urlImagen, String posicion, String indexOfEvent, String fechaUnix){	
		//db.update(table, values, whereClause, whereArgs)
		db.update(DB_NAME, generarContentValues(tituloEvento, categoriEvento, categoriIDEvento, fechaEvento, descripcionEvento,
				fuenteEvento, lugarEvento, direccionEvento, telefonoEvento, boletoEvento, precioEvento, distanciaEvento, 
				latitudEvento, longitudEvento, urlImagen, posicion, indexOfEvent, fechaUnix), INDEX_OF_EVENT+"=?", new String[]{indexOfEvent});
	}

		
	public Cursor cargarTablas(){
		String [] columnas = new String[]{ID,TITULO_EVENTO,CATEGORIA,FECHA,DESCRIPCION,FUENTE,
				LUGAR,DIRECCION,TELEFONO,BOLETO,PRECIO,DISTANCIA,LATITUD,LONGITUD};
		//db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
//		return db.query(DB_NAME, columnas, null, null, null, null, TITULO_EVENTO);
		return db.rawQuery("SELECT * FROM EVENTS", null);
	}

	public Cursor queryEventByIndex(String eventIndex){
		return db.rawQuery("SELECT * FROM EVENTS WHERE INDEX_OF_EVENT = ?", new String[]{eventIndex});
	}
	
	public void cerrarDB(){
		db.close();
	}
}
