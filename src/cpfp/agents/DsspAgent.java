package cpfp.agents;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import cpfp.exceptions.PdbFormatException;
import cpfp.obj.AminoAcid;
import cpfp.obj.Chain;
import cpfp.obj.DsspStructure;

/**
 * Methods to deal with .dssp-files
 * 
 * @author daniel
 */
public final class DsspAgent {
	/**
	 * Load a .dssp-file into the cpfp object model
	 * 
	 * @param filename full absolute path of the .dssp file
	 * @return dssp structure in the cpfp object model
	 */
	public static DsspStructure getStructureFromFile(String filename)
			throws Exception {
		// init parameters
		String pdbId = "ERRR";
		Chain[] chains = null;

		// read file
		List<String> linelist = Files.readAllLines(Paths.get(filename),
				StandardCharsets.UTF_8);
		String[] lines = linelist.toArray(new String[linelist.size()]);

		// for each line in lines
		for (int i = 0; i < lines.length; i++) {
			// TODO use DsspDef class instead
			if (lines[i].startsWith("HEADER")) {
				// grap PDB id
				String[] couls = lines[i].split("\\s+"); // split for whitespace
				pdbId = couls[couls.length - 2];
			} 
			else if (lines[i].startsWith("  #  RESIDUE")) {
				// grap relevant chain data
				String[] dsspDataArray = Arrays.copyOfRange(lines, i + 1,
						lines.length);
				String dsspData = StringAgent
						.concatenateStringArray(dsspDataArray);

				// grap chains
				chains = getChainsFromDsspDataLines(dsspData, pdbId);
				break;
			}
		}

		return new DsspStructure(chains, pdbId);

	}

	/**
	 * parses the body of the .dssp-file to the cpfp object model
	 * 
	 * @param dataBlock String Array in which the data of each AA in the .dssp-file is
	 * @param pdbId the four character long PDB identifier (sth. like 1HHO)
	 * @throws PdbFormatException
	 */
	private static Chain[] getChainsFromDsspDataLines(String dataBlock, String pdbId)
			throws PdbFormatException {
		String[] chainStrings = getBlockChainData(dataBlock);
		Chain[] chains = new Chain[chainStrings.length];

		// for each chain
		for (int i = 0; i < chainStrings.length; i++) {
			AminoAcid[] aas = getAAsOfChain(chainStrings[i]);
			int pdbChainId = i + 1; // TODO correct?

			// TODO fill ontologies to that shit :)
			chains[i] = new Chain(aas, pdbId, pdbChainId);
		}

		return chains;
	}

	private static AminoAcid[] getAAsOfChain(String chainStr) {
		// get all AAs of one chain
		String[] data = StringAgent.getLinesOfString(chainStr);
		AminoAcid[] aas = new AminoAcid[data.length];

		for (int i = 0; i < data.length; i++) {
			// TODO does this work?
			String resNr = data[i].substring(5, 10).trim();
			char chain = data[i].charAt(11);
			char aa = data[i].charAt(13);
			char struct = data[i].charAt(16);
			String phi = data[i].substring(103, 109);
			String psi = data[i].substring(109, 115);

			AminoAcid aktAA = new AminoAcid(aa, chain, Integer.parseInt(resNr),
					struct, Double.parseDouble(phi), Double.parseDouble(psi));
			aas[i] = aktAA;
		}

		return aas;
	}

	/**
	 * Splits the String DSSP AA data block into String[] datablocks for each
	 * chain
	 * 
	 * @param dataBlock
	 * @return String[] for each chain
	 */
	private static String[] getBlockChainData(String dataBlock) {
		String[] lines = StringAgent.getLinesOfString(dataBlock);

		// searches for chains
		for (int i = 0; i < lines.length; i++) {
			// TODO use dsspdefs instead
			if (lines[i].contains("!")) {
				// change to separator char
				lines[i] = "¬";
			}
		}

		// split dataBlock into chain String[]
		String allLines = StringAgent.concatenateStringArray(lines);
		allLines = allLines.replaceAll("\n¬\n", "¬"); // prevent empty lines
		return allLines.split("¬");
	}
}
