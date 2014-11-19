package sferea.todo2day.fragments;

import java.util.ArrayList;
import java.util.List;

import sferea.todo2day.DetailActivity;
import sferea.todo2day.R;
import sferea.todo2day.adapters.ArrayAdapterFavorites;
import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.beans.FavoritosObjeto;
import sferea.todo2day.config.DataBaseSQLiteManager;
import sferea.todo2day.listeners.UpdateableFragmentListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Page_Favorites extends Fragment implements UpdateableFragmentListener {
	View view;

	ListView listaFavoritos;
	DataBaseSQLiteManager manager;
	Cursor cursor;
	ArrayList<FavoritosObjeto> listaObjectFavoritos;
	int iContador = 0;

	String imagenesEventos[] = null;
	public static boolean favoritosActiva[];
	SharedPreferences shrpref_fav;
	List<EventoObjeto> listaEventos;
	FavoritosObjeto fav;

	public static ArrayAdapterFavorites adapterFavorites;

	public Page_Favorites() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.page_favorites, container, false);

		listaObjectFavoritos = new ArrayList<FavoritosObjeto>();
		listaEventos = new ArrayList<EventoObjeto>();

		listaFavoritos = (ListView) view.findViewById(R.id.listviewFavoritos);

		adapterFavorites = new ArrayAdapterFavorites(getActivity());
		listaFavoritos.setAdapter(adapterFavorites);

		listaFavoritos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Page_TimeLine.favoritoActivo = true;
				Page_TimeLine.eventoActivo = false;
				FavoritosObjeto favorito = (FavoritosObjeto) parent
						.getItemAtPosition(position);

				Bundle bundle = new Bundle();
				bundle.putParcelable("Favorito", favorito);
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getActivity().startActivity(intent);

			}
		});

		return view;
	}

	public void llenarFavoritosObjeto() {
		listaObjectFavoritos.clear();
		
		manager = new DataBaseSQLiteManager(getActivity()
				.getApplicationContext());
		cursor = manager.cargarTablas();
		
		try {
			if (cursor.moveToFirst()) {
				do {
					/*
					 * Titulo Categoria FechayHora DescripcionFuente Lugar
					 * Direccion TelefonoDistancia Boleto Longitud Latitud
					 */

					listaObjectFavoritos
							.add(new FavoritosObjeto(
									cursor.getString(cursor
											.getColumnIndex("EVENTO_ID")),
									cursor.getString(cursor
											.getColumnIndex("TITULO_EVENTO")),
									cursor.getString(cursor
											.getColumnIndex("CATEGORIA")),
									cursor.getString(cursor
											.getColumnIndex("CATEGORIA_ID")),
									cursor.getString(cursor
											.getColumnIndex("FECHA")),
									cursor.getString(cursor
											.getColumnIndex("DESCRIPCION")),
									cursor.getString(cursor
											.getColumnIndex("FUENTE")),
									cursor.getString(cursor
											.getColumnIndex("LUGAR")),
									cursor.getString(cursor
											.getColumnIndex("DIRECCION")),
									cursor.getString(cursor
											.getColumnIndex("TELEFONO")),
									cursor.getString(cursor
											.getColumnIndex("DISTANCIA")),
									cursor.getString(cursor
											.getColumnIndex("BOLETO")),
									Double.parseDouble(cursor.getString(cursor
											.getColumnIndex("LONGITUD"))),
									Double.parseDouble(cursor.getString(cursor
											.getColumnIndex("LATITUD"))),
									cursor.getString(cursor
											.getColumnIndex("URL_IMAGEN_EVENTO")),
									Integer.parseInt(cursor.getString(cursor
											.getColumnIndex("POSICION"))),
									cursor.getString(cursor
											.getColumnIndex("INDEX_OF_EVENT")),
									Integer.parseInt(cursor.getString(cursor
											.getColumnIndex("FECHA_UNIX")))));
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			manager.cerrarDB();
		}
	}

	@Override
	public void onResume() {
		llenarFavoritosObjeto();
		adapterFavorites.clear();
		adapterFavorites.addAll(listaObjectFavoritos);
		adapterFavorites.notifyDataSetChanged();
		super.onResume();
	}
	
	@Override
	public void onUpdated() {
		onResume();
	}
}
