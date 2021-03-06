package preferences.completeTree;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import compilateurHistorique.DatasetInfo;
import compilateurHistorique.Instanciation;
import preferences.ProbabilityDistributionLog;
import preferences.heuristiques.HeuristiqueComplexe;
import preferences.penalty.PenaltyWeightFunction;

/*   (C) Copyright 2015, Gimenez Pierre-François 
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
 * Apprentissage d'un arbre lexicographique incomplet
 * @author Pierre-François Gimenez
 *
 */

public class ApprentissageGloutonLexTree extends ApprentissageGloutonLexStructure
{
	private int profondeurMax;
	private int seuil;

	public ApprentissageGloutonLexTree(int profondeurMax, int seuil, HeuristiqueComplexe h)
	{
		this.h = h;
		this.profondeurMax = profondeurMax;
		this.seuil = seuil;
	}
	
	public String toString()
	{
		return "ApprentissageGloutonLexTree, profMax = "+profondeurMax+", seuil = "+seuil+", heuristique = "+h.getClass().getSimpleName();
	}
	
	private LexicographicStructure apprendRecursif(Instanciation instance, ArrayList<String> variablesRestantes, boolean preferred, int profondeur)
	{
		ArrayList<String> variablesTmp = new ArrayList<String>();
		variablesTmp.addAll(variablesRestantes);
	
		String var = h.getRacine(dataset, historique, variablesTmp, instance);
		
		int pasAssez = 0;
		
		// si on a dépassé la profondeur max
		if(variablesTmp.size() < variables.size() - profondeurMax)
			return apprendOrdre(instance, variablesTmp);
		
		// pas du tout assez d'exemples
		if(historique.getNbInstances(instance) < seuil)
		{
//			System.out.println("Pas assez du tout !");
			return apprendOrdre(instance, variablesTmp);
		}
		
		/**
		 * Peut-on avoir un split sans risquer de finir avec trop peu d'exemples dans une branche ?
		 */
		for(String val : dataset.vars[dataset.mapVar.get(var)].values)
		{
			instance.conditionne(var, val);
			int nbInstances = historique.getNbInstances(instance);
			instance.deconditionne(var);
			
			if(nbInstances < seuil) // split impossible !
				pasAssez++;
		}

		/**
		 * Split
		 */
		LexicographicTree best = new LexicographicTree(var, dataset.vars[dataset.mapVar.get(var)].domain, pasAssez == 0, profondeur);
		best.setOrdrePref(historique.getNbInstancesToutesModalitees(var, true, instance));

		// Si c'était la dernière variable, alors c'est une feuille
		if(variablesTmp.size() == 1)
			return best;
		
		variablesTmp.remove(best.getVar());
		int nbMod = best.getNbMod();
		
		if(pasAssez == 0)
		{
			for(int i = 0; i < nbMod; i++)
			{
				// On conditionne par une certaine valeur
				instance.conditionne(best.getVar(), best.getPref(i));			
				best.setEnfant(i, apprendRecursif(instance, variablesTmp, i == 0, profondeur+1));
				instance.deconditionne(best.getVar());
			}
		}
		else
		{
			// Pas de split. On apprend un seul enfant qu'on associe à toutes les branches sortantes.
			LexicographicStructure enfant = apprendRecursif(instance, variablesTmp, true, profondeur+1);
//			for(int i = 0; i < nbMod; i++)
			best.setEnfant(0, enfant);
		}
		// A la fin, le VDD est conditionné de la même manière qu'à l'appel
		return best;
	}
	
/*	public LexicographicStructure apprendDonnees(ArrayList<String> filename, boolean entete)
	{
		return apprendDonnees(filename, entete, -1);
	}
	*/
	public LexicographicStructure apprendDonnees(DatasetInfo dataset, List<Instanciation> instances)
	{
		super.apprendDonnees(dataset, instances);
		ArrayList<String> variablesTmp = new ArrayList<String>();
		variablesTmp.addAll(variables);
		struct = apprendRecursif(new Instanciation(dataset), variables, true, 1);
//		System.out.println("Apprentissage fini");
		struct.updateBase(base);
		return struct;
	}

	/**
	 * Élaguer l'arbre. Commence par les feuilles
	 */
	public void pruneFeuille(PenaltyWeightFunction f)
	{
		LinkedList<LexicographicStructure> file = new LinkedList<LexicographicStructure>();
		LinkedList<LexicographicStructure> fileChercheFeuilles = new LinkedList<LexicographicStructure>();
		
		fileChercheFeuilles.add(struct);
		
		while(!fileChercheFeuilles.isEmpty())
		{
			LexicographicStructure s = fileChercheFeuilles.removeFirst();
			file.addFirst(s);
			List<LexicographicStructure> enfants = s.getEnfants();
			if(!enfants.isEmpty())
			{
				if(s.split)
					for(LexicographicStructure l : enfants)
						fileChercheFeuilles.add(l);
				else
					fileChercheFeuilles.add(enfants.get(0)); // on n'ajoute que celui de gauche
			}
		}
		
		double scoreSansPruning = computeScoreWithMeanRank(f, struct, allInstances);
		
		while(!file.isEmpty())
		{
			LexicographicStructure s = file.removeFirst();
			List<LexicographicStructure> enfants = s.getEnfants();
			if(s.split && enfants != null && enfants.size() > 0) // si enfants est nul (ou de taille nulle), c'est que s est une feuille et donc le pruning ne le concerne pas
			{
				LexicographicTree s2 = (LexicographicTree) s;
				s.split = false;
				s2.setEnfant(0, enfants.get(0));
				
				double scoreAvecPruning = computeScoreWithMeanRank(f, struct, allInstances);
//				System.out.println("Score avant : "+scoreSansPruning+", après : "+scoreAvecPruning);
				if(scoreAvecPruning > scoreSansPruning) // on a amélioré le score !
					scoreSansPruning = scoreAvecPruning;
				else // c'était mieux avec le split
				{
					s.split = true;
					for(int i = 0; i < enfants.size(); i++)
						s2.setEnfant(i, enfants.get(i));
				}
			}
			
		}
		
	}
	

