package sferea.todo2day;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class Application extends android.app.Application {
	
	private static Application singleton;
	
	public static Application getInstance(){
		return singleton;
	}
	
	@Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        singleton = this;
    }
}
