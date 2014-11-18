package sferea.todo2day.adapters;

import sferea.todo2day.fragments.Page_Favorites;
import sferea.todo2day.fragments.Page_TimeLine;
import sferea.todo2day.listeners.UpdateableFragmentListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapterEvents extends FragmentStatePagerAdapter {

	public PagerAdapterEvents(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment frag = null;
		if(arg0==0){
			frag = new Page_TimeLine();
		}else if(arg0==1){
			frag = new Page_Favorites();
		}
		return frag;
	}

	@Override
	public int getCount() {
		return 2;
	}
	
	@Override 
	public int getItemPosition(Object object) {
	    if (object instanceof UpdateableFragmentListener) {
	        ((UpdateableFragmentListener) object).onUpdated();
	    } 
	    //don't return POSITION_NONE, avoid fragment recreation.  
	    return super.getItemPosition(object);
	} 

}
