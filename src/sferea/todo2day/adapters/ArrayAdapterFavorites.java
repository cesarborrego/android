package sferea.todo2day.adapters;

import java.util.ArrayList;

import sferea.todo2day.DetailActivity;
import sferea.todo2day.R;
import sferea.todo2day.Helpers.ImageUtil;
import sferea.todo2day.adapters.ArrayAdapterEvents.ViewHolder;
import sferea.todo2day.config.CategoriasConfig;
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

public class ArrayAdapterFavorites extends ArrayAdapter<FavoritosObjeto> {
	Context contex;
	Activity activityAdapter;
	FavoritosObjeto [] favoritesObjeto;	
	EventoObjeto eventoObjeto;
	ImageLoader imageloader;
	DisplayImageOptions options;
	
	@SuppressWarnings("unchecked")
	public ArrayAdapterFavorites(Activity activity, Context context, ArrayList<FavoritosObjeto> datos) {
		super(activity,  R.layout.row_favorite_tablet, datos);
		this.activityAdapter = activity;
		this.contex = context;
		this.imageloader = ImageUtil.getImageLoader();
		this.options = ImageUtil.getOptionsImageLoader();
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View v = convertView;
		
		if(v == null){
			v = LayoutInflater.from(getContext()).inflate(R.layout.row_favorite_tablet, null);
			viewHolder = new ViewHolder();
//			viewHolder.botonFavorito = (RelativeLayout) v.findViewById(R.id.botonFavEvent);
			viewHolder.categoriaFavorito = (TextView) v.findViewById(R.id.categoriaFavorito);
//			viewHolder.descripcionFavorito = (TextView) v.findViewById(R.id.descripcionFavorito);
			viewHolder.distanciaFavorito = (TextView) v.findViewById(R.id.distanciaFavorito);
			viewHolder.fechaFavorito = (TextView) v.findViewById(R.id.fechaFavorito);
//			viewHolder.lugarFavorito = (TextView) v.findViewById(R.id.lugarFavorito);
			viewHolder.iconFavFavorito = (ImageView) v.findViewById(R.id.iconFavFavorito);
			viewHolder.nombreFavorito = (TextView) v.findViewById(R.id.nombreFavorito);
			viewHolder.thumbnailFavorito = (ImageView) v.findViewById(R.id.thumbnailFavorite);
			viewHolder.iconCategoria = (ImageView) v.findViewById(R.id.iconCategoriaFavorito);
			
			v.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder) v.getTag();
		}
		
		setIconCategoria(position, viewHolder);
		
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Page_TimeLine.favoritoActivo = true;
				Page_TimeLine.eventoActivo = false;
				
				Bundle bundle = new Bundle();
				bundle.putParcelable("Favorito", getItem(position));
				Intent intent = new Intent(contex, DetailActivity.class);
				intent.putExtras(bundle);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				contex.startActivity(intent);
			}
		});
				
		FavoritosObjeto favoritosObjeto = getItem(position);
		
		String nombre = favoritosObjeto.getNombreEvento();
		String categoria = favoritosObjeto.getCategoriaEvento();
		String fecha = favoritosObjeto.getFechaEvento();
		String lugar = favoritosObjeto.getLugarEvento();
		String distancia = favoritosObjeto.getDistanciaEvento();
		String descripcion = favoritosObjeto.getDescripcion();
		
		String imagen = favoritosObjeto.getUrlImagen();
				
		viewHolder.nombreFavorito.setText(nombre);
		viewHolder.categoriaFavorito.setText(categoria);
		viewHolder.fechaFavorito.setText(fecha);
//		viewHolder.lugarFavorito.setText(lugar);
		viewHolder.distanciaFavorito.setText(distancia);
//		viewHolder.descripcionFavorito.setText(descripcion);
		
//		imageloader.displayImage(imagen, viewHolder.thumbnailFavorito, options);
		
		viewHolder.iconFavFavorito.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteFavorites(position);
			}
		});		
		
		if(	DetailActivity.activaRefreshFavorites_Details){
			refreshFavoritesFragment(position);
		}
		
		return v;
	}
	
	
	
	private void deleteFavorites(final int position){
		DataBaseSQLiteManager managerDB = new DataBaseSQLiteManager(activityAdapter.getApplicationContext());
		managerDB.eliminar(getItem(position).getIndexOfEvent());
		refreshFavoritesFragment(position);
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
	
	private void setIconCategoria(int position, ViewHolder eventView){
		CategoriasConfig categoriaId = CategoriasConfig.valueOf(getItem(position).getCategoriaIDEvento().toUpperCase());
		
		switch(categoriaId){
		case A :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_antros);
			getItem(position).setImagenCategoria(R.drawable.ic_small_antros);
			break;
		case B :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_cultura);
			getItem(position).setImagenCategoria(R.drawable.ic_small_cultura);
			break;
		case C :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_cine);
			getItem(position).setImagenCategoria(R.drawable.ic_small_cine);
			break;
		case D :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_deportes);
			getItem(position).setImagenCategoria(R.drawable.ic_small_deportes);
			break;
		case E :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_negocios);
			getItem(position).setImagenCategoria(R.drawable.ic_small_negocios);
			break;
		case F :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_con_ninos);
			getItem(position).setImagenCategoria(R.drawable.ic_small_con_ninos);
			break;
		case G :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_gastronomia);
			getItem(position).setImagenCategoria(R.drawable.ic_small_gastronomia);
			break;
		case H :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_musica);
			getItem(position).setImagenCategoria(R.drawable.ic_small_musica);
			break;
		case I :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_salud);
			getItem(position).setImagenCategoria(R.drawable.ic_small_salud);
			break;
		case J :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_sociales);
			getItem(position).setImagenCategoria(R.drawable.ic_small_sociales);
			break;
		case K :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_tecnologia);
			getItem(position).setImagenCategoria(R.drawable.ic_small_tecnologia);
			break;
		case L :
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_verde);
			getItem(position).setImagenCategoria(R.drawable.ic_small_verde);
			break;
		default:
			eventView.iconCategoria.setImageResource(R.drawable.ic_small_sociales);
			getItem(position).setImagenCategoria(R.drawable.ic_small_sociales);
		}

	}
	
	class ViewHolder{
		
		public TextView nombreFavorito;
		public TextView categoriaFavorito;
		public TextView fechaFavorito;
//		public TextView lugarFavorito;
		public TextView distanciaFavorito;
//		public TextView descripcionFavorito;
		public ImageView thumbnailFavorito;
		public ImageView iconCategoria;
		public ImageView iconFavFavorito;
	}
}
