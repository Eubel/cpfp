package cpfp.exceptions;

public class PdbFormatException extends Exception {
	/**
	 * Exception related to issues with the PDB format
	 * 
	 * @param msg
	 *            Message text
	 */
	public PdbFormatException(String msg) {
		super(msg);
	}
}
