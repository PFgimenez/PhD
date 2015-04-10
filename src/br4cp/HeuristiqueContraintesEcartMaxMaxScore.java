package br4cp;

import java.util.ArrayList;

public class HeuristiqueContraintesEcartMaxMaxScore implements HeuristiqueContraintes {

	public ArrayList<Integer> reorganiseContraintes(LecteurXML l)
	{
		ArrayList<Integer> reorga=new ArrayList<Integer>();

		int valscore;
		int[] score=new int[l.getNbConstraints()];
		for(int i=0; i<l.getNbConstraints(); i++){
			int max=0;
			if(l.cons[i]!=null){
				for(int j=0; j<l.cons[i].scopeID.length; j++){
					for(int k=j+1; k<l.cons[i].scopeID.length; k++){
						valscore=Math.abs(l.cons[i].scopeID[j]-l.cons[i].scopeID[k]);
						if(valscore>max)
							max=valscore;
					}
				}
				score[i]=max;
			}
		}
		
		int max=0;
		int maxVal=-1;
		for(int j=0; j<l.getNbConstraints(); j++){
			for(int i=0; i<l.getNbConstraints(); i++){
				if(l.cons[i]!=null){
					if(!reorga.contains(i) && score[i]>max){
						max=score[i];
						maxVal=i;
					}
				}
			}
			if(maxVal!=-1){
				reorga.add(maxVal);
				max=0;
				maxVal=-1;
			}else{			//reste plus que des contraintes supprimes
				for(int i=0; i<l.getNbConstraints(); i++){
					if(l.cons[i]==null)
						reorga.add(i);
				}
				break;
			}
			
		}
		return reorga;
	}				
	
}