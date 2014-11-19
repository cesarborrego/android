package sferea.todo2day.beans;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase que representa cada evento mostrado en el timeline o en favoritos
 * contiene tambien instancias de imagenes, strings y botones
 * 
 * @params(String nombreEvento, String categoria)
 * 
 * @author maw
 */

public class EventoObjeto implements Parcelable {

	String nombreEvento;
	String categoriaEvento;
	String fechaEvento;
	String horaEvento;
	String urlImgEvento;
	String categoriaIDEvento;
	String lugarEvento;
	double latEvento;
	double lonEvento;
	String distancia;
	Bitmap imagenEvento;
	int imagenCategoria;
	String descripcion;
	String fuente;
	String direccion;
	String telefono;
	int posicion;
	String idOfEvent;
	int fechaUnix;
	int indexOfEvent;
	int isNewEvent;
	TipoBoletoObjeto tipoBoleto;

	public EventoObjeto() {

	}

	public EventoObjeto(String nombreEvento, String catEvento,
			String catIDEvento, String fechaEvento, String descripcionEvento,
			String fuenteEvento, String LugarEvento, String direccionEvento,
			String telefonoEvento, double lat, double lon, String dist,
			int posicion, String idOfEvent,
			int fechaUnix, String urlImgEvento, int imagenCategoria, TipoBoletoObjeto boletos,
			int indexEvento) {
		setNombreEvento(nombreEvento);
		setCategoriaEvento(catEvento);
		setCategoriaIDEvento(catIDEvento);
		setFechaEvento(fechaEvento);
		setDescripcion(descripcionEvento);
		setFuente(fuenteEvento);
		setLugarEvento(LugarEvento);
		setDireccion(direccionEvento);
		setTelefono(telefonoEvento);
		setDistancia(dist);
		setLatEvento(lat);
		setLonEvento(lon);
		setPosicion(posicion);
		setIdOfEvent(idOfEvent);
		setFechaUnix(fechaUnix);
		setUrlImagen(urlImgEvento);
		setImagenCategoria(imagenCategoria);
		setIndexEvento(indexEvento);
		setTipoBoleto(boletos);
	}

	/** Setter & getters */
	public String getNombreEvento() {
		return nombreEvento;
	}

	public void setNombreEvento(String nombreEvento) {
		this.nombreEvento = nombreEvento;
	}

	public int getIsNewEvent() {
		return isNewEvent;
	}

	public void setIsNewEvent(int isNewEvent) {
		this.isNewEvent = isNewEvent;
	}

	public int getIndexEvento() {
		return indexOfEvent;
	}

	public void setIndexEvento(int indexEvento) {
		this.indexOfEvent = indexEvento;
	}

	public String getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(String fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public String getLugarEvento() {
		return lugarEvento;
	}

	public void setLugarEvento(String lugarEvento) {
		this.lugarEvento = lugarEvento;
	}

	public String getDistancia() {
		return distancia;
	}

	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}

	public void setLatLonEvento(double lat, double lon) {
		this.latEvento = lat;
		this.lonEvento = lon;
	}

	public double[] getLatLonEvento() {
		return new double[] { latEvento, lonEvento };
	}

	public Bitmap getImagenEvento() {
		return imagenEvento;
	}

	public void setImagenEvento(Bitmap imagenEvento) {
		this.imagenEvento = imagenEvento;
	}

	public String getCategoriaEvento() {
		return categoriaEvento;
	}

	public void setCategoriaEvento(String categoriaEvento) {
		this.categoriaEvento = categoriaEvento;
	}

	public String getCategoriaIDEvento() {
		return categoriaIDEvento;
	}

	public void setCategoriaIDEvento(String categoriaIDEvento) {
		this.categoriaIDEvento = categoriaIDEvento;
	}

	public String getHoraEvento() {
		return horaEvento;
	}

	public void setHoraEvento(String horaEvento) {
		this.horaEvento = horaEvento;
	}

	/** Nuevos Setter and Getters **/
	public double getLatEvento() {
		return latEvento;
	}

	public void setLatEvento(double latEvento) {
		this.latEvento = latEvento;
	}

	public double getLonEvento() {
		return lonEvento;
	}

	public void setLonEvento(double lonEvento) {
		this.lonEvento = lonEvento;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFuente() {
		return fuente;
	}

	public void setFuente(String fuente) {
		this.fuente = fuente;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	public String getIdOfEvent() {
		return idOfEvent;
	}

	public void setIdOfEvent(String indexOfEvent) {
		this.idOfEvent = indexOfEvent;
	}

	public int getFechaUnix() {
		return fechaUnix;
	}

	public void setFechaUnix(int fechaUnix) {
		this.fechaUnix = fechaUnix;
	}

	public String getUrlImagen() {
		return urlImgEvento;
	}

	public void setUrlImagen(String urlImg) {
		this.urlImgEvento = urlImg;
	}

	public int getImagenCategoria() {
		return imagenCategoria;
	}

	public void setImagenCategoria(int imagenCategoria) {
		this.imagenCategoria = imagenCategoria;
	}

	public TipoBoletoObjeto getTipoBoleto() {
		return tipoBoleto;
	}

	public void setTipoBoleto(TipoBoletoObjeto tipoBoleto) {
		this.tipoBoleto = tipoBoleto;
	}

	/**
	 * Parcelable object
	 */

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getNombreEvento());
		dest.writeString(categoriaEvento);
		dest.writeString(categoriaIDEvento);
		dest.writeString(fechaEvento);
		dest.writeString(lugarEvento);
		dest.writeString(distancia);
		dest.writeString(descripcion);
		dest.writeString(fuente);
		dest.writeString(direccion);
		dest.writeString(telefono);
		dest.writeDouble(latEvento);
		dest.writeDouble(lonEvento);
		dest.writeInt(posicion);
		dest.writeString(idOfEvent);
		dest.writeInt(fechaUnix);
		dest.writeString(urlImgEvento);
		dest.writeInt(imagenCategoria);
		dest.writeInt(indexOfEvent);
		dest.writeInt(isNewEvent);
		dest.writeSerializable(tipoBoleto);

	}

	/** Static field used to regenerate object, individually or as arrays */
	public static final Parcelable.Creator<EventoObjeto> CREATOR = new Parcelable.Creator<EventoObjeto>() {
		public EventoObjeto createFromParcel(Parcel pc) {
			return new EventoObjeto(pc);
		}

		public EventoObjeto[] newArray(int size) {
			return new EventoObjeto[size];
		}
	};

	public EventoObjeto(Parcel pc) {

		setNombreEvento(pc.readString());
		setCategoriaEvento(pc.readString());
		setCategoriaIDEvento(pc.readString());
		setFechaEvento(pc.readString());
		setLugarEvento(pc.readString());
		setDistancia(pc.readString());
		setDescripcion(pc.readString());
		setFuente(pc.readString());
		setDireccion(pc.readString());
		setTelefono(pc.readString());
		setLatEvento(pc.readDouble());
		setLonEvento(pc.readDouble());
		setPosicion(pc.readInt());
		setIdOfEvent(pc.readString());
		setFechaUnix(pc.readInt());
		setUrlImagen(pc.readString());
		setImagenCategoria(pc.readInt());
		setIndexEvento(pc.readInt());
		setIsNewEvent(pc.readInt());
		setTipoBoleto((TipoBoletoObjeto) pc.readSerializable());
	}

}
