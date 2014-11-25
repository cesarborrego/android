package sferea.todo2day.fragments;

import java.util.ArrayList;

import sferea.todo2day.R;
import sferea.todo2day.adapters.PagerAdapterEvents;
import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.helpers.ReadTableDB;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SubF_Events extends Fragment {
	
	private ViewPager viewPager;
	private PagerAdapterEvents pagerAdapter;
	private static final int TIMELINE = 0;
	private static final int FAVORITOS = 1;
	
	private static View selectorTimeline;
	private static View selectorFavorites;
	private static TextView textoTimeline;
	private static TextView textoFavorites;
	
	private Page_TimeLine fragmentTimeline;
	private ReadTableDB reader;

	public SubF_Events(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.subfrag_events, container, false);
		selectorTimeline = ((View)view.findViewById(R.id.selectorOrangeTimeline));
		selectorFavorites = ((View)view.findViewById(R.id.selectorOrangeFavorites));
		textoTimeline = ((TextView)view.findViewById(R.id.title_Timeline));
		textoFavorites = ((TextView)view.findViewById(R.id.title_Favorites));
		
		selectorTimeline.setVisibility(View.VISIBLE);
		textoTimeline.setTextColor(Color.BLACK);
		textoTimeline.setTypeface(null,Typeface.BOLD);
		
		pagerAdapter = new PagerAdapterEvents(getChildFragmentManager());
		
		((RelativeLayout)view.findViewById(R.id.botonTimeline)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(TIMELINE,true);
				pagerAdapter.notifyDataSetChanged();

			}
		});
		
		((RelativeLayout)view.findViewById(R.id.botonFavoritos)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(FAVORITOS,true);
				pagerAdapter.notifyDataSetChanged();
			}
		});
		
		return view;
	}	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		viewPager = (ViewPager)getView().findViewById(R.id.viewPagerEvents);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(arg0==TIMELINE){
					selectorTimeline.setVisibility(View.VISIBLE);
					selectorFavorites.setVisibility(View.INVISIBLE);
					
					textoTimeline.setTextColor(Color.BLACK);
					textoTimeline.setTypeface(null,Typeface.BOLD);
					
					textoFavorites.setTextColor(Color.LTGRAY);
					textoFavorites.setTypeface(null,Typeface.NORMAL);
					
					pagerAdapter.notifyDataSetChanged();
					viewPager.setCurrentItem(0);
					
				}else if (arg0==FAVORITOS){
					selectorTimeline.setVisibility(View.INVISIBLE);
					selectorFavorites.setVisibility(View.VISIBLE);
					
					textoFavorites.setTextColor(Color.BLACK);
					textoFavorites.setTypeface(null,Typeface.BOLD);
					
					textoTimeline.setTextColor(Color.LTGRAY);
					textoTimeline.setTypeface(null,Typeface.NORMAL);
					
					pagerAdapter.notifyDataSetChanged();
					viewPager.setCurrentItem(1);
				}
			}
			
			@Override public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override public void onPageScrollStateChanged(int arg0) {}
		});
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
	    private static final float MIN_SCALE = 0.85f;
	    private static final float MIN_ALPHA = 0.5f;

	    public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();
	        int pageHeight = view.getHeight();

	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);

	        } else if (position <= 1) { // [-1,1]
	            // Modify the default slide transition to shrink the page as well
	            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
	            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
	            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
	            if (position < 0) {
	                view.setTranslationX(horzMargin - vertMargin / 2);
	            } else {
	                view.setTranslationX(-horzMargin + vertMargin / 2);
	            }

	            // Scale the page down (between MIN_SCALE and 1)
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	            // Fade the page relative to its size.
	            view.setAlpha(MIN_ALPHA +
	                    (scaleFactor - MIN_SCALE) /
	                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	private ArrayList<EventoObjeto> getListaEventosFromDB(){
		reader = new ReadTableDB(getActivity().getApplicationContext());
		return reader.fillEventListFromDB();
	}
	
	public void updateTimelineFragment(){
		getTimelineFragment();
		ArrayList<EventoObjeto> listaEventos = getListaEventosFromDB();
		fragmentTimeline.fillEventsAdapter(listaEventos);
	}
	
	private void getTimelineFragment(){
		if(fragmentTimeline == null){
			fragmentTimeline = new Page_TimeLine();
		}
	}
}
