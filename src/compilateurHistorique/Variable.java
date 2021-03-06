package compilateurHistorique;

import java.io.Serializable;
import java.util.ArrayList;

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
 * Juste une petite structure
 * @author Pierre-François Gimenez
 *
 */

public class Variable implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String name;
	public int domain;
	public ArrayList<String> values = new ArrayList<String>();
	public int index; // l'indice de cette variable dans DatasetInfo.vars
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public String toString()
	{
		return name+", values = "+values;
	}
	
	@Override
	public boolean equals(Object o)
	{
/*		if(o instanceof String)
			return name.equals((String)o);
		else */if(o instanceof Variable)
		{
			Variable oVar = (Variable) o;
			if(!name.equals(oVar.name))
				return false;
			if(oVar.domain != domain)
				return false;
			for(int i = 0; i < domain; i++)
				if(!values.get(i).equals(oVar.values.get(i)))
					return false;
			return true;
		}
		return false;
	}

}