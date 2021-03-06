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

package compilateurHistorique;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import compilateur.LecteurCdXml;
/**
 * A bunch of metadata about a dataset
 * @author pgimenez
 *
 */

public class DatasetInfo implements Serializable
{

	private static final long serialVersionUID = 1L;
	public Variable[] vars;
	public HashMap<String, Integer> mapVar; // associe au nom d'une variable sa position dans "vars"
	
	public boolean equals(Object other)
	{
		if(other == null || !(other instanceof DatasetInfo))
			return false;
		DatasetInfo otherDataset = (DatasetInfo) other;
		if(vars.length != otherDataset.vars.length)
		{
			System.out.println("Pas le même nombre de variables");
			return false;
		}
		for(int i = 0; i < vars.length; i++)
			if(!vars[i].equals(otherDataset.vars[i]))
			{
				System.out.println("Variable différente : "+vars[i]+" "+otherDataset.vars[i]);
				return false;
			}
		if(!mapVar.equals(otherDataset.mapVar))
		{
			System.out.println("Map var différentes : "+mapVar+" "+otherDataset.mapVar);
			return false;
		}
		return true;
	}
	
	/**
	 * Initialisation from a bayesian network file
	 * @param rbfile
	 */
	public DatasetInfo(String rbfile)
	{
		// Vérification de toutes les valeurs possibles pour les variables
//		Variable[] vars = null;
		int nbVariables;
		
		NodeList nList;
		try {
			File fXmlFile = new File(rbfile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
	
			nList = doc.getElementsByTagName("VARIABLE");
			nbVariables=nList.getLength();
			vars = new Variable[nbVariables];
			
			for (int temp = 0; temp < nList.getLength(); temp++)
			{
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) nNode;
					//dans une variable
						
					//on parcour le name
					NodeList nList2 = eElement.getElementsByTagName("NAME");
					vars[temp] = new Variable();
					vars[temp].name = nList2.item(0).getTextContent().trim();
					//on parcourt les Values
					nList2 = eElement.getElementsByTagName("OUTCOME");
				    for (int i = 0; i < nList2.getLength(); ++i)
				        vars[temp].values.add(nList2.item(i).getTextContent().trim());
				    vars[temp].domain = vars[temp].values.size();
				}
			}
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Pour les datasets artificiels (générés à la volée)
	 * @param vars
	 */
	public DatasetInfo(Variable[] vars)
	{
		this.vars = vars;
		init();
	}

	public DatasetInfo(List<String> filename, boolean entete)
	{
//		System.out.println("Nouveau dataset info");
		// Vérification de toutes les valeurs possibles pour les variables
		LecteurCdXml lect = null;
		
		int nbvar = -1;
		
//		int[] conversion = null;
		for(String s : filename)
		{
			lect = new LecteurCdXml();
			try {
				lect.lectureCSV(s, entete);
	
				if(nbvar == -1)
				{
					nbvar = lect.nbvar;
					vars = new Variable[nbvar];
					int j = 0;
					for(int i = 0; i < lect.nbvar; i++)
					{
						vars[j] = new Variable();
						vars[j].name = lect.var[i];
						vars[j].domain = 0;
						j++;
					}
				}
	
				for(int i = 0; i < lect.nbligne; i++)
				{
					for(int k = 0; k < lect.nbvar; k++)
					{
						String value = lect.domall[i][k];
						if(!vars[k].values.contains(value))
						{
							vars[k].values.add(new String(value));
							vars[k].domain++;
						}
					}
				}
			} catch(IOException e)
			{
				System.err.println("Fichier "+s+" non lu : "+e);
			}
		}
		init();
	}
	
	private void init()
	{
		mapVar = new HashMap<String, Integer>();
		
		for(int i = 0; i < vars.length; i++)
		{
			assert mapVar.get(vars[i].name) == null : "Deux variables possèdent le même nom ! " + vars[i].name;
			mapVar.put(vars[i].name, i);
		}

		assert mapVar.size() == vars.length : "Erreur : " + mapVar.size() + " != " + vars.length;
		
//		IteratorInstances.setVars(vars);
//		Instanciation.setVars(vars);
		InstanceMemoryManager.getMemoryManager(this).createInstanciation();
		Instanciation.setMemoryManager(InstanceMemoryManager.getMemoryManager(this));
	}
	
	public String toString()
	{
		String out = "Le dataset a "+vars.length+" variables : \n";
		for(Variable v : vars)
		{
			out += v.name+" : ";
			for(int i = 0; i < v.domain; i++)
			{
				out += v.values.get(i);
				if(i != v.domain - 1)
					out += ", ";
				else
					out += "\n";
			}
		}
		return out;
	}

	public void print()
	{
		System.out.println(toString());
	}

	public String toStringEntete()
	{
		String out = "";
		for(int i = 0; i < vars.length; i++)
		{
			out += vars[i].name;
			if(i < vars.length - 1)
				out += ", ";
		}
		return out;
	}
	
}
