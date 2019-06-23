package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.Condiment;
import it.polito.tdp.food.db.FoodDao;
import it.polito.tdp.food.db.IngredientiComuni;

public class Model {
	FoodDao dao;
	SimpleWeightedGraph< Condiment, DefaultWeightedEdge> grafo;
	List<Condiment> ingredienti;
	HashMap<Integer, Condiment> map;
	List<Condiment> ingredientiIndipendenti;
	List<Condiment> best;
	
	
	public Model() {
		dao= new FoodDao();
		grafo= new SimpleWeightedGraph< Condiment, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		ingredienti= new ArrayList<Condiment>() ;
		map= new HashMap<Integer, Condiment>();
	}
	public List<Condiment> ingredientiCalorieInferiori(double calorie) {
		ingredienti= dao.ingredientiCalorieInferiori(calorie, map);
		return ingredienti;
	}
	public String creaGrafo(double calorie){
		String s="";
		double somma=0;
		Graphs.addAllVertices(grafo, this.ingredientiCalorieInferiori(calorie));
		
		List<IngredientiComuni> archi= dao.ingredientiComuni(calorie, map);
		System.out.println(archi);
		for(IngredientiComuni i: archi) {
				//System.out.println(i.getC1()+" "+i.getC2()+" "+i.getNumeroCibiComuni());
				Graphs.addEdgeWithVertices(grafo, i.getC1(), i.getC2(), i.getNumeroCibiComuni());
			}
			
		
	System.out.println("Creato grafo con "+ grafo.vertexSet().size()+ " vertici e "+ grafo.edgeSet().size()+" archi");
		Collections.sort(ingredienti);
		for(Condiment co: ingredienti) {
			for(Condiment con:Graphs.neighborListOf(grafo, co) ) {
				somma= somma+ grafo.getEdgeWeight(grafo.getEdge(co, con));
			}
			s+= "INGREDIENTE: "+co.toString()+ " CIBI IN CUI è PRESENTE: "+somma+"\n";
		}
		return s;
	}
	
	public void dieta() {
		ingredientiIndipendenti= new ArrayList<Condiment>(dao.listAllCondiment());
		ingredientiIndipendenti.removeAll(ingredienti);
	}
	public List<Condiment> dietaEquilibrata(Condiment partenza){
		this.best= new LinkedList<Condiment>();
		List<Condiment> parziale= new LinkedList<Condiment>();
		parziale.add(partenza);
		cercaPercorso(parziale);
		return best ;
		
	}
	private void cercaPercorso(List<Condiment> parziale) {
		//trovo i candidati
		List<Condiment> candidati= nonConnessi(parziale.get(parziale.size()-1));
		
		//ciclo sui candidati e li aggiungo alla parziale
		for(Condiment co: candidati) {
			if(!parziale.contains(co)) {
				parziale.add(co);
				cercaPercorso(parziale);
				parziale.remove(co);
			}
		}
		//condizione di terminazione
		if(parziale.size()>best.size()) {
			best= new LinkedList<Condiment>(parziale);
		}
	}
	private List<Condiment> nonConnessi(Condiment condiment) {
		List<Condiment> nonConnessi = new LinkedList<Condiment>();
		for(Condiment c: grafo.vertexSet()) {
			if(!Graphs.neighborListOf(grafo, condiment).contains(c))
				nonConnessi.add(c);
		}
		return nonConnessi;
	}
	

}
