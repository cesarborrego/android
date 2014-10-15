package sferea.todo2day.subfragments;

import java.io.IOException;

import sferea.todo2day.R;
import sferea.todo2day.config.Constants_Settings;
import sferea.todo2day.config.SharedPreferencesHelper;
import sferea.todo2day.config.TwitterManager;
import android.content.Context;
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
	TextView textoDistancia;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.subfrag_settings, container, false);
		
		botonIniciarSesion = ((RelativeLayout)view.findViewById(R.id.botonIniciarSesion));
		botonCerrarSesion = ((RelativeLayout)view.findViewById(R.id.botonCerrarSesion));
		statusSesion = ((TextView)view.findViewById(R.id.statusSesion));
		
		seekbarDistancia = ((SeekBar)view.findViewById(R.id.seekbarDistancia));
		textoDistancia = ((TextView)view.findViewById(R.id.textoDistancia));
		
		
		checkSettings();
		
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
        
        seekbarDistancia.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				Log.d(null,"progress = "+progress);
				switch(progress){
				case 0:
					textoDistancia.setText("5km");
					break;
				case 1:
					textoDistancia.setText("10km");
					break;
				case 2:
					textoDistancia.setText("15km");
					break;
				case 3:
					textoDistancia.setText("20km");
					break;
				case 4:
					textoDistancia.setText("50km");
					break;
				}
				//TODO Agregar rutina para cambiar kilometros
			}
		});
        
        
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