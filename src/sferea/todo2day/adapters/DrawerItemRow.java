package sferea.todo2day.adapters;

/**
 * Conforma cada fila que aparecera en el menu lateral
 * 
 * @author maw
 *
 */
public class DrawerItemRow {
	
	private String titulo;
	private int icon;
	
	/**
	 * 
	 * @param titulo
	 * @param icon
	 */
	public DrawerItemRow(String titulo, int icon){
		this.titulo = titulo;
		this.icon = icon;
	}

	//Setters & Getters
	public String getTitulo() {return titulo;}
	public void setTitulo(String titulo) {this.titulo = titulo;}
	public int getIcon() {return icon;}
	public void setIcon(int icon) {this.icon = icon;}
	

}
