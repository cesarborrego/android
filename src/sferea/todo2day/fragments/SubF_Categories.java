package sferea.todo2day.fragments;

import sferea.todo2day.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SubF_Categories extends Fragment {
	
	private static final String SHARED_PREFS_NAME = "YIEPPA_PREFERENCES";
	private static final String KEY_ANTROS = "Antros y Bares";
	private static final String KEY_CINE = "Cine y Teatro";
	private static final String KEY_CULTURA = "Cultura";
	private static final String KEY_DEPORTES = "Deportes";
	private static final String KEY_NEGOCIOS = "Negocios";
	private static final String KEY_NINOS = "Con niños";
	private static final String KEY_GASTRONOMIA = "Gastronomía";
	private static final String KEY_MUSICA = "Música";
	private static final String KEY_SALUD = "Salud";
	private static final String KEY_TECNOLOGIA = "Tecnología";
	private static final String KEY_VERDE = "Verde";
	private static final String KEY_SOCIALES = "Sociales";
	private static final String KEY_OTROS = "Otros";
	
	private static final String CLAVE_ANTROS = "a";
	private static final String CLAVE_CULTURA = "b";
	private static final String CLAVE_CINE = "c";
	private static final String CLAVE_DEPORTES = "d";
	private static final String CLAVE_NEGOCIOS = "e";
	private static final String CLAVE_NINOS = "f";
	private static final String CLAVE_GASTRONOMIA = "g";
	private static final String CLAVE_MUSICA = "h";
	private static final String CLAVE_SALUD = "i";
	private static final String CLAVE_SOCIALES = "j";
	private static final String CLAVE_TECNOLOGIA = "k";
	private static final String CLAVE_VERDE = "l";
	private static final String CLAVE_OTROS = "m";
			
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	LinearLayout antrosBaresLayOut,culturaLayOut,cineTeatroLayOut,deportesLayOut,negociosLayOut,ninnosLayOut,
	gastronomiaLayout,musicaLayOut,saludLayOut,socialesLayOut,tecnologiaLayOut,verdeLayOut, otherLayOut;
	ImageView antros, cultura, cine, deportes, negocios, peques, gastronomia, musica, salud, sociales, tecnologia, verde, other;

	public SubF_Categories(){}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);        
		View v =  inflater.inflate(R.layout.subfrag_categories, container, false);
		if(v != null){
			
			//Leeremos el archivo de share que contiene las categorias
			sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
			
			//Validamos que categorias estan prendidas y apagadas
			//Antros y Bares
			antrosBaresLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaAntrosYBares);			
			antros = (ImageView)v.findViewById(R.id.antros_y_baresImg);	 
			
			if(!sharedPreferences.contains(KEY_ANTROS)){
				antros.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_antrosybares_activo));
			} else {
				antros.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_antrosybares_inactivo));
			}
			
			//Cultura
			culturaLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaCultura);
			cultura = (ImageView)v.findViewById(R.id.culturaImgId);	      
