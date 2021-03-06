package recommandation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import compilateurHistorique.DatasetInfo;
import compilateurHistorique.Instanciation;
import compilateurHistorique.Variable;
import graphOperation.DSeparation;
import graphOperation.DTreeGenerator;
import graphOperation.GrapheRC;

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
 * @author Pierre-François Gimenez
 *
 */

public class AlgoOldDRC extends AlgoRecoRB
{
	private DSeparation dsep;
	private DTreeGenerator dtreegenerator;
//	private ArrayList<String> variables;
	private Instanciation instanceReco;
	private GrapheRC g;
	private int seuil;
	private String RBfile;
//	private boolean avecDSep = false;
	private boolean avecHisto = true;
	private double cacheFactor;

	public AlgoOldDRC(String algo)
	{
		this(algo.equals("old-drc") ? 50 : -1, 1);
	}
	
	public AlgoOldDRC(int seuil, double cacheFactor)
	{
		super("hc");
		this.seuil= seuil;
		this.cacheFactor = cacheFactor;
		avecHisto = seuil != -1;
		GrapheRC.config(seuil, avecHisto, cacheFactor, 10);
	}
	
	
	public void describe()
	{
		System.out.println("Old DRC");
		System.out.println("seuil = "+seuil);
		System.out.println("cacheFactor = "+cacheFactor);
	}
	

	@Override
	public void apprendDonnees(DatasetInfo dataset, List<Instanciation> instances, long code)
	{
/*		System.out.println("Apprentissage de ");
		for(int i = 0; i < filename.size(); i++)
		{
			String s = filename.get(i);
			System.out.println("	"+s+".csv");
		}*/
		// Contraintes contient des variables supplémentaire
//		LecteurCdXml lect = new LecteurCdXml();
//		lect.lectureCSV(filename.get(0), entete);
		
		ArrayList<String> variables = new ArrayList<String>();
//		for(int i = 0; i < lect.nbvar; i++)
//			variables.add(lect.var[i]);
		for(Variable v : dataset.vars)
			variables.add(v.name);
		
//		String dataset = filename.get(0).substring(0, 1+filename.get(0).lastIndexOf("/"));
		dsep = new DSeparation(RBfile);
		dtreegenerator = new DTreeGenerator(RBfile);

		g = new GrapheRC(new ArrayList<String>(), variables, dtreegenerator, instances, dataset, dsep);
//		MultiHistoComp.initFamille(dsep.getFamilles());
/*		if(!(new File(dataset+"g"+nbIter)).exists() || !MultiHistoComp.loadCPT(dataset+"cpt"+nbIter))
		{
			historique.initCPT();
			// MultiHistoComp.saveCPT(dataset+"cpt"+nbIter);
		}*/
		instanceReco = new Instanciation(dataset);
//		if((new File(dataset+"g"+nbIter)).exists() && GrapheRC.load(dataset+"g"+nbIter))
			g.construct();
//		else
//		{
//			g.construct();
//			g.save(dataset+"g"+nbIter);
//			System.out.println("Construction du dtree fini");
//		}
		g.printTree();
		g.printGraphe();
	}
	
	@Override
	public String recommande(String variable, ArrayList<String> possibles)
	{
/*		ArrayList<String> requisite;
		if(avecDSep)
			requisite = dsep.getRequisiteObservation(historique.getVarConnues(instanceReco), variable);
		else
			requisite = variables;
		Instanciation sub = instanceReco.subInstanciation(requisite);
		*/
		GrapheRC.nbS = 0;

		HashMap<String, Double> proba = new HashMap<String, Double>();

		ArrayList<String> valeurs, valeurs2;
		
		// On itère que sur les valeurs possibles ou, si on n'a pas cette information, sur toutes les valeurs
		valeurs = dataset.vars[dataset.mapVar.get(variable)].values;
		valeurs2 = new ArrayList<String>();

//		g.reinitCache();
		for(String s : valeurs)
			if(possibles == null || possibles.contains(s))
				valeurs2.add(s);
		
		proba = g.computeToutesProba(instanceReco, variable, valeurs2);
//		for(int i = 0; i < 100; i++)
//			if(profondeur[i] != 0)
//				System.out.println("Profondeur "+i+": "+profondeur[i]);
//		g.printTree();
//		g.printGraphe();
		
		double probaMax = 0;
		String valueMax = null;
		for(String value : proba.keySet())
		{
//			System.out.println(value+": "+proba.get(value)/norm);
			double probaTmp = proba.get(value);
//			somme += probaTmp/norm;
			if(probaTmp >= probaMax)
			{
				probaMax = probaTmp;
				valueMax = value;
			}
		}
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
		g.reinitCache();
		instanceReco.deconditionneTout();
	}
	
	public String toString()
	{
		if(seuil == -1)
			return "AlgoRC";
		else
			return getClass().getSimpleName()+"-"+seuil;
	}
	
	@Override
	public void termine()
	{}
	
	@Override
	public void terminePli()
	{}

	@Override
	public void unassign(String variable)
	{
		instanceReco.deconditionne(variable);
	}

	@Override
	public void apprendRB(String file)
	{
		this.RBfile = file;
	}
	
}
