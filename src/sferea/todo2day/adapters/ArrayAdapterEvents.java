package sferea.todo2day.adapters;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Vector;

import sferea.todo2day.DetailActivity;
import sferea.todo2day.R;
import sferea.todo2day.Helpers.ImageUtil;
import sferea.todo2day.config.Constants_Settings;
import sferea.todo2day.config.DataBaseSQLiteManager;
import sferea.todo2day.config.SharedPreferencesHelper;
import sferea.todo2day.subfragments.Page_TimeLine;
import sferea.todo2day.subfragments.SubF_Events;
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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ArrayAdapterEvents extends ArrayAdapter<List<EventoObjeto>> {
	
	Context thisContext;
	List<EventoObjeto> objectArrayList;
	public static int positionFav;
	public static ImageView iconFavorite = null;
	public static ImageView iconFavoriteInactive = null;
	private byte[] img=null;
	int favoritosAdd = 2;
	ImageLoader imageloader;
	DisplayImageOptions options;
	String inicioTweet; 
	String enTweet; 
	
	public ArrayAdapterEvents(Context context, int resource, int textViewResourceId, List objects){
		super(context, resource, textViewResourceId, objects);
		
		this.objectArrayList = (List<EventoObjeto>)objects;
		this.thisContext = context;
		imageloader = ImageUtil.getImageLoader();
		options = ImageUtil.getOptionsImageLoader();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		View v = convertView;
		if(v == null){
			v = LayoutInflater.from(getContext()).inflate(R.layout.row_event_responsive, null);
			viewHolder = new ViewHolder();
			viewHolder.botonFavoritos = (RelativeLayout)v.findViewById(R.id.botonFavEvent);
			viewHolder.botonRetweetEvent = (RelativeLayout)v.findViewById(R.id.botonRetweetEvent);
			viewHolder.categoriaEvento = (TextView)v.findViewById(R.id.categoriaFavorito);
			viewHolder.nombreEvento = (TextView)v.findViewById(R.id.nombreFavorito);
			viewHolder.fechaEvento = (TextView)v.findViewById(R.id.fechaFavorito);
			viewHolder.lugarEvento = (TextView)v.findViewById(R.id.lugarFavorito);
			viewHolder.distanciaEvento = (TextView)v.findViewById(R.id.distanciaFavorito);
			viewHolder.descripcionEvento = (TextView)v.findViewById(R.id.descripcionFavorito);
			viewHolder.thumbEvento = (ImageView)v.findViewById(R.id.thumbnailFavorite);
			viewHolder.iconCategoria = (ImageView)v.findViewById(R.id.iconCategoriaFavorito);
			viewHolder.iconFavorito = (ImageView)v.findViewById(R.id.iconFavFavorito);
			viewHolder.iconRetweet = (ImageView)v.findViewById(R.id.iconRetweetFavorito);
			
			v.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder)v.getTag();
		}
		
		inicioTweet = v.getResources().getString(R.string.cadenaTweet_inicio);
		enTweet = v.getResources().getString(R.string.cadenaTweet_en);
		
		final int pos = position;
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Constants_Settings constants = new Constants_Settings();
				SharedPreferencesHelper shrpref = new SharedPreferencesHelper(constants.SHARED_PREF_NAME, thisContext);
				String dualPane = shrpref.Get_stringfrom_shprf(constants.DUAL_PANE);			
				
				if(dualPane.equals("true")){
//					ShowFragment();
				}else{
					Bundle bundle = new Bundle();
					bundle.putParcelable("Event", objectArrayList.get(pos));
					Intent intent = new Intent(thisContext, DetailActivity.class);
					intent.putExtras(bundle);
					thisContext.startActivity(intent);
					Page_TimeLine.eventoActivo = true;
					Page_TimeLine.favoritoActivo = false;
				}
			}
		});
		
		setInfoAndListenersInEvent(viewHolder, pos);
		return v;
	}
	
	/**
	 * Esta funcion realiza:
	 * 1)Obtiene los datos (nombre, lugar) del objeto evento
	 * 2)Los hace visibles en la lista
	 * 3)Crea la cadena de Tweet
	 * 4)Pone un OnClickListener al boton de retweet el cual crea un dialogo
	 * 5)Infla y pone el layout del contenido del dialog del tweet
	 * 6)Pone los OnChangedListener al editText del tweet para mostrar al usuario el limite del tweet
	 * 
	 * @param eventView
	 * @param position
	 */
	void setInfoAndListenersInEvent(ViewHolder eventView, final int position){
		
		DataBaseSQLiteManager managerDB = new DataBaseSQLiteManager(thisContext);
		
		String nombreEvento; String categoriaEvento; String fechaEvento; String horarioEvento; String urlImagen;
		String lugarEvento; String distanciaEvento;
		
		Bitmap imagenEvento;
		int imagenCategoria;
		
		/*Nuevos*/
		String descripcionEvento; String fuenteEvento; String direccionEvento; String boletoEvento;String telefono;
		double lat; double lon;
		
		//Obtenemos el objeto
		EventoObjeto eventoObjeto = objectArrayList.get(position);
		
		nombreEvento = eventoObjeto.getNombreEvento();
		categoriaEvento = eventoObjeto.getCategoriaEvento();
		fechaEvento = eventoObjeto.getFechaEvento();
		horarioEvento = eventoObjeto.getHoraEvento();		
		/**Nuevos elementos para llenar el Evento**/
		descripcionEvento = eventoObjeto.getDescripcion();
		fuenteEvento = eventoObjeto.getFuente();
		direccionEvento = eventoObjeto.getDireccion();
		telefono = eventoObjeto.getTelefono();
		boletoEvento = eventoObjeto.getBoleto();
		lat = eventoObjeto.getLatEvento();
		lon = eventoObjeto.getLonEvento();
		lugarEvento = eventoObjeto.getLugarEvento();
		distanciaEvento = "A "+eventoObjeto.getDistancia();
		imagenEvento = eventoObjeto.getImagenEvento(); 
		urlImagen = eventoObjeto.getUrlImagen();
		imagenCategoria = eventoObjeto.getImagenCategoria();
		
		SubF_Events.iFavoritos = new int [objectArrayList.size()];

		String[] categorias = thisContext.getResources().getStringArray(R.array.categorias);
		int[] id_small_icons = {R.drawable.ic_small_antros,R.drawable.ic_small_cine, R.drawable.ic_small_cultura, R.drawable.ic_small_deportes,
				R.drawable.ic_small_negocios, R.drawable.ic_small_con_ninos, R.drawable.ic_small_gastronomia, R.drawable.ic_small_musica,
				R.drawable.ic_small_salud, R.drawable.ic_small_sociales, R.drawable.ic_small_tecnologia, R.drawable.ic_small_verde};
		for(int i=0; i<categorias.length; i++){
			if(categorias[i].equals(categoriaEvento))
				eventView.iconCategoria.setImageResource(id_small_icons[i]);
				id_small_icons[2] = imagenCategoria;
		}
		
		//Asignamos al view las variables intermedias	
		eventView.nombreEvento.setText(nombreEvento);
		eventView.categoriaEvento.setText(categoriaEvento);
		eventView.fechaEvento.setText(fechaEvento);
		eventView.lugarEvento.setText(lugarEvento);
		eventView.distanciaEvento.setText(distanciaEvento);
		eventView.descripcionEvento.setText(descripcionEvento);
		
		imageloader.displayImage(urlImagen, eventView.thumbEvento, options);
		/*if(imagenEvento!=null){
			((ImageView)eventView.findViewById(R.id.thumbnailFavorite)).setImageBitmap(imagenEvento);
		}
		*/
		//Obtenemos el array guardado en el sharepreference FAVORITES para activar o desactivar los favoritos
//		SharedPreferences shrpref_fav = thisContext.getSharedPreferences("FAVORITES", Context.MODE_PRIVATE);
		
		//dependiendo de los resultados prendemos o apagamos las estrellas de favoritos
		Cursor cursor = managerDB.cargarTablas();
		Vector<String> nombreEventoEncontrados = new Vector<String>();
		Vector<Integer> posicion = new Vector<Integer>();
		boolean activaEliminarDB= false;
		try{
			if(cursor.moveToFirst()){
				do{
					nombreEventoEncontrados.addElement(
							cursor.getString(cursor.getColumnIndex("TITULO_EVENTO")));
					posicion.add(
							Integer.parseInt(cursor.getString(cursor.getColumnIndex("POSICION")).toString()));
				}while (cursor.moveToNext());	
			}
		} finally{
			cursor.close();
//			managerDB.cerrarDB();
		}
		
		for(int j =0; j<nombreEventoEncontrados.size(); j++){
			if(nombreEventoEncontrados.get(j).toString().equals(eventoObjeto.getNombreEvento())){
				activaEliminarDB= true;
				eventView.iconFavorito.setImageResource(R.drawable.ic_action_important_active);
				Page_TimeLine.prendeEstrellaDetails = true;
				Page_TimeLine.prendeEstrellaTime_Line[objectArrayList.get(position).getPosicion()] = true;
			}
		}
		
		eventView.nombreEvento.setText(objectArrayList.get(position).getNombreEvento());
		
		Typeface tf;
		try {
			tf = Typeface.createFromAsset(thisContext.getAssets(), "fonts/myriadpro.otf");
			eventView.nombreEvento.setTypeface(tf);
		} catch (Exception e) {
			Log.e(null,"FONT NOT AVAILABLE");
		}
		
		//Crea la cadena del tweet
		String tweetString = inicioTweet+" \""+objectArrayList.get(position).getNombreEvento()+"\" "+enTweet+" "+lugarEvento+" Vía yiepa!";
		lanzarAlertDialogTweet(tweetString, eventView,position);
		agregarFavoritos(eventView, position, managerDB);
	}
	
	/**
	 * Evento del retweet que lanza el dialogo con la cadena completa a tweetear
	 * @param tweetString
	 * @param eventView
	 */
	private void lanzarAlertDialogTweet(final String tweetString, final ViewHolder eventView, int position){
		//Se coloca el OnClickListener al boton de retweet el cual crea un dialogo
		eventView.botonRetweetEvent.setOnClickListener(new OnClickListener() {
//		btnRet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {

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
									if(isConnectedToInternet(getContext())){
										Log.d(null,"conectado!");
										new AsyncTask<Void,Void,Void>() {

											@Override
											protected Void doInBackground(
													Void... params) {
												Log.d(null,"Empezare con lo del tweet");
												Send_Tweet(tweetString, eventView);
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
		});
	}
	
	/**
	 * Esta funci�n va a mandar llamar el boton de favoritos agregando los datos de la fila que se seleccione
	 * @param eventView
	 */
	private void agregarFavoritos(final ViewHolder eventView,
			final int position, final DataBaseSQLiteManager manager){
		
		eventView.botonFavoritos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Si el contexto es de tipo FragmentActivity
				if(v.getContext() instanceof FragmentActivity){
					Log.d(null, "Posicion en el array "+String.valueOf(position));
					if(Page_TimeLine.prendeEstrellaTime_Line[position]){
						//Apagamos estrellas en time line y details y eliminamos
						Page_TimeLine.prendeEstrellaDetails = false;
						Page_TimeLine.prendeEstrellaTime_Line[position] = false;
						
						eventView.iconFavorito.setImageResource(R.drawable.ic_action_important);
						
						manager.eliminar(String.valueOf(objectArrayList.get(position).getIndexOfEvent()));
						
						Log.d(null, "Se elimino registro "+objectArrayList.get(position).getNombreEvento());
						
					} else {
						//Prendemos e insertamos
						Page_TimeLine.prendeEstrellaDetails = true;
						Page_TimeLine.prendeEstrellaTime_Line[position] = true;
						
						eventView.iconFavorito.setImageResource(R.drawable.ic_action_important_active);
						
						manager.insertar(objectArrayList.get(position).getNombreEvento(), 
								objectArrayList.get(position).getCategoriaEvento(),
								objectArrayList.get(position).getFechaEvento(), 
								objectArrayList.get(position).getDescripcion(), 
								objectArrayList.get(position).getFuente(), 
								objectArrayList.get(position).getLugarEvento(), 
								objectArrayList.get(position).getDireccion(), 
								objectArrayList.get(position).getTelefono(), 
								objectArrayList.get(position).getBoleto(), 
								String.valueOf(objectArrayList.get(position).getDistancia()), 
								String.valueOf(objectArrayList.get(position).getLatEvento()), 
								String.valueOf(objectArrayList.get(position).getLonEvento()),
								objectArrayList.get(position).getUrlImagen(),
								String.valueOf(position),
								String.valueOf(objectArrayList.get(position).getIndexOfEvent()),
								String.valueOf(objectArrayList.get(position).getFechaUnix()));
						Log.d(null, "Registro Insertado en DB");
					}
				}
			}		
		});		
	}
	
	private byte[] codificarBitmap(Bitmap b) {
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		//Calidad de imagen png al 30%
        b.compress(Bitmap.CompressFormat.PNG, 30, bos);
        return img=bos.toByteArray();		
	}	
	
	public boolean Send_Tweet(String tweet_text, final ViewHolder eventView) {

		Constants_Settings constants = new Constants_Settings();
		SharedPreferencesHelper shrpref = new SharedPreferencesHelper(constants.SHARED_PREF_NAME, getContext().getApplicationContext());
		
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
				((Activity)thisContext).runOnUiThread(new Runnable(){
			        public void run() {
			        	//TODO cambiar de strings
			        	Toast.makeText(getContext().getApplicationContext(),"El Tweet ya ha sido enviado", Toast.LENGTH_SHORT).show();
			        }
			    });

				return false;

			}

			((Activity)thisContext).runOnUiThread(new Runnable(){
		        public void run() {
//		        	int iContador=0;
//		        	//TODO cambiar de strings
//		        	iContador++;
//		        	contadorTweet(eventView, iContador);
		        	eventView.iconRetweet.setImageResource(R.drawable.ic_action_refresh_inactive);
		        	Toast.makeText(getContext().getApplicationContext(),"Tweet posteado exitosamente", Toast.LENGTH_SHORT).show();
		        }
		    });
			return true;

		} else {
			((Activity)thisContext).runOnUiThread(new Runnable(){
		        public void run() {
		        	//TODO cambiar de strings
		        	Toast.makeText(getContext().getApplicationContext(),"Inicia Sesion en twitter", Toast.LENGTH_SHORT).show();
		        }
		    });
			return false;
		}

	}	
	
	public void contadorTweet(View eventView, int iConta){
		((TextView)eventView.findViewById(R.id.textRetweetCounter)).setText(String.valueOf(iConta));
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
        Toast.makeText(thisContext, "Verificar la conexión a internet", Toast.LENGTH_SHORT).show();
        return false;
     }
	 
	 class ViewHolder{
		 ImageView iconCategoria;
		 ImageView thumbEvento;
		 TextView nombreEvento;
		 TextView categoriaEvento;
		 TextView fechaEvento;
		 TextView lugarEvento;
		 TextView distanciaEvento;
		 TextView descripcionEvento;
		 RelativeLayout botonRetweetEvent;
		 TextView tweetCounter;
		 RelativeLayout botonFavoritos;
		 ImageView iconFavorito;
		 ImageView iconRetweet;
	 }
}
