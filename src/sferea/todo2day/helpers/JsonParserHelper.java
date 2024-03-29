package sferea.todo2day.helpers;

import java.util.ArrayList;

import org.json.JSONException;

import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.beans.TipoBoletoObjeto;
import sferea.todo2day.config.DataBaseSQLiteManagerEvents;
import sferea.todo2day.config.DataBaseSQLiteManagerTickets;
import sferea.todo2day.fragments.Page_TimeLine;
import sferea.todo2day.parsers.EventParser;
import sferea.todo2day.utils.DateUtil;
import android.content.Context;
import android.util.Log;

/**
 * 
 * @author cesar
 * 
 */

public class JsonParserHelper {

	Context thisContext;
	DataBaseSQLiteManagerEvents dataBaseSQLiteManagerEvents;
	DataBaseSQLiteManagerTickets dataBaseSQLiteManagerTickets;
	DateUtil dateUtil;
	ReadTableDB readTableDB;

	public JsonParserHelper(Context c) {
		this.thisContext = c;
		dateUtil = new DateUtil();
		dataBaseSQLiteManagerEvents = new DataBaseSQLiteManagerEvents(
				thisContext);
		dataBaseSQLiteManagerTickets = new DataBaseSQLiteManagerTickets(thisContext);
		readTableDB = new ReadTableDB(thisContext);
	}

	/**
	 * Deserealiza JSON y guarda en DB EVENTS.SQLite en tabla Events
	 * 
	 * @param line
	 */
	public boolean addEventsToDB(String line) {
		boolean respuesta = false;
		if (line != null) {
			try {
				ArrayList<EventoObjeto> listaEventos = null;
				listaEventos = EventParser.parseJsonListaEventos(line);

				if (listaEventos.size() == 0) {
					return respuesta;
				}

				Log.d(null, "Total de eventos " + listaEventos.size());

				for (int i = 0; i < listaEventos.size(); i++) {

					// Lee la tabla events. Si encuentra el indice solo
					// actualiza, en caso contrario inserta
					if (!readTableDB.isIndexOfEventsExist(listaEventos.get(i)
							.getIdOfEvent())) {

						if (Page_TimeLine.isRefreshEvent) {
							listaEventos.get(i).setIsNewEvent(1);
						} else {
							listaEventos.get(i).setIsNewEvent(0);
						}

						dataBaseSQLiteManagerEvents.insertar(listaEventos
								.get(i));
						
						for(int j=0; j<listaEventos.get(i).getTipoBoleto().getListaBoletos().size(); j++){
							
							dataBaseSQLiteManagerTickets.insertar(listaEventos.get(i).getIdOfEvent(), 
									listaEventos.get(i).getTipoBoleto().getListaBoletos().get(j));
						}
						
						

						Log.d("SQLite", "Se inserto registro con ID "
								+ listaEventos.get(i).getIdOfEvent() + "\n"
								+ "y Nombre de evento "
								+ listaEventos.get(i).getNombreEvento());
					} else {

						listaEventos.get(i).setIsNewEvent(0);

						dataBaseSQLiteManagerEvents.actualizar(listaEventos
								.get(i));

						Log.d("SQLite", "Se Actualizo registro con ID "
								+ listaEventos.get(i).getIdOfEvent() + "\n"
								+ "y Nombre de evento "
								+ listaEventos.get(i).getNombreEvento());
					}

					Page_TimeLine.fechaUnix = String.valueOf(listaEventos
							.get(i).getFechaUnix());
					Page_TimeLine.indexEvent = String.valueOf(listaEventos.get(
							i).getIndexEvento());

				}

				respuesta = true;

			} catch (JSONException e) {
				// Si hay error en el parse retorna false
				respuesta = false;
				e.printStackTrace();
			}
			finally{
				dataBaseSQLiteManagerEvents.cerrarDB();
			}

		}
		return respuesta;
	}
}
