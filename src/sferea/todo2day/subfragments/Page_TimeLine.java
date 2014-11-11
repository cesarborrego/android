package sferea.todo2day.subfragments;

import java.util.ArrayList;
import java.util.Date;

import sferea.todo2day.DetailActivity;
import sferea.todo2day.R;
import sferea.todo2day.SplashActivity;
import sferea.todo2day.Helpers.CheckInternetConnection;
import sferea.todo2day.Helpers.ImageUtil;
import sferea.todo2day.Helpers.JsonHelper;
import sferea.todo2day.Helpers.ReadTableDB;
import sferea.todo2day.Helpers.SharedPreferencesHelperFinal;
import sferea.todo2day.adapters.ArrayAdapterEvents;
import sferea.todo2day.adapters.EventoObjeto;
import sferea.todo2day.config.LocationHelper;
import sferea.todo2day.tasks.AddMoreEventsTask;
import sferea.todo2day.tasks.AddMoreTaskListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Page_TimeLine extends Fragment implements AddMoreTaskListener, OnTouchListener, OnScrollListener, OnRefreshListener{

	public static final float REFRESH_THRESHOLD = 50;
	public static ArrayList<EventoObjeto> listaEventos;
	EventoObjeto eo;
	public static ArrayAdapterEvents arrayAdapterEvents;
	ListView listView_Eventos;
	boolean allowRefresh = false;
	boolean refresh = false;
	boolean downCounterUsed = false;

	boolean headerAdded = false;
	float startY;
	String imagenesEventos[] = null;
	public static String activaUbicate = "no";
	public static String activaRuta = "no";

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
	public static String peques = "F";
	public static String gastronomia = "G";
	public static String musica = "H";
	public static String salud = "I";
	public static String sociales = "J";
	public static String tecnologia = "K";
	public static String verde = "L";

	// Debemos obtener la lat y lon correcta desde el gps
	public static double latOrigin = 19.355582;
	public static double lonOrigin = -99.186726;

	// public static double latOrigin;
	// public static double lonOrigin;
	String Titulos[];
	String EventoID[];
	String Categorias[];

	String Fechas[];
	String[] strFecha;
	Date fecha[];
	String formatedDate[];
	String fechaFinal[];

	Long FechayHoraUnix[];
	Long IndexOfEvent[];

	String Descripciones[];
	String Boletos[];
	String Fuentes[];
	String Lugares[];
	String Direcciones[];
	String Telefonos[];
	String Distancias[];
	double Longitudes[];
	double Latitudes[];
	static String ImagenEvento[];

	Bitmap imagenesCargadas[];

	Bitmap img[];

	int imagenesCategorias[];

	SharedPreferences shrpref_fav;

	View view;
	View headerView;
	View footerView;
	View footerAddView;

	boolean isLoading = false;

	LocationHelper locationHelper;
	boolean isScrollActive = true;

	int iContador = 0;
	String catt = "";
	public static int numeroEventos = 0;

	public static boolean eventoActivo = false;
	public static boolean favoritoActivo = false;

	java.text.DateFormat formatoDelTexto;

	String json;
	String jsonAddMore;
	String jsonCache;

	String categorias = "";
	String idevento = "";
	public static String fechaUnix = "";
	public static String indexEvent = "";

	// Esta variable se usa por si no llega a descargar eventos no pasa al hilo
	// de descargar imagenes
	boolean descargaImagenesFirstAccess = false;

	boolean isGpsActive = false;
	boolean isWirelessActive = false;

	int totalItemCount = 0;

	// Aqui tenemos que recuperar los eventos anteriores y juntarlos con los
	// nuevos
	// para que en la lista el total de eventos sea correcto y no por partes
	String[] titulo;
	String[] categoriasSave;
	String[] fechaSave;
	String[] descripciones;
	String[] fuentes;
	String[] lugares;
	String[] direcciones;
	String[] telefonos;
	Double[] latitudes;
	Double[] longitudes;
	String[] distancias;
	String[] boletos;
	Bitmap[] imagenEventoSave;
	int[] imgCategorias;
	int[] indexOfEventSave;
	int[] fechaUnixSave;
	String urlImagenes[];

	JsonHelper jsonHelper;
	
	SwipeRefreshLayout swipeLayout;

	// bandera tomara el valor que retorna @parseFirstJson_AddDB(json) para que
	// dependiendo de su resultado activara
	// las funciones en el hilo principal UI, ya sea mandar un toast avisando
	// que trono o si todo sale bien, llenar la lista de eventos
	boolean bandera = false;

	float y1;
	float y2;

	CheckInternetConnection checkInternetConnection;

	ProgressBar progressFooter;
	TextView footerNoInternet, footerNoInternetClic;
	
	AddMoreEventsTask task;

	public Page_TimeLine() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkInternetConnection = new CheckInternetConnection(getActivity()
				.getApplicationContext(), getActivity());
		jsonHelper = new JsonHelper(getActivity().getApplicationContext(),
				getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// infla el view del timeline
		view = inflater.inflate(R.layout.page_timeline, container, false);
		// Infla el view del header
		headerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.listview_header, null);
		// Infla el view del footer
		footerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.listview_footer, null);
		
		 
	    swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
	    swipeLayout.setOnRefreshListener(this);
	    swipeLayout.setColorScheme(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
		// Crea un nuevo arraylist de eventos
		listaEventos = new ArrayList<EventoObjeto>();
		// Obtiene la vista del listView
		listView_Eventos = ((ListView) view.findViewById(R.id.listviewEventos));

		progressFooter = ((ProgressBar) footerView.findViewById(R.id.progressBarFooter));
		
		footerNoInternet = (TextView) footerView.findViewById(R.id.textoFooterNOInternet);
		
		footerNoInternetClic = (TextView) footerView.findViewById(R.id.textoFooterNOInternetClic);

		readTableEvents_fillListEvent();
		
		if (SplashActivity.leeJSONCache) {
			refreshTimeLine();
		}

		// Crea el arrayAdapter de eventos
		arrayAdapterEvents = new ArrayAdapterEvents(getActivity(),R.layout.row_event_responsive, R.id.listviewEventos,listaEventos);

		// Agrega el header
		listView_Eventos.addHeaderView(headerView);

		// Agrega el footer
		listView_Eventos.addFooterView(footerView);

		// Asigna el arrayAdapter al listview
		listView_Eventos.setAdapter(arrayAdapterEvents);
		
		listView_Eventos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				EventoObjeto evento = (EventoObjeto)parent.getItemAtPosition(position);
				
				Bundle bundle = new Bundle();
				bundle.putParcelable("Event", evento);
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
				eventoActivo = true;
				favoritoActivo = false;
				
			}
		});

		listView_Eventos.setOnScrollListener(this);
		listView_Eventos.setOnTouchListener(this);
		
		return view;
	}

	public void readTableEvents_fillListEvent() {
		new AsyncTask<Void, Void, Void>() {
			ReadTableDB readTableDB;

			protected void onPreExecute() {
				if(getActivity().getApplicationContext()!=null){
					readTableDB = new ReadTableDB(getActivity().getApplicationContext());
				}

			};

			@Override
			protected Void doInBackground(Void... arg0) {
				return null;
			}

			protected void onPostExecute(Void result) {
				readTableDB.readTable_FillList();
				progressFooter.setVisibility(View.GONE);
			};

		}.execute();
	}

	public void addMoreEvents(final double lat, final double lon) {
		
		if(task == null){

			progressFooter.setVisibility(View.VISIBLE);
			
			footerNoInternet.setVisibility(View.GONE);
			footerNoInternetClic.setVisibility(View.GONE);
			
			SharedPreferencesHelperFinal sharedPreferencesHelper = new SharedPreferencesHelperFinal(
					getActivity().getApplicationContext());

			task = new AddMoreEventsTask(getActivity(), this);
			task.execute("http://yapi.sferea.com/?latitud=" + latOrigin
					+ "&longitud=" + lonOrigin + "" + "&radio=10" + "&categoria="
					+ sharedPreferencesHelper.obtieneCategoriasPreferences() + ""
					+ "&numEventos=0" + "&idEvento=" + indexEvent + "" + "&fecha="
					+ fechaUnix + "");
		}
	}

	public void refreshTimeLine() {
		SharedPreferencesHelperFinal sharedPreferencesHelper = new SharedPreferencesHelperFinal(
				getActivity().getApplicationContext());
		new AsyncTask<String, Void, Void>() {

			@Override
			protected void onPreExecute() {
				// TODO El dialog de cargar
				super.onPreExecute();
				
			}

			protected Void doInBackground(String... params) {
				jsonHelper.connectionMongo_Json(params[0]);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				ReadTableDB readTableDB = new ReadTableDB(getActivity().getApplicationContext());
				readTableDB.readTable_FillList();
				arrayAdapterEvents.notifyDataSetChanged();
				swipeLayout.setRefreshing(false);
			}

		}.execute("http://yapidev.sferea.com/?latitud=" + latOrigin + ""
				+ "&longitud=" + lonOrigin + "" + "&radio="
				+ SplashActivity.distanciaEvento + "" + "&categoria="
				+ sharedPreferencesHelper.obtieneCategoriasPreferences() + ""
				+ "&numEventos=" + numeroEventos + "" + "&idEvento=0&fecha=0");
	}

	static {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}

	public void onTaskCompleted(Object result){	
		
		if(getActivity()!=null){
			ReadTableDB readTableDB = new ReadTableDB(getActivity().getApplicationContext());
			if ((Boolean)result) {
				
				if(readTableDB.readTable()==arrayAdapterEvents.getCount()){
					((TextView) footerView.findViewById(R.id.textoFooter)).setVisibility(View.VISIBLE);
					progressFooter.setVisibility(View.GONE);
				}
				
				arrayAdapterEvents.clear();
				readTableDB.readTable_FillList();
				arrayAdapterEvents.notifyDataSetChanged();
			} else {
//				Toast.makeText(
//						getActivity().getApplicationContext(),
//						"No hay Eventos Disponibles\n"
//								+ "Prueba con otras categor���as!\n"
//								+ "Y/o Aumenta el Radio de b���squeda en los ajustes",
//								Toast.LENGTH_LONG).show();
			}
			progressFooter.setVisibility(View.GONE);
			task = null;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		Page_TimeLine.this.totalItemCount = totalItemCount;
		if (((firstVisibleItem + visibleItemCount) == totalItemCount)
				& listView_Eventos.getFirstVisiblePosition() > 0) {
			if (listView_Eventos.getLastVisiblePosition() == listView_Eventos.getAdapter().getCount() - 1
					&& listView_Eventos.getChildAt(listView_Eventos.getChildCount() - 1).getBottom() <= listView_Eventos.getHeight()) {
				Log.d(null, "Final");
				if (checkInternetConnection.isConnectedToInternet()) {
					addMoreEvents(latOrigin, lonOrigin);
					footerNoInternet.setVisibility(View.GONE);
					footerNoInternetClic.setVisibility(View.GONE);
					((TextView) footerView.findViewById(R.id.textoFooter)).setVisibility(View.GONE);
				} else {
					footerNoInternet.setVisibility(View.GONE);
					footerNoInternetClic.setVisibility(View.VISIBLE);
					((TextView) footerView.findViewById(R.id.textoFooter)).setVisibility(View.GONE);
					footerNoInternetClic.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(checkInternetConnection.isConnectedToInternet()){
								footerNoInternetClic.setVisibility(View.GONE);
								addMoreEvents(latOrigin, lonOrigin);
							}
						}
					});
				}
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		float y = event.getY();

		switch (event.getAction()) {

			case MotionEvent.ACTION_MOVE: {
				
				if(y<startY){
					if (checkInternetConnection.isConnectedToInternet()){
						if(listView_Eventos.getFirstVisiblePosition() == 0){
							((TextView) footerView.findViewById(R.id.textoFooter)).setVisibility(View.VISIBLE);
						}
						
					} else {
						if(listView_Eventos.getFirstVisiblePosition() == 0){
							footerNoInternet.setVisibility(View.VISIBLE);
							footerNoInternetClic.setVisibility(View.GONE);
							((TextView) footerView.findViewById(R.id.textoFooter)).setVisibility(View.GONE);
						}else{
							footerNoInternet.setVisibility(View.GONE);
							footerNoInternetClic.setVisibility(View.VISIBLE);
							((TextView) footerView.findViewById(R.id.textoFooter)).setVisibility(View.GONE);
						}
						
					}
				}
				break;
			}
	
			case MotionEvent.ACTION_UP: {
				y2 = event.getY();
				//
				if (startY > y1) {
					View v1 = listView_Eventos.getChildAt(listView_Eventos.getAdapter().getCount() - 1);
	
					if (v1 != null) {
						if (v1.getBottom() <= listView_Eventos.getHeight()) {
							if (checkInternetConnection.isConnectedToInternet()){
								((TextView) footerView.findViewById(R.id.textoFooter)).setVisibility(View.GONE);
								
							} else {
								((TextView) footerView.findViewById(R.id.textoFooterNOInternet)).setVisibility(View.GONE);
							}							
						}
					}
				}
	
				Log.d("FirstVisiblePosition",String.valueOf(listView_Eventos.getFirstVisiblePosition()));
				
				break;
			}
	
			case MotionEvent.ACTION_DOWN: {
				y1 = event.getY();
				Log.i("DOWN", "Y1 = " + y1 + " Y2 = " + y2);
				
				break;
			}
		}
		return false;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		refreshTimeLine();
	}

}
