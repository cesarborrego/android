package sferea.todo2day.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class TipoBoletoObjeto implements Serializable {
	private int id;
	private ArrayList<BoletoObjeto> listaBoletos;
	
	public TipoBoletoObjeto(){
		this.listaBoletos = new ArrayList<BoletoObjeto>();
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public ArrayList<BoletoObjeto> getListaBoletos(){
		return this.listaBoletos;
	}

}
