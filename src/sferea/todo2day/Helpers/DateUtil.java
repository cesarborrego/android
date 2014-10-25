package sferea.todo2day.Helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	java.text.DateFormat formatoDelTexto ;
	String formatoJson;
	String formatoFinal;
	public DateUtil(){}
	
	public String dateTransform(String date){
		
		if (!date.equals("No disponible")) {
			try {
				formatoDelTexto = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				
				Date primeraFecha = (Date) formatoDelTexto.parse(date);
				java.text.DateFormat writeFormat = new SimpleDateFormat(
						"EEE, dd MMM yyyy HH:mm");
				
				formatoJson = writeFormat.format(primeraFecha);

				formatoFinal = formatoJson.substring(0, 1).toUpperCase()
						+ "" + formatoJson.substring(1, 7) + " "
						+ formatoJson.substring(8, 9).toUpperCase() + ""
						+ formatoJson.substring(9, 22);

			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			formatoFinal = date;
		}
		
		return formatoFinal;
	}
}
