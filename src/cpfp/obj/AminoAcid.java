package cpfp.obj;

/**
 * Enummeration of all proteinogenic amino acids
 * 
 * @author daniel
 * @see http://www.biochem.ucl.ac.uk/bsm/dbbrowser/c32/aacode.html
 */
public class AminoAcid {
	// TODO use AminoAcidType instead of char
	/**
	 * Code of the amino acid
	 */
	private char aaType;

	public char aaType() {
		return this.aaType;
	}

	/**
	 * PDB-chain char of the amino acid
	 */
	private char chain;

	public char chain() {
		return this.chain;
	}

	/**
	 * IUPAC psi- peptide backbone torsion angle [deg]
	 */
	private double psi;

	public double psi() {
		return psi;
	}

	/**
	 * IUPAC phi- peptide backbone torsion angle [deg]
	 */
	private double phi;

	public double phi() {
		return phi;
	}

	/**
	 * Original residue number in the Protein Data Bank
	 */
	private int resNr;

	public int resNr() {
		return resNr;
	}

	/**
	 * DSSP- Secondary structure in which this amino acid is
	 */
	private char structure;

	public char structure() {
		return structure;
	}

	public AminoAcid(char type, char chain, int resNr, char structure,
			double phi, double psi) {
		this.aaType = type;
		this.chain = chain;
		this.resNr = resNr;
		this.structure = structure;
		this.phi = phi;
		this.psi = psi;
	}

	@Override public String toString() {
		return "" + this.aaType + this.resNr; // something like V123
	}
}