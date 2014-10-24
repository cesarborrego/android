package sferea.todo2day.Helpers;

import java.io.File;

import sferea.todo2day.Application;
import sferea.todo2day.R;

import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageUtil {
	
	private static ImageLoader imageLoader;
	private static DisplayImageOptions options;
	
	public static ImageLoader getImageLoader() {
		if (imageLoader == null) {
			File cacheDir = StorageUtils.getCacheDirectory(Application.getInstance());

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(Application.getInstance()).threadPriority(10).
					diskCache(new UnlimitedDiscCache(cacheDir)).tasksProcessingOrder(QueueProcessingType.LIFO).build();
			
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		}
		return imageLoader;
	}
	
	public static DisplayImageOptions getOptionsImageLoader() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.evento)
				.showImageForEmptyUri(R.drawable.evento)
				.showImageOnFail(R.drawable.evento).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
		return options;
	}
}
