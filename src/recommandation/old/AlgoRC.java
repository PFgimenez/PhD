package recommandation.old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import compilateurHistorique.DatasetInfo;
import compilateurHistorique.Instanciation;
import graphOperation.DSeparation;
import graphOperation.DTreeGenerator;
import graphOperation.GrapheRC;
import recommandation.AlgoRecoRB;
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
 * Algorithme de recommandation utilisant l'algorithme recursive conditioning (RC)
 * Le d-tree est calculé une fois pour toute
 * @author Pierre-François Gimenez
 *
 */

public class AlgoRC extends AlgoRecoRB
{
	/**
	 * NE PAS UTILISER. APPELER ALGODRC AVEC COMME SEUIL -1 À LA PLACE
	 */
	
	private DatasetInfo dataset;
	private DSeparation dsep;
	private DTreeGenerator dtreegenerator;
	private ArrayList<String> variables;
	private Instanciation instanceReco;
	private GrapheRC g;
	private String RBfile;

	public AlgoRC(double cacheFactor)
	{
		super("hc");
		GrapheRC.config(-1, false, cacheFactor, 0);
	}
	
	public void describe()
	{
		System.out.println("RC");
	}

/*	@Override
	public void apprendContraintes(SALADD contraintes)
	{}*/
	

	@Override
	public void apprendDonnees(DatasetInfo dataset, List<Instanciation> instances, long code) {
		// TODO Auto-generated method stub
		
	}
	
/*	@Override
	public void apprendDonnees(DatasetInfo datasetinfo, ArrayList<String> filename, int nbIter, boolean entete) {
		System.out.println("Apprentissage de ");
		for(int i = 0; i < filename.size(); i++)
		{
			String s = filename.get(i);
			System.out.println("	"+s+".csv");
		}
		dataset = datasetinfo;
		// Contraintes contient des variables supplémentaire
		LecteurCdXml lect = new LecteurCdXml();
		lect.lectureCSV(filename.get(0), entete);
		
		variables = new ArrayList<String>();
		for(int i = 0; i < lect.nbvar; i++)
			variables.add(lect.var[i]);
		
//		String dataset = filename.get(0).substring(0, 1+filename.get(0).lastIndexOf("/"));
		dsep = new DSeparation(RBfile);
		dtreegenerator = new DTreeGenerator(RBfile);
		
		g = new GrapheRC(new ArrayList<String>(), variables, dtreegenerator, filename, datasetinfo, entete, dsep);
		instanceReco = new Instanciation(datasetinfo);
		g.construct();
		System.out.println("Construction du dtree fini");
	}*/
	
	@Override
	public String recommande(String variable, ArrayList<String> possibles)
	{
//		ArrayList<String> requisite;
//		requisite = variables;
//		Instanciation sub = instanceReco.subInstanciation(requisite);
		
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

		double probaMax = 0;
		String valueMax = null;
		for(String value : proba.keySet())
		{
			double probaTmp = proba.get(value);
//			System.out.println(value+" "+probaTmp);	
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
		instanceReco.deconditionneTout();
	}
	
	public String toString()
	{
		return getClass().getSimpleName();
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
