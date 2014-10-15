package sferea.todo2day.subfragments;

import sferea.todo2day.MapActivity;
import sferea.todo2day.R;
import sferea.todo2day.adapters.GridViewAdapterCategories;
import android.R.id;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
//			if(Page_TimeLine.activaCategorias[0][0]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 00", null))){
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 4;
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_antrosybares_activo);
				antros.setImageBitmap(bm);
				iAntro=1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_antrosybares_inactivo);
				antros.setImageBitmap(bm);
//				antros.setImageResource(R.drawable.ic_categorias_antrosybares_inactivo);
				iAntro=2;
			}
			
			//Cultura
			culturaLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaCultura);
			cultura = (ImageView)v.findViewById(R.id.culturaImgId);	      
//			if(Page_TimeLine.activaCategorias[0][1]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 01", null))){
//				cultura.setImageResource(R.drawable.ic_categorias_cultura_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_cultura_activo);
				cultura.setImageBitmap(bm);
				iCultura = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_cultura_inactivo);
				cultura.setImageBitmap(bm);
				iCultura = 2;
			}
			
			//Cine y Teatro
			cineTeatroLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaCineYTeatro);
			cine = (ImageView)v.findViewById(R.id.cineImgId);	     
//			if(Page_TimeLine.activaCategorias[0][2]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 02", null))){	
//				cine.setImageResource(R.drawable.ic_categorias_cineyteatro_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_cineyteatro_activo);
				cine.setImageBitmap(bm);
				iCine = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_cineyteatro_inactivo);
				cine.setImageBitmap(bm);
				iCine = 2;
			}
			
			//Deportes
			deportesLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaDeportes);
			deportes = (ImageView)v.findViewById(R.id.deportesImgId);	
//			if(Page_TimeLine.activaCategorias[1][0]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 10", null))){
//				deportes.setImageResource(R.drawable.ic_categorias_deportes_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_deportes_activo);
				deportes.setImageBitmap(bm);
				iDeportes = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_deportes_inactivo);
				deportes.setImageBitmap(bm);
				iDeportes = 2;
			}
			
			//Negocios
			negociosLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaEmprendedoresYNegocios);
			negocios = (ImageView)v.findViewById(R.id.negociosImgId);	
//			if(Page_TimeLine.activaCategorias[1][1]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 11", null))){
//				negocios.setImageResource(R.drawable.ic_categorias_emprendedoresynegocios_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_emprendedoresynegocios_activo);
				negocios.setImageBitmap(bm);
				iNegocio = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_emprendedoresynegocios_inactivo);
				negocios.setImageBitmap(bm);
				iNegocio = 2;
			}
			
			//peques
			ninnosLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaEventosInfantiles);
			peques = (ImageView)v.findViewById(R.id.pequesImgId);	     
//			if(Page_TimeLine.activaCategorias[1][2]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 12", null))){
//				peques.setImageResource(R.drawable.ic_categorias_eventosinfantiles_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_eventosinfantiles_activo);
				peques.setImageBitmap(bm);
				iPeques = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_eventosinfantiles_inactivo);
				peques.setImageBitmap(bm);
				iPeques = 2;
			}
			
			//Gatronom�a
			gastronomiaLayout = (LinearLayout) v.findViewById(R.id.botonCategoriaGatronomia);
			gastronomia = (ImageView)v.findViewById(R.id.gastronomiaImgId);	 
//			if(Page_TimeLine.activaCategorias[2][0]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 20", null))){
//				gastronomia.setImageResource(R.drawable.ic_categorias_gastronomia_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_gastronomia_activo);
				gastronomia.setImageBitmap(bm);
				iGastronomia = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_gastronomia_inactivo);
				gastronomia.setImageBitmap(bm);
				iGastronomia = 2;
			}
			
			//M�sica
			musicaLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaMusica);
			musica = (ImageView)v.findViewById(R.id.musicaImgId);	
//			if(Page_TimeLine.activaCategorias[2][1]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 21", null))){
//				musica.setImageResource(R.drawable.ic_categorias_musica_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_musica_activo);
				musica.setImageBitmap(bm);
				iMusica = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_musica_inactivo);
				musica.setImageBitmap(bm);
				iMusica = 2;
			}
			
			//Salud
			saludLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaSalud);
			salud = (ImageView)v.findViewById(R.id.saludImgId);	   
//			if(Page_TimeLine.activaCategorias[2][2]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 22", null))){
//				salud.setImageResource(R.drawable.ic_categorias_salud_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_salud_activo);
				salud.setImageBitmap(bm);
				iSalud = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_salud_inactivo);
				salud.setImageBitmap(bm);
				iSalud = 2;
			}
			
			
			//Sociales
			socialesLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaSociales);
			sociales = (ImageView)v.findViewById(R.id.socialesImgId);	
