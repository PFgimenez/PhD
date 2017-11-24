package recommandation;

import java.util.ArrayList;

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

/**
 * Recommandation basée sur le voisinage (most popular choice)
 * @author Pierre-François Gimenez
 *
 */

public class AlgoVoisinsMostPopular extends AlgoVoisins
{
	public AlgoVoisinsMostPopular(int nbVoisins)
	{
		super(nbVoisins);
	}
	
	public AlgoVoisinsMostPopular(ParserProcess pp)
	{
		super(pp);
	}

	public void describe()
	{
		System.out.println("Most popular");
		System.out.println(computer);
	}

	@Override
	public String recommande(String variable, ArrayList<String> possibles)
	{
		return voisins.mostPopularChoice(conf, variable, computer, possibles);
	}
}
