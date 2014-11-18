package sferea.todo2day.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sferea.todo2day.DetailActivity;
import sferea.todo2day.MainActivity;
import sferea.todo2day.MapActivity;
import sferea.todo2day.R;
import sferea.todo2day.SplashActivity;
import sferea.todo2day.config.Constants_Settings;
import sferea.todo2day.config.SharedPreferencesHelper;
import sferea.todo2day.config.TwitterManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SubF_Settings extends Fragment {
	
	//private RequestToken mRequestToken = null;
	private TwitterManager tweet_hlp;
	
	//private int TWITTER_AUTH=1;
	
	public SubF_Settings(){}
	Context contextThis;
	
	RelativeLayout botonIniciarSesion;
	RelativeLayout botonCerrarSesion;
	TextView statusSesion;
	
	SeekBar seekbarDistancia;
	SeekBar seekbarUbicacion;
	TextView textoDistancia, preguntaDistancia, preguntaUbicacion, preguntaSesion;

	String categorias = "";
	
	public static boolean seActivoSettings = false; 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		final View view = inflater.inflate(R.layout.subfrag_settings, container, false);
		
		System.out.println(Page_TimeLine.activaRuta);
		System.out.println(Page_TimeLine.activaUbicate);
		botonIniciarSesion = ((RelativeLayout)view.findViewById(R.id.botonIniciarSesion));
		botonCerrarSesion = ((RelativeLayout)view.findViewById(R.id.botonCerrarSesion));
		statusSesion = ((TextView)view.findViewById(R.id.statusSesion));
		
		seekbarDistancia = ((SeekBar)view.findViewById(R.id.seekbarDistancia));
		seekbarUbicacion = ((SeekBar)view.findViewById(R.id.seekbarUbicacion));
		textoDistancia = ((TextView)view.findViewById(R.id.textoDistancia));
		
		preguntaDistancia = ((TextView)view.findViewById(R.id.preguntaDistanciaID));
		preguntaUbicacion = ((TextView)view.findViewById(R.id.pregunta_ubicacion_ID));
		preguntaSesion = ((TextView)view.findViewById(R.id.pregunta_sesion_ID));
			
		final SharedPreferences prefsMap = view.getContext().getSharedPreferences("map",Context.MODE_PRIVATE);
    	
		SharedPreferences prefsCategorias = view.getContext().getSharedPreferences("Distancia",Context.MODE_PRIVATE);	
		SharedPreferences.Editor editorCategoriasString = prefsCategorias.edit();

		checkSettings();
		
		preguntaUbicacion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Puedes obtener tu ubicaci??n de manera automatica por medio del GPS " +
						"o si lo prefieres, puedes ubicarte desde otra posici??n, por ejemplo, la casa de un amigo, escuela, etc.")
