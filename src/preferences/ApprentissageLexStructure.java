package preferences;

import java.util.ArrayList;

import compilateur.LecteurCdXml;
import compilateurHistorique.HistoComp;
import heuristiques.HeuristiqueOrdre;

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
 * Apprentissage d'une structure basée sur l'ordre lexicographique
 * @author pgimenez
 *
 */

public abstract class ApprentissageLexStructure
{
	protected int nbVar;
	protected long base;
	protected ArrayList<String> variables;
	protected LexicographicStructure struct;
	protected HistoComp historique;
	protected HeuristiqueOrdre h;
	
	public void apprendDomainesVariables(ArrayList<String> filename, boolean entete)
	{
		historique = new HistoComp(filename, entete);
	}
	
	public void apprendDonnees(ArrayList<String> filename, boolean entete) {
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
		nbVar = variables.size();

		historique.compile(filename, entete);

		base = 1;
		for(String var : variables)
			base *= historique.nbModalites(var);

	}
	
	/**
	 * Renvoie le meilleur élément qui vérifie les variables déjà fixées
	 * @param element
	 * @param ordreVariables
	 * @return
	 */
	public String infereBest(String varARecommander, ArrayList<String> possibles, ArrayList<String> element, ArrayList<String> ordreVariables)
	{
		return struct.infereBest(varARecommander, possibles, element, ordreVariables);
	}
	
	public long infereRang(ArrayList<String> element, ArrayList<String> ordreVariables)
	{
		// +1 car les rangs commencent à 1 et pas à 0
		long rang = struct.infereRang(element, ordreVariables) + 1;
//		System.out.println(rang);
		return rang;
	}

	public long rangMax()
	{
		return base;
	}

	protected LexicographicOrder apprendOrdre(HistoComp historique, ArrayList<String> variablesRestantes)
	{
		ArrayList<String> variables = new ArrayList<String>();
		variables.addAll(variablesRestantes);
		int nbVar = variables.size();
		LexicographicOrder[] all = new LexicographicOrder[nbVar];

		int k = 0;
		for(String var : variables)
		{
			int nbMod = historique.nbModalites(var);
			all[k++] = new LexicographicOrder(var, nbMod, h);
		}
		
		for(LexicographicOrder node : all)
			node.setNbExemples(historique.getNbInstancesToutesModalitees(node.getVar(), null, true));

		// Tri à bulles sur les entropies
		LexicographicOrder tmp, struct;
		for(int i = 0; i < nbVar-1; i++)
		{
			for(int j = 0; j < i; j++)
				if(all[i].getHeuristique() < all[i+1].getHeuristique())
				{
					tmp = all[i];
					all[i] = all[j];
					all[j] = tmp;
				}
		}

		struct = null;
		for(int i = 0; i < nbVar; i++)
		{
			all[i].setEnfant(struct);
			struct = all[i];
		}

		return struct;
	}

	public void affiche()
	{
		struct.affiche();
	}

	public void save(String filename)
	{
		struct.save(filename);
	}

	public boolean load(String filename)
	{
		struct = LexicographicStructure.load(filename);
		if(struct == null)
			System.out.println("Le chargement a échoué");
		else
			System.out.println("Lecture de "+filename);
		return struct != null;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}

}
