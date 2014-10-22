package sferea.todo2day.subfragments;

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
	
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editorCategoriasBoolean;
	
	SharedPreferences sharedPreferencesString;
	SharedPreferences.Editor editorCategoriasString;

	LinearLayout antrosBaresLayOut,culturaLayOut,cineTeatroLayOut,deportesLayOut,negociosLayOut,ninnosLayOut,
	gastronomiaLayout,musicaLayOut,saludLayOut,socialesLayOut,tecnologiaLayOut,verdeLayOut, otherLayOut;
	ImageView antros, cultura, cine, deportes, negocios, peques, gastronomia, musica, salud, sociales, tecnologia, verde, other;
	
	
	//Jugar� un poco con los n�meros pares y primos para saber cuando activar o desactivar la img de cada categor�a
	int iAntro=1;
	int iCultura=1;
	int iCine=1;
	int iDeportes=1;
	int iNegocio=1;
	int iPeques=1;
	int iGastronomia=1;
	int iMusica=1;
	int iSalud=1;
	int iSociales=1;
	int iTecnologia = 1;
	int iVerde = 1;
	int iOther = 1;

	public static boolean seActivoCategories = false; 
	public SubF_Categories(){}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);        
		View v =  inflater.inflate(R.layout.subfrag_categories, container, false);
		if(v != null){
			
			//Leeremos el archivo de share que contiene las categorias
			sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("CategoriasBoolean", Context.MODE_PRIVATE);
			editorCategoriasBoolean = sharedPreferences.edit();
			
			sharedPreferencesString = getActivity().getApplicationContext().getSharedPreferences("Categorias", Context.MODE_PRIVATE);
			editorCategoriasString = sharedPreferencesString.edit();
			
			//Validamos que categorias estan prendidas y apagadas
			//Antros y Bares
			antrosBaresLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaAntrosYBares);			
			antros = (ImageView)v.findViewById(R.id.antros_y_baresImg);	 
			
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 00", null))){
				antros.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_antrosybares_activo));
				iAntro=1;
			} else {
				antros.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_antrosybares_inactivo));
				iAntro=2;
			}
			
			//Cultura
			culturaLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaCultura);
			cultura = (ImageView)v.findViewById(R.id.culturaImgId);	      
