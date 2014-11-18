package sferea.todo2day.fragments;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import sferea.todo2day.MapActivity;
import sferea.todo2day.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class SubF_Details extends Fragment {
	
	GoogleMap mMap;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_detail, container, false);
		
		((RelativeLayout)view.findViewById(R.id.capaMapa)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putDoubleArray("LatLon", new double[]{19.427385, -99.167462});;
				Intent intent = new Intent(view.getContext(), MapActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	/*
	private void setUpMapIfNeeded() {
		if(mMap==null)
        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.detallesMap)).getMap();
        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            // The Map is verified. It is now safe to manipulate the map.
        	setUpMap();
        }
	}
	
	private void setUpMap() {
		mMap.setMyLocationEnabled(false);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.427385, -99.167462), 14));
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Fragment fragment = (getFragmentManager().findFragmentById(R.id.detallesMap));   
		   FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		   ft.remove(fragment);
		   ft.commit();
	}
	
	*/
	
}
