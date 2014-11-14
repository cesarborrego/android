package sferea.todo2day.tasks;

import sferea.todo2day.Application;
import sferea.todo2day.Helpers.JsonHelper;
import sferea.todo2day.Helpers.JsonParserHelper;
import sferea.todo2day.Helpers.ReadTableDB;
import android.app.Activity;
import android.os.AsyncTask;

public class AddMoreEventsTask extends AsyncTask<String, Void, Boolean> {
	AddMoreTaskListener listener;
	JsonParserHelper jsonParser;
	Activity activity;
	boolean result = false;

	public AddMoreEventsTask(Activity activity, AddMoreTaskListener listener) {
		this.activity = activity;
		this.listener = listener;
		jsonParser = new JsonParserHelper(Application.getInstance());
	}

	@Override
	protected Boolean doInBackground(String... params) {
		boolean result = false;

		if (!isCancelled()) {
			JsonHelper helper = new JsonHelper(activity.getApplicationContext());
			String json = helper.connectionMongo_Json(params[0]);

			if (json == "")
				return result;

			result = jsonParser.addEventsToDB(json);

		}
		return result;
	}

	@Override
	protected void onPostExecute(Boolean params) {
		listener.onTaskCompleted(params);
	}

}
