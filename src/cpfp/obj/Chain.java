package cpfp.obj;

import cpfp.agents.CpfpV1Agent;
import cpfp.exceptions.PdbFormatException;

/**
 * Represents one Molecule (peptide chain)
 * 
 * @author daniel
 *
 */
public class Chain {
	/**
	 * Amino Acids of the chain. The order matters!
	 */
	private AminoAcid[] aminoAcids;

	public AminoAcid[] aminoAcids() {
		return this.aminoAcids;
	}

	/**
	 * Four character PDB identifier
	 */
	private String pdbId;

	public String pdbId() {
		return this.pdbId;
	}

	/**
	 * The n in 1ABC:n in the PDB identifier = Chain ID
	 */
	private int pdbMolId;

	public int pdbMolId() {
		return this.pdbMolId;
	}

	/**
	 * All accociated GO ontologies mapped with SIFTS
	 * 
	 * @see http://www.ebi.ac.uk/pdbe/docs/sifts/quick.html
	 */
	private GoOntology[] ontologies;

	public GoOntology[] ontologies() {
		return ontologies;
	}

	public Chain(AminoAcid[] aminoAcids, int pdbMolChainId, String pdbId,
			GoOntology[] ontologies) throws PdbFormatException {
		// validate PDB identifier
		if (pdbId.length() != 4) {
			throw new PdbFormatException(
					"The PDB identifier length has to be 4!");
		}

		if (pdbMolChainId < 1) {
			throw new PdbFormatException(
					"The PDB molID has to be a positive value!");
		}

		this.aminoAcids = aminoAcids;
		this.pdbMolId = pdbMolChainId;
		this.ontologies = ontologies;
	}

	/**
	 * Very rudimentary constructor. Do not use it if possible! !!! This
	 * constructor does not provide all information !!!
	 * 
	 * @param aas
	 *            AminoAdid[] all amino acids
	 * @param pdbChainId
	 *            the PDB mol chain ID
	 */
	public Chain(AminoAcid[] aminoAcids, String pdbId, int pdbChainId) {
		this.aminoAcids = aminoAcids;
		this.pdbId = pdbId;
		this.pdbMolId = pdbChainId;
	}
	
	public double[] getCpfpV1Vector() throws Exception {
		return CpfpV1Agent.getCpfpV1Vector(this);
	}

	@Override
	public String toString() {
		return this.pdbId + ":" + this.pdbMolId;
	}
}
