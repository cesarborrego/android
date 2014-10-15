package sferea.todo2day.adapters;

import java.util.List;

import sferea.todo2day.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Contiene el ArrayAdapter que controla la creacion y escuchas de cada fila de los ajustes del menu lateral 
 * @author maw
 *
 */
public class ArrayAdapterSettings extends ArrayAdapter<List<DrawerItemRow>> {
	
	Context thisContext;
	List<DrawerItemRow> objectArrayList;
	
	/**
	 * 
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public ArrayAdapterSettings(Context context, int resource, int textViewResourceId, List objects){
		super(context, resource, textViewResourceId, objects);
		
		this.objectArrayList = (List<DrawerItemRow>)objects;
		this.thisContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater layoutInflater = (LayoutInflater)thisContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//TODO Posiblemente haya varios layouts como en Play Store, para navegacion y ajustes
		View newView = layoutInflater.inflate(R.layout.row_setting, null);
		
		((ImageView)newView.findViewById(R.id.icon)).setImageResource(objectArrayList.get(position).getIcon());
		((TextView)newView.findViewById(R.id.title_setting)).setText(objectArrayList.get(position).getTitulo());
		
		// TODO Regresar el nuevo view
		return newView;
	}

}
