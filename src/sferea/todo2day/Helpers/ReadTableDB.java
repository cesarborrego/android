package sferea.todo2day.Helpers;

import java.util.Vector;

import sferea.todo2day.R;
import sferea.todo2day.adapters.EventoObjeto;
import sferea.todo2day.config.DataBaseSQLiteManagerEvents;
import sferea.todo2day.subfragments.Page_TimeLine;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ReadTableDB {
	Context thisContext;
	DataBaseSQLiteManagerEvents dataBaseSQLiteManagerEvents;

	public ReadTableDB(Context c){
		this.thisContext = c;
		dataBaseSQLiteManagerEvents = new DataBaseSQLiteManagerEvents(thisContext);		
	}
	
	public boolean isIndexOfEventsExist(String indexOfEvent){
		boolean respuesta = false;
		Cursor cursor = dataBaseSQLiteManagerEvents.cargarTablas();
		Vector<String> idEventsFound = new Vector<String>();
		
		try{
			if(cursor.moveToFirst()){
				do{
					idEventsFound.addElement(
							cursor.getString(cursor.getColumnIndex("INDEX_OF_EVENT")));
				}while (cursor.moveToNext());	
			}
		} finally{
			cursor.close();
//			dataBaseSQLiteManagerEvents.cerrarDB();
		}
		
		for(int j =0; j<idEventsFound.size(); j++){
			if(idEventsFound.get(j).toString().equals(indexOfEvent)){
				respuesta= true;
				break;
			} else {
				respuesta = false;
			}
		}
		return respuesta;		
	}
	
	public void readTable_FillList(){
		Page_TimeLine.listaEventos.clear();
		Cursor cursor = dataBaseSQLiteManagerEvents.cargarTablas();
		try{			
			if(cursor.moveToFirst()){
				do{
					/*public EventoObjeto(String nombreEvento, String catEvento, String fechaEvento, String descripcionEvento, 
					 * String fuenteEvento, String LugarEvento, 
						String direccionEvento, String telefonoEvento, double lat, double lon, String dist,
						String boletoEvento,String precio,
							int posicion, int indexOfEvent, int fechaUnix, String urlImgEvento){*/
					
					Page_TimeLine.listaEventos.add(
							new EventoObjeto(
							cursor.getString(cursor.getColumnIndex("TITULO_EVENTO")),
							cursor.getString(cursor.getColumnIndex("CATEGORIA")), 
							cursor.getString(cursor.getColumnIndex("FECHA")), 
							cursor.getString(cursor.getColumnIndex("DESCRIPCION")), 
							cursor.getString(cursor.getColumnIndex("FUENTE")), 
							cursor.getString(cursor.getColumnIndex("LUGAR")), 
							cursor.getString(cursor.getColumnIndex("DIRECCION")), 
							cursor.getString(cursor.getColumnIndex("TELEFONO")), 
							Double.parseDouble(cursor.getString(cursor.getColumnIndex("LATITUD"))),
							Double.parseDouble(cursor.getString(cursor.getColumnIndex("LONGITUD"))), 
							cursor.getString(cursor.getColumnIndex("DISTANCIA")), 
							cursor.getString(cursor.getColumnIndex("BOLETO")), 
							cursor.getString(cursor.getColumnIndex("PRECIO")), 
							Integer.parseInt(cursor.getString(cursor.getColumnIndex("POSICION"))),
							cursor.getString(cursor.getColumnIndex("INDEX_OF_EVENT")),							
							Integer.parseInt(cursor.getString(cursor.getColumnIndex("FECHA_UNIX"))),							
							cursor.getString(cursor.getColumnIndex("URL_IMAGEN_EVENTO")),
							R.drawable.ic_small_antros));
				}while (cursor.moveToNext());	
			}
		} finally{
			cursor.close();
			Page_TimeLine.arrayAdapterEvents.notifyDataSetChanged();
			Page_TimeLine.prendeEstrellaTime_Line = new boolean[Page_TimeLine.listaEventos.size()];
			Log.d("Lista Eventos", "Lista cargada desde DB!");
			dataBaseSQLiteManagerEvents.cerrarDB();
		}
	}
}