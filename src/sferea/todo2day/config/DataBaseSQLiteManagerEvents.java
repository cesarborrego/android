package sferea.todo2day.config;

import sferea.todo2day.beans.EventoObjeto;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseSQLiteManagerEvents {
	
	public static final String DB_NAME="EVENTS";
	public static final String ID ="ID";
	public static final String TITULO_EVENTO = "TITULO_EVENTO";
	public static final String CATEGORIA = "CATEGORIA";
	public static final String CATEGORIA_ID = "CATEGORIA_ID";
	public static final String FECHA = "FECHA";
	public static final String DESCRIPCION = "DESCRIPCION";
	public static final String FUENTE = "FUENTE";
	public static final String LUGAR = "LUGAR";
	public static final String DIRECCION = "DIRECCION";
	public static final String TELEFONO = "TELEFONO";
	public static final String BOLETO_ID = "BOLETO_ID";
	public static final String DISTANCIA = "DISTANCIA";
	public static final String LATITUD = "LATITUD";
	public static final String LONGITUD = "LONGITUD";
	public static final String URL_IMAGEN_EVENTO = "URL_IMAGEN_EVENTO";
	public static final String POSICION = "POSICION";
	public static final String INDEX_OF_EVENT = "INDEX_OF_EVENT";
	public static final String EVENTO_ID = "EVENTO_ID";
	public static final String FECHA_UNIX = "FECHA_UNIX";
	public static final String IS_NEW = "IS_NEW";
	
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
			" "+BOLETO_ID+" text not null, " +
			" "+DISTANCIA+" text," +
			" "+LATITUD+" text not null, " +
			" "+LONGITUD+" text not null, " +
			" "+URL_IMAGEN_EVENTO+" text," +
			" "+POSICION+" integer," +
			" "+EVENTO_ID+" text not null," +
			" "+INDEX_OF_EVENT+" text not null," +
			" "+IS_NEW+" text not null," +
			" "+FECHA_UNIX+" text not null);";
	
	private SQLiteHelperEvents sqLiteHelperEvents;
	private SQLiteDatabase db;
	
	public DataBaseSQLiteManagerEvents (Context context){
		sqLiteHelperEvents = new SQLiteHelperEvents(context);
		db = sqLiteHelperEvents.getWritableDatabase();
	}

	public ContentValues generarContentValues (String idEvento, String tituloEvento, String categoriEvento, String categoriIDEvento, String fechaEvento, 
			String descripcionEvento, String fuenteEvento, String lugarEvento, String direccionEvento,
			String telefonoEvento, String distanciaEvento, String latitudEvento, 
			String longitudEvento, String urlImagen, String posicion, String indexOfEvent, String fechaUnix, String isNew, String boletoId){	
		ContentValues valoresDB = new ContentValues();
		valoresDB.put(EVENTO_ID, idEvento);
		valoresDB.put(TITULO_EVENTO, tituloEvento);
		valoresDB.put(CATEGORIA, categoriEvento);
		valoresDB.put(CATEGORIA_ID, categoriIDEvento);
		valoresDB.put(FECHA, fechaEvento);
		valoresDB.put(DESCRIPCION, descripcionEvento);
		valoresDB.put(FUENTE, fuenteEvento);
		valoresDB.put(LUGAR, lugarEvento);
		valoresDB.put(DIRECCION, direccionEvento);
		valoresDB.put(TELEFONO, telefonoEvento);
		valoresDB.put(DISTANCIA, distanciaEvento);
		valoresDB.put(LATITUD, latitudEvento);
		valoresDB.put(LONGITUD, longitudEvento);
		valoresDB.put(URL_IMAGEN_EVENTO, urlImagen);
		valoresDB.put(POSICION, posicion);
		valoresDB.put(INDEX_OF_EVENT, indexOfEvent);
		valoresDB.put(FECHA_UNIX, fechaUnix);
		valoresDB.put(IS_NEW, isNew);
		valoresDB.put(BOLETO_ID, boletoId);
		return valoresDB;
		
	}
	
	
	public void insertar(EventoObjeto evento){	
				db.insert(DB_NAME, 
				null, 
				generarContentValues(
						evento.getIdOfEvent(),
						evento.getNombreEvento(), 
						evento.getCategoriaEvento(), 
						evento.getCategoriaIDEvento(), 
						evento.getFechaEvento(), 
						evento.getDescripcion(), 
						evento.getFuente(), 
						evento.getLugarEvento(), 
						evento.getDireccion(), 
						evento.getTelefono(), 
						evento.getDistancia(), 
						String.valueOf(evento.getLatEvento()), 
						String.valueOf(evento.getLonEvento()),
						evento.getUrlImagen(), 
						String.valueOf(evento.getPosicion()), 
						String.valueOf(evento.getIndexEvento()), 
						String.valueOf(evento.getFechaUnix()),
						String.valueOf(evento.getIsNewEvent()),
						String.valueOf(evento.getTipoBoleto().getId())));		
	}
	
	public void eliminar(String indexOfEvent){
		db.delete(DB_NAME, EVENTO_ID+"=?", new String[]{indexOfEvent});
	}
	
	public void eliminarAllItems(){
		Log.d("DB EVENTS", "Delete from events;");
		db.execSQL("DELETE FROM EVENTS;");
	}
	
	public void eliminarTabla(){
		db.execSQL("DROP TABLE EVENTS;");
	}
	
	public void crearTabla(){
		db.execSQL("CREATE TABLE EVENTS (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
				" EVENTO_ID TEXT NOT NULL, TITULO_EVENTO TEXT NOT NULL, CATEGORIA TEXT NOT NULL, CATEGORIA_ID TEXT NOT NULL, FECHA TEXT NOT NULL," +
				" DESCRIPCION TEXT NOT NULL, FUENTE TEXT, LUGAR TEXT NOT NULL, DIRECCION TEXT," +
				" TELEFONO TEXT, BOLETO TEXT, PRECIO TEXT, DISTANCIA TEXT, LATITUD TEXT NOT NULL, LONGITUD TEXT NOT NULL," +
				" URL_IMAGEN_EVENTO TEXT, POSICION INTEGER, INDEX_OF_EVENT TEXT NOT NULL, FECHA_UNIX TEXT NOT NULL, IS_NEW TEXT NOT NULL);");
	}
	
	public void eliminarMultiple(String tituloEvento, String tituloEvento2){
		db.delete(DB_NAME, TITULO_EVENTO+" IN (?,?)", new String[]{tituloEvento, tituloEvento2});
	}
	
	public void actualizar(EventoObjeto evento){	
		db.update(DB_NAME, 
				generarContentValues(
						evento.getIdOfEvent(),
						evento.getNombreEvento(), 
						evento.getCategoriaEvento(),
						evento.getCategoriaIDEvento(), 
						evento.getFechaEvento(), 
						evento.getDescripcion(),
						evento.getFuente(), 
						evento.getLugarEvento(), 
						evento.getDireccion(), 
						evento.getTelefono(), 
						evento.getDistancia(), 
						String.valueOf(evento.getLatEvento()), 
						String.valueOf(evento.getLonEvento()), 
						evento.getUrlImagen(),
						String.valueOf(evento.getPosicion()), 
						String.valueOf(evento.getIndexEvento()), 
						String.valueOf(evento.getFechaUnix()),
						String.valueOf(evento.getIsNewEvent()),
						String.valueOf(evento.getTipoBoleto().getId())),	
						EVENTO_ID+"=?", 
				new String[]{evento.getIdOfEvent()});
	}

		
	public Cursor cargarTablas(){
		return db.rawQuery("SELECT * FROM EVENTS", null);
	}

	public Cursor queryEventByIndex(String eventIndex){
		return db.rawQuery("SELECT * FROM EVENTS WHERE EVENTO_ID = ?", new String[]{eventIndex});
	}
	
	public void cerrarDB(){
		db.close();
	}
}
