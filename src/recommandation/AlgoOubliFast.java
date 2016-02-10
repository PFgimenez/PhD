package recommandation;

import java.util.ArrayList;
import java.util.HashMap;

import compilateurHistorique.HistoComp;
import compilateurHistorique.Instanciation;
import graphOperation.DSeparation;
import graphOperation.DTreeGenerator;
import graphOperation.Graphe;
import compilateur.LecteurCdXml;
import compilateur.SALADD;

/*   (C) Copyright 2016, Gimenez Pierre-François
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Algorithme de recommandation avec arbre utilisant une méthode d'oubli
 * Le d-tree est calculé une fois pour toute
 * @author pgimenez
 *
 */

public class AlgoOubliFast implements AlgoReco
{
	private HistoComp historique;
	private DSeparation dsep;
	private DTreeGenerator dtreegenerator;
	private ArrayList<String> variables;
	private Instanciation instanceReco;
	private HashMap<String, HashMap<String, Double>> probaAPriori;
	private SALADD contraintes;
	private Graphe g;

	public AlgoOubliFast(int seuil)
	{
		Graphe.config(seuil);
	}
	
	public void charge(String s)
	{
		historique = HistoComp.load(s);
	}
	
	public void save(String s)
	{
		historique.save(s);
	}
	
	@Override
	public void apprendContraintes(SALADD contraintes)
	{
		this.contraintes = contraintes;
	}
	
	@Override
	public void apprendDonnees(ArrayList<String> filename, int nbIter, boolean entete) {
		System.out.println("Apprentissage de ");
		for(int i = 0; i < filename.size(); i++)
		{
			String s = filename.get(i);
			System.out.println("	"+s+".csv");
		}
		
		// Contraintes contient des variables supplémentaire
		LecteurCdXml lect = new LecteurCdXml();
		lect.lectureCSV(filename.get(0), entete);
		
		variables = new ArrayList<String>();
		for(int i = 0; i < lect.nbvar; i++)
			variables.add(lect.var[i]);
		
//		for(int i = 0; i<lect.nbvar; i++)
//			System.out.println("Var : "+lect.var[i]);
		
//		System.out.println("Nb var: "+lect.nbvar);
		
		historique.compile(filename, entete);

		System.out.println("Compilation de l'historique finie : "+historique.getNbNoeuds()+" nœuds");
		probaAPriori = new HashMap<String, HashMap<String, Double>>();
		
		for(String s : variables)
			probaAPriori.put(s, historique.getProbaToutesModalitees(s, null, false, new Instanciation()));
			
		String dataset = filename.get(0).substring(0, 1+filename.get(0).lastIndexOf("/"));
		dsep = new DSeparation(dataset, nbIter);
		dtreegenerator = new DTreeGenerator(dataset, nbIter);
		instanceReco = new Instanciation();
		g = new Graphe(contraintes, new ArrayList<String>(), variables, historique, dtreegenerator, dsep);		
		g.construct();
	}
	
	@Override
	public String recommande(String variable, ArrayList<String> possibles)
	{
/*		HashMap<String, Double> proba3 = historique.getProbaToutesModalitees(variable, possibles, false);
		double probaMax3 = 0;
		String valueMax3 = null;
		for(String value : proba3.keySet())
		{
			System.out.println(value+": "+proba3.get(value));
			
			double probaTmp = proba3.get(value);
			if(probaTmp >= probaMax3)
			{
				probaMax3 = probaTmp;
				valueMax3 = value;
			}
		}
		
		if(true)
			return valueMax3;
		*//*
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
//		System.out.println();
//		System.out.println("Reco pour "+variable);
//		System.out.println("Connaissances : "+instanceReco);
		ArrayList<String> requisite = dsep.getRequisiteObservation(historique.getVarConnues(instanceReco), variable);
		Instanciation sub = instanceReco.subInstanciation(requisite);
		
/*		System.out.print("Requisite : ");
		for(String s : requisite)
			System.out.print(s+" ");
		System.out.println();
*/
		Graphe.nbS = 0;
//		System.out.println("Nb exemples sans oubli : "+historique.getNbInstances(sub));
		HashMap<String, Double> proba = new HashMap<String, Double>();

		ArrayList<String> valeurs;
		
		// On itère que sur les valeurs possibles ou, si on n'a pas cette information, sur toutes les valeurs
		valeurs = historique.getValues(variable);
		
		double norm = g.computeProba(sub,variable);

		for(String s : valeurs)
		{
			if(possibles != null && !possibles.contains(s))
				continue;
			sub.conditionne(variable, s);
//			System.out.println("Conditionnement de "+variable+" à "+s);
			proba.put(s, g.computeProba(sub.clone(), variable));
			sub.deconditionne(variable);
		}
//		g.reinitCache();
		g.printTree();
		g.printGraphe();
		
		double somme = 0;
		double probaMax = 0;
		String valueMax = null;
		for(String value : proba.keySet())
		{
//			System.out.println(value+": "+proba.get(value)/norm);
			
			double probaTmp = proba.get(value)/norm;
			somme += probaTmp;
			if(probaTmp >= probaMax)
			{
				probaMax = probaTmp;
				valueMax = value;
			}
		}
//		System.out.println("Somme des proba : "+somme);
		
//		proba = historique.getProbaToutesModalitees(variable, possibles);
/*		HashMap<String, Double> proba2 = historique.getProbaToutesModalitees(variable, possibles, false, sub);
		System.out.println("Calcul classique, avec "+historique+": ("+historique.getNbInstances(sub)+")");
		for(String value : proba2.keySet())
			System.out.println(value+": "+proba2.get(value));
	*/	
/*
		if(Graphe.nbS > 1)
		{
			int z = 0;
			z = 1/z;
		}
*/
		
		return valueMax;
	}

	@Override
	public void setSolution(String variable, String solution)
	{
		instanceReco.conditionne(variable, solution);
	}

	@Override
	public void oublieSession()
	{
		instanceReco.deconditionneTout();
	}
	
	public String toString()
	{
		return getClass().getSimpleName();
	}
	
	@Override
	public void termine()
	{}
	
	public void initHistorique(ArrayList<String> filename, boolean entete)
	{
		historique = new HistoComp(filename, entete);
	}

}