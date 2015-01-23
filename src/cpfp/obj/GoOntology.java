package cpfp.obj;

public class GoOntology {
	/**
	 * Ontology number given by the Gene Ontology Consortium
	 */
	private int goId;

	public int goId() {
		return this.goId;
	}

	/**
	 * Short term description given by the Gene Ontology Consortium
	 */
	private String name;

	public String name() {
		return this.name;
	}
	
	/**
	 * Type of the GO-Term
	 * e.g. cellular compartment or molecular function
	 */
	private GoType type;
	
	public GoType type() {
		return this.type;
	}

	/**
	 * Creates a Ontology defined by the Gene Ontology Consortium
	 * 
	 * @param goId number of the GO-ID
	 * @param type type of the GO-Term
	 *            
	 */
	public GoOntology(int goId, GoType type) {
		this.goId = goId;
		this.type = type;
	}

	@Override
	public String toString() {
		return String.format("GO:%07d", 1);
	}
}
