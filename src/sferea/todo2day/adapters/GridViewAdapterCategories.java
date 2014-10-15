package sferea.todo2day.adapters;

import sferea.todo2day.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapterCategories extends BaseAdapter {
	
	private Context mContext;
	private String[] cats;
	private Integer[] images={
			//R.drawable.antros_bares,R.drawable.cine_teatro,R.drawable.cultura,R.drawable.deportes,
			//R.drawable.negocios,R.drawable.ninios,R.drawable.musica,R.drawable.salud,R.drawable.sociales,
			//R.drawable.tecnologia_ch,R.drawable.verde
	};
	
	public GridViewAdapterCategories(Context context){
		mContext = context;
		cats = mContext.getResources().getStringArray(R.array.categorias);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cats.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parentView) {
		
		View newView = LayoutInflater.from(mContext).inflate(R.layout.cell_categorie, parentView, false);
		((TextView)newView.findViewById(R.id.cellCategorieTitle)).setText(cats[position]);
		((ImageView)newView.findViewById(R.id.cell_categorie_background)).setImageResource(images[position]);
		return newView;
	}

}
