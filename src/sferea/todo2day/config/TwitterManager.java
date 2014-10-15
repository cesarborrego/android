package sferea.todo2day.config;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import sferea.todo2day.R;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Clase que realiza las funciones de twitter
 * @author maw
 *
 */
public class TwitterManager {

	private Twitter twitter;
	private RequestToken mRequestToken = null;
	private AccessToken accesToken;
	//TODO Que haces con el accesToken, guadarlo o que onda
	
	Constants_Settings constants = new Constants_Settings();
	
	//twitter
	public String OAUTH_CONSUMER_KEY = constants.OAUTH_CONSUMER_KEY;
	public String OAUTH_CONSUMER_SECRET = constants.OAUTH_CONSUMER_SECRET;
	public String CALLBACKURL = constants.TWITTER_CALLBACK;
	
	// Registros a guardar en Shared preferences
	public String TW_ACCTOKEN = constants.TW_ACCTOKEN;
	public String TW_ACCTOKEN_SECRET = constants.TW_ACCTOKEN_SECRET;
	
	//Username and AvatarURL
	public String USER_NAME = constants.USER_NAME;
	public String AVATAR = constants.AVATAR_URL;
	
	//instancia a clase para manejar el acceso al Shared preferences
	SharedPreferencesHelper shrpref;
	
	/**
	 * Constructor recibe como parametro el contexto 
	 * @param context
	 */
	public TwitterManager(Context context) {
		// creo referencia para manejar shared prefrences
		shrpref = new SharedPreferencesHelper(constants.SHARED_PREF_NAME, context);

		// Empezamos a crear objeto para interactuar con twitter
		twitter = new TwitterFactory().getInstance();
		mRequestToken = null;

		//Seteamos claves para la autenticacion usando OAuth
		twitter.setOAuthConsumer(OAUTH_CONSUMER_KEY, OAUTH_CONSUMER_SECRET);

		String callbackURL = CALLBACKURL;

		try {
			//Tomamos request token en base a callback URL
			//es utilizado para en el web view para obtener las claves
			mRequestToken = twitter.getOAuthRequestToken(callbackURL);
		} catch (TwitterException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Guarda nuevo OAuth en shared preferences
	 * @param OAuth
	 */
	public void Store_OAuth_verifier(final String OAuth, final Context viewContext, final RelativeLayout botonIniciarSesion,
			final RelativeLayout botonCerrarSesion, final TextView statusSesion) {
		
		// Pair up our request with the response
		new AsyncTask<Void, Void, AccessToken>() {

			@Override
			protected AccessToken doInBackground(Void... params) {
				try {
					return twitter.getOAuthAccessToken(mRequestToken, OAuth);
				} catch (TwitterException e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(AccessToken result) {
				if(result!=null){
					shrpref.Write_String(TW_ACCTOKEN, result.getToken());
					shrpref.Write_String(TW_ACCTOKEN_SECRET, result.getTokenSecret());
					
					shrpref.Write_String(USER_NAME,result.getScreenName());
					statusSesion.setText(viewContext.getResources().getString(R.string.bienvenido)+" "+result.getScreenName()+"!");
					
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... params) {
							try {
								shrpref.Write_String(AVATAR,((User)twitter.showUser(twitter.getId())).getProfileImageURL());
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (TwitterException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return null;
						}
					}.execute();
					botonIniciarSesion.setVisibility(View.GONE);
					botonCerrarSesion.setVisibility(View.VISIBLE);
				}
				super.onPostExecute(result);
			}
			
			
		}.execute();

		//shrpref.Write_String(TW_ACCTOKEN, at.getToken());
		//shrpref.Write_String(TW_ACCTOKEN_SECRET, at.getTokenSecret());

	}
	
	// Devolvemos autentication URL
	public String get_AuthenticationURL() {
		return mRequestToken.getAuthenticationURL();
	}



	// Borramos del shared preferences valores TW_ACCTOKEN y TW_ACCTOKEN_SECRET
	public void Logoff(Context viewContext) {
		shrpref.Remove_Value(TW_ACCTOKEN);
		shrpref.Remove_Value(TW_ACCTOKEN_SECRET);
		shrpref.Remove_Value(USER_NAME);
		shrpref.Remove_Value(AVATAR);
		
		/*
		botonIniciarSesion.setVisibility(View.VISIBLE);
		botonCerrarSesion.setVisibility(View.GONE);
		statusSesion.setText(viewContext.getResources().getString(R.string.titSesion));
		*/
	}

	/*Verifica si en el shared preferences estan guardados 
	TW_ACCTOKEN
	TW_ACCTOKEN_SECRET*/
	public boolean verify_logindata(){


		if (shrpref.isExist(TW_ACCTOKEN))
			{
			if (shrpref.isExist(TW_ACCTOKEN_SECRET)){				

				//ok estan estos datos guardados
				return true;
			}else{
				return false;
			}
			}else{
				//tiene que estar si o si los dos 
				return false;
			}

	}

	// Enviamos Tweet
	public boolean Send_Tweet(String tweet_text) {

		//Cargamos keys del shared preferences 
		String accessToken = shrpref.Get_stringfrom_shprf(TW_ACCTOKEN);
		String accessTokenSecret = shrpref.Get_stringfrom_shprf(TW_ACCTOKEN_SECRET);

		Log.e("accessToken= "+accessToken,"accessTokenSecret "+accessTokenSecret);

		//Validamos clave cargadas del shared preferences
		if ((accessToken != null) && (accessTokenSecret != null)) {
			/* Luego creamos el objeto configuracion de T4j
			 * pasamos como parametros las claves
			 * consumer key and consumer Secret 
			 * y los accessToken y accessTokensecret  para la autenticacion OAuth
			 */
			Configuration conf = new ConfigurationBuilder()
					.setOAuthConsumerKey(OAUTH_CONSUMER_KEY)
					.setOAuthConsumerSecret(OAUTH_CONSUMER_SECRET)
					//datos obtenidos del shared pref
					.setOAuthAccessToken(accessToken)
					.setOAuthAccessTokenSecret(accessTokenSecret).build();

            //usamos lo seteado anteriormente para obtener una instancia para autenticacion OAuth.
			//creamos objeto para acceder a twitter.
			Twitter t = new TwitterFactory(conf).getInstance();

			try {
                //Actualizamos estado, envamos el twwet.
				t.updateStatus(tweet_text);

			} catch (TwitterException e) {//error
				e.printStackTrace();
				Log.e("Error Tweet NO Enviado!!", "FAIL");

				return false;

			}

			Log.e("Tweet Enviado!!", "ok");
			return true;

		} else {
			Log.e("Error Tweet NO Enviado!!", "FAIL");
			return false;
		}

	}
}
