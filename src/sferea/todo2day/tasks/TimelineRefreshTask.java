package sferea.todo2day.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.json.JSONException;

import sferea.todo2day.Application;
import sferea.todo2day.Helpers.ReadTableDB;
import sferea.todo2day.adapters.EventoObjeto;
import sferea.todo2day.config.DataBaseSQLiteManagerEvents;
import sferea.todo2day.parsers.EventParser;
import sferea.todo2day.subfragments.Page_TimeLine;
import android.os.AsyncTask;
import android.util.Log;

public class TimelineRefreshTask extends AsyncTask<String, Void, Boolean> {

	DataBaseSQLiteManagerEvents dataBaseSQLiteManagerEvents;
	ReadTableDB readTableDB;

	@Override
	protected Boolean doInBackground(String... params) {

		dataBaseSQLiteManagerEvents = new DataBaseSQLiteManagerEvents(
				Application.getInstance());

		readTableDB = new ReadTableDB(Application.getInstance());

		boolean respuesta = false;
		if (params[0] != null) {
			try {
				ArrayList<EventoObjeto> listaEventos = null;
				listaEventos = EventParser.parseJsonListaEventos(params[0]);

				if (listaEventos.size() == 0) {
					return respuesta;
				}

				Log.d(null, "Total de eventos " + listaEventos.size());

				for (int i = 0; i < listaEventos.size(); i++) {

					// Lee la tabla events. Si encuentra el indice solo
					// actualiza, en caso contrario inserta
					if (!readTableDB.isIndexOfEventsExist(listaEventos.get(i)
							.getIdOfEvent())) {

						if (Page_TimeLine.refresh) {
							listaEventos.get(i).setIsNewEvent(1);
						} else {
							listaEventos.get(i).setIsNewEvent(0);
						}

						dataBaseSQLiteManagerEvents.insertar(listaEventos
								.get(i));

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

				sortListByDate(listaEventos);

				Page_TimeLine.listaEventos = null;
				Page_TimeLine.listaEventos = listaEventos;

				// Si todo se guarda en la DB retorna true
				respuesta = true;

			} catch (JSONException e) {
				// Si hay error en el parse retorna false
				respuesta = false;
				e.printStackTrace();
			}

		}
		return respuesta;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		Page_TimeLine.eventsLoaded = result;
	};

	private void sortListByDate(ArrayList<EventoObjeto> eventos) {
		Collections.sort(eventos, new Comparator<EventoObjeto>() {

			@Override
			public int compare(EventoObjeto lhs, EventoObjeto rhs) {
				// TODO Auto-generated method stub
				SimpleDateFormat formatoFecha = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				int result = 0;

				try {
					Date d1 = (Date) formatoFecha.parse(lhs.getFechaEvento());
					Date d2 = (Date) formatoFecha.parse(rhs.getFechaEvento());

					result = d1.compareTo(d2);

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}

		});
	}

}
