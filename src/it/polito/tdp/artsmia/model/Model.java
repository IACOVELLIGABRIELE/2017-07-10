package it.polito.tdp.artsmia.model;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private List<ArtObject> artObjects ;
	private Graph<ArtObject, DefaultWeightedEdge> graph; //grafo con vertici ArtObject e visto che non è orientato ma pesato metto DefaultWeightEdge
	private List<ArtObject> best;
	/**
	 * Popola la lista artObject dal database
	 */
	
	public void creaGrafo() {
		//Leggi lista oggetti DB e salva nella lista artObjects
		
		ArtsmiaDAO dao = new ArtsmiaDAO();
		this.artObjects = dao.listObjects();
		
		//Crea il grafo con zero vertici e zero archi
		// grafo pesato, semplice e non orientato
		
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungi i vertici
		
	/*	for(ArtObject ao : this.artObjects) {
			this.graph.addVertex(ao);
		}
		questa opzione viene fatta da "Graphs.addAllVertices(this.graph, this.artObjects);" 
		
	*/
		
		Graphs.addAllVertices(this.graph, this.artObjects);
		
		//Aggiungi archi e il loro peso
		
		for(ArtObject ao : this.artObjects) {
			
			List<ArtObjectAndCount> connessi = dao.listArtObjectAndCount(ao);
			
			for(ArtObjectAndCount c : connessi) {
				ArtObject dest = new ArtObject(c.getArtObjectId(), null, null, null, 0, null, null, null, null, null, 0, null, null, null, null, null);
				Graphs.addEdge(this.graph, ao, dest, c.getCount());
				System.out.format("(%d, %d) peso %d \n", ao.getId(), dest.getId(), c.getCount());
			}
		}
		
		
		
		
		
		
		/*  VERSIONE 1 
		 
		for(ArtObject partenza : this.artObjects) {
			for(ArtObject arrivo : this.artObjects) {
				if(!partenza.equals(arrivo) && partenza.getId() < arrivo.getId()) { //escludo i loop && faccio si che il grafo non sia orientato non ripeto archi ( es. 1-5, 5-1) nei grafi non orientati è lo stesso arco
					int peso = exhibitionComuni(partenza, arrivo);
					
				//System.out.format("(%d, %d) peso %d \n", partenza.getId(), arrivo.getId(),peso);
					
					if(peso != 0) {
						
						System.out.format("(%d, %d) peso %d\n", partenza.getId(), arrivo.getId(),peso);
						Graphs.addEdge(this.graph, partenza, arrivo, peso);     
						
						//questa dichiarazione ha lo stesso valore
						//DefaultWeightedEdge e = this.graph.addEdge(partenza, arrivo);
					   // this.graph.setEdgeWeight(e, peso);
						
					}
				}
			}
		}*/
		
		
	}

	private int exhibitionComuni(ArtObject partenza, ArtObject arrivo) {
		ArtsmiaDAO dao = new ArtsmiaDAO();
		
		int comuni = dao.contaExhibitionComuni(partenza, arrivo);
		
		return comuni;
	}
	
	// 2 PUNTO RICORSIONE
	
	public List<ArtObject> camminoMassimo(int startId, int LUN){
		
		//trovare vertice partenza
		
		ArtObject start = null;
		for(ArtObject ao : this.artObjects) {
			if(ao.getId() == startId) {
			//	start.setClassification(ao.getClassification());
			}
		}
		
		List<ArtObject> parziale = new ArrayList<>();
		parziale.add(start);
		
		best = parziale;
		
		cerca(parziale,1,LUN);
		
		return best;
	}
	
	private void cerca (List<ArtObject> parziale, int livello, int LUN) {
		
		if(livello == LUN) {
			//caso terminale 
			if(peso(parziale) > peso(best)) {
				best = new ArrayList<>(parziale);
				System.out.println(best);
			}
		}
		
		//trova vertici adiacenti all'ultimo della sequenza 
		
		ArtObject ultimo = parziale.get(parziale.size()-1);        //non posso fare ricorsione su lista vuota 
		
		List<ArtObject> adiacenti = Graphs.neighborListOf(this.graph, ultimo);
		for(ArtObject prova : adiacenti) {
			if(!parziale.contains(prova) && prova.getClassification().equals(parziale.get(0).getClassification())) {
				parziale.add(prova);
				cerca(parziale,livello+1,LUN);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	public int peso(List<ArtObject> parziale) {
		int peso = 0;
		for(int i=0; i<parziale.size()-1;i++) {
			DefaultWeightedEdge e = this.graph.getEdge(parziale.get(i), parziale.get(i+1));
			int pesoarco = (int) graph.getEdgeWeight(e);
			peso += pesoarco;
		}
		return peso;
	}

}
	