//			if(Page_TimeLine.activaCategorias[0][1]){
			if(!sharedPreferences.contains(KEY_CULTURA)){
				cultura.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cultura_activo));
			} else {
				cultura.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cultura_inactivo));
			}
			
			//Cine y Teatro
			cineTeatroLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaCineYTeatro);
			cine = (ImageView)v.findViewById(R.id.cineImgId);	     
			if(!sharedPreferences.contains(KEY_CINE)){
				cine.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cineyteatro_activo));
			} else {
				cine.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cineyteatro_inactivo));
			}
			
			//Deportes
			deportesLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaDeportes);
			deportes = (ImageView)v.findViewById(R.id.deportesImgId);	
			if(!sharedPreferences.contains(KEY_DEPORTES)){
				deportes.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_deportes_activo));
			} else {
				deportes.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_deportes_inactivo));
			}
			
			//Negocios
			negociosLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaEmprendedoresYNegocios);
			negocios = (ImageView)v.findViewById(R.id.negociosImgId);	
			if(!sharedPreferences.contains(KEY_NEGOCIOS)){
				negocios.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_emprendedoresynegocios_activo));
			} else {
				negocios.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_emprendedoresynegocios_inactivo));
			}
			
			//peques
			ninnosLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaEventosInfantiles);
			peques = (ImageView)v.findViewById(R.id.pequesImgId);	     
			
			if(!sharedPreferences.contains(KEY_NINOS)){
				peques.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_eventosinfantiles_activo));
			} else {
				peques.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_eventosinfantiles_inactivo));
			}
			
			//Gatronom???a
			gastronomiaLayout = (LinearLayout) v.findViewById(R.id.botonCategoriaGatronomia);
			gastronomia = (ImageView)v.findViewById(R.id.gastronomiaImgId);	 
			if(!sharedPreferences.contains(KEY_GASTRONOMIA)){
				gastronomia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_gastronomia_activo));
			} else {
				gastronomia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_gastronomia_inactivo));
			}
			
			//M???sica
			musicaLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaMusica);
			musica = (ImageView)v.findViewById(R.id.musicaImgId);	
			if(!sharedPreferences.contains(KEY_MUSICA)){
				musica.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_musica_activo));
			} else {
				musica.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_musica_inactivo));
			}
			
			//Salud
			saludLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaSalud);
			salud = (ImageView)v.findViewById(R.id.saludImgId);	   
			if(!sharedPreferences.contains(KEY_SALUD)){
				salud.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_salud_activo));
			} else {
				salud.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_salud_inactivo));
			}
			
			
			//Sociales
			socialesLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaSociales);
			sociales = (ImageView)v.findViewById(R.id.socialesImgId);	
			if(!sharedPreferences.contains(KEY_SOCIALES)){
				sociales.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_activo));
			} else {
				sociales.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_inactivo));
			}
			
			//Tecnolog???a
			tecnologiaLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaTecnologia);
			tecnologia = (ImageView)v.findViewById(R.id.tecnologiaImgId);	    
			if(!sharedPreferences.contains(KEY_TECNOLOGIA)){
				tecnologia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_tecnologia_activo));
			} else {
				tecnologia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_tecnologia_inactivo));
			}
			
			//Verde
			verdeLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaVerde);
			verde = (ImageView)v.findViewById(R.id.verdeImgId);	     
			if(!sharedPreferences.contains(KEY_VERDE)){
				verde.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_verde_activo));
			} else {
				verde.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_verde_inactivo));
			}
			
			//Other
			otherLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaOther);
			other = (ImageView)v.findViewById(R.id.otherImgId);	     
			if(!sharedPreferences.contains(KEY_OTROS)){
				other.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_activo));
			} else {
				other.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_inactivo));
			}

			     
			//Acciones
			antrosBaresLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_ANTROS)){
						antros.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_antrosybares_activo));
						editor.remove(KEY_ANTROS);
					} else {
						antros.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_antrosybares_inactivo));
						editor.putString(KEY_ANTROS, CLAVE_ANTROS);
					}
					editor.commit();
				}
			});
			
			
			culturaLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_CULTURA)){
						cultura.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cultura_activo));
						editor.remove(KEY_CULTURA);
					} else {
						cultura.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cultura_inactivo));
						editor.putString(KEY_CULTURA, CLAVE_CULTURA);
					}
					editor.commit();
				}
			});

			
			cineTeatroLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_CINE)){
						cine.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cineyteatro_activo));
						editor.remove(KEY_CINE);
					} else {
						cine.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cineyteatro_inactivo));
						editor.putString(KEY_CINE, CLAVE_CINE);
					}
					editor.commit();
				}
			});

			     
			deportesLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_DEPORTES)){
						deportes.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_deportes_activo));
						editor.remove(KEY_DEPORTES);
					} else {
						deportes.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_deportes_inactivo));
						editor.putString(KEY_DEPORTES, CLAVE_DEPORTES);
					}
					editor.commit();
				}
			});
			
			     
			negociosLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_NEGOCIOS)){
						negocios.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_emprendedoresynegocios_activo));
						editor.remove(KEY_NEGOCIOS);
					} else {
						negocios.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_emprendedoresynegocios_inactivo));
						editor.putString(KEY_NEGOCIOS, CLAVE_NEGOCIOS);
					}
					editor.commit();
				}
			});

			
			ninnosLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_NINOS)){
						peques.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_eventosinfantiles_activo));
						editor.remove(KEY_NINOS);
					} else {
						peques.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_eventosinfantiles_inactivo));
						editor.putString(KEY_NINOS, CLAVE_NINOS);
					}
					editor.commit();
				}
			});

			    
			gastronomiaLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_GASTRONOMIA)){
						gastronomia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_gastronomia_activo));
						editor.remove(KEY_GASTRONOMIA);

					} else {
						gastronomia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_gastronomia_inactivo));
						editor.putString(KEY_GASTRONOMIA, CLAVE_GASTRONOMIA);
					}
					editor.commit();
				}
			});

			     
			musicaLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_MUSICA)){
						musica.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_musica_activo));
						editor.remove(KEY_MUSICA);
					} else {
						musica.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_musica_inactivo));
						editor.putString(KEY_MUSICA, CLAVE_MUSICA);
					}
					editor.commit();
				}
			});

			  
			saludLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_SALUD)){
						salud.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_salud_activo));
						editor.remove(KEY_SALUD);
					} else {
						salud.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_salud_inactivo));
						editor.putString(KEY_SALUD, CLAVE_SALUD);
					}
					editor.commit();
				}
			});

			     
			socialesLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_SOCIALES)){
						sociales.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_activo));
						editor.remove(KEY_SOCIALES);
					} else {
						sociales.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_inactivo));
						editor.putString(KEY_SOCIALES, CLAVE_SOCIALES);
					}
					editor.commit();
				}
			});

			 
			tecnologiaLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_TECNOLOGIA)){
						tecnologia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_tecnologia_activo));
						editor.remove(KEY_TECNOLOGIA);
					} else {
						tecnologia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_tecnologia_inactivo));
						editor.putString(KEY_TECNOLOGIA, CLAVE_TECNOLOGIA);
					}
					editor.commit();
				}
			});
			
			
			verdeLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_VERDE)){
						verde.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_verde_activo));
						editor.remove(KEY_VERDE);
					} else {
						verde.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_verde_inactivo));
						editor.putString(KEY_VERDE, CLAVE_VERDE);
					}
					editor.commit();
				}
			});
			
			otherLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(sharedPreferences.contains(KEY_OTROS)){
						other.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_activo));
						editor.remove(KEY_OTROS);
					} else {
						other.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_inactivo));
						editor.putString(KEY_OTROS, CLAVE_OTROS);
					}
					editor.commit();
				}
			});
		}
		return v;
	}
	
	private Bitmap getScaledBitmap(int resourceId){
		BitmapFactory.Options bmoptions = new BitmapFactory.Options();
		bmoptions.inDensity = DisplayMetrics.DENSITY_MEDIUM;
		bmoptions.inTargetDensity = getActivity().getApplicationContext().getResources().getDisplayMetrics().densityDpi;
		bmoptions.inScaled = true;
		Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), resourceId, bmoptions);
		
		return bm;
	}
}