//			if(Page_TimeLine.activaCategorias[0][1]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 01", null))){
				cultura.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cultura_activo));
				iCultura = 1;
			} else {
				cultura.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cultura_inactivo));
				iCultura = 2;
			}
			
			//Cine y Teatro
			cineTeatroLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaCineYTeatro);
			cine = (ImageView)v.findViewById(R.id.cineImgId);	     
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 02", null))){	
				cine.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cineyteatro_activo));
				iCine = 1;
			} else {
				cine.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cineyteatro_inactivo));
				iCine = 2;
			}
			
			//Deportes
			deportesLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaDeportes);
			deportes = (ImageView)v.findViewById(R.id.deportesImgId);	
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 10", null))){
				deportes.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_deportes_activo));
				iDeportes = 1;
			} else {
				deportes.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_deportes_inactivo));
				iDeportes = 2;
			}
			
			//Negocios
			negociosLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaEmprendedoresYNegocios);
			negocios = (ImageView)v.findViewById(R.id.negociosImgId);	
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 11", null))){
				negocios.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_emprendedoresynegocios_activo));
				iNegocio = 1;
			} else {
				negocios.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_emprendedoresynegocios_inactivo));
				iNegocio = 2;
			}
			
			//peques
			ninnosLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaEventosInfantiles);
			peques = (ImageView)v.findViewById(R.id.pequesImgId);	     
			
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 12", null))){
				peques.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_eventosinfantiles_activo));
				iPeques = 1;
			} else {
				peques.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_eventosinfantiles_inactivo));
				iPeques = 2;
			}
			
			//Gatronom�a
			gastronomiaLayout = (LinearLayout) v.findViewById(R.id.botonCategoriaGatronomia);
			gastronomia = (ImageView)v.findViewById(R.id.gastronomiaImgId);	 
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 20", null))){
				gastronomia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_gastronomia_activo));
				iGastronomia = 1;
			} else {
				gastronomia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_gastronomia_inactivo));
				iGastronomia = 2;
			}
			
			//M�sica
			musicaLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaMusica);
			musica = (ImageView)v.findViewById(R.id.musicaImgId);	
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 21", null))){
				musica.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_musica_activo));
				iMusica = 1;
			} else {
				musica.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_musica_inactivo));
				iMusica = 2;
			}
			
			//Salud
			saludLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaSalud);
			salud = (ImageView)v.findViewById(R.id.saludImgId);	   
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 22", null))){
				salud.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_salud_activo));
				iSalud = 1;
			} else {
				salud.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_salud_inactivo));
				iSalud = 2;
			}
			
			
			//Sociales
			socialesLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaSociales);
			sociales = (ImageView)v.findViewById(R.id.socialesImgId);	
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 30", null))){
				sociales.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_activo));
				iSociales = 1;
			} else {
				sociales.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_inactivo));
				iSociales = 2;
			}
			
			//Tecnolog�a
			tecnologiaLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaTecnologia);
			tecnologia = (ImageView)v.findViewById(R.id.tecnologiaImgId);	    
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 31", null))){
				tecnologia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_tecnologia_activo));
				iTecnologia = 1;
			} else {
				tecnologia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_tecnologia_inactivo));
				iTecnologia = 2;
			}
			
			//Verde
			verdeLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaVerde);
			verde = (ImageView)v.findViewById(R.id.verdeImgId);	     
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 32", null))){
				verde.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_verde_activo));
				iVerde = 1;
			} else {
				verde.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_verde_inactivo));
				iVerde = 2;
			}
			
			//Other
			otherLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaOther);
			other = (ImageView)v.findViewById(R.id.otherImgId);	     
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 40", null))){
				other.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_activo));
				iOther = 1;
			} else {
				other.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_inactivo));
				iOther = 2;
			}

			     
			//Acciones
			antrosBaresLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 00", null))){
						antros.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_antrosybares_inactivo));
						editorCategoriasBoolean.putString("Activa_Categoria 00", String.valueOf(false));
						editorCategoriasString.putString("Categories 0", "a");
					} else {
						antros.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_antrosybares_activo));
						editorCategoriasBoolean.putString("Activa_Categoria 00", String.valueOf(true));
						editorCategoriasString.putString("Categories 0", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});
			
			
			culturaLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 01", null))){
						cultura.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cultura_inactivo));
						editorCategoriasBoolean.putString("Activa_Categoria 01", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 1", "b");
					} else {
						cultura.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cultura_activo));
						editorCategoriasBoolean.putString("Activa_Categoria 01", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 1", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});

			
			cineTeatroLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 02", null))){
						cine.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cineyteatro_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 02", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 2", "c");
					} else {
						cine.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_cineyteatro_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 02", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 2", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});

			     
			deportesLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 10", null))){
						deportes.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_deportes_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 10", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 3", "d");
					} else {
						deportes.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_deportes_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 10", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 3", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});
			
			     
			negociosLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 11", null))){
						negocios.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_emprendedoresynegocios_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 11", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 4", "e");
					} else {
						negocios.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_emprendedoresynegocios_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 11", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 4", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});

			
			ninnosLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 12", null))){
						peques.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_eventosinfantiles_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 12", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 5", "f");
					} else {
						peques.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_eventosinfantiles_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 12", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 5", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});

			    
			gastronomiaLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 20", null))){
						gastronomia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_gastronomia_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 20", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 6", "g");
					} else {
						gastronomia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_gastronomia_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 20", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 6", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});

			     
			musicaLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 21", null))){
						musica.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_musica_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 21", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 7", "h");
					} else {
						musica.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_musica_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 21", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 7", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});

			  
			saludLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 22", null))){
						salud.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_salud_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 22", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 8", "i");
					} else {
						salud.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_salud_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 22", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 8", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});

			     
			socialesLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 30", null))){
						sociales.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 30", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 9", "j");
					} else {
						sociales.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 30", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 9", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});

			 
			tecnologiaLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 31", null))){
						tecnologia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_tecnologia_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 31", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 10", "k");
					} else {
						tecnologia.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_tecnologia_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 31", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 10", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();
				}
			});
			
			
			verdeLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 32", null))){
						verde.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_verde_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 32", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 11", "l");
					} else {
						verde.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_verde_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 32", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 11", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();				
				}
			});
			
			otherLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 40", null))){
						other.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_inactivo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 40", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 12", "m");
					} else {
						other.setImageBitmap(getScaledBitmap(R.drawable.ic_categorias_sociales_activo));
						
						editorCategoriasBoolean.putString("Activa_Categoria 40", String.valueOf(true));
						
						editorCategoriasString.putString("Categories 12", "");
					}
					editorCategoriasBoolean.commit();
					editorCategoriasString.commit();				
				}
			});
		}
		
		seActivoCategories = true;
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
