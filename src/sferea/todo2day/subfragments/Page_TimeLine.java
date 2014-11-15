package sferea.todo2day.subfragments;

import java.util.ArrayList;

import sferea.todo2day.Application;
import sferea.todo2day.DetailActivity;
import sferea.todo2day.R;
import sferea.todo2day.SplashActivity;
import sferea.todo2day.Helpers.CheckInternetConnection;
import sferea.todo2day.Helpers.ImageUtil;
import sferea.todo2day.Helpers.JsonHelper;
import sferea.todo2day.Helpers.JsonParserHelper;
import sferea.todo2day.Helpers.ReadTableDB;
import sferea.todo2day.Helpers.SharedPreferencesHelperFinal;
import sferea.todo2day.adapters.ArrayAdapterEvents;
import sferea.todo2day.adapters.EventoObjeto;
import sferea.todo2day.config.LocationHelper;
import sferea.todo2day.tasks.AddMoreEventsTask;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class Page_TimeLine extends Fragment implements OnTouchListener,
		OnScrollListener {

	public static ArrayList<EventoObjeto> listaEventos;
	public static ArrayList<String> listaFavoritos;
	public static ArrayAdapterEvents arrayAdapterEvents;
	public static boolean eventsLoaded = false;
	ListView listView_Eventos;
	public static boolean refresh = false;
	boolean loadingMore = false;

	boolean headerAdded = false;
	float startY;
	public static String activaUbicate = "no";
	public static String activaRuta = "no";

	// Debemos obtener la lat y lon correcta desde el gps
	public static double latOrigin = 19.355582;
	public static double lonOrigin = -99.186726;

	SharedPreferences shrpref_fav;

	View view;
	View headerView;
	View footerView;
	View footerAddView;

	boolean isLoading = false;

	LocationHelper locationHelper;

	int iContador = 0;
	public static int numeroEventos = 0;

	public static boolean eventoActivo = false;
	public static boolean favoritoActivo = false;

	java.text.DateFormat formatoDelTexto;

	public static String fechaUnix = "";
	public static String indexEvent = "";

	boolean isGpsActive = false;
	boolean isWirelessActive = false;

	JsonHelper jsonHelper;
	JsonParserHelper jsonParser;
	ReadTableDB reader;
	ImageLoader imageloader;
	DisplayImageOptions options;
	SwipeRefreshLayout swipeLayout;

	// bandera tomara el valor que retorna @parseFirstJson_AddDB(json) para que
	// dependiendo de su resultado activara
	// las funciones en el hilo principal UI, ya sea mandar un toast avisando
	// que trono o si todo sale bien, llenar la lista de eventos
	boolean bandera = false;
	int currentFirstVisibleItem;
	int currentVisibleItemCount;
	int totalItemCount;
	int currentScrollState;

	float y1;

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
		jsonHelper = new JsonHelper(Application.getInstance());
		jsonParser = new JsonParserHelper(Application.getInstance());
		imageloader = ImageUtil.getImageLoader();
		options = ImageUtil.getOptionsImageLoader();
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

		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				refresh = true;
				refreshTimeLine();
				// TODO Auto-generated method stub

			}
		});
		swipeLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.CYAN);

		listaEventos = new ArrayList<EventoObjeto>();
		listaFavoritos = new ArrayList<String>();

		ReadTableDB reader = new ReadTableDB(Application.getInstance());

		listaEventos = reader.fillEventListFromDB();
		listaFavoritos = reader.fillFavoriteListFromDB();

		// Obtiene la vista del listView
		listView_Eventos = ((ListView) view.findViewById(R.id.listviewEventos));

		progressFooter = ((ProgressBar) footerView
				.findViewById(R.id.progressBarFooter));

		footerNoInternet = (TextView) footerView
				.findViewById(R.id.textoFooterNOInternet);

		footerNoInternetClic = (TextView) footerView
				.findViewById(R.id.textoFooterNOInternetClic);

		// Crea el arrayAdapter de eventos
		arrayAdapterEvents = new ArrayAdapterEvents(getActivity(),
				R.layout.row_event_responsive, R.id.listviewEventos,
				listaEventos, imageloader, options);

		// Agrega el header
		listView_Eventos.addHeaderView(headerView);

		// Agrega el footer
		listView_Eventos.addFooterView(footerView);

		// Asigna el arrayAdapter al listview
		listView_Eventos.setAdapter(arrayAdapterEvents);

		numeroEventos = arrayAdapterEvents.getCount();

		PauseOnScrollListener listener = new PauseOnScrollListener(imageloader,
				true, true);

		listView_Eventos.setOnScrollListener(listener);
		listView_Eventos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				EventoObjeto evento = (EventoObjeto) parent
						.getItemAtPosition(position);

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

	public void readTableEvents_fillListFavorites() {
		new AsyncTask<Void, Void, ArrayList<EventoObjeto>>() {

			@Override
			protected void onPreExecute() {
				// TODO El dialog de cargar
			}

			protected ArrayList<EventoObjeto> doInBackground(Void... params) {
				ReadTableDB reader = new ReadTableDB(Application.getInstance());
				// Crea un nuevo arraylist de eventos

				listaFavoritos = reader.fillFavoriteListFromDB();
				return reader.fillEventListFromDB();
			}

			@Override
			protected void onPostExecute(ArrayList<EventoObjeto> result) {
				super.onPostExecute(result);
				arrayAdapterEvents.clear();
				listaEventos.clear();
				listaEventos.addAll(result);
				arrayAdapterEvents.notifyDataSetChanged();
				numeroEventos = arrayAdapterEvents.getCount();
			}

		}.execute();
	}

	public void addMoreEvents(final double lat, final double lon) {

		progressFooter.setVisibility(View.VISIBLE);

		footerNoInternet.setVisibility(View.GONE);
		footerNoInternetClic.setVisibility(View.GONE);

		SharedPreferencesHelperFinal sharedPreferencesHelper = new SharedPreferencesHelperFinal(
				getActivity().getApplicationContext());

		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				boolean result = false;
				JsonHelper helper = new JsonHelper(Application.getInstance());
				String json = helper.connectionMongo_Json(params[0]);

				if (json == "")
					return result;

				result = jsonParser.addEventsToDB(json);

				return result;
			}

			@Override
			protected void onPostExecute(Boolean params) {

				if (params) {
					reader = new ReadTableDB(getActivity()
							.getApplicationContext());
					if (reader.readTable() == arrayAdapterEvents.getCount()) {
						progressFooter.setVisibility(View.GONE);
						((TextView) footerView.findViewById(R.id.textoFooter))
								.setVisibility(View.VISIBLE);
					} else {
						listaEventos.clear();
						listaEventos.addAll(reader.fillEventListFromDB());
						arrayAdapterEvents.notifyDataSetChanged();
						
						numeroEventos = arrayAdapterEvents.getCount();
					}

				} else {
					progressFooter.setVisibility(View.GONE);
					((TextView) footerView.findViewById(R.id.textoFooter))
							.setVisibility(View.VISIBLE);
				}
				
				loadingMore = false;

			}
		}.execute("http://yapi.sferea.com/?latitud=" + latOrigin + "&longitud="
				+ lonOrigin + "" + "&radio=10" + "&categoria="
				+ sharedPreferencesHelper.obtieneCategoriasPreferences() + ""
				+ "&numEventos=0" + "&idEvento=" + indexEvent + "" + "&fecha="
				+ fechaUnix + "");
	}

	public void refreshTimeLine() {
		SharedPreferencesHelperFinal sharedPreferencesHelper = new SharedPreferencesHelperFinal(
				getActivity().getApplicationContext());
		new AsyncTask<String, Void, Void>() {

			ArrayList<EventoObjeto> lista = null;

			@Override
			protected void onPreExecute() {
				// TODO El dialog de cargar
			}

			protected Void doInBackground(String... params) {
				String result = jsonHelper.connectionMongo_Json(params[0]);
				jsonParser.addEventsToDB(result);
				reader = new ReadTableDB(Application.getInstance());
				lista = reader.fillEventListFromDB();

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				listaEventos.clear();
				arrayAdapterEvents.clear();
				listaEventos.addAll(lista);
				listView_Eventos.setAdapter(arrayAdapterEvents);
				refresh = false;
				swipeLayout.setRefreshing(false);
				numeroEventos = arrayAdapterEvents.getCount();
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
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.currentScrollState = scrollState;
		this.isScrollCompleted();

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		this.totalItemCount = totalItemCount;
		this.currentFirstVisibleItem = firstVisibleItem;
		this.currentVisibleItemCount = visibleItemCount;

	}

	private void isScrollCompleted() {
		if (this.currentScrollState == SCROLL_STATE_IDLE
				&& ((this.currentFirstVisibleItem + this.currentVisibleItemCount) == this.totalItemCount)
				&& this.totalItemCount != 0) {

			Log.d(null, "Final");
			if (checkInternetConnection.isConnectedToInternet()) {
				if (!loadingMore) {
					loadingMore = true;
					addMoreEvents(latOrigin, lonOrigin);
					footerNoInternet.setVisibility(View.GONE);
					footerNoInternetClic.setVisibility(View.GONE);
					((TextView) footerView.findViewById(R.id.textoFooter))
							.setVisibility(View.GONE);
				}
			} else {
				footerNoInternet.setVisibility(View.GONE);
				footerNoInternetClic.setVisibility(View.VISIBLE);
				((TextView) footerView.findViewById(R.id.textoFooter))
						.setVisibility(View.GONE);
				footerNoInternetClic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (checkInternetConnection.isConnectedToInternet()) {
							footerNoInternetClic.setVisibility(View.GONE);
							addMoreEvents(latOrigin, lonOrigin);
						}
					}
				});
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		float y = event.getY();

		switch (event.getAction()) {

		case MotionEvent.ACTION_MOVE: {

			if (y < startY) {
				if (checkInternetConnection.isConnectedToInternet()) {
					if (listView_Eventos.getFirstVisiblePosition() == 0) {
						((TextView) footerView.findViewById(R.id.textoFooter))
								.setVisibility(View.VISIBLE);
					}

				} else {
					if (listView_Eventos.getFirstVisiblePosition() == 0) {
						footerNoInternet.setVisibility(View.VISIBLE);
						footerNoInternetClic.setVisibility(View.GONE);
						((TextView) footerView.findViewById(R.id.textoFooter))
								.setVisibility(View.GONE);
					} else {
						footerNoInternet.setVisibility(View.GONE);
						footerNoInternetClic.setVisibility(View.VISIBLE);
						((TextView) footerView.findViewById(R.id.textoFooter))
								.setVisibility(View.GONE);
					}

				}
			}
			break;
		}

		case MotionEvent.ACTION_UP: {
			//
			if (startY > y1) {
				View v1 = listView_Eventos.getChildAt(listView_Eventos
						.getAdapter().getCount() - 1);

				if (v1 != null) {
					if (v1.getBottom() <= listView_Eventos.getHeight()) {
						if (checkInternetConnection.isConnectedToInternet()) {
							((TextView) footerView
									.findViewById(R.id.textoFooter))
									.setVisibility(View.GONE);

						} else {
							((TextView) footerView
									.findViewById(R.id.textoFooterNOInternet))
									.setVisibility(View.GONE);
						}
					}
				}
			}

			Log.d("FirstVisiblePosition",
					String.valueOf(listView_Eventos.getFirstVisiblePosition()));

			break;
		}

		case MotionEvent.ACTION_DOWN: {
			y1 = event.getY();

			break;
		}
		}
		return false;
	}

}
