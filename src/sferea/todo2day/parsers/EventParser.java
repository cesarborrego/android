package sferea.todo2day.parsers;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.utils.DateUtil;

public class EventParser {
	private static final String KEY_NUMBER_ITEMS = "numberItems";
	private static final String KEY_DATA = "data";
	private static final String KEY_ITEM = "Item";
	private static final String KEY_EVENT_ID = "EventID";
	private static final String KEY_EVENT_NAME = "EventName";
	private static final String KEY_CATEGORY = "Category";
	private static final String KEY_CATEGORY_ID = "CategoryID";
	private static final String KEY_DATE = "Date";
	private static final String KEY_DESCRIPTION = "Description";
	private static final String KEY_SOURCE = "Source";
	private static final String KEY_ADDRESS = "Address";
	private static final String KEY_PLACE = "Place";
	private static final String KEY_PHONE = "Phone";
	private static final String KEY_TICKET_TYPE = "TicketType";
	private static final String KEY_PRICE = "Price";
	private static final String KEY_DISTANCE = "Distance";
	private static final String KEY_LATITUDE = "Latitude";
	private static final String KEY_LONGITUDE = "Longitude";
	private static final String KEY_IMG_BANNER = "ImgBanner";
	private static final String KEY_UNIX_DATE = "UnixDate";
	private static final String KEY_INDEX_OF_EVENT = "IndexOfEvent";

	private static DateUtil dateUtil;

	public static ArrayList<EventoObjeto> parseJsonListaEventos(String json) throws JSONException {
		ArrayList<EventoObjeto> listaEventos = new ArrayList<EventoObjeto>();

			JSONObject jsonObj = new JSONObject(json);
			int itemsCount = jsonObj.getInt(KEY_NUMBER_ITEMS);

			if (itemsCount > 0) {

				dateUtil = new DateUtil();

				JSONObject jsonData = (JSONObject) jsonObj.get(KEY_DATA);

				for (int i = 0; i < itemsCount; i++) {
					JSONObject jsonItem = (JSONObject) jsonData.get(KEY_ITEM + i);
					EventoObjeto evento = parseJsonEvento(jsonItem);

					if (evento != null)
						listaEventos.add(parseJsonEvento(jsonItem));
				}
			}
		
		return listaEventos;
	}

	public static EventoObjeto parseJsonEvento(JSONObject json)
			throws JSONException {
		EventoObjeto evento = new EventoObjeto();

		evento.setIdOfEvent(json.getString(KEY_EVENT_ID));
		evento.setNombreEvento(json.getString(KEY_EVENT_NAME));
		evento.setCategoriaEvento(json.getString(KEY_CATEGORY));
		evento.setCategoriaIDEvento(json.getString(KEY_CATEGORY_ID));
		evento.setDescripcion(json.optString(KEY_DESCRIPTION, "No disponible"));
		evento.setFechaEvento(json.getString(KEY_DATE));
		evento.setFuente(json.getString(KEY_SOURCE));
		evento.setDireccion(json.getString(KEY_ADDRESS));
		evento.setLugarEvento("Av. Viaducto Rio de la Piedad y Rio Churubusco S/N, Granjas Mexico, 08400 Ciudad de Mexico, D.F.");
		evento.setTelefono(json.getString(KEY_PHONE));
		evento.setBoleto(json.getString(KEY_TICKET_TYPE));
		evento.setPrecio(json.getString(KEY_PRICE));
		evento.setDistancia(json.getString(KEY_DISTANCE));
		evento.setLatEvento(json.getInt(KEY_LATITUDE));
		evento.setLonEvento(json.getInt(KEY_LONGITUDE));
		evento.setUrlImagen(json.getString(KEY_IMG_BANNER));
		evento.setFechaUnix(json.getInt(KEY_UNIX_DATE));
		evento.setIndexEvento(json.getInt(KEY_INDEX_OF_EVENT));

		return evento;
	}
}
