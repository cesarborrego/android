package sferea.todo2day.adapters;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import sferea.todo2day.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.widget.TextView;

/**
 * Clase que representa cada evento mostrado en el timeline o en favoritos
 * contiene tambien instancias de imagenes, strings y botones
 * 
 * @params(String nombreEvento, String categoria)
 * 
 * @author maw
 */
public class FavoritosObjeto implements Parcelable{
	
	String nombreEvento; String categoriaEvento; String fechaEvento; String horaEvento; String DistanciaEvento;
	String lugarEvento; double latEvento; double lonEvento;
	double distancia; Bitmap imagenEvento, btnRetweet, btnFav; int imagenCategoria, iContaRet, iContaFav;
	
	//Nuevos campos para el nuevo constructor
	String descripcion, fuente, direccion, telefono, boleto, urlImagen;
	
	int posicion, indexOfEvent, fechaUnix;
	
	public FavoritosObjeto(String nombreEvento, String categoria, String fecha, String lugar, double distancia){
		this.nombreEvento = nombreEvento;
		this.categoriaEvento = categoria;
		this.fechaEvento = fecha;
		this.lugarEvento = lugar;
		this.distancia = distancia;
	}
	
	public FavoritosObjeto(String nombreEvento, String catEvento, String fecha_horaEvento, String descripcionEvento, 
			String fuenteEvento, String LugarEvento, 
			String direccionEvento, String telefonoEvento, String dist, String boletoEvento, double lon, double lat,
			String urlImagen, int posicion, int indexOfEvent, int fechaUnix){
		/*Titulo
		Categoria
		FechayHora
		Descripcion
		Fuente
		Lugar
		Direccion
		Telefono
		Distancia
		Boleto
		Longitud
		Latitud
		Imagen*/
		setNombreEvento(nombreEvento);
		setCategoriaEvento(catEvento);
		setFechaEvento(fecha_horaEvento);
		setDescripcion(descripcionEvento);
		setFuente(fuenteEvento);
		setLugarEvento(LugarEvento);
		setDireccion(direccionEvento);
		setTelefono(telefonoEvento);
		setDistanciaEvento(dist);
		setBoleto(boletoEvento);
		setLonEvento(lon);
		setLatEvento(lat);
		setPosicion(posicion);
		setUrlImagen(urlImagen);
		setIndexOfEvent(indexOfEvent);
		setFechaUnix(fechaUnix);
	}

	/** Setter & getters*/
	public String getNombreEvento() { return nombreEvento; } 	
	public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }

	public String getFechaEvento() { return fechaEvento; } 
	public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }

	public String getLugarEvento() { return lugarEvento; } 
	public void setLugarEvento(String lugarEvento) { this.lugarEvento = lugarEvento; }

	public double getDistancia() { return distancia; } 
	public void setDistancia(double distancia) { this.distancia = distancia; }
	
	public void setLatLonEvento(double lat, double lon){ this.latEvento = lat; this.lonEvento = lon; } 
	public double[] getLatLonEvento(){ return new double[]{latEvento,lonEvento}; }
	
	public Bitmap getImagenEvento() { return imagenEvento; }
	public void setImagenEvento(Bitmap imagenEvento) { this.imagenEvento = imagenEvento; }
	
	public String getCategoriaEvento() {return categoriaEvento;}
	public void setCategoriaEvento(String categoriaEvento) {this.categoriaEvento = categoriaEvento;}

	public int getImagenCategoria() {return imagenCategoria;}
	public void setImagenCategoria(int imagenCategoria) {this.imagenCategoria = imagenCategoria;}

	public String getHoraEvento() { return horaEvento; }
	public void setHoraEvento(String horaEvento) { this.horaEvento = horaEvento; }		
	
	/**Nuevos Setter and Getters**/
	public double getLatEvento() {return latEvento;}
	public void setLatEvento(double latEvento) {this.latEvento = latEvento;}

	public double getLonEvento() {return lonEvento;}
	public void setLonEvento(double lonEvento) {this.lonEvento = lonEvento;}

	public String getDescripcion() {return descripcion;}
	public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

	public String getFuente() {return fuente;}
	public void setFuente(String fuente) {this.fuente = fuente;}

	public String getDireccion() {return direccion;}
	public void setDireccion(String direccion) {this.direccion = direccion;}

	public String getTelefono() {return telefono;}
	public void setTelefono(String telefono) {this.telefono = telefono;}

	public String getBoleto() {return boleto;}
	public void setBoleto(String boleto) {this.boleto = boleto;}	
	
	public Bitmap getBtnRetweet() {return btnRetweet;}
	public void setBtnRetweet(Bitmap btnRetweet) {this.btnRetweet = btnRetweet;}
	
	public Bitmap getBtnFav() {return btnFav;}
	public void setBtnFav(Bitmap btnFav) {this.btnFav = btnFav;}

	public int getiContaRet() {return iContaRet;}
	public void setiContaRet(int iContaRet) {this.iContaRet = iContaRet;}

	public int getiContaFav() {return iContaFav;}
	public void setiContaFav(int iContaFav) {this.iContaFav = iContaFav;}
	
	public String getDistanciaEvento() {return DistanciaEvento;}
	public void setDistanciaEvento(String distanciaEvento) {DistanciaEvento = distanciaEvento;}
	
	

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	
	

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
	
	

	public int getIndexOfEvent() {
		return indexOfEvent;
	}

	public void setIndexOfEvent(int indexOfEvent) {
		this.indexOfEvent = indexOfEvent;
	}

	public int getFechaUnix() {
		return fechaUnix;
	}

	public void setFechaUnix(int fechaUnix) {
		this.fechaUnix = fechaUnix;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getNombreEvento());
		dest.writeString(categoriaEvento);
		dest.writeString(fechaEvento);
		dest.writeString(lugarEvento);
		dest.writeString(DistanciaEvento);
		dest.writeString(descripcion);
		dest.writeString(fuente);
		dest.writeString(direccion);
		dest.writeString(telefono);
		dest.writeString(boleto);
		dest.writeDouble(latEvento);
		dest.writeDouble(lonEvento);
		dest.writeInt(posicion);
		
		
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		imagenEvento.compress(Bitmap.CompressFormat.JPEG, 100, stream);		
//		byte[] byteArray = stream.toByteArray();		
//		dest.writeInt(byteArray.length);
//		dest.writeByteArray(byteArray);
		
