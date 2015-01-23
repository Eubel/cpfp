package cpfp.obj;

import java.util.ArrayList;

import cpfp.exceptions.PdbFormatException;

/**
 * Represents a .dssp-file
 * 
 * @author daniel
 * @see http://swift.cmbi.ru.nl/gv/dssp/ Explanation
 */
public class DsspStructure {
	/**
	 * Four character PDB identifier
	 */
	private String pdbId;

	public String pdbId() {
		return this.pdbId;
	}

	/**
	 * Amino acid chains of the Structure
	 */
	private Chain[] chains;

	public Chain[] chains() {
		return this.chains;
	}

	/**
	 * Creates a Representation of a .dssp-file
	 * 
	 * @param chains
	 *            array of peptide chains
	 * @param pdbId
	 *            Four character PDB identifier
	 * @throws Exception
	 */
	public DsspStructure(Chain[] chains, String pdbId)
			throws PdbFormatException {
		// validate PDB identifier
		if (pdbId.length() != 4) {
			throw new PdbFormatException(
					"The PDB identifier length has to be 4!");
		}

		this.chains = chains;
		this.pdbId = pdbId;
	}
	
	/**
	 * Get all amino acids from all chains of this DsspStructure
	 * @return all amino acids from all chains of this DsspStructure
	 */
	public AminoAcid[] getAllAminoAcids()
	{
		ArrayList<AminoAcid> res = new ArrayList<AminoAcid>();
		
		for(Chain c : this.chains()) {
			for(AminoAcid a : c.aminoAcids()) {
				res.add(a);
			}
		}
		
		return res.toArray(new AminoAcid[res.size()]);
	}

	@Override
	public String toString() {
		return this.pdbId;
	}
}
