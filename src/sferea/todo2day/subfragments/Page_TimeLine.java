package sferea.todo2day.subfragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.internal.ff;

import sferea.todo2day.R;
import sferea.todo2day.SplashActivity;
import sferea.todo2day.Helpers.CheckInternetConnection;
import sferea.todo2day.Helpers.ImageUtil;
import sferea.todo2day.Helpers.JsonHelper;
import sferea.todo2day.Helpers.ParseJson_AddDB;
import sferea.todo2day.Helpers.ReadTableDB;
import sferea.todo2day.Helpers.SharedPreferencesHelperFinal;
import sferea.todo2day.adapters.ArrayAdapterEvents;
import sferea.todo2day.adapters.EventoObjeto;
import sferea.todo2day.config.LocationHelper;
import sferea.todo2day.tasks.AddMoreEventsTask;
import sferea.todo2day.tasks.TaskListener;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Page_TimeLine extends Fragment implements TaskListener{

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
	AddMoreEventsTask task;

	// bandera tomara el valor que retorna @parseFirstJson_AddDB(json) para que
	// dependiendo de su resultado activara
	// las funciones en el hilo principal UI, ya sea mandar un toast avisando
	// que trono o si todo sale bien, llenar la lista de eventos
	boolean bandera = false;

	float y1;
	float y2;

	CheckInternetConnection checkInternetConnection;

	ProgressBar progressFooter;
	TextView footerNoInternet;

	public Page_TimeLine() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkInternetConnection = new CheckInternetConnection(getActivity()
				.getApplicationContext(), getActivity());
		jsonHelper = new JsonHelper(getActivity().getApplicationContext(),
				getActivity());
		if (SplashActivity.leeJSONCache) {
		} else {
			if (jsonHelper.leerPrimerJson() != null) {
				json = jsonHelper.leerPrimerJson();
				guardaPrimerJsonDB();
			}
		}
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
		

		//		footerAddView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer_noevents, null);

		// Crea un nuevo arraylist de eventos
		listaEventos = new ArrayList<EventoObjeto>();
		// Obtiene la vista del listView
		listView_Eventos = ((ListView) view.findViewById(R.id.listviewEventos));

		progressFooter = ((ProgressBar) footerView.findViewById(R.id.progressBarFooter));
		
		footerNoInternet = (TextView) footerView.findViewById(R.id.textoFooterNOInternet);

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

		listView_Eventos
		.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view,
					int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view,
					int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				Log.d(null, "firsVisibleItem=" + firstVisibleItem
						+ "\n+visibleItemCount=" + visibleItemCount
						+ "\ntotalItemCount" + totalItemCount
						+ "\nloadedItems"
						+ (firstVisibleItem + visibleItemCount));
				Page_TimeLine.this.totalItemCount = totalItemCount;
				if (((firstVisibleItem + visibleItemCount) == totalItemCount)
						& listView_Eventos.getFirstVisiblePosition() > 0) {
					if (listView_Eventos.getLastVisiblePosition() == listView_Eventos.getAdapter().getCount() - 1
							&& listView_Eventos.getChildAt(listView_Eventos.getChildCount() - 1).getBottom() <= listView_Eventos.getHeight()) {
						Log.d(null, "Final");
						if (checkInternetConnection.isConnectedToInternet()) {
							addMoreEvents(latOrigin, lonOrigin);
						} else {
							footerNoInternet.setVisibility(View.VISIBLE);
							footerNoInternet.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									if(checkInternetConnection.isConnectedToInternet()){
										footerNoInternet.setVisibility(View.GONE);
										addMoreEvents(latOrigin, lonOrigin);
									}
								}
							});
						}
					}
				}
			}
		});

		listView_Eventos.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float y = event.getY();

				switch (event.getAction()) {

				case MotionEvent.ACTION_MOVE: {
					if (!downCounterUsed) {
						startY = y;
						downCounterUsed = true;
					}

					y1 = event.getY();

					allowRefresh = (listView_Eventos.getFirstVisiblePosition() == 0);
					Log.d("FirstVisible", String.valueOf(listView_Eventos
							.getFirstVisiblePosition()));

					if (allowRefresh) {
						if ((y - startY) > REFRESH_THRESHOLD) {
							refresh = true;
							ImageUtil.getImageLoader().clearDiskCache();
							if (!headerAdded) {
								// listView_Eventos.addHeaderView(headerView);
								((TextView) headerView
										.findViewById(R.id.textoHeaderListview))
										.setVisibility(View.VISIBLE);
								headerAdded = true;
							}
						} else {
							refresh = false;
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

					Log.d("FirstVisiblePosition",
							String.valueOf(listView_Eventos
									.getFirstVisiblePosition()));
					if (refresh) {
						refreshTimeLine();
					} else {
						((TextView) headerView.findViewById(R.id.textoHeaderListview)).setVisibility(View.GONE);
						((ProgressBar) headerView.findViewById(R.id.progressBarHeader)).setVisibility(View.GONE);
					}
					downCounterUsed = false;
					refresh = false;
					break;
				}

				case MotionEvent.ACTION_DOWN: {
					y1 = event.getY();
					Log.i("DOWN", "Y1 = " + y1 + " Y2 = " + y2);
					if(checkInternetConnection.isConnectedToInternet()){
						if(listView_Eventos.getFirstVisiblePosition() == 0){
							((TextView) footerView.findViewById(R.id.textoFooter)).setVisibility(View.VISIBLE);
						}
					}else{
						((TextView) footerView.findViewById(R.id.textoFooterNOInternet)).setVisibility(View.VISIBLE);
					}
					break;
				}
				}
				return false;
			}
		});
		return view;
	}

	public void guardaPrimerJsonDB() {
		new AsyncTask<Void, Void, Void>() {
			ParseJson_AddDB parseJson_AddDB;

			protected void onPreExecute() {
				if (getActivity() != null) {
					if (!isCancelled()) {
						parseJson_AddDB = new ParseJson_AddDB(getActivity()
								.getApplicationContext(), getActivity());
					}
				}
			};

			@Override
			protected Void doInBackground(Void... arg0) {
				if (!isCancelled()) {
					bandera = parseJson_AddDB.parseFirstJson_AddDB(json);
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				if (bandera) {
					// Toast.makeText(getActivity().getApplicationContext(),"Listo verifica la DB Events!",
					// Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(
							getActivity().getApplicationContext(),
							"No hay Eventos Disponibles\n"
									+ "Prueba con otras categorías!\n"
									+ "Y/o Aumenta el Radio de búsqueda en los ajustes",
									Toast.LENGTH_LONG).show();
				}
			};
		}.execute();
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
				((TextView) headerView.findViewById(R.id.textoHeaderListview))
				.setVisibility(View.GONE);
				((ProgressBar) headerView.findViewById(R.id.progressBarHeader))
				.setVisibility(View.VISIBLE);
			}

			protected Void doInBackground(String... params) {
				jsonHelper.connectionMongo_JsonFake(params[0]);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				((TextView) headerView.findViewById(R.id.textoHeaderListview))
				.setVisibility(View.GONE);
				((ProgressBar) headerView.findViewById(R.id.progressBarHeader))
				.setVisibility(View.GONE);
				headerAdded = false;
			}

		}.execute("http://yapi.sferea.com/?latitud=" + latOrigin + ""
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
		task.cancel(true);
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
				Toast.makeText(
						getActivity().getApplicationContext(),
						"No hay Eventos Disponibles\n"
								+ "Prueba con otras categorías!\n"
								+ "Y/o Aumenta el Radio de búsqueda en los ajustes",
								Toast.LENGTH_LONG).show();
			}
			progressFooter.setVisibility(View.GONE);
			task = null;
		}
	}
}
