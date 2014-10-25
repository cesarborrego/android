package sferea.todo2day.adapters;

import java.util.List;

import sferea.todo2day.DetailActivity;
import sferea.todo2day.R;
import sferea.todo2day.Helpers.ImageUtil;
import sferea.todo2day.config.DataBaseSQLiteManager;
import sferea.todo2day.subfragments.Page_Favorites;
import sferea.todo2day.subfragments.Page_TimeLine;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ArrayAdapterFavorites extends ArrayAdapter {
	Context contex;
	Activity activityAdapter;
	FavoritosObjeto [] favoritesObjeto;
	List<FavoritosObjeto> listaObjetosFavoritos;	
	EventoObjeto eventoObjeto;
	ImageLoader imageloader;
	DisplayImageOptions options;
	
	@SuppressWarnings("unchecked")
	public ArrayAdapterFavorites(Activity activity, Context context, List<FavoritosObjeto> datos) {
		super(activity,  R.layout.row_favorites_responsive_tablet, datos);
		this.listaObjetosFavoritos = datos;
		this.activityAdapter = activity;
		this.contex = context;
		this.imageloader = ImageUtil.getImageLoader();
		this.options = ImageUtil.getOptionsImageLoader();
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = activityAdapter.getLayoutInflater();
		View newView = inflater.inflate(R.layout.row_favorites_responsive_tablet, null);
		
		newView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Page_TimeLine.favoritoActivo = true;
				Page_TimeLine.eventoActivo = false;
				
				Bundle bundle = new Bundle();
				bundle.putParcelable("Favorito", listaObjetosFavoritos.get(position));
				Intent intent = new Intent(contex, DetailActivity.class);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				contex.startActivity(intent);
			}
		});
				
		FavoritosObjeto favoritosObjeto = listaObjetosFavoritos.get(position);
		
		String nombre = favoritosObjeto.getNombreEvento();
		String categoria = favoritosObjeto.getCategoriaEvento();
		String fecha = favoritosObjeto.getFechaEvento();
		String lugar = favoritosObjeto.getLugarEvento();
		String distancia = favoritosObjeto.getDistanciaEvento();
		String descripcion = favoritosObjeto.getDescripcion();
		
		String imagen = favoritosObjeto.getUrlImagen();
				
		((TextView)newView.findViewById(R.id.nombreFavorito)).setText(nombre);
		((TextView)newView.findViewById(R.id.categoriaFavorito)).setText(categoria);
		((TextView)newView.findViewById(R.id.fechaFavorito)).setText(fecha);
		((TextView)newView.findViewById(R.id.lugarFavorito)).setText(lugar);
		((TextView)newView.findViewById(R.id.distanciaFavorito)).setText(distancia);
		((TextView)newView.findViewById(R.id.descripcionFavorito)).setText(descripcion);
		
		imageloader.displayImage(imagen, (ImageView)newView.findViewById(R.id.thumbnailFavorite), options);
//		ImageView imgFavorite = null;
//		if(imagen!=null){
//			imgFavorite = (ImageView)newView.findViewById(R.id.thumbnailFavorite);
//			imgFavorite.setImageBitmap(imagen);
//		}
		
		((RelativeLayout)newView.findViewById(R.id.botonFavEvent)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteFavorites(position);
			}
		});		
		
		if(	DetailActivity.activaRefreshFavorites_Details){
			refreshFavoritesFragment(position);
		}
		return newView;
	}
	
	
	
	private void deleteFavorites(final int position){
		DataBaseSQLiteManager managerDB = new DataBaseSQLiteManager(activityAdapter.getApplicationContext());
		managerDB.eliminar(listaObjetosFavoritos.get(position).getIndexOfEvent());
		refreshFavoritesFragment(position);
		Page_TimeLine.prendeEstrellaDetails = false;
		Page_TimeLine.prendeEstrellaTime_Line[position] = false;
		Page_TimeLine.arrayAdapterEvents.notifyDataSetChanged();
	}
	
	private void refreshFavoritesFragment(int position){
		// la volvemos a hacer falsa	
		DetailActivity.activaRefreshFavorites_Details= false;
		 //inicializamos la varible
		 Fragment fragment = null;
		 fragment = new Page_Favorites();

		 if(fragment!=null){
			 FragmentManager fragmentManager = ((FragmentActivity)activityAdapter).getSupportFragmentManager();
			 fragmentManager.beginTransaction().replace(R.id.content_Favorites, fragment).commit();
		 }		 
	 }
}