//		ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
//		btnRetweet.compress(Bitmap.CompressFormat.JPEG, 100, stream1);		
//		byte[] byteArray1 = stream1.toByteArray();		
//		dest.writeInt(byteArray1.length);
//		dest.writeByteArray(byteArray1);
//		
//		ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
//		btnFav.compress(Bitmap.CompressFormat.JPEG, 100, stream2);		
//		byte[] byteArray2 = stream2.toByteArray();		
//		dest.writeInt(byteArray2.length);
//		dest.writeByteArray(byteArray2);
	}	
	
	/** Static field used to regenerate object, individually or as arrays */
	public static final Parcelable.Creator<FavoritosObjeto> CREATOR = new Parcelable.Creator<FavoritosObjeto>() {
		public FavoritosObjeto createFromParcel(Parcel pc) {
			return new FavoritosObjeto(pc);
		}
		public FavoritosObjeto[] newArray(int size) {
			return new FavoritosObjeto[size];
		}
	};
	
	public FavoritosObjeto(Parcel pc) {
		
		setNombreEvento(pc.readString());
		setCategoriaEvento(pc.readString());
		setFechaEvento(pc.readString());
		setLugarEvento(pc.readString());
		setDistanciaEvento(pc.readString ());
		setDescripcion(pc.readString());
		setFuente(pc.readString());
		setDireccion(pc.readString());
		setTelefono(pc.readString());
		setBoleto(pc.readString());
		setLatEvento(pc.readDouble());
		setLonEvento(pc.readDouble());
		setPosicion(pc.readInt());
		
		//Obtenemos la imagen 
//		byte[] byteArrayImage= new byte[pc.readInt()];
//		pc.readByteArray(byteArrayImage);		
//		setImagenEvento(BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.length));
		
//		//Obtenemos la imagen Retweet
//		byte[] byteArrayBtnRet= new byte[pc.readInt()];
//		pc.readByteArray(byteArrayBtnRet);		
//		setBtnRetweet(BitmapFactory.decodeByteArray(byteArrayBtnRet, 0, byteArrayBtnRet.length));
//		
//		//Obtenemos la imagen Favoritos
//		byte[] byteArrayBtnFav= new byte[pc.readInt()];
//		pc.readByteArray(byteArrayBtnFav);		
//		setBtnFav(BitmapFactory.decodeByteArray(byteArrayBtnFav, 0, byteArrayBtnFav.length));
	}
}
