package sferea.todo2day.adapters;

import sferea.todo2day.subfragments.Page_Favorites;
import sferea.todo2day.subfragments.Page_TimeLine;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapterEvents extends FragmentPagerAdapter {

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

}
