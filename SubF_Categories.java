package sferea.todo2day.subfragments;

import sferea.todo2day.R;
import sferea.todo2day.adapters.GridViewAdapterCategories;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class SubF_Categories extends Fragment {
	
	public SubF_Categories(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.subfrag_categories, container, false);
		
		//GridView gridview = (GridView) view.findViewById(R.id.categoriesGrid);
	    //gridview.setAdapter(new GridViewAdapterCategories(view.getContext()));

		return view;
	}
}
