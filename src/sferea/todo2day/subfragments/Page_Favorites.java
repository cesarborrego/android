package sferea.todo2day.subfragments;

import java.util.ArrayList;
import java.util.List;

import sferea.todo2day.R;
import sferea.todo2day.adapters.ArrayAdapterFavorites;
import sferea.todo2day.adapters.EventoObjeto;
import sferea.todo2day.adapters.FavoritosObjeto;
import sferea.todo2day.config.DataBaseSQLiteManager;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Page_Favorites extends Fragment {
	View view;
	
	ListView listaFavoritos;
	DataBaseSQLiteManager manager;
	Cursor cursor;
	ArrayList<FavoritosObjeto> listaObjectFavoritos;
	int iContador = 0;
	
	String imagenesEventos [] = null;
	public static boolean favoritosActiva[];
	SharedPreferences shrpref_fav;
	List<EventoObjeto> listaEventos;
	FavoritosObjeto fav;
	
	public static ArrayAdapterFavorites adapterFavorites;
		
	public Page_Favorites(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.page_favorites, container, false);
		manager = new DataBaseSQLiteManager(getActivity().getApplicationContext());
		
		listaObjectFavoritos = new ArrayList<FavoritosObjeto>();
		listaEventos = new ArrayList<EventoObjeto>();
		
		listaFavoritos = (ListView)view.findViewById(R.id.listviewFavoritos);
		
		llenarFavoritosObjeto();
		
		adapterFavorites = new ArrayAdapterFavorites(getActivity(), getActivity().getApplicationContext(),listaObjectFavoritos);
		listaFavoritos.setAdapter(adapterFavorites);
		adapterFavorites.notifyDataSetChanged();
	
		return view;
	}	
	
	public void llenarFavoritosObjeto(){
		listaObjectFavoritos.clear();
		cursor = manager.cargarTablas();
		try{
			if(cursor.moveToFirst()){
				do{
					/*Titulo 	Categoria	FechayHora 	Descripcion  
					 *Fuente    Lugar 		Direccion  	Telefono
					 *Distancia Boleto 		Longitud  	Latitud*/
					
					listaObjectFavoritos.add(new FavoritosObjeto(
							cursor.getString(cursor.getColumnIndex("TITULO_EVENTO")),
							cursor.getString(cursor.getColumnIndex("CATEGORIA")),
							cursor.getString(cursor.getColumnIndex("CATEGORIA_ID")),
							cursor.getString(cursor.getColumnIndex("FECHA")), 
							cursor.getString(cursor.getColumnIndex("DESCRIPCION")), 
							cursor.getString(cursor.getColumnIndex("FUENTE")), 
							cursor.getString(cursor.getColumnIndex("LUGAR")), 
							cursor.getString(cursor.getColumnIndex("DIRECCION")), 
							cursor.getString(cursor.getColumnIndex("TELEFONO")), 
							cursor.getString(cursor.getColumnIndex("DISTANCIA")), 
							cursor.getString(cursor.getColumnIndex("BOLETO")), 
							Double.parseDouble(cursor.getString(cursor.getColumnIndex("LONGITUD"))), 
							Double.parseDouble(cursor.getString(cursor.getColumnIndex("LATITUD"))),
							cursor.getString(cursor.getColumnIndex("URL_IMAGEN_EVENTO")),
							Integer.parseInt(cursor.getString(cursor.getColumnIndex("POSICION"))),
							cursor.getString(cursor.getColumnIndex("INDEX_OF_EVENT")),
							Integer.parseInt(cursor.getString(cursor.getColumnIndex("FECHA_UNIX")))));
				}while (cursor.moveToNext());	
			}
		} finally{
			cursor.close();
			manager.cerrarDB();
		}
	}	
	
	public Bitmap decodBitmap(byte [] img){
		Bitmap b=BitmapFactory.decodeByteArray(img, 0, img.length);
		return b;
	}	
}
