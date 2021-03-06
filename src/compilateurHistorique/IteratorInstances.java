package compilateurHistorique;

import java.util.Iterator;
import java.util.List;

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
 * Un itérateur sur les instances
 * @author Pierre-François Gimenez
 *
 */

public class IteratorInstances implements Iterator<Instanciation>
{
	private Variable[] varsToInstantiate = null;
	private DatasetInfo dataset;
	private int tailleSet;
	private Instanciation instance;
	private int nbActuel, nbMax;
	public int nbMemory;
	
	public IteratorInstances(DatasetInfo dataset)
	{
		this(-1, dataset);
	}
	
	public IteratorInstances(int nbMemory, DatasetInfo dataset)
	{
		this.dataset = dataset;
		this.nbMemory = nbMemory;
		this.varsToInstantiate = new Variable[dataset.vars.length];
	}
	
	public final void init(Instanciation instanceActuelle, List<String> varsToInstantiate)
	{
		int[] index = new int[varsToInstantiate.size()];
		for(int i = 0; i < index.length; i++)
			index[i] = dataset.mapVar.get(varsToInstantiate.get(i));
		init(instanceActuelle, index);
	}
	
	/**
	 * On ignore les variables du cutset déjà instanciée dans l'instance fournie
	 * @param instanceActuelle
	 * @param vars
	 * @param mapVariables
	 * @param varsToInstantiate
	 */
	public final void init(Instanciation instanceActuelle, int[] varsToInstantiate)
	{
		instance = instanceActuelle;
		nbMax = 1;
		tailleSet = 0;
		for(int i = 0; i < varsToInstantiate.length; i++)
		{
			int indice = varsToInstantiate[i];
			if(instance.values[indice] == null)
			{
				Variable var = dataset.vars[indice];
				this.varsToInstantiate[tailleSet++] = var;
				instance.values[indice] = 0;
				nbMax *= var.domain;
				instance.nbVarInstanciees++;
			}
		}
		nbActuel = 0;
	}
	
	@Override
	public final boolean hasNext()
	{
		return nbActuel < nbMax;
	}

	@Override
	public final Instanciation next()
	{
		boolean diff = true;
		int nbVar = 0;
		int tmp = nbActuel;
		while(diff && nbVar < tailleSet)
		{
			Variable v = varsToInstantiate[nbVar++];
			int val = tmp % v.domain;
			tmp /= v.domain;
			instance.values[v.index] = val;
			diff = val == 0;
		}
		nbActuel++;
		return instance;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

}
