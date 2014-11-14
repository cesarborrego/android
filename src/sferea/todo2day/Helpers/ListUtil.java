package sferea.todo2day.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import sferea.todo2day.adapters.EventoObjeto;

public class ListUtil {
	public static void sortListByDate(ArrayList<EventoObjeto> eventos) {
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
