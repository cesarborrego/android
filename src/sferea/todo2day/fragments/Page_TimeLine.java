package sferea.todo2day.fragments;

import java.util.ArrayList;

import sferea.todo2day.Application;
import sferea.todo2day.DetailActivity;
import sferea.todo2day.R;
import sferea.todo2day.SplashActivity;
import sferea.todo2day.adapters.ArrayAdapterEvents;
import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.helpers.CheckInternetConnection;
import sferea.todo2day.helpers.JsonHelper;
import sferea.todo2day.helpers.JsonParserHelper;
import sferea.todo2day.helpers.LocationHelper;
import sferea.todo2day.helpers.ReadTableDB;
import sferea.todo2day.helpers.SharedPreferencesHelperFinal;
import sferea.todo2day.listeners.UpdateableFragmentListener;
import sferea.todo2day.utils.ImageUtil;
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
		OnScrollListener, UpdateableFragmentListener {

	public static ArrayList<EventoObjeto> listaEventos;
	public static ArrayList<String> listaFavoritos;
	public static ArrayAdapterEvents arrayAdapterEvents;
	public static boolean eventsLoaded = false;
	
	LocationHelper locationHelper;
	
	ListView listView_Eventos;
	public static boolean refresh = false;
	boolean loadingMore = false;

	boolean headerAdded = false;
	float startY;
	public static String activaUbicate = "no";
	public static String activaRuta = "no";

	// Debemos obtener la lat y lon correcta desde el gps
	public static double latOrigin;
	public static double lonOrigin;

	SharedPreferences shrpref_fav;

	View view;
	View headerView;
	View footerView;
	View footerAddView;

	boolean isLoading = false;

	int iContador = 0;
	public static int numeroEventos = 0;

	public static boolean eventoActivo = false;
	public static boolean favoritoActivo = false;

	java.text.DateFormat formatoDelTexto;

	public static String fechaUnix = "";
	public static String indexEvent = "";

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

	public Page_TimeLine() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkInternetConnection = new CheckInternetConnection(getActivity()
				.getApplicationContext(), getActivity());
		jsonHelper = new JsonHelper(Application.getInstance());
		imageloader = ImageUtil.getImageLoader();
		options = ImageUtil.getOptionsImageLoader();
		
		locationHelper = new LocationHelper(getActivity().getApplicationContext());
		listaEventos = new ArrayList<EventoObjeto>();
		listaFavoritos = new ArrayList<String>();
	
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
		swipeLayout.setColorSchemeColors(Color.rgb(255, 140, 0));

		// Obtiene la vista del listView
		listView_Eventos = ((ListView) view.findViewById(R.id.listviewEventos));

		progressFooter = ((ProgressBar) footerView
				.findViewById(R.id.progressBarFooter));

		footerNoInternet = (TextView) footerView
				.findViewById(R.id.textoFooterNOInternet);

		footerNoInternetClic = (TextView) footerView
				.findViewById(R.id.textoFooterNOInternetClic);

		// Crea el arrayAdapter de eventos
		arrayAdapterEvents = new ArrayAdapterEvents(getActivity(), imageloader, options);

		// Agrega el header
		listView_Eventos.addHeaderView(headerView);

		// Agrega el footer
		listView_Eventos.addFooterView(footerView);

		// Asigna el arrayAdapter al listview
		
		listView_Eventos.setAdapter(arrayAdapterEvents);
		
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
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		reader = new ReadTableDB(Application.getInstance());
		
		listaEventos = reader.fillEventListFromDB();
		
		listaFavoritos = reader.fillFavoriteListFromDB();
		
		if(listaEventos != null)
		{
			arrayAdapterEvents.clear();
		
			arrayAdapterEvents.addAll(listaEventos);
			
			arrayAdapterEvents.notifyDataSetChanged();
		}
		
		numeroEventos = arrayAdapterEvents.getCount();
		super.onResume();
	}


	public void addMoreEvents(final double lat, final double lon) {

		progressFooter.setVisibility(View.VISIBLE);
			
			latOrigin = locationHelper.getLatitude();
			lonOrigin = locationHelper.getLongitude();

		footerNoInternet.setVisibility(View.GONE);
		footerNoInternetClic.setVisibility(View.GONE);

		SharedPreferencesHelperFinal sharedPreferencesHelper = new SharedPreferencesHelperFinal(
				getActivity().getApplicationContext());
		
		jsonParser = new JsonParserHelper(getActivity());

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
					if (reader.getEventsDBCount() == arrayAdapterEvents.getCount()) {
						progressFooter.setVisibility(View.GONE);
						((TextView) footerView.findViewById(R.id.textoFooter))
								.setVisibility(View.VISIBLE);
					} else {
						arrayAdapterEvents.clear();
						arrayAdapterEvents.addAll(reader.fillEventListFromDB());
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
				+ lonOrigin + "" + "&radio=" + SplashActivity.distanciaEvento + "&categoria="
				+ sharedPreferencesHelper.obtieneCategoriasPreferences() + ""
				+ "&numEventos=0" + "&idEvento=" + indexEvent + "" + "&fecha="
				+ fechaUnix + "");
	}

	public void refreshTimeLine() {
		SharedPreferencesHelperFinal sharedPreferencesHelper = new SharedPreferencesHelperFinal(
				getActivity().getApplicationContext());
		latOrigin = locationHelper.getLatitude();
		lonOrigin = locationHelper.getLongitude();
		
		new AsyncTask<String, Void, Void>() {

			ArrayList<EventoObjeto> lista = null;

			@Override
			protected void onPreExecute() {
				// TODO El dialog de cargar
			}

			protected Void doInBackground(String... params) {
				String result = jsonHelper.connectionMongo_Json(params[0]);
				jsonParser = new JsonParserHelper(Application.getInstance());
				jsonParser.addEventsToDB(result);
				reader = new ReadTableDB(Application.getInstance());
				lista = reader.fillEventListFromDB();

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				arrayAdapterEvents.clear();
				arrayAdapterEvents.addAll(lista);
				arrayAdapterEvents.notifyDataSetChanged();
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

	@Override
	public void onUpdated() {
		reader = new ReadTableDB(Application.getInstance());
		listaFavoritos = reader.fillFavoriteListFromDB();
		arrayAdapterEvents.notifyDataSetChanged();
	}

}
