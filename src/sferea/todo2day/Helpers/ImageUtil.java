package sferea.todo2day.Helpers;

import java.io.File;

import sferea.todo2day.Application;
import sferea.todo2day.R;

import android.graphics.Bitmap;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageUtil {

	private static ImageLoader imageLoader;
	private static DisplayImageOptions options;

	public static ImageLoader getImageLoader() {
		if (imageLoader == null) {
			File cacheDir = StorageUtils.getCacheDirectory(Application
					.getInstance());

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					Application.getInstance()).threadPriority(10)
					.diskCacheExtraOptions(500, 500, null)
					.diskCache(new UnlimitedDiscCache(cacheDir))
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					.memoryCacheExtraOptions(500, 500).build();

			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		}
		return imageLoader;
	}

	public static DisplayImageOptions getOptionsImageLoader() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.event_placeholder)
				.showImageForEmptyUri(R.drawable.event_placeholder)
				.showImageOnFail(R.drawable.event_placeholder)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();
		return options;
	}
}
