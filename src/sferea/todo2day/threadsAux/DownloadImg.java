package sferea.todo2day.threadsAux;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import sferea.todo2day.adapters.FavoritosObjeto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadImg extends AsyncTask<String, Void, Bitmap>{

	@Override
	protected Bitmap doInBackground(String... params) {
		HttpUriRequest request = new HttpGet(params[0]);
        HttpClient httpClient = new DefaultHttpClient();
        Bitmap bitmap = null;
        HttpResponse response = null;
		try {
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            byte[] bytes = null;
			try {
				bytes = EntityUtils.toByteArray(entity);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 
            bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                    bytes.length);
        }
        Log.d(null, "Imagen cargada");
        return bitmap;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
	}
}
