package sferea.todo2day.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.util.Log;

public class JsonHelper {

	Context thisCOntext;

	public JsonHelper(Context c) {
		this.thisCOntext = c;
	}

	public String connectionMongo_Json(String param) {
		String result = "";
		InputStream inputStream = null;

		URL url = null;
		HttpURLConnection con = null;

		try {
			url = new URL(param);
			con = (HttpURLConnection) url.openConnection();
			inputStream = con.getInputStream();
			Log.e(null, param);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (inputStream != null) {
			result = readStream(inputStream);
		}

		return result;
	}

	public String readStream(InputStream in) {
		String line = "";
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(in));
		try {
			line = bufferedReader.readLine();
		}

		catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return line;
	}
}
