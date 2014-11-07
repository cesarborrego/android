package sferea.todo2day.tasks;

import sferea.todo2day.Helpers.JsonHelper;
import sferea.todo2day.Helpers.ParseJson_AddDB;
import sferea.todo2day.Helpers.ReadTableDB;
import android.app.Activity;
import android.os.AsyncTask;

public class AddMoreEventsTask extends AsyncTask<String, Void, Boolean> {
	AddMoreTaskListener listener;
	Activity activity;
	boolean result;
	
	public AddMoreEventsTask(Activity activity, AddMoreTaskListener listener){
		this.activity = activity;
		this.listener = listener;
	}

	@Override
	protected Boolean doInBackground(String... params) {
			if(!isCancelled()){
				JsonHelper helper = new JsonHelper(activity.getApplicationContext(), activity);
				helper.connectionMongo_Json(params[0]);
				ParseJson_AddDB parseJson_AddDB = new ParseJson_AddDB(activity
						.getApplicationContext(), activity);
				result = parseJson_AddDB.parseFirstJson_AddDB(helper.leerPrimerJson());
				
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(Boolean params){
		listener.onTaskCompleted(params);
	}

}
