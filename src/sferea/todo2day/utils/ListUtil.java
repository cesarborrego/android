package sferea.todo2day.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.beans.FavoritosObjeto;


public class ListUtil {
	public static void sortListByDateEvents(ArrayList<EventoObjeto> eventos) {
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

				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}
		});
	}
	
	public static void sortListByDateFavorites(ArrayList<FavoritosObjeto> eventos) {
		Collections.sort(eventos, new Comparator<FavoritosObjeto>() {

			@Override
			public int compare(FavoritosObjeto lhs, FavoritosObjeto rhs) {
				// TODO Auto-generated method stub
				SimpleDateFormat formatoFecha = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				int result = 0;

				try {
					Date d1 = (Date) formatoFecha.parse(lhs.getFechaEvento());
					Date d2 = (Date) formatoFecha.parse(rhs.getFechaEvento());

					result = d1.compareTo(d2);

				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}
		});
	}
}

