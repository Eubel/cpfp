package cpfp;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cpfp.agents.CpfpV1Agent;
import cpfp.agents.DsspAgent;
import cpfp.agents.FileAgent;
import cpfp.app.StopWatch;
import cpfp.obj.AminoAcid;
import cpfp.obj.Chain;
import cpfp.obj.DsspStructure;
import cpfp.app.CpfpBatchApp;

/**
 * Main class for access to the cpfp package
 * Testing area
 */
public class Main {
	/**
	 * All tests should be placed here
	 * Time keeping is automatically provided by the main method !
	 * @throws Exceptions of any kind
	 */
	@SuppressWarnings("deprecation")
	private static void doTests() throws Exception
	{
		/*
		 * TESTING AREA
		 * 
		 * time keeping included
		 */
		
		doBatchExp();
		doStdCpfpExamples();
		CpfpBatchApp.calculate(
				"batchfilePath", 
				"dssp folder path", 
				"normalized proteome ramachandran matrix", 
				"output folder");
	}

	public static void main(String[] args) {
		try {
			// init for time keeping
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			StopWatch stopWatch = new StopWatch();

			// set start time
			Date startTime = new Date();
			stopWatch.start();
			System.out.println("\nTESTING CPFP STARTED AT "
					+ dateFormat.format(startTime) + "\n");
			
			doTests();

			// set end time
			Date endTime = new Date();
			stopWatch.stop();

			// show time stats
			System.out.println("\nTESTING CPFP ENDED AT "
					+ dateFormat.format(endTime) + "\n");
			System.out.println("Seconds needed: " + stopWatch.getSeconds());
			System.exit(0);

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void doBatchExp() throws Exception {
		String batchFilePath = "...";
		String dsspLocationPath = "...";
		Chain[] allChains = FileAgent.getChainsFromBatchFile(
				batchFilePath, dsspLocationPath);
		FileAgent.saveCpfpV1Vectors(allChains, "...");
		System.out.println(allChains.length);
	}

	private static void doStdCpfpExamples() throws Exception {
		System.out.println("CPFP	v0.1a1 \ntested: 1HHO");

		// get structure
		DsspStructure betaGlobine = DsspAgent
				.getStructureFromFile("...");

		// get the first psi angle in the first Chain of betaGlobine
		double firstPsi = betaGlobine.chains()[0].aminoAcids()[0].psi();
		System.out.println("first psi: " + firstPsi);

		// get the Phi of Gly-15 in the first Chain (1HHO:1)
		double phiOfS3ChainA = betaGlobine.chains()[0].aminoAcids()[14].phi();
		System.out.println("15th phi: " + phiOfS3ChainA);

		// get all helical AAs of all chains of 1HHO (DSSP-Code H)
		List<AminoAcid> helicalAas = select(betaGlobine.getAllAminoAcids(),
				having(on(AminoAcid.class).structure(), equalTo('H')));
		String allHelicals = Arrays.toString(helicalAas.toArray());
		System.out.println("Alpha helical AAs: " + allHelicals);

		// get all Glycines of the first chain of betaGlobine (1HHO:1)
		List<AminoAcid> glycines = select(betaGlobine.chains()[0].aminoAcids(),
				having(on(AminoAcid.class).aaType(), equalTo('G')));
		String allGlycines = Arrays.toString(glycines.toArray());
		System.out.println("All glycines of the first chain: " + allGlycines);

		// get the CPFP V1 vector of 1HHO:1
		double[] cpfpv1Vec = CpfpV1Agent
				.getCpfpV1Vector(betaGlobine.chains()[0]);
		String cpfpVec = Arrays.toString(cpfpv1Vec);
		System.out.println("CPFP V1 Vector: " + cpfpVec);

		// save CPFP Vectors of 1HHO to Desktop
		Chain[] chains = betaGlobine.chains();
		FileAgent.saveCpfpV1Vectors(chains,
				"...");

		// get all Chains from a List of PDB chain identifiers
		Chain[] allDefBehavChains = FileAgent.getChainsFromBatchFile(
				"...",
				"...");
	}
}
