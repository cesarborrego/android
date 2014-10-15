package sferea.todo2day.subfragments;

import sferea.todo2day.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Page_Favorites extends Fragment {
	
	public Page_Favorites(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.page_favorites, container, false);
		return view;
	}

}
