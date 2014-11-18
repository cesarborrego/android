package sferea.todo2day.helpers;

import java.util.ArrayList;

import sferea.todo2day.R;
import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.config.DataBaseSQLiteManager;
import sferea.todo2day.config.DataBaseSQLiteManagerEvents;
import sferea.todo2day.fragments.Page_TimeLine;
import sferea.todo2day.utils.ListUtil;
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

	public ArrayList<EventoObjeto> fillEventListFromDB() {
		ArrayList<EventoObjeto> lista = null;
		Cursor cursor = dataBaseSQLiteManagerEvents.cargarTablas();

		try {
			if (cursor.moveToFirst()) {
				
				lista = new ArrayList<EventoObjeto>();

				do {
					
					lista.add(getEventoObjetoFromDB(cursor));
					
				} while (cursor.moveToNext());
				
				ListUtil.sortListByDate(lista);
			}
		} finally {
			cursor.close();
			Log.d("Lista Eventos", "Lista cargada desde DB!");
			dataBaseSQLiteManagerEvents.cerrarDB();
		}
		
		return lista;
	}
	
	public void AddEventsToAdapterFromDB() {
		Cursor cursor = dataBaseSQLiteManagerEvents.cargarTablas();
		EventoObjeto evento;

		try {
			if (cursor.moveToFirst()) {

				do {
					
					evento = getEventoObjetoFromDB(cursor);
					if(Page_TimeLine.arrayAdapterEvents.getPosition(evento) != -1)
						Page_TimeLine.arrayAdapterEvents.add(evento);
					
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			Log.d("Lista Eventos", "Lista cargada desde DB!");
			dataBaseSQLiteManagerEvents.cerrarDB();
		}
	}
	
	
	private EventoObjeto getEventoObjetoFromDB(Cursor cursor){
		EventoObjeto evento = new EventoObjeto();
		evento.setIdOfEvent(cursor.getString(cursor.getColumnIndex("EVENTO_ID")));
		evento.setDescripcion(cursor.getString(cursor.getColumnIndex("DESCRIPCION")));
		evento.setNombreEvento(cursor.getString(cursor.getColumnIndex("TITULO_EVENTO")));
		evento.setCategoriaEvento(cursor.getString(cursor.getColumnIndex("CATEGORIA")));
		evento.setCategoriaIDEvento(cursor.getString(cursor.getColumnIndex("CATEGORIA_ID")));
		evento.setFechaEvento(cursor.getString(cursor.getColumnIndex("FECHA")));
		evento.setFuente(cursor.getString(cursor.getColumnIndex("FUENTE")));
		evento.setLugarEvento(cursor.getString(cursor.getColumnIndex("LUGAR")));
		evento.setDireccion(cursor.getString(cursor.getColumnIndex("DIRECCION")));
		evento.setTelefono(cursor.getString(cursor.getColumnIndex("TELEFONO")));
		evento.setLatEvento(Double.parseDouble(cursor.getString(cursor.getColumnIndex("LATITUD"))));
		evento.setLonEvento(Double.parseDouble(cursor.getString(cursor.getColumnIndex("LONGITUD"))));
		evento.setDistancia(cursor.getString(cursor.getColumnIndex("DISTANCIA")));
		evento.setBoleto(cursor.getString(cursor.getColumnIndex("BOLETO")));
		evento.setPrecio(cursor.getString(cursor.getColumnIndex("PRECIO")));
		evento.setPosicion(Integer.parseInt(cursor.getString(cursor.getColumnIndex("POSICION"))));
		evento.setIndexEvento(Integer.parseInt(cursor.getString(cursor.getColumnIndex("INDEX_OF_EVENT"))));
		evento.setFechaUnix(Integer.parseInt(cursor.getString(cursor.getColumnIndex("FECHA_UNIX"))));
		evento.setUrlImagen(cursor.getString(cursor.getColumnIndex("URL_IMAGEN_EVENTO")));
		evento.setIsNewEvent(Integer.parseInt(cursor.getString(cursor.getColumnIndex("IS_NEW"))));
		evento.setImagenCategoria(R.drawable.ic_small_antros);
		
		return evento;
	}
	
	public ArrayList<String> fillFavoriteListFromDB() {
		ArrayList<String> favoritos = new ArrayList<String>();
		Cursor cursor = dataBaseSQLiteManagerFavorites.cargarTablas();
		try {
			
			if (cursor.moveToFirst()) {
				
				do{
					
					favoritos.add(cursor.getString(cursor.getColumnIndex("INDEX_OF_EVENT")));
				
				}while(cursor.moveToNext());
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			cursor.close();
			dataBaseSQLiteManagerFavorites.cerrarDB();
		}
		
		return favoritos;
	}
	
	public int getEventsDBCount() {
		Cursor cursor = dataBaseSQLiteManagerEvents.cargarTablas();
		return cursor.getCount();
	}
}
