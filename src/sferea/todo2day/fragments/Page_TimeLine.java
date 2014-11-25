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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class Page_TimeLine extends Fragment implements OnTouchListener,
		OnScrollListener, UpdateableFragmentListener {

	private static ArrayList<EventoObjeto> listaEventos;
	public static ArrayList<String> listaFavoritos;
	private static ArrayAdapterEvents arrayAdapterEvents;
	public static boolean eventsLoaded = false;
	private static final String SHARED_PREFS_NAME = "YIEPPA_PREFERENCES";
	private SharedPreferences preferences;
	private AddMoreEventsTask addMoreTask;
	private RefreshTimeLineTask refreshTask;
	private boolean loadingMore = false;
	private LocationHelper locationHelper;
	private ListView listView_Eventos;
	public static boolean isRefreshEvent = false;
	private float startY;
	public static String activaUbicate = "no";
	public static String activaRuta = "no";

	public static double latOrigin;
	public static double lonOrigin;

	private View view;
	private View headerView;
	private View footerView;

	private boolean isLoading = true;

	int iContador = 0;
	public static int numeroEventos = 0;

	public static boolean eventoActivo = false;
	public static boolean favoritoActivo = false;

	public static String fechaUnix = "";
	public static String indexEvent = "";

	private JsonHelper jsonHelper;
	private JsonParserHelper jsonParser;
	private ReadTableDB reader;
	private ImageLoader imageloader;
	private DisplayImageOptions options;
	private SwipeRefreshLayout swipeLayout;
	private int currentFirstVisibleItem;
	private int currentVisibleItemCount;
	private int totalItemCount;
	private int currentScrollState;
	private float y1;

	private CheckInternetConnection checkInternetConnection;
	private ProgressBar progressFooter;
	private TextView footerNoInternet, footerNoInternetClic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkInternetConnection = new CheckInternetConnection(getActivity());
		jsonHelper = new JsonHelper(getActivity());
		imageloader = ImageUtil.getImageLoader();
		options = ImageUtil.getOptionsImageLoader();
		
		locationHelper = new LocationHelper(getActivity());
		listaEventos = new ArrayList<EventoObjeto>();
		listaFavoritos = new ArrayList<String>();
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.page_timeline, container, false);

		headerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.listview_header, null);
		footerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.listview_footer, null);

		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				
				refreshTimeLine();

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
		
		listaEventos = getListaEventosFromDB();
		
		listaFavoritos = fillFavoritosFromDB();
		
		if(listaEventos != null)
		{
			fillEventsAdapter(listaEventos);
		}

		super.onResume();
	}
	
	@Override
	public void onPause() {
		if(addMoreTask != null && addMoreTask.getStatus() == AsyncTask.Status.RUNNING){
			addMoreTask.cancel(true);
		}
		
		if(refreshTask != null && refreshTask.getStatus() == AsyncTask.Status.RUNNING){
			refreshTask.cancel(true);
		}
		
		super.onPause();
	};

	public void addMoreEvents(final double lat, final double lon) {

		progressFooter.setVisibility(View.VISIBLE);
		
		locationHelper.getLocation();
			
		latOrigin = locationHelper.getLatitude();
		lonOrigin = locationHelper.getLongitude();

		footerNoInternet.setVisibility(View.GONE);
		footerNoInternetClic.setVisibility(View.GONE);
		
		isRefreshEvent = false;
		
		addMoreTask = new AddMoreEventsTask();
		addMoreTask.execute(getRequestUrl(latOrigin, lonOrigin, indexEvent, fechaUnix));
	}

	public void refreshTimeLine() {
		
		if(!verificarConexiones()){
			isRefreshEvent = false;
			swipeLayout.setRefreshing(false);
			return;
		}
		
		locationHelper.getLocation();
		
		latOrigin = locationHelper.getLatitude();
		lonOrigin = locationHelper.getLongitude();
		
		isRefreshEvent = true;
		
		refreshTask = new RefreshTimeLineTask();
		refreshTask.execute(getRequestUrl(latOrigin, lonOrigin, "0", "0"));
	}
	
	public void fillEventsAdapter(ArrayList<EventoObjeto> eventos){
		arrayAdapterEvents.clear();
		arrayAdapterEvents.addAll(eventos);
		arrayAdapterEvents.notifyDataSetChanged();
		numeroEventos = arrayAdapterEvents.getCount();
	}
	
	private ArrayList<EventoObjeto> getListaEventosFromDB(){
		reader = new ReadTableDB(getActivity().getApplicationContext());
		return reader.fillEventListFromDB();
	}
	
	private ArrayList<String> fillFavoritosFromDB(){
		reader = new ReadTableDB(getActivity().getApplicationContext());
		return reader.fillFavoriteListFromDB();
	}
	
	private boolean makeEventsRequest(String url){
		String result = jsonHelper.connectionMongo_Json(url);
		
		if(result.isEmpty())
			return false;
		
		jsonParser = new JsonParserHelper(getActivity().getApplicationContext());
		return jsonParser.addEventsToDB(result);
	}
	
	private String getRequestUrl(double latitud, double longitud, String idEvento, String fechaUnix){
		
		SharedPreferencesHelperFinal sharedPreferencesHelper = new SharedPreferencesHelperFinal(getActivity());
		preferences = getActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		int distancia = preferences.getInt("Distancia", 5);
		
		return "http://yapidev.sferea.com/?latitud=" + latitud + ""
				+ "&longitud=" + longitud + "" + "&radio="
				+ distancia + "" + "&categoria="
				+ sharedPreferencesHelper.obtieneCategoriasPreferences() + ""
				+ "&numEventos=" + numeroEventos + "" + "&idEvento=" + idEvento + "&fecha=" + fechaUnix;
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

	/*
	 * Método de callback que es llamado para actualizar el timeline cuando el ViewPager es posicionado en este
	 * fragment.
	 */
	@Override
	public void onUpdated() {
		reader = new ReadTableDB(Application.getInstance());
		listaFavoritos = reader.fillFavoriteListFromDB();
		arrayAdapterEvents.notifyDataSetChanged();
	}
	
	private boolean verificarConexiones(){
		if(!locationHelper.canGetLocation()){
			Toast.makeText(getActivity(), R.string.errorUbicacion, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(!checkInternetConnection.isConnectedToInternet()) {
			Toast.makeText(getActivity(), R.string.NoInternetToastMessage, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	
	private class AddMoreEventsTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			return makeEventsRequest(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean params) {

			if(!this.isCancelled())
			{
				if (params) {
					reader = new ReadTableDB(getActivity());
					if (reader.getEventsDBCount() == arrayAdapterEvents.getCount()) {
						progressFooter.setVisibility(View.GONE);
						((TextView) footerView.findViewById(R.id.textoFooter))
								.setVisibility(View.VISIBLE);
					} else {
						
						ArrayList<EventoObjeto> eventos = getListaEventosFromDB();
						fillEventsAdapter(eventos);
					}
	
				} else {
					progressFooter.setVisibility(View.GONE);
					((TextView) footerView.findViewById(R.id.textoFooter))
							.setVisibility(View.VISIBLE);
				}
			}
			
			loadingMore = false;

		}
	}
	
	private class RefreshTimeLineTask extends AsyncTask<String, Void, Void> {

		ArrayList<EventoObjeto> lista = null;
		
		protected Void doInBackground(String... params) {
			makeEventsRequest(params[0]);
			lista = getListaEventosFromDB();
		
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(!this.isCancelled()){
				if(lista != null){
					fillEventsAdapter(lista);
				}
				swipeLayout.setRefreshing(false);
			}
			isRefreshEvent = false;
		}

	}

}
