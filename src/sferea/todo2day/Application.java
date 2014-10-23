package sferea.todo2day;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class Application extends android.app.Application {
	@Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
         
         // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
    }
}