//			if(Page_TimeLine.activaCategorias[3][0]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 30", null))){
//				sociales.setImageResource(R.drawable.ic_categorias_sociales_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_sociales_activo);
				sociales.setImageBitmap(bm);
				iSociales = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_sociales_inactivo);
				sociales.setImageBitmap(bm);
				iSociales = 2;
			}
			
			//Tecnolog�a
			tecnologiaLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaTecnologia);
			tecnologia = (ImageView)v.findViewById(R.id.tecnologiaImgId);	    
//			if(Page_TimeLine.activaCategorias[3][1]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 31", null))){
//				tecnologia.setImageResource(R.drawable.ic_categorias_tecnologia_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_tecnologia_activo);
				tecnologia.setImageBitmap(bm);
				iTecnologia = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_tecnologia_inactivo);
				tecnologia.setImageBitmap(bm);
				iTecnologia = 2;
			}
			
			//Verde
			verdeLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaVerde);
			verde = (ImageView)v.findViewById(R.id.verdeImgId);	     
//			if(Page_TimeLine.activaCategorias[3][2]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 32", null))){
//				verde.setImageResource(R.drawable.ic_categorias_verde_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_verde_activo);
				verde.setImageBitmap(bm);
				iVerde = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_verde_inactivo);
				verde.setImageBitmap(bm);
				iVerde = 2;
			}
			
			//Other
			otherLayOut = (LinearLayout) v.findViewById(R.id.botonCategoriaOther);
			other = (ImageView)v.findViewById(R.id.otherImgId);	     
//			if(Page_TimeLine.activaCategorias[4][0]){
			if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 40", null))){
//				other.setImageResource(R.drawable.ic_categorias_sociales_activo);
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_sociales_activo);
				other.setImageBitmap(bm);
				iOther = 1;
			} else {
				Bitmap bm = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.ic_categorias_sociales_inactivo);
				other.setImageBitmap(bm);
				iOther = 2;
			}

			     
			//Acciones
			antrosBaresLayOut.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(Boolean.parseBoolean(sharedPreferences.getString("Activa_Categoria 00", null))){
						antros.setImageResource(R.drawable.ic_categorias_antrosybares_inactivo);
						editorCategoriasBoolean.putString("Activa_Categoria 00", String.valueOf(false));
						editorCategoriasString.putString("Categories 0", "a");
					} else {
						antros.setImageResource(R.drawable.ic_categorias_antrosybares_activo);
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
						cultura.setImageResource(R.drawable.ic_categorias_cultura_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 01", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 1", "b");
					} else {
						cultura.setImageResource(R.drawable.ic_categorias_cultura_activo);
						
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
						cine.setImageResource(R.drawable.ic_categorias_cineyteatro_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 02", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 2", "c");
					} else {
						cine.setImageResource(R.drawable.ic_categorias_cineyteatro_activo);
						
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
						deportes.setImageResource(R.drawable.ic_categorias_deportes_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 10", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 3", "d");
					} else {
						deportes.setImageResource(R.drawable.ic_categorias_deportes_activo);
						
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
						negocios.setImageResource(R.drawable.ic_categorias_emprendedoresynegocios_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 11", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 4", "e");
					} else {
						negocios.setImageResource(R.drawable.ic_categorias_emprendedoresynegocios_activo);
						
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
						peques.setImageResource(R.drawable.ic_categorias_eventosinfantiles_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 12", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 5", "f");
					} else {
						peques.setImageResource(R.drawable.ic_categorias_eventosinfantiles_activo);
						
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
						gastronomia.setImageResource(R.drawable.ic_categorias_gastronomia_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 20", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 6", "g");
					} else {
						gastronomia.setImageResource(R.drawable.ic_categorias_gastronomia_activo);
						
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
						musica.setImageResource(R.drawable.ic_categorias_musica_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 21", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 7", "h");
					} else {
						musica.setImageResource(R.drawable.ic_categorias_musica_activo);
						
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
						salud.setImageResource(R.drawable.ic_categorias_salud_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 22", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 8", "i");
					} else {
						salud.setImageResource(R.drawable.ic_categorias_salud_activo);
						
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
						sociales.setImageResource(R.drawable.ic_categorias_sociales_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 30", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 9", "j");
					} else {
						sociales.setImageResource(R.drawable.ic_categorias_sociales_activo);
						
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
						tecnologia.setImageResource(R.drawable.ic_categorias_tecnologia_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 31", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 10", "k");
					} else {
						tecnologia.setImageResource(R.drawable.ic_categorias_tecnologia_activo);
						
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
						verde.setImageResource(R.drawable.ic_categorias_verde_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 32", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 11", "l");
					} else {
						verde.setImageResource(R.drawable.ic_categorias_verde_activo);
						
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
						other.setImageResource(R.drawable.ic_categorias_sociales_inactivo);
						
						editorCategoriasBoolean.putString("Activa_Categoria 40", String.valueOf(false));
						
						editorCategoriasString.putString("Categories 12", "m");
					} else {
						other.setImageResource(R.drawable.ic_categorias_sociales_activo);
						
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
}
