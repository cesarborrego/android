package sferea.todo2day.fragments;

import java.util.ArrayList;
import java.util.List;

import sferea.todo2day.Application;
import sferea.todo2day.DetailActivity;
import sferea.todo2day.R;
import sferea.todo2day.adapters.ArrayAdapterFavorites;
import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.beans.FavoritosObjeto;
import sferea.todo2day.config.DataBaseSQLiteManagerFavorites;
import sferea.todo2day.helpers.ReadTableDB;
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
	DataBaseSQLiteManagerFavorites manager;
	Cursor cursor;
	ArrayList<FavoritosObjeto> listaObjectFavoritos;
	int iContador = 0;

	String imagenesEventos[] = null;
	public static boolean favoritosActiva[];
	SharedPreferences shrpref_fav;
	List<EventoObjeto> listaEventos;
	FavoritosObjeto fav;
	
	ReadTableDB reader;

	public static ArrayAdapterFavorites adapterFavorites;

	public Page_Favorites() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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

	@Override
	public void onResume() {
		reader = new ReadTableDB(Application.getInstance());	
		listaObjectFavoritos = reader.fillFavoritesListFromDB();
		if(listaObjectFavoritos!=null){					
			adapterFavorites.clear();
			adapterFavorites.addAll(listaObjectFavoritos);
			adapterFavorites.notifyDataSetChanged();
		}
		super.onResume();
	}
	
	@Override
	public void onUpdated() {
		onResume();
	}
}
