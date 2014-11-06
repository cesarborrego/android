package sferea.todo2day.Helpers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sferea.todo2day.config.DataBaseSQLiteManagerEvents;
import sferea.todo2day.subfragments.Page_TimeLine;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * 
 * @author cesar
 *
 */

public class ParseJson_AddDB {
	
	Context thisContext;
	Activity thisActivity;
	DataBaseSQLiteManagerEvents dataBaseSQLiteManagerEvents;
	DateUtil dateUtil;
	ReadTableDB readTableDB;
	
	public ParseJson_AddDB(Context c, Activity a){
		this.thisContext = c;
		this.thisActivity = a;
		dateUtil = new DateUtil();
		dataBaseSQLiteManagerEvents = new DataBaseSQLiteManagerEvents(thisContext);
		readTableDB = new ReadTableDB(thisContext);
	}
	
	/**
	 * Deserealiza JSON y guarda en DB EVENTS.SQLite en tabla Events
	 * @param line
	 */
	public boolean parseFirstJson_AddDB (String line){
		boolean respuesta = false;
		if(line!=null){
			JSONParser parser = new JSONParser();
			Object object = null;
			try {
				object = parser.parse(line);
				
				JSONObject jsonObject = (JSONObject) object;

				String totalEvents = (String) jsonObject.get("numberItems");
				Log.d(null, "Total de eventos " + totalEvents);
				
				if(totalEvents!=null && !totalEvents.equals("0")){
					
					JSONObject jsonInicio = (JSONObject) jsonObject.get("data");

					int iCantidad = Integer.parseInt(totalEvents);
					Page_TimeLine.numeroEventos = iCantidad;
					for (int i = 0; i < iCantidad; i++) {
						if(thisActivity!=null){

							JSONObject jsonItem = (JSONObject) jsonInicio.get("Item" + i);
							
							//Lee la tabla events. Si encuentra el indice solo actualiza, en caso contrario inserta
							if(!readTableDB.isIndexOfEventsExist((String) jsonItem.get("EventID"))){
								dataBaseSQLiteManagerEvents.insertar((String)jsonItem.get("EventName"), 
										(String)jsonItem.get("Category"),
										(String)jsonItem.get("CategoryID"),
										dateUtil.dateTransform((String)jsonItem.get("Date")), 
										jsonItem.containsKey("Description") ? (String) jsonItem.get("Description") : "No disponible", 
										(String) jsonItem.get("Source"), 
										(String) jsonItem.get("Address"), 
										"Av. Viaducto Rio de la Piedad y Rio Churubusco S/N, Granjas Mexico, 08400 Ciudad de Mexico, D.F.", 
										(String) jsonItem.get("Phone"), 
										(String) jsonItem.get("TicketType"), 
										(String) jsonItem.get("Price"), 
										(String) jsonItem.get("Distance"), 
										(String) jsonItem.get("Latitude"), 
										(String) jsonItem.get("Longitude"), 
										(String) jsonItem.get("ImgBanner"), 
										String.valueOf(i), 
										(String) jsonItem.get("EventID"), 
										String.valueOf((Long) jsonItem.get("UnixDate")));
								Log.d("SQLite", "Se inserto registro con ID "+(String) jsonItem.get("EventID")+"\n" +
										"y Nombre de evento "+(String)jsonItem.get("EventName"));
							}else{
								dataBaseSQLiteManagerEvents.actualizar((String)jsonItem.get("EventName"), 
										(String)jsonItem.get("Category"),
										(String)jsonItem.get("CategoryID"),
										dateUtil.dateTransform((String)jsonItem.get("Date")), 
										jsonItem.containsKey("Description") ? (String) jsonItem.get("Description") : "No disponible", 
										(String) jsonItem.get("Source"), 
										(String) jsonItem.get("Address"), 
										"Av. Viaducto Rio de la Piedad y Rio Churubusco S/N, Granjas Mexico, 08400 Ciudad de Mexico, D.F.", 
										(String) jsonItem.get("Phone"), 
										(String) jsonItem.get("TicketType"), 
										(String) jsonItem.get("Price"), 
										(String) jsonItem.get("Distance"), 
										(String) jsonItem.get("Latitude"), 
										(String) jsonItem.get("Longitude"), 
										(String) jsonItem.get("ImgBanner"), 
										String.valueOf(i), 
										(String) jsonItem.get("EventID"), 
										String.valueOf((Long) jsonItem.get("UnixDate")));
								Log.d("SQLite", "Se Actualizo registro con ID "+(String) jsonItem.get("EventID")+"\n" +
										"y Nombre de evento "+(String)jsonItem.get("EventName"));
							}

							Page_TimeLine.fechaUnix = String.valueOf((Long) jsonItem.get("UnixDate"));
							Page_TimeLine.indexEvent = String.valueOf((Long) jsonItem.get("IndexOfEvent"));
						
						}
					}
					//Si todo se guarda en la DB retorna true
					respuesta = true;
				}else{
					//si no hay eventos retorna falso
					respuesta = false;
				}
					
			} catch (ParseException e) {
				//Si hay error en el parse retorna false
				respuesta = false;
				e.printStackTrace();
			}	
		
		}
		return respuesta;
	}
}
