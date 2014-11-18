package sferea.todo2day.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseSQLiteManager {
	
	public static final String DB_NAME="FAVORITES";
	public static final String ID ="ID";
	public static final String EVENTO_ID = "EVENTO_ID";
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
	public static final String DISTANCIA = "DISTANCIA";
	public static final String LATITUD = "LATITUD";
	public static final String LONGITUD = "LONGITUD";
	public static final String URL_IMAGEN_EVENTO = "URL_IMAGEN_EVENTO";
	public static final String POSICION = "POSICION";
	public static final String INDEX_OF_EVENT = "INDEX_OF_EVENT";
	public static final String FECHA_UNIX = "FECHA_UNIX";
	
	//CREAR TABLA
//	public static final String CREATE_TABLE = " CREATE TABLE "+DB_NAME+" " +
//			"( "+ID+" integer primary key autoincrement," +
//			" "+TITULO_EVENTO+" text not null," +
//			" "+CATEGORIA+" text not null, " +
//			" "+FECHA+" text not null, " +
//			" "+DESCRIPCION+" text not null, " +
//			" "+FUENTE+" text, " +
//			" "+LUGAR+" text not null, " +
//			" "+DIRECCION+" text," +
//			" "+TELEFONO+" text, " +
//			" "+BOLETO+" text, " +
//			" "+DISTANCIA+" text," +
//			" "+LATITUD+" text not null, " +
//			" "+LONGITUD+" text not null);";
	
//	//CREAR TABLA
	public static final String CREATE_TABLE = " CREATE TABLE "+DB_NAME+" " +
			"( "+ID+" integer primary key autoincrement," +
			" "+EVENTO_ID+" text not null," +
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
			" "+DISTANCIA+" text," +
			" "+LATITUD+" text not null, " +
			" "+LONGITUD+" text not null, " +
			" "+URL_IMAGEN_EVENTO+" text," +
			" "+POSICION+" integer," +
			" "+INDEX_OF_EVENT+" text not null," +
			" "+FECHA_UNIX+" text not null);";
	
	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase db;
	
	public DataBaseSQLiteManager (Context context){
		sqLiteHelper = new SQLiteHelper(context);
		db = sqLiteHelper.getWritableDatabase();
	}
	
//	public ContentValues generarContentValues (String tituloEvento, String categoriEvento, String fechaEvento, 
//			String descripcionEvento, String fuenteEvento, String lugarEvento, String direccionEvento,
//			String telefonoEvento, String boletoEvento, String distanciaEvento, String latitudEvento, 
//			String longitudEvento){
	public ContentValues generarContentValues (String eventId, String tituloEvento, String categoriEvento, String categoriIDEvento, String fechaEvento, 
			String descripcionEvento, String fuenteEvento, String lugarEvento, String direccionEvento,
			String telefonoEvento, String boletoEvento, String distanciaEvento, String latitudEvento, 
			String longitudEvento, String urlImagen, String posicion, String indexOfEvent, String fechaUnix){	
		ContentValues valoresDB = new ContentValues();
		valoresDB.put(EVENTO_ID, eventId);
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
		valoresDB.put(DISTANCIA, distanciaEvento);
		valoresDB.put(LATITUD, latitudEvento);
		valoresDB.put(LONGITUD, longitudEvento);
		valoresDB.put(URL_IMAGEN_EVENTO, urlImagen);
		valoresDB.put(POSICION, posicion);
		valoresDB.put(INDEX_OF_EVENT, indexOfEvent);
		valoresDB.put(FECHA_UNIX, fechaUnix);
		return valoresDB;
		
	}
	
	public void insertar(String eventId, String tituloEvento, String categoriEvento, String categoriIDEvento, String fechaEvento, 
			String descripcionEvento, String fuenteEvento, String lugarEvento, String direccionEvento,
			String telefonoEvento, String boletoEvento, String distanciaEvento, String latitudEvento, 
			String longitudEvento, String urlImagen, String posicion, String indexOfEvent, String fechaUnix){	
		//db.insert(table, nullColumnHack, values)		
		db.insert(DB_NAME, null, generarContentValues(eventId, tituloEvento, categoriEvento, categoriIDEvento, fechaEvento, descripcionEvento, 
				fuenteEvento, lugarEvento, direccionEvento, telefonoEvento, boletoEvento, distanciaEvento, latitudEvento, longitudEvento,
				urlImagen, posicion, indexOfEvent, fechaUnix));		
	}
	
	public void eliminar(String indexOfEvent){
		//db.delete(table, whereClause, whereArgs)
		db.delete(DB_NAME, EVENTO_ID+"=?", new String[]{indexOfEvent});
	}
	
	public void eliminarAllItems(){
		db.execSQL("Delete from favorites;");
	}
	
	public void eliminarTabla(){
		db.execSQL("Drop table favorites;");
	}
	
	public void crearTabla(){
		db.execSQL("CREATE TABLE FAVORITES (ID_EVENTO INTEGER PRIMARY KEY AUTOINCREMENT," +
				" EVENTO_ID TEXT NOT NULL, TITULO_EVENTO TEXT NOT NULL, CATEGORIA TEXT NOT NULL, CATEGORIA_ID TEXT NOT NULL, FECHA TEXT NOT NULL," +
				" DESCRIPCION TEXT NOT NULL, FUENTE TEXT, LUGAR TEXT NOT NULL, DIRECCION TEXT," +
				" TELEFONO TEXT, BOLETO TEXT, DISTANCIA TEXT, LATITUD TEXT NOT NULL, LONGITUD TEXT NOT NULL," +
				" URL_IMAGEN_EVENTO TEXT, POSICION INTEGER, INDEX_OF_EVENT TEXT NOT NULL,FECHA_UNIX TEXT NOT NULL);");
	}
	
	public void eliminarMultiple(String tituloEvento, String tituloEvento2){
		//db.delete(table, whereClause, whereArgs)
		db.delete(DB_NAME, TITULO_EVENTO+" IN (?,?)", new String[]{tituloEvento, tituloEvento2});
	}
	
	public void modificarValoresDB(String eventoId, String tituloEvento, String categoriEvento, String categoriIDEvento, String fechaEvento, 
			String descripcionEvento, String fuenteEvento, String lugarEvento, String direccionEvento,
			String telefonoEvento, String boletoEvento, String distanciaEvento, String latitudEvento, 
			String longitudEvento, String urlImagen, String posicion, String indexOfEvent, String fechaUnix){	
		//db.update(table, values, whereClause, whereArgs)
		db.update(DB_NAME, generarContentValues(eventoId, tituloEvento, categoriEvento, categoriIDEvento, fechaEvento, descripcionEvento,
				fuenteEvento, lugarEvento, direccionEvento, telefonoEvento, boletoEvento, distanciaEvento, 
				latitudEvento, longitudEvento, urlImagen, posicion, indexOfEvent, fechaUnix), INDEX_OF_EVENT+"=?", new String[]{indexOfEvent});
	}
		
	public Cursor cargarTablas(){
		String [] columnas = new String[]{ID, EVENTO_ID,TITULO_EVENTO,CATEGORIA,FECHA,DESCRIPCION,FUENTE,LUGAR,DIRECCION,TELEFONO,BOLETO,DISTANCIA,LATITUD,LONGITUD};
		//db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
//		return db.query(DB_NAME, columnas, null, null, null, null, TITULO_EVENTO);
		return db.rawQuery("SELECT * FROM FAVORITES", null);
	}
	
	public Cursor queryEventByIndex(String index){
		return db.rawQuery("SELECT * FROM FAVORITES WHERE INDEX_OF_EVENT = ?", new String[]{index});
	}
	
	public void cerrarDB(){
		db.close();
	}
}
