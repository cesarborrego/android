package sferea.todo2day.subfragments;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import sferea.todo2day.R;
import sferea.todo2day.adapters.ArrayAdapterEvents;
import sferea.todo2day.adapters.EventoObjeto;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Page_TimeLine extends Fragment {
	
	public static final float REFRESH_THRESHOLD = 50;
	List<EventoObjeto> listaEventos;
	ArrayAdapterEvents arrayAdapterEvents;
	ListView listView_Eventos;
	boolean allowRefresh=false;
	boolean refresh=false;
	boolean downCounterUsed=false;
	boolean headerAdded=false;
	float startY;
	
	View view;
	View headerView;
	View footerView;
	boolean isLoading=false;
	
	public Page_TimeLine(){}

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
		
		FAKEfill();
		
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
		
		/**
		 * 
		 */
		listView_Eventos.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float y = event.getY();
				
				switch (event.getAction()) {
					
					case MotionEvent.ACTION_MOVE:{
						if(!downCounterUsed){
							startY=y;
							downCounterUsed=true;
						}
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
						allowRefresh = (listView_Eventos.getFirstVisiblePosition() == 0);
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
		
		listView_Eventos.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				//Log.d(null,"firsVisibleItem="+firstVisibleItem+"\n+visibleItemCount="+visibleItemCount+"\ntotalItemCount"+totalItemCount+"\nloadedItems"+(firstVisibleItem + visibleItemCount));
				if((visibleItemCount+firstVisibleItem >= totalItemCount) && !isLoading){
					isLoading=true;
					Log.d(null,"Es hora de cargar!");
					addMoreItems();
				}
			}
		});
		return view;
	}
	
	/**
	 * Tarea asincrona que agrega al final de la lista mas eventos
	 */
	public void addMoreItems(){
		new AsyncTask<Void,Void,Void>() {
			
			//ANTES de que empiece se agrega el footer
			@Override
			protected void onPreExecute() {
				// TODO El dialog de cargar
				super.onPreExecute();
				((ProgressBar)footerView.findViewById(R.id.progressBarFooter)).setVisibility(View.VISIBLE);
				//listView_Eventos.addFooterView(footerView);
			}

			//Empieza la tarea asincrona
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Cambiar al real
				try {
					//Duerme el hilo 
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				FAKEloadMoreItems();
				return null;
			}

			//Una vez que termina el proceso
			@Override
			protected void onPostExecute(Void result) {
				// TODO Hacer el real
				super.onPostExecute(result);
				//Quita el footer
				((ProgressBar)footerView.findViewById(R.id.progressBarFooter)).setVisibility(View.GONE);
				//listView_Eventos.removeFooterView(footerView);
				//Muestra los nuevos eventos agregados en la lista
				arrayAdapterEvents.notifyDataSetChanged();
				//Sale del estado de carga
				isLoading=false;
			}
			
		}.execute();
	}
	
	public void refreshTimeLine(){
		new AsyncTask<Void,Void,Void>() {
			
			@Override
			protected void onPreExecute() {
				// TODO El dialog de cargar
				super.onPreExecute();
				((TextView)headerView.findViewById(R.id.textoHeaderListview)).setVisibility(View.GONE);
				((ProgressBar)headerView.findViewById(R.id.progressBarHeader)).setVisibility(View.VISIBLE);
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Crear rutina de actualizacion real
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FAKEfill();
				return null;
			}
	
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				//listView_Eventos.removeHeaderView(headerView);
				arrayAdapterEvents.notifyDataSetChanged();
				((TextView)headerView.findViewById(R.id.textoHeaderListview)).setVisibility(View.GONE);
				((ProgressBar)headerView.findViewById(R.id.progressBarHeader)).setVisibility(View.GONE);
				headerAdded=false;
			}
			
		}.execute();
	}
	
	public void FAKEfill(){
		listaEventos.clear();
		
		/*
		for (int i=0; i<20; i++){			
			listaEventos.add(new EventoObjeto("Evento "+i, "Categoria"));
		}
		*/
		
		/*
		<string-array name="categorias">
	        <item>Antros y Bares</item>
	        <item>Cine y Teatro</item>
	        <item>Cultura</item>
	        <item>Deportes</item>
	        <item>Negocios</item>
	        <item>Con ninios</item>
	        <item>Gastronomia</item>
	        <item>Musica</item>
	        <item>Salud</item>
	        <item>Sociales</item>
	        <item>Tecnologia</item>
	        <item>Verde</item>
	    </string-array>
	    */
		
		String[] nombres ={
				"Apertura Bar Avalon",
				"Made in Mexico",
				"Orquesta Filarmonica del IPN",
				"Mexico vs Camerun",
				"Simposio \"Vision Empresarial\" en el WTC",
				"Dora la Exploradora EN VIVO",
				"Inauguracion \"Vinedo San Rafael\"",
				"FSOE 300 feat W&W and Shogun",
				"Yoga Colectivo",
				"Inauguracion Acuario Inbursa",
				"Microsoft XBox en E3",
				"Reforestacion Chapultepec 2014"
		};
		
		String[] lugares={
				"Bar Avalon",
				"Teatro Insurgentes",
				"Planetario Luis Enrique Erro",
				"Estadio Mane Garrincha",
				"World Trade Center DF",
				"Teatro Zaragoza",
				"Cuernavaca, Morelos",
				"PEPSI Center",
				"Parque Naucalli",
				"Acuario Inbursa, Plaza Carso",
				"World Trade Center DF",
				"Bosque de Chapultepec"
		};
		
		String[] fechas = {
			"Lun 31 de Mayo","Mar 15 de Febrero","Mie 23 de Diciembre","Jue 1 de Marzo","Vie 30 de Noviembre","Sab 16 de Enero",
			"Dom 9 de Julio","Lun 20 de Junio", "Mar 1 de Abril", "Mie 30 de Agosto", "Juev 6 de Octubre", "Vie 15 Septiembre"
		};
		
		String[] categorias = getResources().getStringArray(R.array.categorias);  
		
		Bitmap[] imagenesPrueba ={
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba1),//0
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba2),//1
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba3),//2
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba4),//3
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba7),//4
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba8),//5
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba13),//6
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba6),//7
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba9),//8
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba10),//9
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba11),//10
				BitmapFactory.decodeResource(getResources(), R.drawable.prueba12)//11
		};
		
		//EventoObjeto(String nomEvento, String catEvento, String fechEvento, String horEvento, String lugEvento, double latEv, double lonEv, double dist, Bitmap img)
		for(int i=0; i<categorias.length; i++){
			listaEventos.add(new EventoObjeto(nombres[i],categorias[i], fechas[i]+" 17:45", "17:45", lugares[i], 19.355470,-99.186774, 34.7, imagenesPrueba[i]));
		}
	}
	
	public void FAKEloadMoreItems(){
		int tam=listaEventos.size();
		for(int i=tam; i<(tam+20); i++){
			listaEventos.add(new EventoObjeto("Evento "+i, "Categoria"));
		}
	}
}

























