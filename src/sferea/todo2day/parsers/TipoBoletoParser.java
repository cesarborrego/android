package sferea.todo2day.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sferea.todo2day.beans.BoletoObjeto;
import sferea.todo2day.beans.TipoBoletoObjeto;

public class TipoBoletoParser {
	private static final String KEY_ID = "id";
	private static final String KEY_TICKETS = "Tickets";
	private static final String KEY_TICKET_TYPE = "ticketType";
	private static final String KEY_QUANTITY = "quantity";
	private static final String KEY_PRICE = "price";
	
	public static final int GRATIS = 1;
	public static final int GENERAL = 2;
	public static final int VARIOS = 3;
	public static final int NO_DISPONIBLE = 1;
	
	public static TipoBoletoObjeto parseJsonTipoBoletos(JSONObject json) throws JSONException{
		TipoBoletoObjeto tipoBoleto = new TipoBoletoObjeto();
		
		tipoBoleto.setId(json.getInt(KEY_ID));
		
		if(tipoBoleto.getId() != GRATIS && tipoBoleto.getId() != NO_DISPONIBLE){
			JSONArray arrayBoletos = json.getJSONArray(KEY_TICKETS);
			tipoBoleto.getListaBoletos().addAll(parseJSONArrayBoletos(arrayBoletos));
		}
		
		return tipoBoleto;
	}
	
	private static ArrayList<BoletoObjeto> parseJSONArrayBoletos(JSONArray listaBoletos) throws JSONException{
		ArrayList<BoletoObjeto> arrayBoletos = new ArrayList<BoletoObjeto>();
		BoletoObjeto boleto;
		for (int i = 0; i < listaBoletos.length(); i++){
			boleto = parseJSONBoleto(listaBoletos.getJSONObject(i));
			arrayBoletos.add(boleto);
		}
		
		return arrayBoletos;
	}
	
	private static BoletoObjeto parseJSONBoleto(JSONObject objeto) throws JSONException{
		BoletoObjeto boleto = new BoletoObjeto();
		boleto.setTipo(objeto.getString(KEY_TICKET_TYPE));
		boleto.setPrecio(objeto.getString(KEY_PRICE));
		boleto.setCantidad(objeto.getString(KEY_QUANTITY));
		
		return boleto;
	}
}
