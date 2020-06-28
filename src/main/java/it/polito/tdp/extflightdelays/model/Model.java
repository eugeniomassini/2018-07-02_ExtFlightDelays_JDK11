package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> idMap;
	private ExtFlightDelaysDAO dao;
	
	private List<Airport> best;
	private Double migliaUsate;
	private Double MIGLIAMASSIME;
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
	}

	public void creaGrafo(Double minima) {
		
		idMap = new HashMap<Integer, Airport>();
		
		grafo = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		dao.loadAllAirports(idMap);
		
		for(Arco a: dao.getArchi(minima, idMap)) {
			
//			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			
			if(!this.grafo.containsVertex(a.getA1())) {
				this.grafo.addVertex(a.getA1());
			}
			if (!this.grafo.containsVertex(a.getA2())) {
				this.grafo.addVertex(a.getA2());
			}
			
			Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			
		}
		
		System.out.format("#Vertici: %d\n#Archi: %d\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
		
	}

	public Set<Airport> getAeroporti() {
		// TODO Auto-generated method stub
		return this.grafo.vertexSet();
	}

	public List<Vicino> getVicini(Airport partenza) {
		
		List<Vicino> vicini = new ArrayList<>();
		
		for(Airport a: Graphs.neighborListOf(this.grafo, partenza)) {
			
			Vicino v = new Vicino(a, this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, a)));
			
			vicini.add(v);
			
		}
		
		Collections.sort(vicini);
		
		return vicini;
		
	}
	
	public void cerca(Airport partenza, Double migliaMassime) {
		MIGLIAMASSIME = migliaMassime;
		this.best = new ArrayList<Airport>();
		List <Airport> parziale = new ArrayList<Airport>();
		
		parziale.add(partenza);
		ricorsione(parziale, migliaMassime);
			
	}

	private void ricorsione(List<Airport> parziale, Double migliaMassime) {
		
		if(migliaMassime<0) {
			if(parziale.size()>best.size()) {
//				parziale.remove(parziale.size()-1);
				best = new ArrayList<Airport>(parziale);
				best.remove(best.size()-1);
				this.migliaUsate = calcolaMiglia(best);
			}
		}
		
		List<Airport> vicini = Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1));
		
		if(migliaMassime<minMigliaDaFare(vicini, parziale.get(parziale.size()-1))) {
			return;
		}
			
		for(Airport a: vicini) {
			
			if(!parziale.contains(a)){
				Double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), a));
				parziale.add(a);
				ricorsione(parziale, migliaMassime-peso);
				parziale.remove(parziale.size()-1);
			}
			
		}
			
	}

	private Double calcolaMiglia(List<Airport> best) {
		double peso = 0.0 ;
		for(int i=1; i<best.size(); i++) {
			double p = this.grafo.getEdgeWeight(this.grafo.getEdge(best.get(i-1), best.get(i))) ;
			peso += p ;
		}
		return peso ;
	}

	private Double minMigliaDaFare(List<Airport> vicini, Airport airport) {
		
		Double minimo =0.0;
		
		for(int i=0; i<vicini.size();i++) {
			if(i==0) {
				minimo=this.grafo.getEdgeWeight(this.grafo.getEdge(airport, vicini.get(i)));
			}
			else if(this.grafo.getEdgeWeight(this.grafo.getEdge(airport, vicini.get(i)))<minimo) {
				minimo = this.grafo.getEdgeWeight(this.grafo.getEdge(airport, vicini.get(i)));
			}
			
			
		}

		
		return minimo;
	}

	public List<Airport> getBest() {
		return best;
	}

	public Double getMigliaUsate() {
		return migliaUsate;
	}

}
