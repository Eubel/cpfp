package cpfp.app;

import java.io.File;
import java.util.Scanner;

import cpfp.agents.FileAgent;
import cpfp.agents.RamachandranAgent;
import cpfp.exceptions.StopwatchException;
import cpfp.obj.Chain;

/**
 * Tool to deal with batches of PDB IDs
 * @author daniel
 */
public class CpfpBatchApp {
	public static void main(String[] args) {
		/**
		 * Scanner for reading user inputs
		 */
		Scanner scanner = new Scanner(System.in);
		
		/**
		 * Stop watch to measure time of calculation
		 */
		StopWatch stopWatch = new StopWatch();
		
		//show welcome message
		System.out.println("CPFP Batch App");
		System.out.println("Reads a list of PDB IDs and procuces an tsv-file which contains the vectors for each Chain\nand makes ramachandran matricies as well");
		
		//get path of dssp reposity
		System.out.println("Path of the DSSP reposity (containg files like 1hho.dssp):");
		String dsspPath = scanner.nextLine();
		dsspPath = dsspPath.trim();
		if(! dsspPath.endsWith("/")) dsspPath += "/"; // add '/' if necessary
		
		//get batch list file
		System.out.println("\nNOTE: You can search http://www.pdb.org for your structures and get a batch file by saving the selected IDs report.");
		System.out.println("Path of the batch file");
		String batchFilepath = scanner.nextLine();
		
		//get normalized proteome matrix file
		System.out.println("Path of the normalisized proteome ramachandran matrix file:");
		String proteomRamaFilepath = scanner.nextLine();
		
		//get file path for saving 
		System.out.println("\nPath of the output files which will be generated:");
		String savePath = scanner.nextLine();
		if( ! savePath.endsWith("/")) savePath += "/";
		
		//init calculation
		System.out.println("Calculation started. This could take several minutes.");
		scanner.close();
		
		//do bare calculation
		stopWatch.start();
		try {
			calculate(batchFilepath, dsspPath, proteomRamaFilepath, savePath);
			stopWatch.stop();
			
			//finish
			System.out.println("finished.");
			System.out.println("Seconds needed: " + stopWatch.getSeconds());
			System.exit(0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO make this private. only for testing
	/**
	 * @deprecated only for testing!
	 * @param batchFilepath
	 * @param dsspPath
	 * @param proteomRamaFilepath
	 * @param savePath
	 * @throws Exception
	 */
	public static void calculate(String batchFilepath, String dsspPath, String proteomRamaFilepath, String savePath) throws Exception{
		//bare calculation
		Chain[] allChains = FileAgent.getChainsFromBatchFile(batchFilepath, dsspPath);
		double[][] normProteomMatrix = FileAgent.getMatrixFromFile(proteomRamaFilepath, '\t');
		double[][] ramaMatrix = RamachandranAgent.getRamachandranMatrixOfChains(allChains);
		double[][] normRamaMatrix = RamachandranAgent.normalizeMatrix(ramaMatrix);
		double[][] ramaDiffMatrix = RamachandranAgent.getDiffMatrix(normRamaMatrix, normProteomMatrix);
		
        File dir = new File(savePath);
        dir.mkdir();
		
		FileAgent.saveMatrix(ramaMatrix, savePath + "rama-count.tsv");
		FileAgent.saveMatrix(normRamaMatrix, savePath + "rama-norm.tsv");
		FileAgent.saveMatrix(ramaDiffMatrix, savePath + "rama-norm-diff.tsv");
		FileAgent.savePosNegMatrix(ramaDiffMatrix, savePath + "rama-norm-diff-split");
		FileAgent.savePosNegMatrix(normRamaMatrix, savePath + "rama-norm-split");
		FileAgent.saveCpfpV1Vectors(allChains, savePath + "vectors.tsv");
	}
}
