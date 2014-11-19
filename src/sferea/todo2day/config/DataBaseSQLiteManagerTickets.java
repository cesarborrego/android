package sferea.todo2day.config;

import sferea.todo2day.beans.BoletoObjeto;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseSQLiteManagerTickets {
	
	public static final String DB_NAME="TICKETS";
	//Seccion de boletos
	public static final String ID = "ID";
	public static final String EVENT_ID = "EVENT_ID";
	public static final String TIPO_BOLETO = "TIPO_BOLETO";
	public static final String PRECIO_BOLETO = "PRECIO_BOLETO";
	public static final String CANTIDAD_BOLETO = "CANTIDAD_BOLETO";
	
	//CREAR TABLA
	public static final String CREATE_TABLE = " CREATE TABLE "+DB_NAME+" " +
			"( " +ID+" integer PRIMARY KEY autoincrement, " +
			" "+EVENT_ID+" text not null," +
			" "+TIPO_BOLETO+" TEXT NOT NULL," +
			" "+PRECIO_BOLETO+" TEXT NOT NULL," +
			" "+CANTIDAD_BOLETO+" TEXT NOT NULL)";
	
	private SQLiteHelperTickets sqLiteHelperTickets;
	private SQLiteDatabase db;
	
	public DataBaseSQLiteManagerTickets (Context context){
		sqLiteHelperTickets = new SQLiteHelperTickets(context);
		db = sqLiteHelperTickets.getWritableDatabase();
	}

	public ContentValues generarContentValues (			
			String indexOfEvent, 
			String tipoBoleto,
			String precioBoleto,
			String cantidadBoleto){	
		ContentValues valoresDB = new ContentValues();		
		valoresDB.put(EVENT_ID, indexOfEvent);
		valoresDB.put(TIPO_BOLETO, tipoBoleto);
		valoresDB.put(PRECIO_BOLETO, precioBoleto);
		valoresDB.put(CANTIDAD_BOLETO, cantidadBoleto);
		return valoresDB;
	}
	
	
	public void insertar(String indexOfEvent, BoletoObjeto boletoObjeto){	
				db.insert(DB_NAME, 
				null, 
				generarContentValues(indexOfEvent,
						boletoObjeto.getTipo(),
						String.valueOf(boletoObjeto.getPrecio()),
						String.valueOf(boletoObjeto.getCantidad())));		
	}
	
	public void actualizar(String indexOfEvent, BoletoObjeto boletoObjeto){	
		db.update(DB_NAME, 
				generarContentValues(
						indexOfEvent,
						boletoObjeto.getTipo(),
						String.valueOf(boletoObjeto.getPrecio()),
						String.valueOf(boletoObjeto.getCantidad())),
						EVENT_ID+"=?", 
				new String[]{indexOfEvent});
	}
	
	public void eliminar(String indexOfEvent){
		db.delete(DB_NAME, EVENT_ID+"=?", new String[]{indexOfEvent});
	}
		
	public Cursor cargarTablas(){
		return db.rawQuery("SELECT * FROM TICKETS", null);
	}

	public Cursor queryTicketByEventIndex(String indexOfEvent){
		return db.rawQuery("SELECT * FROM TICKETS WHERE EVENT_ID = ?", new String[]{indexOfEvent});
	}
	
	public void cerrarDB(){
		db.close();
	}
}
