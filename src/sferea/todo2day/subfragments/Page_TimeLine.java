package sferea.todo2day.subfragments;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sferea.todo2day.MainActivity;
import sferea.todo2day.R;
import sferea.todo2day.SplashActivity;
import sferea.todo2day.Helpers.JsonHelper;
import sferea.todo2day.adapters.ArrayAdapterEvents;
import sferea.todo2day.adapters.EventoObjeto;
import sferea.todo2day.config.LocationHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Page_TimeLine extends Fragment {
	
	public static final float REFRESH_THRESHOLD = 50;
	List<EventoObjeto> listaEventos;
	EventoObjeto eo;
	public static ArrayAdapterEvents arrayAdapterEvents;
	ListView listView_Eventos;
	boolean allowRefresh=false;
	boolean refresh=false;
	boolean downCounterUsed=false;
	boolean headerAdded=false;
	float startY;
	String imagenesEventos [] = null;
	public static String activaUbicate= "no";
	public static String activaRuta= "no";
	
	public static boolean antrosActiva = true;
	public static boolean culturaActiva = true;
	public static boolean cineActiva = true;
	public static boolean deportesActiva = true;
	public static boolean negociosActiva = true;
	public static boolean pequesActiva = true;
	public static boolean gastronomiaActiva = true;
	public static boolean musicaActiva = true;
	public static boolean saludActiva = true;
	public static boolean socialesActiva = true;
	public static boolean tecnologiaActiva = true;
	public static boolean verdeActiva = true;
	
	public static String antros = "A";
	public static String cultura = "B";
	public static String cine = "C";
	public static String deportes = "D";
	public static String negocios = "E";
	public static String peques= "F";
	public static String gastronomia = "G";
	public static String musica = "H";
	public static String salud = "I";
	public static String sociales= "J";
	public static String tecnologia= "K";
	public static String verde = "L";
		
	//Debemos obtener la lat y lon correcta desde el gps
	public static double latOrigin= 19.355582;
	public static double lonOrigin =-99.186726;
	
//	public static double latOrigin;
//	public static double lonOrigin;
	
	public static boolean prendeEstrellaTime_Line[] ;
	String Titulos[];
	String EventoID[];
	String Categorias [];
	
	String Fechas [];
	String [] strFecha;
	Date fecha [];
	String formatedDate []; 
	String fechaFinal [];
	
	Long FechayHoraUnix[];
	Long IndexOfEvent [];
	
	String Descripciones [];
	String Boletos [];
	String Fuentes [];
	String Lugares [];
	String Direcciones [];
	String Telefonos [];
	String Distancias [];
	double Longitudes [];
	double Latitudes [];
	static String ImagenEvento [];
	
	Bitmap imagenesCargadas[];	
	
	Bitmap img [];
	
	int imagenesCategorias[];
	
	SharedPreferences shrpref_fav;	
	
	View view;
	View headerView;
	View footerView;
	boolean isLoading=false;
	
	LocationHelper locationHelper;
	
	
	
	int iContador = 0;
	String catt = "";
	int numeroEventos = 0;
	
	public static boolean eventoActivo = false;
	public static boolean favoritoActivo = false;
	public static boolean prendeEstrellaDetails = false;
	
	java.text.DateFormat formatoDelTexto ;	

	String json;
	String jsonAddMore;
	String jsonCache;
	
	String categorias = "";
	String idevento="";
	String fechaUnix="";
	String indexEvent ="";
	
	//Esta variable se usa por si no llega a descargar eventos no pasa al hilo de descargar imagenes
	boolean descargaImagenesFirstAccess = false;
	
	boolean isGpsActive = false;
	boolean isWirelessActive = false;
	
	int totalItemCount = 0;
	
	//Aqui tenemos que recuperar los eventos anteriores y juntarlos con los nuevos
	// para que en la lista el total de eventos sea correcto y no por partes
	String [] titulo;
	String []  categoriasSave;
	String []  fechaSave;
	String []  descripciones;
	String []  fuentes;
	String []  lugares;
	String []  direcciones;
	String []  telefonos;
	Double []  latitudes;
	Double []  longitudes;
	String []  distancias;
	String []  boletos;
	Bitmap []  imagenEventoSave;
	int []  imgCategorias;
	int [] indexOfEventSave;
	int [] fechaUnixSave;
	
	JsonHelper jsonHelper;
	public Page_TimeLine(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		jsonHelper = new JsonHelper(getActivity().getApplicationContext());
//		gps();
		if(SplashActivity.leeJSONCache){
			if(jsonHelper.leerJsonCache()!=null){
				jsonCache = jsonHelper.leerJsonCache();
			}
		}else{
			if(jsonHelper.leerPrimerJson()!=null){
				json = jsonHelper.leerPrimerJson();
			}
		}
	}

	private void nuevoJsonObject (String line) throws IOException, ParseException{
		JSONParser parser = new JSONParser();
		Object object = parser.parse(line);
		JSONObject jsonObject = (JSONObject) object;

		String totalEvents = (String) jsonObject.get("numberItems");
		Log.d(null, "Total de eventos " + totalEvents);
		
		if(totalEvents!=null && !totalEvents.equals("0")){
			//Habilitamos la descarga de la/las imagenes
			descargaImagenesFirstAccess = true;
			
			JSONObject jsonInicio = (JSONObject) jsonObject.get("data");

			int iCantidad = Integer.parseInt(totalEvents);
//			int iCantidad = 10;
			numeroEventos = iCantidad;

			Titulos = new String[iCantidad];
			EventoID = new String[iCantidad];
			Categorias = new String[iCantidad];

			Fechas = new String[iCantidad];
			fecha = new Date[iCantidad];
			formatedDate = new String[iCantidad];
			fechaFinal = new String[iCantidad];
			strFecha = new String[iCantidad];
			
			FechayHoraUnix = new Long[iCantidad];
			IndexOfEvent = new Long [iCantidad];

			Descripciones = new String[iCantidad];
			Boletos = new String[iCantidad];
			Fuentes = new String[iCantidad];
			Lugares = new String[iCantidad];
			Direcciones = new String[iCantidad];
			Telefonos = new String[iCantidad];
			Distancias = new String[iCantidad];
			Longitudes = new double[iCantidad];
			Latitudes = new double[iCantidad];
			ImagenEvento = new String[iCantidad];

			imagenesCategorias = new int[iCantidad];

			img = new Bitmap[iCantidad];		
			
			for (int i = 0; i < iCantidad; i++) {
				JSONObject jsonItem = (JSONObject) jsonInicio.get("Item" + i);
				Titulos[i] = (String) jsonItem.get("EventName");
				if(Titulos[i]==null){
					Titulos[i] = "No Disponible";
				}else{
					Titulos[i] = (String) jsonItem.get("EventName");
				}
				
				EventoID[i] = (String)jsonItem.get("EventID");
				
				Categorias[i] = (String) jsonItem.get("Category");

				Fechas[i] = (String) jsonItem.get("Date");

				if (!Fechas[i].equals("No disponible")) {
					try {
						formatoDelTexto = new SimpleDateFormat(
								"yyyy-MM-dd'T'HH:mm:ss");
						Date primeraFecha = (Date) formatoDelTexto.parse(Fechas[i]
								.toString());
						java.text.DateFormat writeFormat = new SimpleDateFormat(
								"EEE, dd MMM yyyy HH:mm");
						strFecha[i] = writeFormat.format(primeraFecha);

						fechaFinal[i] = strFecha[i].substring(0, 1).toUpperCase()
								+ "" + strFecha[i].substring(1, 7) + " "
								+ strFecha[i].substring(8, 9).toUpperCase() + ""
								+ strFecha[i].substring(9, 22);

					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					fechaFinal[i] = Fechas[i];
				}
				
				FechayHoraUnix[i] = (Long) jsonItem.get("UnixDate");
				
				IndexOfEvent[i] = (Long)jsonItem.get("IndexOfEvent");
				
				Descripciones[i] = "Descripción: "
						+ (String) jsonItem.get("Description");
				
				Fuentes[i] = "Fuente: " + (String) jsonItem.get("Source");
				
				Lugares[i] = "Lugar: " + (String) jsonItem.get("Address");
				
//				Direcciones[i] = (String) jsonItem.get("Direccion");
				Direcciones[i] = "Abundio Martinez";

				if (!jsonItem.get("Phone").equals("No disponible")||
						jsonItem.get("Phone").equals("")) {
					Telefonos[i] = (String) jsonItem.get("Phone");
				} else {
					Telefonos[i] = "56-58-11-11";
				}

				Distancias[i] = (String) jsonItem.get("Distance");
				
				Boletos[i] = (String) jsonItem.get("TicketType");
				
				Longitudes[i] = Double.parseDouble((String) jsonItem
						.get("Longitude"));
				
				Latitudes[i] = Double.parseDouble((String) jsonItem.get("Latitude"));

				if (jsonItem.get("ImgBanner").toString().equals("No disponible")) {
					ImagenEvento[i] = "http://fernandacharquero.com.ar/img/sociales04.jpg";
				} else {
					ImagenEvento[i] = (String) jsonItem.get("ImgBanner");
				}

				imagenesCategorias[i] = R.drawable.ic_small_antros;
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				img[i] = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),
						R.drawable.evento,options);
			}
			for (int j = 0; j < iCantidad; j++) {
				listaEventos.add(new EventoObjeto(Titulos[j], Categorias[j],
						fechaFinal[j].toString(), Descripciones[j], Fuentes[j],
						Lugares[j], Direcciones[j], Telefonos[j], Latitudes[j],
						Longitudes[j], Distancias[j], Boletos[j], img[j],
						imagenesCategorias[j], j, Integer.parseInt(String.valueOf(IndexOfEvent[j])),
						Integer.parseInt(String.valueOf(FechayHoraUnix[j]))));
			}
			prendeEstrellaTime_Line = new boolean[listaEventos.size()];
		}else{
			Toast.makeText(getActivity().getApplicationContext(), "No hay Eventos Disponibles\n" +
					"Prueba con otras categorías!\n" +
					"Y/o Aumenta el Radio de búsqueda en los ajustes", Toast.LENGTH_LONG).show();
		}
	}
	
	private void nuevoJsonObjectAddMore (String line) throws IOException, ParseException{
		JSONParser parser = new JSONParser();
		Object object = parser.parse(line);
		JSONObject jsonObject = (JSONObject) object;

		String totalEvents = (String) jsonObject.get("numberItems");
		Log.d(null, "Total de eventos " + totalEvents);
		
		if(!totalEvents.equals("0")){
			
			JSONObject jsonInicio = (JSONObject) jsonObject.get("data");

			int iCantidad = Integer.parseInt(totalEvents);
//			int iCantidad = 10;
			numeroEventos = iCantidad;

			Titulos = new String[iCantidad];
			EventoID = new String[iCantidad];
			Categorias = new String[iCantidad];

			Fechas = new String[iCantidad];
			fecha = new Date[iCantidad];
			formatedDate = new String[iCantidad];
			fechaFinal = new String[iCantidad];
			strFecha = new String[iCantidad];
			
			FechayHoraUnix = new Long[iCantidad];
			IndexOfEvent = new Long [iCantidad];

			Descripciones = new String[iCantidad];
			Boletos = new String[iCantidad];
			Fuentes = new String[iCantidad];
			Lugares = new String[iCantidad];
			Direcciones = new String[iCantidad];
			Telefonos = new String[iCantidad];
			Distancias = new String[iCantidad];
			Longitudes = new double[iCantidad];
			Latitudes = new double[iCantidad];
			ImagenEvento = new String[iCantidad];

			imagenesCategorias = new int[iCantidad];

			img = new Bitmap[iCantidad];
			
			for (int i = 0; i < iCantidad; i++) {
				JSONObject jsonItem = (JSONObject) jsonInicio.get("Item" + i);
				Titulos[i] = (String) jsonItem.get("EventName");
				if(Titulos[i]==null){
					Titulos[i] = "No Disponible";
				}else{
					Titulos[i] = (String) jsonItem.get("EventName");
				}
				
				EventoID[i] = (String)jsonItem.get("EventID");
				
				Categorias[i] = (String) jsonItem.get("Category");

				Fechas[i] = (String) jsonItem.get("Date");

				if (!Fechas[i].equals("No disponible")) {
					try {
						formatoDelTexto = new SimpleDateFormat(
								"yyyy-MM-dd'T'HH:mm:ss");
						Date primeraFecha = (Date) formatoDelTexto.parse(Fechas[i]
								.toString());
						java.text.DateFormat writeFormat = new SimpleDateFormat(
								"EEE, dd MMM yyyy HH:mm");
						strFecha[i] = writeFormat.format(primeraFecha);

						fechaFinal[i] = strFecha[i].substring(0, 1).toUpperCase()
								+ "" + strFecha[i].substring(1, 7) + " "
								+ strFecha[i].substring(8, 9).toUpperCase() + ""
								+ strFecha[i].substring(9, 22);

					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					fechaFinal[i] = Fechas[i];
				}
				
				FechayHoraUnix[i] = (Long) jsonItem.get("UnixDate");
				
				IndexOfEvent[i] = (Long)jsonItem.get("IndexOfEvent");
				
				Descripciones[i] = "Descripción: "
						+ (String) jsonItem.get("Description");
				
				Fuentes[i] = "Fuente: " + (String) jsonItem.get("Source");
				
				Lugares[i] = "Lugar: " + (String) jsonItem.get("Address");
				
//				Direcciones[i] = (String) jsonItem.get("Direccion");
				Direcciones[i] = "Abundio Martinez";

				if (!jsonItem.get("Phone").equals("No disponible")||
						jsonItem.get("Phone").equals("")) {
					Telefonos[i] = (String) jsonItem.get("Phone");
				} else {
					Telefonos[i] = "56-58-11-11";
				}

				Distancias[i] = (String) jsonItem.get("Distance");
				
				Boletos[i] = (String) jsonItem.get("TicketType");
				
				Longitudes[i] = Double.parseDouble((String) jsonItem
						.get("Longitude"));
				
				Latitudes[i] = Double.parseDouble((String) jsonItem.get("Latitude"));

				if (jsonItem.get("ImgBanner").toString().equals("No disponible")) {
					ImagenEvento[i] = "http://fernandacharquero.com.ar/img/sociales04.jpg";
				} else {
					ImagenEvento[i] = (String) jsonItem.get("ImgBanner");
				}

				imagenesCategorias[i] = R.drawable.ic_small_antros;
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				img[i] = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),
						R.drawable.evento,options);
			}
			for (int j = 0; j < iCantidad; j++) {
				listaEventos.add(new EventoObjeto(Titulos[j], Categorias[j],
						fechaFinal[j].toString(), Descripciones[j], Fuentes[j],
						Lugares[j], Direcciones[j], Telefonos[j], Latitudes[j],
						Longitudes[j], Distancias[j], Boletos[j], img[j],
						imagenesCategorias[j], j, Integer.parseInt(String.valueOf(IndexOfEvent[j])),
						Integer.parseInt(String.valueOf(FechayHoraUnix[j]))));
			}
			prendeEstrellaTime_Line = new boolean[listaEventos.size()];
			arrayAdapterEvents.notifyDataSetChanged();
		}else{
			Toast.makeText(getActivity().getApplicationContext(), "No hay Eventos Disponibles\n" +
					"Prueba con otras categorías!\n" +
					"Y/o Aumenta el Radio de búsqueda en los ajustes", Toast.LENGTH_LONG).show();
		}
	}
	
	public void cargaJsonCache(){
		
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(jsonCache);
			JSONArray jsonArray = (JSONArray)jsonObject.get("JsonCache");
			Iterator iterator = jsonArray.iterator();
			int iContador =0;
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			Bitmap img = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),
					R.drawable.evento,options);
			listaEventos.clear();
			while (iterator.hasNext()){
				JSONObject jsonObject2 = (JSONObject)iterator.next();
				listaEventos.add(new EventoObjeto(jsonObject2.get("titulo"+iContador).toString(),
						jsonObject2.get("categorias"+iContador).toString(),
						jsonObject2.get("fechas"+iContador).toString(), 
						jsonObject2.get("descripciones"+iContador).toString(),
						jsonObject2.get("fuentes"+iContador).toString(),
						jsonObject2.get("lugares"+iContador).toString(),
						jsonObject2.get("direcciones"+iContador).toString(),
						jsonObject2.get("telefonos"+iContador).toString(),
						Double.parseDouble(jsonObject2.get("latitudes"+iContador).toString()),
						Double.parseDouble(jsonObject2.get("longuitudes"+iContador).toString()),
						jsonObject2.get("distancias"+iContador).toString(),
						jsonObject2.get("boletos"+iContador).toString(),img, 
						Integer.parseInt(jsonObject2.get("imagenesCat"+iContador).toString()),
						Integer.parseInt(jsonObject2.get("posiciones"+iContador).toString()),
						Integer.parseInt(jsonObject2.get("indexOfEvents"+iContador).toString()),
						Integer.parseInt(jsonObject2.get("fechaUnix"+iContador).toString())));
				//Aqui vamos a recuperar los valores de index y fecha unix para que al momento de agregar mas eventos
				//le pasemos estos valores a la URL de mongo
				fechaUnix = jsonObject2.get("fechaUnix"+iContador).toString();
				indexEvent = jsonObject2.get("indexOfEvents"+iContador).toString();
				iContador++;
			}
			arrayAdapterEvents.notifyDataSetChanged();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		//infla el view del timeline
		view = inflater.inflate(R.layout.page_timeline, container, false);
		//Infla el view del header
		headerView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_header, null);
		//Infla el view del footer
		footerView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
		
		//Crea un nuevo arraylist de eventos
		listaEventos = new ArrayList<EventoObjeto>();
		
		if(SplashActivity.leeJSONCache){
			cargaJsonCache();
		}else{
			funcionesPrincipales();
		}
		
		//Crea el arrayAdapter de eventos
		arrayAdapterEvents = new ArrayAdapterEvents(getActivity(), R.layout.row_event_responsive, R.id.listviewEventos, listaEventos);
		//Obtiene la vista del listView 
		listView_Eventos = ((ListView)view.findViewById(R.id.listviewEventos));
		
		//Agrega el header
		listView_Eventos.addHeaderView(headerView);
		
		//Agrega el footer
		listView_Eventos.addFooterView(footerView);
		
		//Asigna el arrayAdapter al listview
		listView_Eventos.setAdapter(arrayAdapterEvents);
		
		listView_Eventos.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				Log.d(null,"firsVisibleItem="+firstVisibleItem+
						"\n+visibleItemCount="+visibleItemCount+
						"\ntotalItemCount"+totalItemCount+
						"\nloadedItems"+(firstVisibleItem + visibleItemCount));
				Page_TimeLine.this.totalItemCount = totalItemCount;
				if(((firstVisibleItem + visibleItemCount)==totalItemCount)&
						listView_Eventos.getFirstVisiblePosition()>0){
//					addMoreEvents(latOrigin, lonOrigin);
					if (listView_Eventos.getLastVisiblePosition() == listView_Eventos.getAdapter().getCount() - 1
							&& listView_Eventos.getChildAt(listView_Eventos.getChildCount() - 1).getBottom() <= listView_Eventos.getHeight()) {
							Log.d(null, "Final");
							addMoreEvents(latOrigin, lonOrigin);
					}
				}
			}
		});	
		
		listView_Eventos.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float y = event.getY();
				
				switch (event.getAction()) {
					
					case MotionEvent.ACTION_MOVE:{
						Log.d("Movimiento", String.valueOf(y));
						if(!downCounterUsed){
							startY=y;
							downCounterUsed=true;
						}
						allowRefresh = (listView_Eventos.getFirstVisiblePosition() == 0);
						Log.d("FirstVisible", String.valueOf(listView_Eventos.getFirstVisiblePosition()));
						if(allowRefresh){
							if((y - startY) > REFRESH_THRESHOLD)
							{ 
								refresh = true;
								if(!headerAdded){
									//listView_Eventos.addHeaderView(headerView);
									((TextView)headerView.findViewById(R.id.textoHeaderListview)).setVisibility(View.VISIBLE);
									headerAdded=true;
								}
							}
							else{ refresh=false; }
						}
						break;
					}
					
					case MotionEvent.ACTION_UP:{
						Log.d("FirstVisiblePosition", String.valueOf(listView_Eventos.getFirstVisiblePosition()));
						if(refresh){
							refreshTimeLine();
						}else{
							((TextView)headerView.findViewById(R.id.textoHeaderListview)).setVisibility(View.GONE);
							((ProgressBar)headerView.findViewById(R.id.progressBarHeader)).setVisibility(View.GONE);
						}
						downCounterUsed=false;
						refresh = false;
					}
				}
				return false;
			}
		});
		
		return view;
	}
	
	public void funcionesPrincipales(){
		try {
			if(json!=null){
				nuevoJsonObject(json);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(ImagenEvento!=null){
			downloadPictureFirstAccess(ImagenEvento);
		}
	}
	
	//Se ejecuta solo pa refresh
	public void funcionesRefresh(){
		//Ejecutamos primero lectura de json y presentamos lista en hilo principal
//		try {
//			nuevoJsonObject(json);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		downloadPictureSecondAccessRefresh(ImagenEvento);
//		leerJsonCache();
//		cargaJsonCache();
	}
	
	public void funcionesAddMore(){
		for (int j = 0; j < titulo.length; j++) {		
		listaEventos.add(new EventoObjeto(titulo[j],
				categoriasSave[j],
				fechaSave[j].toString(),
				descripciones[j], fuentes[j],
				lugares[j], direcciones[j],
				telefonos[j], latitudes[j],
				longitudes[j], distancias[j],
				boletos[j], imagenEventoSave[j],
				imgCategorias[j],j, indexOfEventSave[j], fechaUnixSave[j]));
	}
		arrayAdapterEvents.notifyDataSetChanged();
		//Ejecutamos primero lectura de json y presentamos lista en hilo principal
		try {
			nuevoJsonObjectAddMore(jsonAddMore);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		crearJSONCache();
//		if(ImagenEvento!=null){
//			downloadPictureSecondAccessMore(ImagenEvento);
//		}
	}
	
	private void crearJSONCache() {
		new AsyncTask<Void, Void, Void>(){
			JSONObject items;
			JSONObject jsonFinal = new JSONObject();
			JSONArray jsonArray = new JSONArray();

			@SuppressWarnings("unchecked")
			@Override
			protected Void doInBackground(Void... params) {
				if(getActivity()!=null){
					for(int j = 0;j<listaEventos.size(); j++){
						items = new JSONObject();
						items.put("titulo"+j, listaEventos.get(j).getNombreEvento());
						items.put("categorias"+j, listaEventos.get(j).getCategoriaEvento());
						items.put("fechas"+j, listaEventos.get(j).getFechaEvento());
						items.put("descripciones"+j, listaEventos.get(j).getDescripcion());
						items.put("fuentes"+j, listaEventos.get(j).getFuente());
						items.put("lugares"+j, listaEventos.get(j).getLugarEvento());
						items.put("direcciones"+j, listaEventos.get(j).getDireccion());
						items.put("telefonos"+j, listaEventos.get(j).getTelefono());
						items.put("latitudes"+j, listaEventos.get(j).getLatEvento());
						items.put("longuitudes"+j, listaEventos.get(j).getLonEvento());
						items.put("distancias"+j, listaEventos.get(j).getDistancia());
						items.put("boletos"+j, listaEventos.get(j).getBoleto());
//						imagenEventoSave[j] = listaEventos.get(j).getImagenEvento();
						items.put("imagenesCat"+j, listaEventos.get(j).getImagenCategoria());
						items.put("posiciones"+j, listaEventos.get(j).getPosicion());
						items.put("indexOfEvents"+j, listaEventos.get(j).getIndexOfEvent());
						items.put("fechaUnix"+j, listaEventos.get(j).getFechaUnix());
						jsonArray.add(items);
					}
					jsonFinal.put("JsonCache", jsonArray);
					
					OutputStreamWriter outputStreamWriter = null;
					try {
						outputStreamWriter = new OutputStreamWriter(getActivity().getApplicationContext()
								.openFileOutput("JsonCache.txt", Context.MODE_PRIVATE));
						outputStreamWriter.write(jsonFinal.toJSONString());
						outputStreamWriter.flush();
						outputStreamWriter.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				Log.d(null, "Json Cache Creado!");	
				jsonCache = jsonHelper.leerJsonCache();
				return null;
			}
			
			protected void onPostExecute(Void result) {
				cargaJsonCache();
			};
			
		}.execute();
	}

	/**
	 * 
	 * @param urlMapaEstatico
	 */
	public void downloadPictureFirstAccess(final String[] urlsImagenes) {
		new AsyncTask<Void, Void, Void>(){
			Bitmap[] downloadBitmap=null;
			@Override
			protected Void doInBackground(Void... params) {
				downloadBitmap = new Bitmap[urlsImagenes.length];
				for (int i = 0; i < urlsImagenes.length; i++) {
					try {
						downloadBitmap[i] = downloadBitmap(urlsImagenes[i]);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				listaEventos.clear();
				return null;
			}
			
			protected void onPostExecute(Void result) {
				if(getActivity()!=null){
					for (int j = 0; j < urlsImagenes.length; j++) {
						listaEventos.add(new EventoObjeto(Titulos[j],
								Categorias[j],
								fechaFinal[j].toString(),
								Descripciones[j], Fuentes[j],
								Lugares[j], Direcciones[j],
								Telefonos[j], Latitudes[j],
								Longitudes[j], Distancias[j],
								Boletos[j], downloadBitmap[j],
								imagenesCategorias[j], j, Integer.parseInt(String.valueOf(IndexOfEvent[j])),
								Integer.parseInt(String.valueOf(FechayHoraUnix[j]))));
					}
					arrayAdapterEvents.notifyDataSetChanged();
				}
				
			};
		}.execute();
	}
	
	public void downloadPictureSecondAccessMore(final String[] urlsImagenes) {
		new AsyncTask<Void, Void, Void>(){
			Bitmap[] downloadBitmap=null;
			
			
			
			protected void onPreExecute() {};
			
			@Override
			protected Void doInBackground(Void... params) {
				downloadBitmap = new Bitmap[urlsImagenes.length];
				for (int i = 0; i < urlsImagenes.length; i++) {
					try {
						downloadBitmap[i] = downloadBitmap(urlsImagenes[i]);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}
			
			protected void onPostExecute(Void result) {
				
//				for (int j = 0; j < urlsImagenes.length; j++) {
//					//Limpiamos lista y como ya tenemos cargada la info en los arreglos
//					//eso vamos a setear a la nueva lista					
//					listaEventos.add(new EventoObjeto(titulo[j],
//							categoriasSave[j],
//							fechaSave[j].toString(),
//							descripciones[j], fuentes[j],
//							lugares[j], direcciones[j],
//							telefonos[j], latitudes[j],
//							longitudes[j], distancias[j],
//							boletos[j], downloadBitmap[j],
//							imgCategorias[j],j));
//				}
				listaEventos.clear();
				for (int j = 0; j < urlsImagenes.length; j++) {
					listaEventos.add(new EventoObjeto(Titulos[j],
							Categorias[j],
							fechaFinal[j].toString(),
							Descripciones[j], Fuentes[j],
							Lugares[j], Direcciones[j],
							Telefonos[j], Latitudes[j],
							Longitudes[j], Distancias[j],
							Boletos[j], downloadBitmap[j],
							imagenesCategorias[j], j, Integer.parseInt(String.valueOf(IndexOfEvent[j])),
							Integer.parseInt(String.valueOf(FechayHoraUnix[j]))));
				}
				arrayAdapterEvents.notifyDataSetChanged();
				this.cancel(true);
			};
			
		}.execute();
	}
	
	public void downloadPictureSecondAccessRefresh(final String[] urlsImagenes) {
		new AsyncTask<Void, Void, Void>(){
			Bitmap[] downloadBitmap=null;
			@Override
			protected Void doInBackground(Void... params) {
				downloadBitmap = new Bitmap[urlsImagenes.length];
				for (int i = 0; i < urlsImagenes.length; i++) {
					try {
						downloadBitmap[i] = downloadBitmap(urlsImagenes[i]);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}
			
			protected void onPostExecute(Void result) {
				//Limpiamos lista cargamos nuevos objetos y presentamos los nuevos elementos
				listaEventos.clear();
				for (int j = 0; j < urlsImagenes.length; j++) {
					listaEventos.add(new EventoObjeto(Titulos[j],
							Categorias[j],
							fechaFinal[j].toString(),
							Descripciones[j], Fuentes[j],
							Lugares[j], Direcciones[j],
							Telefonos[j], Latitudes[j],
							Longitudes[j], Distancias[j],
							Boletos[j], downloadBitmap[j],
							imagenesCategorias[j], j, Integer.parseInt(String.valueOf(IndexOfEvent[j])),
							Integer.parseInt(String.valueOf(FechayHoraUnix[j]))));
				}
				arrayAdapterEvents.notifyDataSetChanged();
			};
			
		}.execute();
	}
	
	/**
	 * Descarga img a traves de su url y la codifica a bitmap
	 * @param url
	 * @return Bitmap bitmap
	 * @throws IOException
	 */
   private Bitmap downloadBitmap(String url) throws IOException {
       HttpUriRequest request = new HttpGet(url.toString());
       HttpClient httpClient = new DefaultHttpClient();
       HttpResponse response = httpClient.execute(request);

       StatusLine statusLine = response.getStatusLine();
       int statusCode = statusLine.getStatusCode();
       if (statusCode == 200) {
           HttpEntity entity = response.getEntity();
           byte[] bytes = EntityUtils.toByteArray(entity);

           Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                   bytes.length);
           return bitmap;
       } else {
           throw new IOException("Download failed, HTTP response code "
                   + statusCode + " - " + statusLine.getReasonPhrase());
       }
   }
   
	public void addMoreItems(){
		addMoreEvents(latOrigin, lonOrigin);
	}

	public void refreshTimeLine(){
		new AsyncTask<String, Void, InputStream>(){
			
			@Override
			protected void onPreExecute() {
				// TODO El dialog de cargar
				super.onPreExecute();
				((TextView)headerView.findViewById(R.id.textoHeaderListview)).setVisibility(View.GONE);
				((ProgressBar)headerView.findViewById(R.id.progressBarHeader)).setVisibility(View.VISIBLE);
				
				SharedPreferences prefsCategorias = getActivity().getApplicationContext()
						.getSharedPreferences("Categorias",Context.MODE_PRIVATE);
				int coma =0;
				categorias ="";
				//SOn 13 categorias
				for (int x=0; x<13; x++){
					if (!prefsCategorias.getString("Categories " + x, "Desactivada")
							.equals("Desactivada")
							& !prefsCategorias.getString("Categories " + x,
									"Desactivada").equals("")) {
						if(coma!=0){
							categorias += ","+prefsCategorias.getString("Categories "+x, null);	
						} else {
							categorias += prefsCategorias.getString("Categories "+x, null);	
						}
						coma++;		
					}
				}
			}
			protected InputStream doInBackground(String... params) {
				InputStream inputStream = null;
					if(getActivity()!=null){
					URL url = null;
					HttpURLConnection con = null;
					if(!isCancelled()){					
						try {
							url = new URL(params[0]);
							Log.d(null,params[0]);
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							con = (HttpURLConnection) url.openConnection();
							inputStream = con.getInputStream();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if(inputStream!=null){
//							readStream(inputStream);
							//								leerJson();
						}
					}
				}
				return inputStream;
			}
	
			@Override
			protected void onPostExecute(InputStream result) {
				super.onPostExecute(result);
				//listView_Eventos.removeHeaderView(headerView);
				//Quitamos todos los elementos ya que después los vamos a recuperar
				listaEventos.clear();
				arrayAdapterEvents.notifyDataSetChanged();
				((TextView)headerView.findViewById(R.id.textoHeaderListview)).setVisibility(View.GONE);
				((ProgressBar)headerView.findViewById(R.id.progressBarHeader)).setVisibility(View.GONE);
				headerAdded=false;
				if(getActivity()!=null){
					funcionesRefresh();
				}
			}
			
		}.execute("http://yapi.sferea.com/?latitud="+latOrigin+"" +
				"&longitud="+lonOrigin+"" +
				"&radio="+SplashActivity.distanciaEvento+"" +
				"&categoria="+categorias+"" +
				"&numEventos="+numeroEventos+"" +
				"&idEvento=0&fecha=0");
	}	

	//Consulta mongo
	public void addMoreEvents(final double lat, final double lon) {	

//		for (int i=0; i<listaEventos.size(); i++){
//			idevento = String.valueOf(listaEventos.get(i).get);
//			fechaUnix = String.valueOf(FechayHoraUnix[i]);
//			indexEvent = String.valueOf(IndexOfEvent[i]);
//		}
		SharedPreferences prefsCategorias = getActivity().getApplicationContext().getSharedPreferences("Categorias",Context.MODE_PRIVATE);
		int coma =0;
		categorias ="";
		//SOn 13 categorias
		for (int x = 0; x < 13; x++) {
			if (!prefsCategorias.getString("Categories " + x, "Desactivada")
					.equals("Desactivada")
					& !prefsCategorias.getString("Categories " + x,
							"Desactivada").equals("")) {
				if (coma != 0) {
					categorias += ","
							+ prefsCategorias
							.getString("Categories " + x, null);
				} else {
					categorias += prefsCategorias.getString("Categories " + x,
							null);
				}
				coma++;
			}
		}
		
		//Aqui tenemos que recuperar los eventos anteriores y juntarlos con los nuevos
		// para que en la lista el total de eventos sea correcto y no por partes
		titulo = new String[listaEventos.size()];
		categoriasSave = new String[listaEventos.size()];
		fechaSave = new String[listaEventos.size()];
		descripciones = new String[listaEventos.size()];
		fuentes = new String[listaEventos.size()];
		lugares = new String[listaEventos.size()];
		direcciones = new String[listaEventos.size()];
		telefonos = new String[listaEventos.size()];
		latitudes = new Double[listaEventos.size()];
		longitudes = new Double[listaEventos.size()];
		distancias = new String[listaEventos.size()];
		boletos = new String[listaEventos.size()];
		imagenEventoSave = new Bitmap[listaEventos.size()];
		imgCategorias = new int[listaEventos.size()];
		indexOfEventSave = new int[listaEventos.size()];
		fechaUnixSave = new int[listaEventos.size()];
		
		for (int j = 0; j < listaEventos.size(); j++) {			
			titulo[j] = listaEventos.get(j).getNombreEvento();
			Log.e("NuevoArray", titulo[j]);
			categoriasSave[j] = listaEventos.get(j).getCategoriaEvento();
			fechaSave[j] = listaEventos.get(j).getFechaEvento();
			descripciones[j] = listaEventos.get(j).getDescripcion();
			fuentes[j] = listaEventos.get(j).getFuente();
			lugares[j] = listaEventos.get(j).getLugarEvento();
			direcciones[j] = listaEventos.get(j).getDireccion();
			telefonos[j] = listaEventos.get(j).getTelefono();
			latitudes[j] = listaEventos.get(j).getLatEvento();
			longitudes[j] = listaEventos.get(j).getLonEvento();
			distancias[j] = listaEventos.get(j).getDistancia();
			boletos[j] = listaEventos.get(j).getBoleto();
			imagenEventoSave[j] = listaEventos.get(j).getImagenEvento();
			imgCategorias[j] = listaEventos.get(j).getImagenCategoria();
			indexOfEventSave[j] = listaEventos.get(j).getIndexOfEvent();
			fechaUnixSave[j] = listaEventos.get(j).getFechaUnix();
		}
		
		new AsyncTask<String, Void, Void>(){

			protected void onPreExecute() {
				((ProgressBar)footerView.findViewById(R.id.progressBarFooter)).setVisibility(View.VISIBLE);				
			};

			@Override
			protected Void doInBackground(String... params) {
				InputStream inputStream = null;
				if(getActivity()!=null){					
					URL url = null;
					HttpURLConnection con = null;
					if(!isCancelled()){					
						try {
							url = new URL(params[0]);
							Log.e(null,params[0]);
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							con = (HttpURLConnection) url.openConnection();
							inputStream = con.getInputStream();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if(inputStream!=null){
							readStreamAddMOre(inputStream);
						}
					}					
				}
				Log.d(null, "Ejecutandose Hilo add more");
				
				return null;				
			}

			@Override
			protected void onPostExecute(Void result) {
				this.cancel(true);
				//Quita el footer
				((ProgressBar)footerView.findViewById(R.id.progressBarFooter)).setVisibility(View.GONE);
				if(getActivity()!=null){
					listaEventos.clear();
					arrayAdapterEvents.clear();
					funcionesAddMore();
				}
			};

			@Override
			protected void onCancelled() {
				super.onCancelled();
			};

		}.execute("http://yapi.sferea.com/?latitud="+latOrigin+"&longitud="+lonOrigin+"" +
//				"&radio="+SplashActivity.distanciaEvento+"" +
				"&radio=10" +
				"&categoria="+categorias+"" +
//				"&numEventos="+numeroEventos+"" +
				"&numEventos=0" +
				"&idEvento="+indexEvent+"" +
				"&fecha="+fechaUnix+"");
	}


	private void readStream(InputStream in) {
		if(in!=null){
			BufferedReader reader = null;
			OutputStreamWriter outputStreamWriter = null;
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					if(getActivity()!=null){
						outputStreamWriter = new OutputStreamWriter(getActivity().getApplicationContext().openFileOutput("Json.txt", Context.MODE_PRIVATE));
						outputStreamWriter.write(line);	    					
						outputStreamWriter.flush();
						outputStreamWriter.close();						
					}
				}
				Log.d(null, "Json Creado!");
//				leerJson();
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}	    		
			}
		}
	}
	
	private void readStreamAddMOre(InputStream in) {
		if(in!=null){
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				while ((line = reader.readLine()) != null) {
					if(getActivity()!=null){
						jsonAddMore = line;
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}  finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}	    		
			}
		}
	}
	
	
	
	public void gps(){
		LocationManager locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		
		isGpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isWirelessActive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
//		//Verificamos que este el gps activado, si no entonces toma la ubicacion por red
//		if(isGpsActive){
//			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//			latOrigin = location.getLatitude();
//			lonOrigin = location.getLongitude();
//			Log.d("GPS", "Latitud "+latOrigin);
//			Log.d("GPS", "Longitud "+lonOrigin);
//		}else{
//			Toast.makeText(getApplicationContext(), "Yieppa funciona mejor cuando activas el GPS :)", Toast.LENGTH_LONG).show();
//			//Asumimos que esta prendido el wireless ya que si no lo fuera, no entraría a la app
//			if(isWirelessActive){
//				Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//				latOrigin = location.getLatitude();
//				lonOrigin = location.getLongitude();
//				Log.d("WIFI", "Latitud "+latOrigin);
//				Log.d("WIFI", "Longitud "+lonOrigin);
//			}
//			
////			finish();
//		}
//		if(isWirelessActive){
//			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//			latOrigin = location.getLatitude();
//			lonOrigin = location.getLongitude();
//			Log.d("WIFI", "Latitud "+latOrigin);
//			Log.d("WIFI", "Longitud "+lonOrigin);
//		}

		LocationListener locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				//				Log.d("GPS", "Latitud"+location.getLatitude());
				//				Log.d("GPS", "Longitud"+location.getLongitude());
				latOrigin = location.getLatitude();
				lonOrigin = location.getLongitude();
			}
		};
//		if(isGpsActive){
//			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);			
//		}else{
//			if(isWirelessActive){
//				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, locationListener);
//			}
//		}
		if(isWirelessActive){
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, locationListener);
		}
	}
	
	// convert InputStream to String
		private static String getStringFromInputStream(InputStream is) {
	 
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
	 
			String line;
			try {
	 
				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	 
			return sb.toString();
	 
		}
	
	static{
	     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();  
	     StrictMode.setThreadPolicy(policy);  
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}
}