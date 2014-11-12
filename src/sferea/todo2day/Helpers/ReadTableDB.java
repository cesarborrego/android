package sferea.todo2day.Helpers;

import sferea.todo2day.R;
import sferea.todo2day.adapters.EventoObjeto;
import sferea.todo2day.config.DataBaseSQLiteManager;
import sferea.todo2day.config.DataBaseSQLiteManagerEvents;
import sferea.todo2day.subfragments.Page_TimeLine;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ReadTableDB {
	Context thisContext;
	DataBaseSQLiteManagerEvents dataBaseSQLiteManagerEvents;
	DataBaseSQLiteManager dataBaseSQLiteManagerFavorites;

	public ReadTableDB(Context c) {
		this.thisContext = c;
		dataBaseSQLiteManagerEvents = new DataBaseSQLiteManagerEvents(
				thisContext);
		dataBaseSQLiteManagerFavorites = new DataBaseSQLiteManager(
				thisContext);
	}

	public boolean isIndexOfEventsExist(String indexOfEvent) {
		boolean respuesta = false;


		Cursor cursor = dataBaseSQLiteManagerEvents
				.queryEventByIndex(indexOfEvent);

		if (cursor.getCount() > 0) {
			respuesta = true;
		} else {
			respuesta = false;
		}	
		return respuesta;
	}

	public void readTable_FillList() {
		Cursor cursor = dataBaseSQLiteManagerEvents.cargarTablas();
		String descripcion;
		try {
			if (cursor.moveToFirst()) {
				Page_TimeLine.listaEventos.clear();
				do {
					if(cursor.getString(cursor.getColumnIndex("DESCRIPCION"))==null){
						descripcion = "No disponible";
					}else{
						descripcion = cursor.getString(cursor.getColumnIndex("DESCRIPCION"));
					}
					Page_TimeLine.listaEventos
							.add(new EventoObjeto(
									cursor.getString(cursor
											.getColumnIndex("TITULO_EVENTO")),
									cursor.getString(cursor
											.getColumnIndex("CATEGORIA")),
									cursor.getString(cursor
													.getColumnIndex("CATEGORIA_ID")),
									cursor.getString(cursor
											.getColumnIndex("FECHA")),
									descripcion,
									cursor.getString(cursor
											.getColumnIndex("FUENTE")),
									cursor.getString(cursor
											.getColumnIndex("LUGAR")),
									cursor.getString(cursor
											.getColumnIndex("DIRECCION")),
									cursor.getString(cursor
											.getColumnIndex("TELEFONO")),
									Double.parseDouble(cursor.getString(cursor
											.getColumnIndex("LATITUD"))),
									Double.parseDouble(cursor.getString(cursor
											.getColumnIndex("LONGITUD"))),
									cursor.getString(cursor
											.getColumnIndex("DISTANCIA")),
									cursor.getString(cursor
											.getColumnIndex("BOLETO")),
									cursor.getString(cursor
											.getColumnIndex("PRECIO")),
									Integer.parseInt(cursor.getString(cursor
											.getColumnIndex("POSICION"))),
									cursor.getString(cursor
											.getColumnIndex("INDEX_OF_EVENT")),
									Integer.parseInt(cursor.getString(cursor
											.getColumnIndex("FECHA_UNIX"))),
									cursor.getString(cursor
											.getColumnIndex("URL_IMAGEN_EVENTO")),
									R.drawable.ic_small_antros));				
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			Log.d("Lista Eventos", "Lista cargada desde DB!");
			dataBaseSQLiteManagerEvents.cerrarDB();
		}
	}
	
	public void readFavoritesTable() {
		Cursor cursor = dataBaseSQLiteManagerFavorites.cargarTablas();
		try {
			if (cursor.moveToFirst()) {
				do{
					Page_TimeLine.favorites.add(cursor.getString(cursor.getColumnIndex("INDEX_OF_EVENT")));
				}while(cursor.moveToNext());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			cursor.close();
			dataBaseSQLiteManagerFavorites.cerrarDB();
		}
	}
	
	public int readTable() {
		Cursor cursor = dataBaseSQLiteManagerEvents.cargarTablas();
		return cursor.getCount();
	}
}
