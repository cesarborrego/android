package sferea.todo2day.beans;

import java.util.ArrayList;

public class TipoBoletoObjeto {
	private int id;
	private ArrayList<BoletoObjeto> boletos;
	
	public TipoBoletoObjeto(int id){
		this.id = id;
		this.boletos = new ArrayList<BoletoObjeto>();
		
	}
	
	public int getId() {
		return id;
	}
	
	public ArrayList<BoletoObjeto> getBoletos(){
		return this.boletos;
	}

}
