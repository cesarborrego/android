package sferea.todo2day;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import sferea.todo2day.adapters.ArrayAdapterFavorites;
import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.beans.FavoritosObjeto;
import sferea.todo2day.config.CategoriasConfig;
import sferea.todo2day.config.Constants_Settings;
import sferea.todo2day.config.DataBaseSQLiteManager;
import sferea.todo2day.config.SharedPreferencesHelper;
import sferea.todo2day.fragments.Page_Favorites;
import sferea.todo2day.fragments.Page_TimeLine;
import sferea.todo2day.helpers.CheckInternetConnection;
import sferea.todo2day.utils.ImageUtil;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailActivity extends ActionBarActivity {
	
	EventoObjeto evento;
	ArrayAdapterFavorites arrayAdapterFavorites;
	GoogleMap mMap;
	public static boolean activaRefreshFavorites_Details = false;
	DataBaseSQLiteManager manager = null;

	FavoritosObjeto favoritosObjeto;
	TextView tel;
	ImageView btnF;
	ImageView btnR;
	ImageView phoneOrg;
	ImageLoader imageloader;
	DisplayImageOptions options;
	String tweetString;
	
	
	CheckInternetConnection checkInternetConnection;
	
//	String imageHttpAddress = "http://androideity.com/wp-content/uploads/2011/12/animacionframe.png";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		imageloader = ImageUtil.getImageLoader();
		options = ImageUtil.getOptionsImageLoader();
		checkInternetConnection = new CheckInternetConnection(getApplicationContext(), this);
				
		ActionBar actionBar = getSupportActionBar();	//Obtiene el ActionBar para < Android 4.0
		actionBar.setDisplayHomeAsUpEnabled(true);		//Habilitar el boton superior
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(R.string.titleColors);
		actionBar.setIcon(R.drawable.ic_action_go_to_today_dark);
		actionBar.setBackgroundDrawable(new ColorDrawable(0xFFF78326));
		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		if(actionBarTitleId > 0 ){ 
			TextView title = (TextView)findViewById(actionBarTitleId);
			if(title!=null){title.setTextColor(Color.WHITE);}
		}
		
		btnR = (ImageView)findViewById(R.id.tweetImgId);
		btnF = (ImageView)findViewById(R.id.favoritoImgId);
		phoneOrg = (ImageView) findViewById(R.id.organizadorImgId);
		
		manager = new DataBaseSQLiteManager(getApplicationContext());
		
		DataBaseSQLiteManager managerDBFavorites = new DataBaseSQLiteManager(
				getApplicationContext());
		
		Cursor cursor = null;
		int pantalla = getResources().getConfiguration().screenLayout;
		
		if(this!=null){
			if(Page_TimeLine.eventoActivo){			
				Log.d(null, "Entra por time_line");
				Bundle extras = getIntent().getExtras();
				evento = extras.getParcelable("Event");
				SharedPreferences prefs = getSharedPreferences("latlong",Context.MODE_PRIVATE);		
				if(evento!=null){			
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("lat", String.valueOf(evento.getLatEvento()));
					editor.putString("lon", String.valueOf(evento.getLonEvento()));
					editor.commit(); 
				}
				
				cursor = managerDBFavorites.queryEventByIndex(evento.getIdOfEvent());
				
				if (cursor.getCount() > 0) {
					btnF.setImageResource(R.drawable.favorito_encendido);
				} else {
					btnF.setImageResource(R.drawable.favorito);
				}
				
				//Colocamos la info del evento en el activity_detail
				((TextView)findViewById(R.id.detallesTitulo)).setText(evento.getNombreEvento());
				imageloader.displayImage(evento.getUrlImagen(), (ImageView)findViewById(R.id.imagenHeader), options);
				imagenCategoria(evento.getCategoriaIDEvento());
				((TextView)findViewById(R.id.detallesCategoria)).setText(evento.getCategoriaEvento());
				((TextView)findViewById(R.id.detallesFecha)).setText(evento.getFechaEvento());
				((TextView)findViewById(R.id.detallesLugar)).setText(evento.getLugarEvento());
				((TextView)findViewById(R.id.detallesDescripcion)).setText(evento.getDescripcion());
//				((TextView)findViewById(R.id.detallesFuente)).setText(evento.getFuente());
				((TextView)findViewById(R.id.detallesDireccion)).setText(evento.getDireccion());
				((TextView)findViewById(R.id.detallesTelefono)).setText(evento.getTelefono());
//				((TextView)findViewById(R.id.tipoBoleto)).setText(evento.getBoleto());
//				((TextView)findViewById(R.id.precioBoleto)).setText(evento.getPrecio());
				((TextView)findViewById(R.id.detallesDistancia)).setText("a "+String.valueOf(evento.getDistancia()));
				tel = (TextView)findViewById(R.id.detallesTelefono);
				tel.setText(evento.getTelefono());
				
				String inicioTweet = getResources().getString(R.string.cadenaTweet_inicio);
				String enTweet = getResources().getString(R.string.cadenaTweet_en);
				tweetString = inicioTweet+" \""+evento.getNombreEvento()+"\" "+enTweet+" "+evento.getLugarEvento()+" Vía yiepa!";
				
				if(checkInternetConnection.isConnectedToInternet()){
					String pixelesMapa = null;
					Log.d("PANTALLA", String.valueOf(pantalla));
					if(pantalla<=34){
						pixelesMapa = "350x250";
					}else{
						if(pantalla>=268435476){
							pixelesMapa = "495x250";
						}else{
							pixelesMapa = "640x200";
						}
					}
					String imageHttpAddress ="http://maps.googleapis.com/maps/api/staticmap?" +
							"center="+evento.getLatEvento()+","+evento.getLonEvento()+"" +
							"&zoom=15" +
							"&size="+pixelesMapa+"" +
							"&scale=2" +
							"&maptype=roadmap" +
							"&markers=color:blue%7C"+evento.getLatEvento()+","+evento.getLonEvento()+"" +
							"&sensor=true_or_false";
					Log.d(null, imageHttpAddress);
					
					downloadPicture(imageHttpAddress);
				}else{
					((ImageView) findViewById(R.id.imgMapa)).setImageResource(R.drawable.nomapa);
				}
				
			} else {
				Log.d(null, "Entra por favoritos");
				Bundle extras1 = getIntent().getExtras();
				favoritosObjeto = extras1.getParcelable("Favorito");
				if(favoritosObjeto!=null){
					SharedPreferences prefs = getSharedPreferences("latlong",Context.MODE_PRIVATE);	
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("lat", String.valueOf(favoritosObjeto.getLatEvento()));
					editor.putString("lon", String.valueOf(favoritosObjeto.getLonEvento()));
					editor.commit(); 
				}
				
				cursor = managerDBFavorites.queryEventByIndex(favoritosObjeto.getIndexOfEvent());
				
				if (cursor.getCount() > 0) {
					btnF.setImageResource(R.drawable.favorito_encendido);
				} else {
					btnF.setImageResource(R.drawable.favorito);
				}
				
				((TextView)findViewById(R.id.detallesTitulo)).setText(favoritosObjeto.getNombreEvento());
				imageloader.displayImage(favoritosObjeto.getUrlImagen(), (ImageView)findViewById(R.id.imagenHeader), options);
				imagenCategoria(favoritosObjeto.getCategoriaIDEvento());
				((TextView)findViewById(R.id.detallesCategoria)).setText(favoritosObjeto.getCategoriaEvento());
				((TextView)findViewById(R.id.detallesFecha)).setText(favoritosObjeto.getFechaEvento());
				((TextView)findViewById(R.id.detallesLugar)).setText(favoritosObjeto.getLugarEvento());
				((TextView)findViewById(R.id.detallesDescripcion)).setText(favoritosObjeto.getDescripcion());
//				((TextView)findViewById(R.id.detallesFuente)).setText(favoritosObjeto.getFuente());
				((TextView)findViewById(R.id.detallesDireccion)).setText(favoritosObjeto.getDireccion());
				((TextView)findViewById(R.id.detallesTelefono)).setText(favoritosObjeto.getTelefono());
				((TextView)findViewById(R.id.etiquetaBoletos)).setText(favoritosObjeto.getBoleto());
				((TextView)findViewById(R.id.detallesDistancia)).setText("a "+String.valueOf(favoritosObjeto.getDistanciaEvento()));
				tel = (TextView)findViewById(R.id.detallesTelefono);
				tel.setText(favoritosObjeto.getTelefono());
				
				String inicioTweet = getResources().getString(R.string.cadenaTweet_inicio);
				String enTweet = getResources().getString(R.string.cadenaTweet_en);
				tweetString = inicioTweet+" \""+favoritosObjeto.getNombreEvento()+"\" "+enTweet+" "+favoritosObjeto.getLugarEvento()+" Vía yiepa!";
				
				if(checkInternetConnection.isConnectedToInternet()){
					String pixelesMapa = null;
					if(pantalla<=34){
						pixelesMapa = "350x250";
					}else{
						if(pantalla>=268435476){
							pixelesMapa = "495x250";
						}else{
							pixelesMapa = "640x200";
						}
					}
					String imageHttpAddress ="http://maps.googleapis.com/maps/api/staticmap?" +
							"center="+favoritosObjeto.getLatEvento()+","+favoritosObjeto.getLonEvento()+"" +
							"&zoom=15" +
							"&size="+pixelesMapa+"" +
							"&scale=2" +
							"&maptype=roadmap" +
							"&markers=color:blue%7C"+favoritosObjeto.getLatEvento()+","+favoritosObjeto.getLonEvento()+"" +
							"&sensor=true_or_false";
					Log.d(null, imageHttpAddress);
					
					downloadPicture(imageHttpAddress);
				}else{
					((ImageView) findViewById(R.id.imgMapa)).setImageResource(R.drawable.nomapa);
				}
			}
			
			tel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!tel.getText().toString().equals("No disponible")){
						Uri number = Uri.parse("tel:"+tel.getText().toString());
				        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
				        startActivity(callIntent);
					}else{
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorTelefonoNoDisponible), Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			phoneOrg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!tel.getText().toString().equals("No disponible")){
						Uri number = Uri.parse("tel:"+tel.getText().toString());
				        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
				        startActivity(callIntent);
					}else{
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorTelefonoNoDisponible), Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			btnF.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					agregarFavoritos();
				}
			});
			
			btnR.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					lanzarAlertDialogTweet(tweetString, btnR, arg0);
				}
			});
			
			((RelativeLayout)findViewById(R.id.capaMapa)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkInternetConnection.isConnectedToInternet()){
						Page_TimeLine.activaRuta= "si";
						Bundle bundle = new Bundle();
						if(Page_TimeLine.eventoActivo){
							bundle.putDoubleArray("LatLon", new double[]{evento.getLonEvento(), evento.getLatEvento()});
						} else if(Page_TimeLine.favoritoActivo){
							bundle.putDoubleArray("LatLon", new double[]{favoritosObjeto.getLonEvento(), favoritosObjeto.getLatEvento()});
						}
						Intent intent = new Intent(getApplicationContext(), MapActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}else{
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorVerificarConexion), Toast.LENGTH_SHORT).show();
					}
				}
			});		
		}
	}	
	
	/**
	 * Obtenemos un mapa est�tico con las coordenadas que indicaremos despues
	 * Hata ahora s�lo se pide la url a cargar del api de google
	 * @param urlMapaEstatico
	 */
	public void downloadPicture(final String urlMapaEstatico) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap downloadBitmap = downloadBitmap(urlMapaEstatico);
                    final ImageView imageView = (ImageView) findViewById(R.id.imgMapa);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(downloadBitmap);
                        }
                    });
 
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }).start();
 
    }
 
    private Bitmap downloadBitmap(String url) throws IOException {
        HttpUriRequest request = new HttpGet(url.toString());
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
 
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(entity);
 
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                    bytes.length);
            return bitmap;
        } else {
            throw new IOException("Download failed, HTTP response code "
                    + statusCode + " - " + statusLine.getReasonPhrase());
        }
    }
    
    /**
	 * Evento del retweet que lanza el dialogo con la cadena completa a tweetear
	 * @param tweetString
	 * @param eventView
	 */
	private void lanzarAlertDialogTweet(final String tweetString, final ImageView btnRet, final View v){


		//Si el contexto es de tipo FragmentActivity
		if(v.getContext() instanceof FragmentActivity){
			//Crea un nuevo dialog
			DialogFragment tweetDialog = new DialogFragment (){
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {

					//Infla la vista del contenido del dialog
					View tweetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_tweet, null);
					EditText editTweet = ((EditText)tweetView.findViewById(R.id.editText_Tweet));
					final TextView contador = ((TextView)tweetView.findViewById(R.id.caracteresRestantes));
					final String muyLargo = getActivity().getResources().getString(R.string.tweetMuyLargo);

					//Pone los OnChangedListener al editText del tweet para mostrar al usuario el limite del tweet
					editTweet.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							if(140-s.length()>=0){
								//si esta dentro del limite de 140 caracteres
								contador.setText(""+(140-s.length()));
								contador.setTextColor(Color.parseColor("#828282"));
							}else{
								//si pasa los 140 caracteres
								contador.setText(muyLargo);
								contador.setTextColor(Color.parseColor("#AB4D44"));
							}
						}
						@Override public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
						@Override public void afterTextChanged(Editable s) {}
					});

					//Cambia el texto del editText por la cadena tweet creada
					editTweet.setText(tweetString);

					// Use the Builder class for convenient dialog construction
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

					//TODO cambiar cadenas para obtenerlas de R.strings
					builder.setMessage("Twittear evento?")
					.setPositiveButton("Twittear!", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if(isConnectedToInternet(getApplicationContext())){
								Log.d(null,"conectado!");
								new AsyncTask<Void,Void,Void>() {

									@Override
									protected Void doInBackground(
											Void... params) {
										Log.d(null,"Empezare con lo del tweet");
										Send_Tweet(tweetString, getView(), btnRet);
										return null;
									}

									@Override
									protected void onPostExecute(
											Void result) {
										// TODO Auto-generated method stub
										super.onPostExecute(result);
									}

									@Override
									protected void onPreExecute() {
										// TODO Auto-generated method stub
										super.onPreExecute();
									}

								}.execute();
							}
						}
					})
					.setNegativeButton("Mejor no", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {}
					})
					.setView(tweetView);
					// Create the AlertDialog object and return it
					return builder.create();

				}
			};
			tweetDialog.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(), "Tweet Event");
		}
	
	}
	
	public boolean isConnectedToInternet(Context _context){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
           }
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorVerificarConexion), Toast.LENGTH_SHORT).show();
        return false;
     }
	
	public boolean Send_Tweet(String tweet_text, final View eventView, final ImageView btnRet) {

		Constants_Settings constants = new Constants_Settings();
		SharedPreferencesHelper shrpref = new SharedPreferencesHelper(constants.SHARED_PREF_NAME, getApplicationContext());
		
		//Cargamos keys del shared preferences 
		String accessToken = shrpref.Get_stringfrom_shprf(constants.TW_ACCTOKEN);
		String accessTokenSecret = shrpref.Get_stringfrom_shprf(constants.TW_ACCTOKEN_SECRET);

		Log.d(null,"accessToken= "+accessToken+"\naccessTokenSecret "+accessTokenSecret);

		//Validamos clave cargadas del shared preferences
		if ((accessToken != null) && (accessTokenSecret != null)) {
			/* Luego creamos el objeto configuracion de T4j
			 * pasamos como parametros las claves
			 * consumer key and consumer Secret 
			 * y los accessToken y accessTokensecret  para la autenticacion OAuth
			 */
			Configuration conf = new ConfigurationBuilder()
					.setOAuthConsumerKey(constants.OAUTH_CONSUMER_KEY)
					.setOAuthConsumerSecret(constants.OAUTH_CONSUMER_SECRET)
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
				runOnUiThread(new Runnable(){
			        public void run() {
			        	//TODO cambiar de strings
			        	Toast.makeText(getApplicationContext(),"El Tweet ya ha sido enviado", Toast.LENGTH_SHORT).show();
			        }
			    });

				return false;

			}

			runOnUiThread(new Runnable(){
		        public void run() {
		        	btnRet.setImageResource(R.drawable.ic_action_refresh_inactive);
		        	Toast.makeText(getApplicationContext(),"Tweet posteado exitosamente", Toast.LENGTH_SHORT).show();
		        }
		    });
			return true;

		} else {
			runOnUiThread(new Runnable(){
		        public void run() {
		        	//TODO cambiar de strings
		        	Toast.makeText(getApplicationContext(),"Inicia Sesion en twitter", Toast.LENGTH_SHORT).show();
		        }
		    });
			return false;
		}

	}	
	
	/**
	 * Esta funci�n va a mandar llamar el boton de favoritos agregando los datos de la fila que se seleccione
	 * @param eventView
	 */
	private void agregarFavoritos(){
		Cursor cursor;
		//Validamos si entro por time_line
		if(Page_TimeLine.eventoActivo){			
			cursor = manager.queryEventByIndex(evento.getIdOfEvent());
			//Entro en time_line y ahora obtenemos el estado de la estrella 
			if(cursor.getCount() > 0){
				//Si esta prendida solo apaga y elimina el registro
				manager.eliminar(String.valueOf(evento.getIdOfEvent()));
				
				Log.d(null, "Se elimino Registro en DB "+evento.getNombreEvento());
								//Activamos que se refresque favoritos
				activaRefreshFavorites_Details = true;
				
				btnF.setImageResource(R.drawable.favorito);
			}else{
				//Si esta apagada solo prendemos e insertamos
				manager.insertar(evento.getIdOfEvent(),
						evento.getNombreEvento(), 
						evento.getCategoriaEvento(),
						evento.getCategoriaIDEvento(),
						evento.getFechaEvento(), 
						evento.getDescripcion(), 
						evento.getFuente(), 
						evento.getLugarEvento(), 
						evento.getDireccion(), 
						evento.getTelefono(), 
						String.valueOf(evento.getDistancia()), 
						String.valueOf(evento.getLatEvento()), 
						String.valueOf(evento.getLonEvento()),
						evento.getUrlImagen(),
						String.valueOf(evento.getPosicion()),
						String.valueOf(evento.getIdOfEvent()),
						String.valueOf(evento.getFechaUnix()));
				Log.d(null, "Registro Insertado en DB");						
								
				//Activamos que se refresque favoritos
				activaRefreshFavorites_Details = true;
				
				btnF.setImageResource(R.drawable.favorito_encendido);
			}
		} else {
			//Entro por favoritos y ahora obtenemos el estado de la estrella 
			cursor = manager.queryEventByIndex(favoritosObjeto.getIndexOfEvent());
			
			if(cursor.getCount() > 0){
				//Si esta prendida solo apaga y elimina el registro
				manager.eliminar(String.valueOf(favoritosObjeto.getIndexOfEvent()));
				
				Log.d(null, "Se elimino Registro en DB "+favoritosObjeto.getNombreEvento());
				
				//Activamos que se refresque favoritos
				activaRefreshFavorites_Details = true;				
				btnF.setImageResource(R.drawable.favorito);
			}else{
				//Si esta apagada solo prendemos e insertamos
				manager.insertar(
						favoritosObjeto.getIdEvento(),
						favoritosObjeto.getNombreEvento(), 
						favoritosObjeto.getCategoriaEvento(),
						favoritosObjeto.getCategoriaIDEvento(),
						favoritosObjeto.getFechaEvento(), 
						favoritosObjeto.getDescripcion(), 
						favoritosObjeto.getFuente(), 
						favoritosObjeto.getLugarEvento(), 
						favoritosObjeto.getDireccion(), 
						favoritosObjeto.getTelefono(), 
						String.valueOf(favoritosObjeto.getDistanciaEvento()), 
						String.valueOf(favoritosObjeto.getLatEvento()), 
						String.valueOf(favoritosObjeto.getLonEvento()),
						favoritosObjeto.getUrlImagen(),
						String.valueOf(favoritosObjeto.getPosicion()),
						String.valueOf(favoritosObjeto.getIndexOfEvent()),
						String.valueOf(favoritosObjeto.getFechaUnix()));
				Log.d(null, "Registro Insertado en DB");						
				
				//Activamos que se refresque favoritos
				activaRefreshFavorites_Details = true;
				btnF.setImageResource(R.drawable.favorito_encendido);
			}
		}
		
		Page_Favorites.adapterFavorites.notifyDataSetChanged();
	}
	
	public void imagenCategoria(String categoriaID){
		CategoriasConfig categoriaId = CategoriasConfig.valueOf(categoriaID.toUpperCase());
		switch (categoriaId) {
		case A:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.musica);
			break;

		case B:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.cultura);
			break;
			
		case C:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.cine_y_teatro);
			break;
			
		case D:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.deportes);
			break;
			
		case E:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.emprendedores_y_negocios);
			break;
			
		case F:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.con_ninnos);
			break;
			
		case G:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.gastronomia);
			break;
			
		case H:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.musica);
			break;
			
		case I:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.salud);
			break;
			
		case J:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.sociales);
			break;
			
		case K:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.tecnologia);
			break;
			
		case L:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.verde_y_responsabilidad_social);
			break;
			
		case M:
			((ImageView)findViewById(R.id.iconCategoria)).setImageResource(R.drawable.sociales);
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
		
//		managerDB.cerrarDB();
		super.onBackPressed();
	}
}

