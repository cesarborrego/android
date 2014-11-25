package sferea.todo2day.helpers;

import java.util.ArrayList;

import sferea.todo2day.R;
import sferea.todo2day.beans.BoletoObjeto;
import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.beans.FavoritosObjeto;
import sferea.todo2day.beans.TipoBoletoObjeto;
import sferea.todo2day.config.DataBaseSQLiteManagerFavorites;
import sferea.todo2day.config.DataBaseSQLiteManagerEvents;
import sferea.todo2day.config.DataBaseSQLiteManagerTickets;
import sferea.todo2day.fragments.Page_Favorites;
import sferea.todo2day.fragments.Page_TimeLine;
import sferea.todo2day.utils.ListUtil;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ReadTableDB {
	Context thisContext;
	DataBaseSQLiteManagerEvents dataBaseSQLiteManagerEvents;
	DataBaseSQLiteManagerFavorites dataBaseSQLiteManagerFavorites;
	DataBaseSQLiteManagerTickets dataBaseSQLiteManagerTickets;
	
	public ReadTableDB(Context c) {
		this.thisContext = c;
		dataBaseSQLiteManagerEvents = new DataBaseSQLiteManagerEvents(
				thisContext);
		dataBaseSQLiteManagerFavorites = new DataBaseSQLiteManagerFavorites(
				thisContext);		
		dataBaseSQLiteManagerTickets = new DataBaseSQLiteManagerTickets(
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
				
				ListUtil.sortListByDateEvents(lista);
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
		evento.setPosicion(Integer.parseInt(cursor.getString(cursor.getColumnIndex("POSICION"))));
		evento.setIndexEvento(Integer.parseInt(cursor.getString(cursor.getColumnIndex("INDEX_OF_EVENT"))));
		evento.setFechaUnix(Integer.parseInt(cursor.getString(cursor.getColumnIndex("FECHA_UNIX"))));
		evento.setUrlImagen(cursor.getString(cursor.getColumnIndex("URL_IMAGEN_EVENTO")));
		evento.setIsNewEvent(Integer.parseInt(cursor.getString(cursor.getColumnIndex("IS_NEW"))));
		evento.setImagenCategoria(R.drawable.ic_small_antros);
		evento.setTipoBoleto(getTipoBoleto(cursor));
		return evento;
	}
	
	public ArrayList<FavoritosObjeto> fillFavoritesListFromDB() {
		ArrayList<FavoritosObjeto> lista = null;
		Cursor cursor = dataBaseSQLiteManagerFavorites.cargarTablas();

		try {
			if (cursor.moveToFirst()) {
				
				lista = new ArrayList<FavoritosObjeto>();

				do {
					
					lista.add(getFavoritesObjetoFromDB(cursor));
					
				} while (cursor.moveToNext());
				
				ListUtil.sortListByDateFavorites(lista);
			}
		} finally {
			cursor.close();
			Log.d("Lista Favoritos", "Lista cargada desde DB!");
			dataBaseSQLiteManagerFavorites.cerrarDB();
		}
		
		return lista;
	}
	
	public void AddFavoritesToAdapterFromDB() {
		Cursor cursor = dataBaseSQLiteManagerFavorites.cargarTablas();
		FavoritosObjeto favoritosObjeto;

		try {
			if (cursor.moveToFirst()) {

				do {
					
					favoritosObjeto = getFavoritesObjetoFromDB(cursor);
					if(Page_Favorites.adapterFavorites.getPosition(favoritosObjeto) != -1)
						Page_Favorites.adapterFavorites.add(favoritosObjeto);
					
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			Log.d("Lista Favoritos", "Lista cargada desde DB!");
			dataBaseSQLiteManagerFavorites.cerrarDB();
		}
	}
	
	private FavoritosObjeto getFavoritesObjetoFromDB(Cursor cursor){
		FavoritosObjeto favoritoObjeto = new FavoritosObjeto();
		favoritoObjeto.setIdEvento(cursor.getString(cursor.getColumnIndex("EVENTO_ID")));		
		favoritoObjeto.setNombreEvento(cursor.getString(cursor.getColumnIndex("TITULO_EVENTO")));
		favoritoObjeto.setCategoriaEvento(cursor.getString(cursor.getColumnIndex("CATEGORIA")));
		favoritoObjeto.setCategoriaIDEvento(cursor.getString(cursor.getColumnIndex("CATEGORIA_ID")));
		favoritoObjeto.setFechaEvento(cursor.getString(cursor.getColumnIndex("FECHA")));
		favoritoObjeto.setDescripcion(cursor.getString(cursor.getColumnIndex("DESCRIPCION")));
		favoritoObjeto.setFuente(cursor.getString(cursor.getColumnIndex("FUENTE")));
		favoritoObjeto.setLugarEvento(cursor.getString(cursor.getColumnIndex("LUGAR")));
		favoritoObjeto.setDireccion(cursor.getString(cursor.getColumnIndex("DIRECCION")));
		favoritoObjeto.setTelefono(cursor.getString(cursor.getColumnIndex("TELEFONO")));
		favoritoObjeto.setLatEvento(Double.parseDouble(cursor.getString(cursor.getColumnIndex("LATITUD"))));
		favoritoObjeto.setLonEvento(Double.parseDouble(cursor.getString(cursor.getColumnIndex("LONGITUD"))));
		favoritoObjeto.setPosicion(Integer.parseInt(cursor.getString(cursor.getColumnIndex("POSICION"))));
		favoritoObjeto.setDistanciaEvento(cursor.getString(cursor.getColumnIndex("DISTANCIA")));
		favoritoObjeto.setUrlImagen(cursor.getString(cursor.getColumnIndex("URL_IMAGEN_EVENTO")));
		favoritoObjeto.setIndexOfEvent(cursor.getString(cursor.getColumnIndex("INDEX_OF_EVENT")));
		favoritoObjeto.setFechaUnix(Integer.parseInt(cursor.getString(cursor.getColumnIndex("FECHA_UNIX"))));
		favoritoObjeto.setImagenCategoria(R.drawable.ic_small_antros);
		favoritoObjeto.setTipoBoletoObjeto(getTipoBoleto(cursor));
		return favoritoObjeto;
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
	
	private TipoBoletoObjeto getTipoBoleto (Cursor cursor){
		TipoBoletoObjeto tipoBoletoObjeto = new TipoBoletoObjeto();
		tipoBoletoObjeto.setId(Integer.parseInt((cursor.getString(cursor.getColumnIndex("BOLETO_ID")))));
		tipoBoletoObjeto.setListaBoletos(fillTicketsFromDB(cursor.getString(cursor.getColumnIndex("EVENTO_ID"))));
		return tipoBoletoObjeto;
	}
	
	public ArrayList<BoletoObjeto> fillTicketsFromDB(String indexOfEvent){
		ArrayList<BoletoObjeto> tickets = new ArrayList<BoletoObjeto>();
		Cursor cursor = dataBaseSQLiteManagerTickets.queryTicketByEventIndex(indexOfEvent);
		try {
			if(cursor.moveToFirst()){
				do{
					tickets.add(getBoletoObjeto(cursor));
				}while(cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
//			dataBaseSQLiteManagerTickets.cerrarDB();
		}
		return tickets;
	}
	
	private BoletoObjeto getBoletoObjeto (Cursor cursor){
		BoletoObjeto boletoObjeto = new BoletoObjeto();
		boletoObjeto.setTipo(cursor.getString(cursor.getColumnIndex("TIPO_BOLETO")));
		boletoObjeto.setPrecio(Integer.parseInt(cursor.getString(cursor.getColumnIndex("PRECIO_BOLETO"))));
		boletoObjeto.setCantidad(cursor.getString(cursor.getColumnIndex("CANTIDAD_BOLETO")));
		return boletoObjeto;	
	}
	
	public int getEventsDBCount() {
		Cursor cursor = dataBaseSQLiteManagerEvents.cargarTablas();
		return cursor.getCount();
	}
}
