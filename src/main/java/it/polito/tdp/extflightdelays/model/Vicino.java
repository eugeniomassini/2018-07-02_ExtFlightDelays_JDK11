package it.polito.tdp.extflightdelays.model;

public class Vicino implements Comparable<Vicino>{

	private Airport airport;
	private Double peso;
	public Vicino(Airport airport, Double peso) {
		super();
		this.airport = airport;
		this.peso = peso;
	}
	public Airport getAirport() {
		return airport;
	}
	public void setAirport(Airport airport) {
		this.airport = airport;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return airport.getAirportName() + ": " + peso;
	}
	@Override
	public int compareTo(Vicino o) {
		// TODO Auto-generated method stub
		return Double.compare(this.peso, o.peso);
	}
	
	
	
	
}
