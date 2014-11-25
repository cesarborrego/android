package sferea.todo2day.adapters;

import sferea.todo2day.R;
import sferea.todo2day.beans.TipoBoletoObjeto;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterVariosBoletos extends ArrayAdapter<TipoBoletoObjeto>{

	public ArrayAdapterVariosBoletos(Context context) {
		super(context, 0);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null){
			v = LayoutInflater.from(getContext()).inflate(R.layout.varios_boletos, null);
			for(int i =0 ; i<getItem(position).getListaBoletos().size(); i++){
				((TextView)v.findViewById(R.id.varBoletoID)).setText(getItem(position).getListaBoletos().get(i).getTipo());
				((TextView)v.findViewById(R.id.varPrecioBoletoID)).setText(String.valueOf(getItem(position).getListaBoletos().get(i).getPrecio()));
			}
		}
		return convertView;
	}

}