	/**
	 * Élaguer l'arbre. Commence par la racine. N'élague qu'à partir de la profondeur spécifiée (profondeur = 1 : on élague dès la racine)
	 */
	public void pruneRacineProfondeurMin(PenaltyWeightFunction f, ProbabilityDistributionLog p, int profondeurMin)
	{
		LinkedList<LexicographicStructure> file = new LinkedList<LexicographicStructure>();
		file.add(struct);
		double scoreSansPruning = computeScore(f, p);
		
		while(!file.isEmpty())
		{
			LexicographicStructure s = file.removeFirst();
			List<LexicographicStructure> enfants = s.getEnfants();
			if(s.profondeur >= profondeurMin && s.split && enfants != null && enfants.size() > 0) // si enfants est nul (ou de taille nulle), c'est que s est une feuille et donc le pruning ne le concerne pas
			{
				LexicographicTree s2 = (LexicographicTree) s;
				s.split = false;
				s2.setEnfant(0, enfants.get(0));
				
				double scoreAvecPruning = computeScore(f, p);
//				System.out.println("Score avant : "+scoreSansPruning+", après : "+scoreAvecPruning);
				if(scoreAvecPruning > scoreSansPruning) // on a amélioré le score !
					scoreSansPruning = scoreAvecPruning;
				else // c'était mieux avec le split
				{
					s.split = true;
					for(int i = 0; i < enfants.size(); i++)
						s2.setEnfant(i, enfants.get(i));
				}
			}
			
			if(!enfants.isEmpty())
			{
				if(s.split)
					for(LexicographicStructure l : enfants)
						file.add(l);
				else
					file.add(enfants.get(0)); // on n'ajoute que celui de gauche
			}
		}
		
	}
	
	/**
	 * Élaguer l'arbre. Commence par la racine
	 */
	public void pruneRacine(PenaltyWeightFunction f, ProbabilityDistributionLog p)
	{
		LinkedList<LexicographicStructure> file = new LinkedList<LexicographicStructure>();
		file.add(struct);
		double scoreSansPruning = computeScore(f, p);
		
		while(!file.isEmpty())
		{
			LexicographicStructure s = file.removeFirst();
			List<LexicographicStructure> enfants = s.getEnfants();
			if(s.split && enfants != null && enfants.size() > 0) // si enfants est nul (ou de taille nulle), c'est que s est une feuille et donc le pruning ne le concerne pas
			{
				LexicographicTree s2 = (LexicographicTree) s;
				s.split = false;
				s2.setEnfant(0, enfants.get(0));
				
				double scoreAvecPruning = computeScore(f, p);
//				System.out.println("Score avant : "+scoreSansPruning+", après : "+scoreAvecPruning);
				if(scoreAvecPruning > scoreSansPruning) // on a amélioré le score !
					scoreSansPruning = scoreAvecPruning;
				else // c'était mieux avec le split
				{
					s.split = true;
					for(int i = 0; i < enfants.size(); i++)
						s2.setEnfant(i, enfants.get(i));
				}
			}
			
			if(!enfants.isEmpty())
			{
				if(s.split)
					for(LexicographicStructure l : enfants)
						file.add(l);
				else
					file.add(enfants.get(0)); // on n'ajoute que celui de gauche
			}
		}
		
	}
	
	/**
	 * Calcule le score de l'arbre sur les données précédemment apprises
	 * @param f
	 * @param p
	 * @return
	 */
	public double computeScore(PenaltyWeightFunction f, ProbabilityDistributionLog p)
	{
		BigDecimal LL = new BigDecimal(0);
		for(Instanciation i : allInstances)
		{
			ArrayList<String> val = new ArrayList<String>();
			List<String> var = i.getVarConditionees();
			for(String v : var)
				val.add(i.getValue(v));
			BigDecimal pr = p.logProbability(struct.infereRang(val, var));
			LL = LL.add(pr);
//			System.out.println(pr);
		}
//		System.out.println("LL : "+LL.doubleValue()+", phi : "+f.phi(allInstances.length)+", taille : "+struct.getNbNoeuds());
		return LL.doubleValue() - f.phi(allInstances.size()) * struct.getNbNoeuds();
	}
	
	/**
	 * Calcule le score de l'arbre sur les données précédemment apprises
	 * @param f
	 * @param p
	 * @return
	 */
	public BigInteger rangMoyen()
	{
		BigInteger out = BigInteger.ZERO;
		for(Instanciation i : allInstances)
		{
			ArrayList<String> val = new ArrayList<String>();
			List<String> var = i.getVarConditionees();
			for(String v : var)
				val.add(i.getValue(v));
			out = out.add(struct.infereRang(val, var));
		}
		return out.divide(BigInteger.valueOf(allInstances.size()));
	}

	public double computeScoreWithMeanRank(PenaltyWeightFunction f, LexicographicStructure struct, List<Instanciation> instances)
	{
		BigDecimal empRank = new BigDecimal(struct.sommeRang(instances)).divide(new BigDecimal(struct.getRangMax()), 250, RoundingMode.HALF_EVEN);
//		System.out.println(empRank+" "+f.phi(instances.length) * struct.getNbNoeuds());
		return - empRank.doubleValue() - f.phi(instances.size()) * struct.getNbNoeuds();
	}
}
