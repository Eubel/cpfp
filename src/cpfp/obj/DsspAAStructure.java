package cpfp.obj;

/**
 * Secondary structure elements in DSSP
 * 
 * @author daniel
 * @see http://swift.cmbi.ru.nl/gv/dssp/ Output short
 */
public enum DsspAAStructure {
	/**
	 * alpha Helix
	 */
	H,
	/**
	 * isolated beta-bridge
	 */
	B,
	/**
	 * extended strand, participates in beta-ladder
	 */
	E,
	/**
	 * 3_10 helix
	 */
	G,
	/**
	 * pi helix
	 */
	I,
	/**
	 * hydrogen bonded turn
	 */
	T,
	/**
	 * bend
	 */
	S
}
