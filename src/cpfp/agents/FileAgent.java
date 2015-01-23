package cpfp.agents;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import oth.DoubleFormatUtil;
import cpfp.obj.Chain;
import cpfp.obj.DsspStructure;

/**
 * Methods for parsing java object data into files
 * @author daniel
 */
public class FileAgent {
	
	/**
	 * Calculates the CPFP V1 vector of all of these chains
	 * and saves it into a tab separated file (UNIX format)
	 * @param chains chain array of the chins to be examined
	 * @param filepath the absolute location for the file
	 * @throws IOException the file could not be written
	 */
	public static void saveCpfpV1Vectors(Chain[] chains, String filepath) throws IOException {
		Date saveTime = new Date();
				
		//set decimal format
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("0.00", otherSymbols); // 0.2 -> 0.20

		String fileContent = "# CPFP V1 - Vector dump from " + saveTime.toString() + "\n";
		fileContent += "#Fractions of DSSP structure types\n";
		fileContent += "#PDB Chain Mol ID\talpha-Helix(H)\tbeta-Bridge(B)\tbeta-Bulge(E)\t3_10-helix(G)\tpi-helix(I)\tturn(T)\tbend(S)\tother\n";
		
		//make lines for each chain
		for(Chain chain : chains) {
			double[] vector;
			
			try {
				vector = chain.getCpfpV1Vector();
			} 
			catch (Exception e) {
				//ignore chain and write error comment
				fileContent += "#" + chain.toString() + "\tERROR\n";
				break;
			}
			
			//save line if vector could be calculated
			String line = chain.toString() + "\t";
			for(double d : vector) line += df.format(d) + "\t";
			line = line.trim();
			line += "\n";
			//sth. like 1HHO:1	0.1	0.4	0.2 ...
			
			fileContent += line;
		}
		
		//save file
		Path file = Paths.get(filepath);
		Files.write(file, fileContent.getBytes(), StandardOpenOption.CREATE_NEW);
	}
	
	/**
	 * Saves a matrix into a file
	 * Format: UNIX tsv - file
	 * Note: rounds each value to 16 digits
	 * @param matrix the matrix which sould be saved
	 * @param filepath the save file path of the file
	 * @throws IOException File could not be written.
	 */
	public static void saveMatrix(double[][] matrix, String filepath) throws IOException {		
		//make content string
		StringBuffer buf = new StringBuffer("");
		for(double[] row : matrix) {
			for(double d : row) {
				// string += double is a way to slow so use extra format and buffering
				DoubleFormatUtil.formatDouble(d, 16, 16, buf);
				buf.append("\t");
			}
			//finished printing the whole line so add a new line at the end
			buf.append("\n");
		}
		
		//save file
		Path file = Paths.get(filepath);
		Files.write(file, buf.toString().getBytes(), StandardOpenOption.CREATE_NEW);
	}
	
	/**
	 * Gets one matrix and saves it in two separate files.
	 * One has the positive values and one has he negative values.
	 * Filenames: filepath.pos.tsv and filepath.neg.tsv
	 * @param matrix
	 * @param filepath
	 * @throws IOException
	 */
	public static void savePosNegMatrix(double[][] matrix, String filepath) throws IOException {
		//get only the positive values
		double[][] posMat = new double[matrix[0].length][matrix[1].length];
		double[][] negMat = new double[matrix[0].length][matrix[1].length];
		
		for(int x = 0; x < matrix[0].length; x ++) {
			for(int y = 0; y < matrix[1].length; y ++) {
				if(matrix[x][y] <= 0) {
					//negative or zero value
					posMat[x][y] = 0;
					negMat[x][y] = Math.abs(matrix[x][y]);
				}
				else {
					//positive value
					posMat[x][y] = matrix[x][y];
					negMat[x][y] = 0;
				}
			}
		}
		
		//saves the two matricies
		saveMatrix(negMat, filepath + "-neg.tsv");
		saveMatrix(posMat, filepath + "-pos.tsv");
	}
	
	/**
	 * load a double[][] matrix from a file
	 * @param filepath of the csv file in UNIX format, UTF8 encoded. No header rows were accepted! Data only!
	 * @param separatorChar 
	 * @return matrix
	 * @throws IOException csv file can't be loaded
	 * @throws ParseException String cant be parsed to double
	 */
	public static double[][] getMatrixFromFile(String filepath, char separatorChar) throws IOException, ParseException {
		//load file
		List<String> content = Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);
		
		/**
		 * Number of data columns in a row
		 */
		int coulCount = content.get(0).split("" + separatorChar).length;
		int rowCount = content.size();
		/**
		 * The result matrix which should be returned
		 */
		double[][] resMat = new double[rowCount][coulCount];
		
		NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
		
		//for each line
		for(int aktRow = 0; aktRow < rowCount; aktRow ++) {
			String[] couls = content.get(aktRow).split("" + separatorChar);
			
			//for each column
			for(int aktCol = 0; aktCol < coulCount; aktCol ++) {
				double val = nf.parse(couls[aktCol]).doubleValue();
				resMat[aktRow][aktCol] = val;
			}
		}
		
		return resMat;
	}
	
	/**
	 * Get Chains from a List of PDB mol IDs
	 * Unix file format (\n), UTF-8
	 * for comments: ignores Lines with starts with #
	 * ignores empty lines too
	 * uses Console output for logging
	 * @param filename the absolute path of the file which contains the pdb chain list
	 * @param dsspLocation the absolute path of the folder which contains all .dssp structure files.
 	 *		  should end with '/' and contain filenames like "1hho.dssp".
	 * @return all available chains which were in the batch file
	 */
	public static Chain[] getChainsFromBatchFile(String filename, String dsspLocation) throws Exception {
		List<Chain> chains = new ArrayList<Chain>();
		
		//read File
		List<String> batchLines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
		
		for(String line : batchLines) {
			if(line.startsWith("#") || line.isEmpty()) continue; //ignore comments and empty lines
			
			//get PDB ID
			String[] parts = null;
			String aktPdbId;
			boolean useAllChains = true;
			if(line.contains(":")) {
				//one specific chain like 1HHO:1
				parts = line.split(":");
				aktPdbId = parts[0]; 
				useAllChains = false;
			}
			else {
				//all Chains like 1HHO
				aktPdbId = line;
				useAllChains = true;
			}
			
			//get Structure
			String dsspPath = dsspLocation + aktPdbId.toLowerCase() + ".dssp";
			DsspStructure aktStruct = null;
			try {
				aktStruct = DsspAgent.getStructureFromFile(dsspPath);
			} 
			catch(NoSuchFileException nfE) {
				//dssp file not in local database
				System.out.println("File " + dsspPath + " not found. Ignores " + aktPdbId + ".");
				continue;
			}
			
			if(useAllChains) {
				//add all chains
				for(Chain c : aktStruct.chains()) chains.add(c);
			}
			else {
				//add the right chain
				int aktChainMolId = Integer.parseInt(parts[1]);
				for(Chain c : aktStruct.chains()) {
					if(c.pdbMolId() == aktChainMolId) {
						chains.add(c);
						break; //there is only one chain per molId
					}
				}
			}
		}
		
		return chains.toArray(new Chain[chains.size()]);
	}
}
