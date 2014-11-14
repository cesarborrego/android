package sferea.todo2day.adapters;

import java.util.ArrayList;

import sferea.todo2day.R;
import sferea.todo2day.Helpers.ImageUtil;
import sferea.todo2day.config.CategoriasConfig;
import sferea.todo2day.config.DataBaseSQLiteManager;
import sferea.todo2day.subfragments.Page_TimeLine;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
			viewHolder.categoriaFavorito = (TextView) v.findViewById(R.id.categoriaFavorito);
			viewHolder.distanciaFavorito = (TextView) v.findViewById(R.id.distanciaFavorito);
			viewHolder.fechaFavorito = (TextView) v.findViewById(R.id.fechaFavorito);
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
				
		viewHolder.nombreFavorito.setText(getItem(position).getNombreEvento());
		viewHolder.categoriaFavorito.setText(getItem(position).getCategoriaEvento());
		viewHolder.fechaFavorito.setText(getItem(position).getFechaEvento());
		viewHolder.distanciaFavorito.setText("a " + getItem(position).getDistanciaEvento().toLowerCase());
		
		imageloader.displayImage(getItem(position).getUrlImagen(), viewHolder.thumbnailFavorito, options);
		
		viewHolder.iconFavFavorito.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteFavorites(position);
			}
		});		
		
		return v;
	}
	
	
	
	private void deleteFavorites(final int position){
		DataBaseSQLiteManager managerDB = new DataBaseSQLiteManager(activityAdapter.getApplicationContext());
		managerDB.eliminar(getItem(position).getIndexOfEvent());
		Page_TimeLine.favorites.remove(getItem(position).getIndexOfEvent());
		this.remove(getItem(position));
		this.notifyDataSetChanged();
		Page_TimeLine.arrayAdapterEvents.notifyDataSetChanged();
	}

	
	private void setIconCategoria(int position, ViewHolder eventView){
		CategoriasConfig categoriaId = CategoriasConfig.valueOf(getItem(position).getCategoriaIDEvento().toUpperCase());
		
		switch(categoriaId){
		case A :
			eventView.iconCategoria.setImageResource(R.drawable.ic_bar_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_antros);
			break;
		case B :
			eventView.iconCategoria.setImageResource(R.drawable.ic_cultura_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_cultura);
			break;
		case C :
			eventView.iconCategoria.setImageResource(R.drawable.ic_cine_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_cine);
			break;
		case D :
			eventView.iconCategoria.setImageResource(R.drawable.ic_deportes_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_deportes);
			break;
		case E :
			eventView.iconCategoria.setImageResource(R.drawable.ic_negocios_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_negocios);
			break;
		case F :
			eventView.iconCategoria.setImageResource(R.drawable.ic_ninios_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_con_ninos);
			break;
		case G :
			eventView.iconCategoria.setImageResource(R.drawable.ic_gastronomia_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_gastronomia);
			break;
		case H :
			eventView.iconCategoria.setImageResource(R.drawable.ic_musica_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_musica);
			break;
		case I :
			eventView.iconCategoria.setImageResource(R.drawable.ic_salud_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_salud);
			break;
		case J :
			eventView.iconCategoria.setImageResource(R.drawable.ic_sociales_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_sociales);
			break;
		case K :
			eventView.iconCategoria.setImageResource(R.drawable.ic_tecnologia_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_tecnologia);
			break;
		case L :
			eventView.iconCategoria.setImageResource(R.drawable.ic_verde_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_verde);
			break;
		default:
			eventView.iconCategoria.setImageResource(R.drawable.ic_sociales_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_sociales);
		}

	}
	
	class ViewHolder{
		
		public TextView nombreFavorito;
		public TextView categoriaFavorito;
		public TextView fechaFavorito;
		public TextView distanciaFavorito;
		public ImageView thumbnailFavorito;
		public ImageView iconCategoria;
		public ImageView iconFavFavorito;
	}
}
