package sferea.todo2day.subfragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import sferea.todo2day.DetailActivity;
import sferea.todo2day.R;
import sferea.todo2day.adapters.ArrayAdapterEvents;
import sferea.todo2day.adapters.ArrayAdapterFavorites;
import sferea.todo2day.adapters.EventoObjeto;
import sferea.todo2day.adapters.FavoritosObjeto;
import sferea.todo2day.config.DataBaseSQLiteManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Page_Favorites extends Fragment {
	View view;
	
	ListView listaFavoritos;
	DataBaseSQLiteManager manager;
	Cursor cursor;
	List<FavoritosObjeto> listaObjectFavoritos;
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
							decodBitmap(cursor.getBlob(cursor.getColumnIndex("IMAGEN_EVENTO"))),
							Integer.parseInt(cursor.getString(cursor.getColumnIndex("POSICION")))));
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
