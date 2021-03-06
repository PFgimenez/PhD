package recommandation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import compilateurHistorique.DatasetInfo;
import compilateurHistorique.Instanciation;
import preferences.ProbabilityDistributionLog;
import preferences.completeTree.ApprentissageGloutonMultipleTree;
import preferences.completeTree.LexicographicMultipleTree;
import preferences.heuristiques.HeuristiqueChoix;
import preferences.heuristiques.HeuristiqueMultipleComposedDuel;
import preferences.heuristiques.HeuristiqueOptimale;
import preferences.penalty.BIC;
import preferences.penalty.PenaltyWeightFunction;
import recommandation.parser.ParserProcess;

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

// Recommandation par apprentissage de préférences

public class AlgoLexMultipleTree extends Clusturable
{

	private ApprentissageGloutonMultipleTree algo;
	private LexicographicMultipleTree struct;
	private HashMap<String, String> valeurs;
	private boolean prune;
	private PenaltyWeightFunction phi = new BIC();
	private ProbabilityDistributionLog p;
	private BigInteger rangMoyen;
	
	public AlgoLexMultipleTree(ParserProcess pp)
	{
		int seuil = 20;
		this.prune = Boolean.parseBoolean(pp.read());
		int taille = Integer.parseInt(pp.read());
		algo = new ApprentissageGloutonMultipleTree(300, seuil, new HeuristiqueMultipleComposedDuel(taille));
//		algo = new ApprentissageGloutonMultipleTree(300, seuil, new HeuristiqueOptimale(taille));
//		algo = new ApprentissageGloutonMultipleTree(300, seuil, new HeuristiqueChoix(taille, seuil));
		valeurs = new HashMap<String, String>();
	}

	@Override
	public int hashCode()
	{
		return algo.hashCode() * 2 + (prune ? 1 + 2 * phi.hashCode() : 0);
	}
	
	public AlgoLexMultipleTree()
	{
		this(new ApprentissageGloutonMultipleTree(300, 20, new HeuristiqueMultipleComposedDuel(2)), false);
	}
	
	public AlgoLexMultipleTree(ApprentissageGloutonMultipleTree algo, boolean prune)
	{
		this.prune = prune;
		this.algo = algo;
		valeurs = new HashMap<String, String>();
	}
	
	public void describe()
	{
		System.out.print("LP-multiple-tree "+algo+" (prune = "+prune);
		if(prune)
			System.out.print(", phi = "+phi+", p = "+p);
		System.out.println(")");
	}
	
	@Override
	public void apprendDonnees(DatasetInfo dataset, List<Instanciation> instances, long code)
	{
		struct = LexicographicMultipleTree.load("tmp/multiple-lextree-"+code+"-"+hashCode());
		if(struct == null)
		{
			if(prune)
			{
				// On tente de charger la version non-prune
				prune = false;
				struct = LexicographicMultipleTree.load("tmp/multiple-lextree-"+code+"-"+hashCode());
				prune = true;
			}
			if(struct == null)
			{
				System.out.println("Apprentissage du LP-tree avec "+instances.size()+" exemples…");
				struct = algo.apprendDonnees(dataset, instances);
			}
			assert struct != null;
			
			if(prune)
			{
				System.out.println("Nb nœuds avant prune : "+struct.getNbNoeuds());
				algo.pruneFeuille(phi, struct, dataset, instances);
				System.out.println("Nb nœuds après prune : "+struct.getNbNoeuds());
			}
			else
				System.out.println("Nb nœuds : "+struct.getNbNoeuds());
			struct.save("tmp/multiple-lextree-"+code+"-"+hashCode());
		}
		rangMoyen = struct.rangMoyen(instances);
		struct.affiche("nouveau");
	}
	
	public void printMeanRank()
	{
		BigInteger rangMax = struct.getRangMax();
		System.out.println("Rang moyen : "+rangMoyen);
		System.out.println("Rang max : "+rangMax);
		System.out.println("Rang moyen / rang max : "+new BigDecimal(rangMoyen.multiply(BigInteger.valueOf(100))).divide(new BigDecimal(rangMax), 10, RoundingMode.HALF_EVEN)+"%");
	}

	@Override
	public String recommande(String variable, ArrayList<String> possibles)
	{
		return struct.infereBest(variable, valeurs, possibles);
	}

	@Override
	public void setSolution(String variable, String solution)
	{
		valeurs.put(variable, solution);
	}

	@Override
	public void oublieSession()
	{
		valeurs.clear();
	}

	@Override
	public void terminePli()
	{
		printMeanRank();
	}
	
	@Override
	public void termine()
	{}

	public String toString()
	{
		return getClass().getSimpleName();
	}

	@Override
	public void unassign(String variable)
	{
		valeurs.remove(variable);
	}

	@Override
	public HashMap<String, Double> metricCoeff()
	{
		HashMap<String, Double> out = new HashMap<String, Double>();
		BigInteger rangMax = struct.getRangMax();
		out.put("Rang moyen", new BigDecimal(rangMoyen).divide(new BigDecimal(rangMax), 250, RoundingMode.HALF_EVEN).doubleValue());
		return out;
	}

	@Override
	public HashMap<String, Double> metric()
	{
		HashMap<String, Double> out = new HashMap<String, Double>();
		System.out.println("Taille arbre : "+struct.getNbNoeuds());
		out.put("Taille arbre", (double)struct.getNbNoeuds());
		return out;
	}

}