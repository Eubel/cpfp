package cpfp.agents;

import java.util.ArrayList;
import java.util.List;

import cpfp.obj.*;

/**
 * Methods akin to CPFP V1
 * calculating the vectors
 * @author daniel
 *
 */
public class CpfpV1Agent { //TODO geht das so?
	/**
	 * calculates the CPFP V1 structure vector
	 * @param chain which contains AminoAcids[] poly peptide chain
	 * @return
	 * @throws Exception
	 */
	public static double[] getCpfpV1Vector(Chain chain) throws Exception {
		
		/*	DSSP definition
		 *	@see http://swift.cmbi.ru.nl/gv/dssp/
    	 *	H = α-helical
    	 *	B = residue in isolated β-bridgea
    	 * 	E = extended strand, participates in β ladder
    	 *	G = 3-helix (3_10 helix)
    	 *	I = 5 helix (π-helix)
    	 *	T = hydrogen bonded turn
    	 *	S = bend 
    	 * ' '= other
		 */
		
		// Selections
		AminoAcid[] hs = getArrayofStructureAAs('H', chain);
		AminoAcid[] bs = getArrayofStructureAAs('B', chain);
		AminoAcid[] es = getArrayofStructureAAs('E', chain);
		AminoAcid[] gs = getArrayofStructureAAs('G', chain);
		AminoAcid[] is = getArrayofStructureAAs('I', chain);
		AminoAcid[] ts = getArrayofStructureAAs('T', chain);
		AminoAcid[] ss = getArrayofStructureAAs('S', chain);
		AminoAcid[] os = getArrayofStructureAAs(' ', chain); //all others
		
		/**
		 * number of all amino acids which were in one of these structure selections
		 */
		int length = chain.aminoAcids().length;
		
		//fractions
		double hF = getFractionOfAAs(hs, length);
		double bF = getFractionOfAAs(bs, length);
		double eF = getFractionOfAAs(es, length);
		double gF = getFractionOfAAs(gs, length);
		double iF = getFractionOfAAs(is, length);
		double tF = getFractionOfAAs(ts, length);
		double sF = getFractionOfAAs(ss, length);
		double oF = getFractionOfAAs(os, length);
		
		// RMSDs
		//TODO add RMSDs?
		
		//round
		hF = Math.round(hF * 100)/100.0;
		bF = Math.round(bF * 100)/100.0;
		eF = Math.round(eF * 100)/100.0;
		gF = Math.round(gF * 100)/100.0;
		iF = Math.round(iF * 100)/100.0;
		tF = Math.round(tF * 100)/100.0;
		sF = Math.round(sF * 100)/100.0;
		oF = Math.round(oF * 100)/100.0;

		double[] res = {hF, bF, eF, gF, iF, tF, sF, oF};
		return res;
	}
	
	/**
	 * gets a selection of all amino acids with the same DSSP structure
	 * @param structureChar DSSP structure key
	 * @param chain poly peptide chain to be examined
	 * @return selected amino acids
	 */
	private static AminoAcid[] getArrayofStructureAAs(char structureChar, Chain chain) {
		List<AminoAcid> selection = new ArrayList<AminoAcid>();
		
		for(AminoAcid aa : chain.aminoAcids()) {
			if(aa.structure() == structureChar) selection.add(aa);
		}
		
		return selection.toArray(new AminoAcid[selection.size()]);
		
		/*This example doesn't work...
		 * selection by structure, not by AAtype!
		 * 
		 * List<AminoAcid> selection = select(
         *       chain.aminoAcids(), 
         *       having(on(AminoAcid.class).structure(), 
         *       equalTo(structureChar)));
		 *	return selection.toArray(new AminoAcid[selection.size()]);
		 */
	}
	
	/**
	 * get the fraction of amino acids
	 * @param selectedAcids amino acids of the selected fraction
	 * @param size number of all amino acids in the chain
	 * @return
	 */
	private static double getFractionOfAAs(AminoAcid[] selectedAcids, int size) {
		return (double)selectedAcids.length / size;
	}
	
	/**
	 * Calculates the average deviation (RMSD) of phi and psi of an amino acids array
	 */
	private static double getRMSDofAAs(AminoAcid[] acids) {
		//TODO passt das so ?
		double sumDelta = 0;
		//TODO calculate not count
		int counter = 0;
		
		for(int x=0; x < acids.length; x ++) {
			for(int y=0; y < acids.length; y ++) {
				double phiDelta = Math.abs(acids[x].phi() - acids[y].phi());
				double psiDelta = Math.abs(acids[x].psi() - acids[y].psi());
				sumDelta += Math.sqrt(phiDelta*phiDelta + psiDelta*psiDelta);
				counter ++;
			}
		}
		
		return sumDelta / counter;
	}
}
