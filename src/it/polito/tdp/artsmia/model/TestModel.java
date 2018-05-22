package it.polito.tdp.artsmia.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		m.creaGrafo();
		
		List<ArtObject> best = m.camminoMassimo(4318, 4);
		
		

	}

}
