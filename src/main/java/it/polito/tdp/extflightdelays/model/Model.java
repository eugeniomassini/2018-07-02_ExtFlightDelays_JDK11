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
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
	}

	public void creaGrafo(Double minima) {
		
		idMap = new HashMap<Integer, Airport>();
		
		grafo = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		dao.loadAllAirports(idMap);
		
		for(Arco a: dao.getArchi(minima, idMap)) {
			
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			
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
	
	

}
