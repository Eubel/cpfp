package cpfp.app;

import java.util.Scanner;

/**
 * @deprecated testing class only!
 * @author daniel
 */
public class CpfpBatchSimplifiedApp {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("absolute batch path:");
		String batchPath = scanner.nextLine();
		System.out.println("absolute ressult folder path:");
		String resPath = scanner.nextLine();
		
		scanner.close();
		
		try {
			CpfpBatchApp.calculate(
					batchPath, 
					"/home/daniel/S/Bioinfo-Projekt/dat/dssp/", 
					"/home/daniel/Uni/Semester V/Bioinformatik/Projekt/res/8 proteom/normProteom.tsv", 
					resPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
