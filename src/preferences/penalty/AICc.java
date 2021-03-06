package preferences.penalty;

/*   (C) Copyright 2017, Gimenez Pierre-François 
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
 * Akaike Information Criterion corrected
 * @author Pierre-François Gimenez
 *
 */

public class AICc implements PenaltyWeightFunction
{
	private double k;
	
	public AICc(double k)
	{
		this.k = k;
	}
	
	@Override
	public int hashCode()
	{
		return (int)(20184+k*10000);
	}
	
	@Override
	public double phi(int n)
	{
		return k+(k+1)*(k+2)/(n-k-2);
	}
	
	public String toString()
	{
		return "AICc with parameter : "+k;
	}
}
