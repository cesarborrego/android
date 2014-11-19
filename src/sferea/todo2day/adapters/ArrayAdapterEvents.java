package sferea.todo2day.adapters;

import java.util.ArrayList;

import sferea.todo2day.R;
import sferea.todo2day.beans.EventoObjeto;
import sferea.todo2day.config.CategoriasConfig;
import sferea.todo2day.config.Constants_Settings;
import sferea.todo2day.config.DataBaseSQLiteManager;
import sferea.todo2day.config.SharedPreferencesHelper;
import sferea.todo2day.fragments.Page_TimeLine;
import sferea.todo2day.fragments.SubF_Events;
import sferea.todo2day.utils.DateUtil;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ArrayAdapterEvents extends ArrayAdapter<EventoObjeto> {

	Context thisContext;
	ImageLoader imageloader;
	DisplayImageOptions options;
	String inicioTweet;
	String enTweet;
	
	public ArrayAdapterEvents(Context context, ImageLoader imageloader, DisplayImageOptions options) {
		super(context, 0);
		this.thisContext = context;
		this.imageloader = imageloader;
		this.options = options;
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder viewHolder;
		
		if (row == null) {
			row = LayoutInflater.from(getContext()).inflate(
					R.layout.row_event_smartphone, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.categoriaEvento = (TextView) row
					.findViewById(R.id.categoriaFavorito);
			viewHolder.nombreEvento = (TextView) row
					.findViewById(R.id.nombreFavorito);
			viewHolder.fechaEvento = (TextView) row
					.findViewById(R.id.fechaFavorito);
			viewHolder.distanciaEvento = (TextView) row
					.findViewById(R.id.distanciaFavorito);
			viewHolder.thumbEvento = (ImageView) row
					.findViewById(R.id.thumbnailFavorite);
			viewHolder.iconCategoria = (ImageView) row
					.findViewById(R.id.iconCategoriaFavorito);
			viewHolder.iconFavorito = (ImageView) row
					.findViewById(R.id.iconFavFavorito);
			viewHolder.iconRetweet = (ImageView) row
					.findViewById(R.id.iconRetweetFavorito);
			viewHolder.iconNewEvent = (ImageView) row
					.findViewById(R.id.iconNuevoEvento);

			row.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) row.getTag();
		}

		inicioTweet = row.getResources().getString(R.string.cadenaTweet_inicio);
		enTweet = row.getResources().getString(R.string.cadenaTweet_en);
		viewHolder.thumbEvento.setImageResource(R.drawable.event_placeholder);
		viewHolder.iconFavorito.setImageResource(R.drawable.ic_favorito);
		viewHolder.iconNewEvent.setVisibility(View.GONE);
		setIconCategoria(position, viewHolder);

		setInfoAndListenersInEvent(viewHolder, position);
		return row;
	}

	/**
	 * Esta funcion realiza: 1)Obtiene los datos (nombre, lugar) del objeto
	 * evento 2)Los hace visibles en la lista 3)Crea la cadena de Tweet 4)Pone
	 * un OnClickListener al boton de retweet el cual crea un dialogo 5)Infla y
	 * pone el layout del contenido del dialog del tweet 6)Pone los
	 * OnChangedListener al editText del tweet para mostrar al usuario el limite
	 * del tweet
	 * 
	 * @param eventView
	 * @param position
	 */
	void setInfoAndListenersInEvent(ViewHolder eventView, final int position) {

		DataBaseSQLiteManager managerDBFavorites = new DataBaseSQLiteManager(
				thisContext);

		/* Nuevos */

		SubF_Events.iFavoritos = new int[this.getCount()];

		// Asignamos al view las variables intermedias
		eventView.nombreEvento.setText(getItem(position).getNombreEvento());
		eventView.categoriaEvento.setText(getItem(position).getCategoriaEvento());
		eventView.fechaEvento.setText(DateUtil.dateTransform(getItem(position).getFechaEvento()) + " hrs.");
		eventView.distanciaEvento.setText("a " + getItem(position).getDistancia().toLowerCase() + ".");

		if (!getItem(position).getUrlImagen().equals("No disponible")) {
			imageloader.displayImage(getItem(position).getUrlImagen(), eventView.thumbEvento, options);
		}
		
		if(getItem(position).getIsNewEvent() == 1){
			eventView.iconNewEvent.setVisibility(View.VISIBLE);
		}
		else{
			eventView.iconNewEvent.setVisibility(View.GONE);
		}

		if(Page_TimeLine.listaFavoritos.contains(getItem(position).getIdOfEvent())){
			eventView.iconFavorito
			.setImageResource(R.drawable.ic_favorito_encendido);
		}
		else{
			eventView.iconFavorito
			.setImageResource(R.drawable.ic_favorito);
		}

		Typeface tf;
		try {
			tf = Typeface.createFromAsset(thisContext.getAssets(),
					"fonts/myriadpro.otf");
			eventView.nombreEvento.setTypeface(tf);
		} catch (Exception e) {
			Log.e(null, "FONT NOT AVAILABLE");
		}

		// Crea la cadena del tweet
		String tweetString = inicioTweet + " \""
				+ getItem(position).getNombreEvento() + "\" " + enTweet + " "
				+ getItem(position).getLugarEvento() + " Viva yiepa!";
		lanzarAlertDialogTweet(tweetString, eventView, position);
		agregarFavoritos(eventView, position, managerDBFavorites);
	}

	/**
	 * Evento del retweet que lanza el dialogo con la cadena completa a tweetear
	 * 
	 * @param tweetString
	 * @param eventView
	 */
	private void lanzarAlertDialogTweet(final String tweetString,
			final ViewHolder eventView, int position) {
		// Se coloca el OnClickListener al boton de retweet el cual crea un
		// dialogo

		eventView.iconRetweet.setOnClickListener(new OnClickListener() {
			// btnRet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {

				// Si el contexto es de tipo FragmentActivity
				if (v.getContext() instanceof FragmentActivity) {
					// Crea un nuevo dialog
					DialogFragment tweetDialog = new DialogFragment() {
						@Override
						public Dialog onCreateDialog(Bundle savedInstanceState) {

							// Infla la vista del contenido del dialog
							View tweetView = getActivity().getLayoutInflater()
									.inflate(R.layout.dialog_tweet, null);
							EditText editTweet = ((EditText) tweetView
									.findViewById(R.id.editText_Tweet));
							final TextView contador = ((TextView) tweetView
									.findViewById(R.id.caracteresRestantes));
							final String muyLargo = getActivity()
									.getResources().getString(
											R.string.tweetMuyLargo);

							// Pone los OnChangedListener al editText del tweet
							// para mostrar al usuario el limite del tweet
							editTweet.addTextChangedListener(new TextWatcher() {
								@Override
								public void onTextChanged(CharSequence s,
										int start, int before, int count) {
									if (140 - s.length() >= 0) {
										// si esta dentro del limite de 140
										// caracteres
										contador.setText(""
												+ (140 - s.length()));
										contador.setTextColor(Color
												.parseColor("#828282"));
									} else {
										// si pasa los 140 caracteres
										contador.setText(muyLargo);
										contador.setTextColor(Color
												.parseColor("#AB4D44"));
									}
								}

								@Override
								public void beforeTextChanged(CharSequence s,
										int start, int count, int after) {
								}

								@Override
								public void afterTextChanged(Editable s) {
								}
							});

							// Cambia el texto del editText por la cadena tweet
							// creada
							editTweet.setText(tweetString);

							// Use the Builder class for convenient dialog
							// construction
							AlertDialog.Builder builder = new AlertDialog.Builder(
									getActivity());

							// TODO cambiar cadenas para obtenerlas de R.strings
							builder.setMessage("Twittear evento?")
									.setPositiveButton(
											"Twittear!",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													if (isConnectedToInternet(getContext())) {
														Log.d(null,
																"conectado!");
														new AsyncTask<Void, Void, Void>() {

															@Override
															protected Void doInBackground(
																	Void... params) {
																Log.d(null,
																		"Empezare con lo del tweet");
																Send_Tweet(
																		tweetString,
																		eventView);
																return null;
															}

															@Override
															protected void onPostExecute(
																	Void result) {
																// TODO
																// Auto-generated
																// method stub
																super.onPostExecute(result);
															}

															@Override
															protected void onPreExecute() {
																// TODO
																// Auto-generated
																// method stub
																super.onPreExecute();
															}

														}.execute();
													}
												}
											})
									.setNegativeButton(
											"Mejor no",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
												}
											}).setView(tweetView);
							// Create the AlertDialog object and return it
							return builder.create();

						}
					};
					tweetDialog.show(((FragmentActivity) v.getContext())
							.getSupportFragmentManager(), "Tweet Event");
				}
			}
		});
	}

	/**
	 * Esta funci���n va a mandar llamar el boton de favoritos agregando los
	 * datos de la fila que se seleccione
	 * 
	 * @param eventView
	 */
	private void agregarFavoritos(final ViewHolder eventView,
			final int position, final DataBaseSQLiteManager managerDBFavorites) {

		eventView.iconFavorito.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Si el contexto es de tipo FragmentActivity
				if (v.getContext() instanceof FragmentActivity) {
					Log.d(null,
							"Posicion en el array " + String.valueOf(position));

										
					if(Page_TimeLine.listaFavoritos.contains(getItem(position).getIdOfEvent())){
							eventView.iconFavorito
							.setImageResource(R.drawable.ic_favorito);
						
							Page_TimeLine.listaFavoritos.remove(getItem(position).getIdOfEvent());
									
						managerDBFavorites.eliminar(String.valueOf(getItem(
								position).getIdOfEvent()));

						Log.d(null, "Se elimino registro "
								+ getItem(position).getNombreEvento());

					} else {
						eventView.iconFavorito
								.setImageResource(R.drawable.ic_favorito_encendido);
						
						Page_TimeLine.listaFavoritos.add(getItem(position).getIdOfEvent());

						managerDBFavorites
								.insertar(getItem(position).getIdOfEvent(),
										getItem(position).getNombreEvento(),
										getItem(position).getCategoriaEvento(),
										getItem(position).getCategoriaIDEvento(),
										getItem(position).getFechaEvento(),
										getItem(position).getDescripcion(),
										getItem(position).getFuente(),
										getItem(position).getLugarEvento(),
										getItem(position).getDireccion(),
										getItem(position).getTelefono(),
										String.valueOf(getItem(position).getDistancia()),
										String.valueOf(getItem(position).getLatEvento()), 
										String.valueOf(getItem(position).getLonEvento()),
										getItem(position).getUrlImagen(),
										String.valueOf(position), 
										String.valueOf(getItem(position).getIdOfEvent()),
										String.valueOf(getItem(position).getFechaUnix()));
						Log.d(null, "Registro Insertado en DB");
					}
				}
			}
		});
	}

	public boolean Send_Tweet(String tweet_text, final ViewHolder eventView) {

		Constants_Settings constants = new Constants_Settings();
		SharedPreferencesHelper shrpref = new SharedPreferencesHelper(
				constants.SHARED_PREF_NAME, getContext()
						.getApplicationContext());

		// Cargamos keys del shared preferences
		String accessToken = shrpref
				.Get_stringfrom_shprf(constants.TW_ACCTOKEN);
		String accessTokenSecret = shrpref
				.Get_stringfrom_shprf(constants.TW_ACCTOKEN_SECRET);

		Log.d(null, "accessToken= " + accessToken + "\naccessTokenSecret "
				+ accessTokenSecret);

		// Validamos clave cargadas del shared preferences
		if ((accessToken != null) && (accessTokenSecret != null)) {
			/*
			 * Luego creamos el objeto configuracion de T4j pasamos como
			 * parametros las claves consumer key and consumer Secret y los
			 * accessToken y accessTokensecret para la autenticacion OAuth
			 */
			Configuration conf = new ConfigurationBuilder()
					.setOAuthConsumerKey(constants.OAUTH_CONSUMER_KEY)
					.setOAuthConsumerSecret(constants.OAUTH_CONSUMER_SECRET)
					// datos obtenidos del shared pref
					.setOAuthAccessToken(accessToken)
					.setOAuthAccessTokenSecret(accessTokenSecret).build();

			// usamos lo seteado anteriormente para obtener una instancia para
			// autenticacion OAuth.
			// creamos objeto para acceder a twitter.
			Twitter t = new TwitterFactory(conf).getInstance();

			try {
				// Actualizamos estado, envamos el twwet.
				t.updateStatus(tweet_text);

			} catch (TwitterException e) {// error
				e.printStackTrace();
				((Activity) thisContext).runOnUiThread(new Runnable() {
					public void run() {
						// TODO cambiar de strings
						Toast.makeText(getContext().getApplicationContext(),
								"El Tweet ya ha sido enviado",
								Toast.LENGTH_SHORT).show();
					}
				});

				return false;

			}

			((Activity) thisContext).runOnUiThread(new Runnable() {
				public void run() {
					// int iContador=0;
					// //TODO cambiar de strings
					// iContador++;
					// contadorTweet(eventView, iContador);
					Toast.makeText(getContext().getApplicationContext(),
							"Tweet posteado exitosamente", Toast.LENGTH_SHORT)
							.show();
				}
			});
			return true;

		} else {
			((Activity) thisContext).runOnUiThread(new Runnable() {
				public void run() {
					// TODO cambiar de strings
					Toast.makeText(getContext().getApplicationContext(),
							"Inicia Sesion en twitter", Toast.LENGTH_SHORT)
							.show();
				}
			});
			return false;
		}

	}

	public boolean isConnectedToInternet(Context _context) {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		Toast.makeText(thisContext, "Verificar la conexi��n a internet",
				Toast.LENGTH_SHORT).show();
		return false;
	}

	private void setIconCategoria(int position, ViewHolder eventView) {
		CategoriasConfig categoriaId = CategoriasConfig.valueOf(getItem(
				position).getCategoriaIDEvento().toUpperCase());

		switch (categoriaId) {
		case A:
			eventView.iconCategoria.setImageResource(R.drawable.ic_bar_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_antros);
			break;
		case B:
			eventView.iconCategoria
					.setImageResource(R.drawable.ic_cultura_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_cultura);
			break;
		case C:
			eventView.iconCategoria.setImageResource(R.drawable.ic_cine_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_cine);
			break;
		case D:
			eventView.iconCategoria
					.setImageResource(R.drawable.ic_deportes_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_deportes);
			break;
		case E:
			eventView.iconCategoria
					.setImageResource(R.drawable.ic_negocios_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_negocios);
			break;
		case F:
			eventView.iconCategoria
					.setImageResource(R.drawable.ic_ninios_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_con_ninos);
			break;
		case G:
			eventView.iconCategoria
					.setImageResource(R.drawable.ic_gastronomia_white);
			getItem(position).setImagenCategoria(
					R.drawable.ic_small_gastronomia);
			break;
		case H:
			eventView.iconCategoria
					.setImageResource(R.drawable.ic_musica_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_musica);
			break;
		case I:
			eventView.iconCategoria.setImageResource(R.drawable.ic_salud_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_salud);
			break;
		case J:
			eventView.iconCategoria
					.setImageResource(R.drawable.ic_sociales_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_sociales);
			break;
		case K:
			eventView.iconCategoria
					.setImageResource(R.drawable.ic_tecnologia_white);
			getItem(position)
					.setImagenCategoria(R.drawable.ic_small_tecnologia);
			break;
		case L:
			eventView.iconCategoria.setImageResource(R.drawable.ic_verde_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_verde);
			break;
		default:
			eventView.iconCategoria
					.setImageResource(R.drawable.ic_sociales_white);
			getItem(position).setImagenCategoria(R.drawable.ic_small_sociales);
		}

	}

	static class ViewHolder {
		ImageView iconCategoria;
		ImageView thumbEvento;
		TextView nombreEvento;
		TextView categoriaEvento;
		TextView fechaEvento;
		TextView distanciaEvento;
		ImageView iconFavorito;
		ImageView iconRetweet;
		ImageView iconNewEvent;
	}
}