//				        .setTitle("Atenci???n!!")
				        .setCancelable(false)
				        .setNeutralButton("Aceptar",
				                new DialogInterface.OnClickListener() {
				                    public void onClick(DialogInterface dialog, int id) {
				                        dialog.cancel();
				                    }
				                });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		
		preguntaDistancia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Aumenta el radio de b??squeda de eventos respecto a tu posici??n. ??Pru??balo!")
				//				        .setTitle("Atenci???n!!")
				.setCancelable(false)
				.setNeutralButton("Aceptar",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		preguntaSesion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Logueate con tu cuenta de Twitter y comparte con todos tus amigos los eventos que m??s te gusten!")
				//		        .setTitle("Atenci???n!!")
				.setCancelable(false)
				.setNeutralButton("Aceptar",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
        botonIniciarSesion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				validar_login(true);
			}
		});
        
        botonCerrarSesion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(tweet_hlp!=null)
					tweet_hlp.Logoff(v.getContext());
				checkSettings();
			}
		});
        
        switch (SplashActivity.distanciaEvento) {
		case 5:
			seekbarDistancia.setProgress(0);
			textoDistancia.setText(""+SplashActivity.distanciaEvento+"km");
			break;
		case 10:
			textoDistancia.setText(""+SplashActivity.distanciaEvento+"km");
			seekbarDistancia.setProgress(1);
			break;
		case 15:
			textoDistancia.setText(""+SplashActivity.distanciaEvento+"km");
			seekbarDistancia.setProgress(2);
			break;
		case 20:
			textoDistancia.setText(""+SplashActivity.distanciaEvento+"km");
			seekbarDistancia.setProgress(3);
			break;
		case 50:
			textoDistancia.setText(""+SplashActivity.distanciaEvento+"km");
			seekbarDistancia.setProgress(4);
			break;
		}
        seekbarDistancia.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override public void onStopTrackingTouch(SeekBar seekBar) {
//				downloadJSON(19.355582, -99.186726);
			}
			@Override public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				Log.d(null,"progress = "+progress);
				switch(progress){
				case 0:
					textoDistancia.setText("5km");
					SplashActivity.distanciaEvento = 5;
					seekbarUbicacion.setSaveEnabled(true);
					Log.d(null, String.valueOf(SplashActivity.distanciaEvento));
					break;
				case 1:
					textoDistancia.setText("10km");
					SplashActivity.distanciaEvento = 10;
					seekbarUbicacion.setSaveEnabled(true);
					Log.d(null, String.valueOf(SplashActivity.distanciaEvento));
					break;
				case 2:
					textoDistancia.setText("15km");
					SplashActivity.distanciaEvento = 15;
					seekbarUbicacion.setSaveEnabled(true);
					Log.d(null, String.valueOf(SplashActivity.distanciaEvento));
					break;
				case 3:
					textoDistancia.setText("20km");
					SplashActivity.distanciaEvento = 20;
					seekbarUbicacion.setSaveEnabled(true);
					Log.d(null, String.valueOf(SplashActivity.distanciaEvento));
					break;
				case 4:
					textoDistancia.setText("50km");
					SplashActivity.distanciaEvento = 50;
					seekbarUbicacion.setSaveEnabled(true);
					Log.d(null, String.valueOf(SplashActivity.distanciaEvento));
					break;
				}
				//TODO Agregar rutina para cambiar kilometros
			}
		});
        
        if(Page_TimeLine.activaUbicate.equals("si")){
        	seekbarUbicacion.setProgress(1);
        }
        
        seekbarUbicacion.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				switch (progress) {				
				case 0:
					//Activa gps
					Page_TimeLine.activaUbicate="no";
					seekbarUbicacion.setSaveEnabled(true);
					Page_TimeLine.latOrigin = 19.355582;
					Page_TimeLine.lonOrigin = -99.186726;
					break;

				case 1:
					//Clic largo en mapa	
					Page_TimeLine.activaUbicate="si";
					Page_TimeLine.activaRuta="no";
					seekbarUbicacion.setSaveEnabled(true);					
					Intent intent = new Intent(view.getContext(), MapActivity.class);
					startActivity(intent);
					break;
				}
				
			}
		});
        
        seActivoSettings = true;
		return view;
	}

	public void validar_login(final Boolean desdeBoton) {
		
		if(isConnectedToInternet(getActivity().getApplicationContext())){
		
			new AsyncTask<Void, Void, Void>() {
	
				@Override
				protected Void doInBackground(Void... params) {
					tweet_hlp = new TwitterManager(getActivity().getApplicationContext());
					return null;
				}
	
				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					//Verificamos que haya tokens guardados, de lo contrario las obtenemos con un webView
					if(!tweet_hlp.verify_logindata()){
						
						final WebView wv = (WebView)getActivity().findViewById(R.id.settingsWebview);
	//					wv.setVisibility(View.VISIBLE);
						wv.setWebViewClient(new WebViewClient(){
	
							@Override
							public boolean shouldOverrideUrlLoading(WebView view, String url) {
								if (url.startsWith("twitterapp://connect")) {
									wv.setVisibility(View.GONE);
									Uri uri = Uri.parse(url);
							        String oauthVerifier = uri.getQueryParameter("oauth_verifier");
							        if(oauthVerifier!=null){
										//Grabamos el valor de oauthVerifier en el shared preferences
										tweet_hlp.Store_OAuth_verifier(oauthVerifier, getActivity().getApplicationContext(),
												botonIniciarSesion, botonCerrarSesion,statusSesion);
							        }
								}
								return super.shouldOverrideUrlLoading(view, url);
							}
						});
						wv.loadUrl(tweet_hlp.get_AuthenticationURL());
						wv.setVisibility(View.VISIBLE);
						
					}else{
						checkSettings();
					}
				}
				
				
			}.execute();
		
		}else{
			Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.checkInternet), Toast.LENGTH_SHORT).show();
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
        return false;
    }
	
	public void checkSettings(){
		
		final Context settingsContext = getActivity().getApplicationContext();
		
		if(tweet_hlp==null && isConnectedToInternet(settingsContext)){
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					tweet_hlp = new TwitterManager(settingsContext);
					return null;
				}
			}.execute();
		}
		
		Constants_Settings constants = new Constants_Settings();
		SharedPreferencesHelper shrpref = new SharedPreferencesHelper(constants.SHARED_PREF_NAME, settingsContext);
		String userName = shrpref.Get_stringfrom_shprf(constants.USER_NAME);
		
		if(userName!=null){
			botonIniciarSesion.setVisibility(View.GONE);
			botonCerrarSesion.setVisibility(View.VISIBLE);
			statusSesion.setText(settingsContext.getResources().getString(R.string.bienvenido)+" "
					+shrpref.Get_stringfrom_shprf(constants.USER_NAME)+"!");
		}else{
			botonIniciarSesion.setVisibility(View.VISIBLE);
			botonCerrarSesion.setVisibility(View.GONE);
			statusSesion.setText(settingsContext.getResources().getString(R.string.titSesion));
		}
	}	
	
	
   
}