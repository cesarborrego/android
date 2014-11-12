package sferea.todo2day.subfragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sferea.todo2day.DetailActivity;
import sferea.todo2day.R;
import sferea.todo2day.SplashActivity;
import sferea.todo2day.Helpers.CheckInternetConnection;
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

public class Page_TimeLine extends Fragment implements AddMoreTaskListener, OnTouchListener, OnScrollListener {

	public static ArrayList<EventoObjeto> listaEventos;
	public static ArrayList<String> favorites;
	public static ArrayAdapterEvents arrayAdapterEvents;
	ListView listView_Eventos;
	boolean allowRefresh = false;
	boolean refresh = false;

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

	int totalItemCount = 0;

	JsonHelper jsonHelper;
	
	SwipeRefreshLayout swipeLayout;

	// bandera tomara el valor que retorna @parseFirstJson_AddDB(json) para que
	// dependiendo de su resultado activara
	// las funciones en el hilo principal UI, ya sea mandar un toast avisando
	// que trono o si todo sale bien, llenar la lista de eventos
	boolean bandera = false;

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
	    swipeLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				
				refreshTimeLine();
				// TODO Auto-generated method stub
				
			}
		});
	    swipeLayout.setColorSchemeColors(
	    		Color.RED, Color.GREEN, Color.CYAN);
	    
		// Crea un nuevo arraylist de eventos
		listaEventos = new ArrayList<EventoObjeto>();
		// Obtiene la vista del listView
		listView_Eventos = ((ListView) view.findViewById(R.id.listviewEventos));

		progressFooter = ((ProgressBar) footerView.findViewById(R.id.progressBarFooter));
		
		footerNoInternet = (TextView) footerView.findViewById(R.id.textoFooterNOInternet);
		
		footerNoInternetClic = (TextView) footerView.findViewById(R.id.textoFooterNOInternetClic);
		
		favorites = new ArrayList<String>();
		
		readTableEvents_fillListEvent();
		
		ReadTableDB readTableDB = new ReadTableDB(getActivity().getApplicationContext());
		
		readTableDB.readFavoritesTable();

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
		
		ReadTableDB readTableDB = new ReadTableDB(getActivity().getApplicationContext());
		readTableDB.readTable_FillList();
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
				((TextView) footerView.findViewById(R.id.textoFooter)).setVisibility(View.VISIBLE);
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
				
				break;
			}
		}
		return false;
	}

}
