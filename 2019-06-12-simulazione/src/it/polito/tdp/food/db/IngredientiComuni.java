package it.polito.tdp.food.db;

public class IngredientiComuni {
	Condiment c1;
	Condiment c2;
	int numeroCibiComuni;
	
	public IngredientiComuni(Condiment c1, Condiment c2, int numeroCibiComuni) {
		super();
		this.c1 = c1;
		this.c2 = c2;
		this.numeroCibiComuni = numeroCibiComuni;
	}

	public Condiment getC1() {
		return c1;
	}

	public void setC1(Condiment c1) {
		this.c1 = c1;
	}

	public Condiment getC2() {
		return c2;
	}

	public void setC2(Condiment c2) {
		this.c2 = c2;
	}

	public int getNumeroCibiComuni() {
		return numeroCibiComuni;
	}

	public void setNumeroCibiComuni(int numeroCibiComuni) {
		this.numeroCibiComuni = numeroCibiComuni;
	}
	
	

}
