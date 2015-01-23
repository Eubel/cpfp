package cpfp.agents;

import cpfp.obj.*;

public class RamachandranAgent {
	/**
	 * Calculates a ramachandran matrix of all amino acids in all Chain.
	 * This matrix could be used to draw ramachandran plots
	 * @param chains which have to be examined
	 * @return [phi,psi] 360 x 360 matrix
	 */
	public static double[][] getRamachandranMatrixOfChains(Chain[] chains) {
		/**
		 * represents a ramachandran plot
		 */
		double[][] res = new double[360][360];
		
		//for each chain
		for(Chain c : chains) {
			//for each amino acid
    		for(AminoAcid a : c.aminoAcids()) {
    			int x = (int)a.phi() + 180;
    			int y = (int)a.psi() + 180;
    			
    			//increment ramachandran matrix
    			try {
    				res[y][x] ++;
    			}
    			catch(Exception e) {
    				//ignores out of bound angles
    				continue;
    			}
    		}	
		}
		
		return res;
	}
	
	/**
	 * Calculates the normalized difference ramachandran matrix of two groups of chains.
	 */
	public static double[][] getDifferenceRamachandranMatrix(Chain[] chainsA, Chain[] chainsB) {
		double[][] ramaMatA = getRamachandranMatrixOfChains(chainsA);
		double[][] ramaMatAnorm = normalizeMatrix(ramaMatA);
		
		double[][] ramaMatB = getRamachandranMatrixOfChains(chainsB);
		double[][] ramaMatBnorm = normalizeMatrix(ramaMatB);
		
		return getDiffMatrix(ramaMatAnorm, ramaMatBnorm);
		
	}
	
	public static double[][] normalizeMatrix(double[][] matrix) {
		double sum = 0;
		for(int x = 0; x < matrix[0].length; x ++) {
			for(int y = 0; y < matrix[1].length; y ++) {
				sum += matrix[x][y];
			}
		}
		
		/**
		 * Normalized matrix
		 */
		double[][] normMatrix = new double[matrix[0].length][matrix[1].length];
		for(int x = 0; x < normMatrix[0].length; x ++) {
			for(int y = 0; y < normMatrix[1].length; y ++) {
				normMatrix[x][y] = matrix[x][y] / sum; 
			}
		}
		
		return normMatrix;
	}
	
	/**
	 * Calculates the Difference in each element of two matricies
	 * @param a
	 * @param b
	 * @return a-b for each element
	 * @throws MatrixDimensionException
	 */
	public static double[][] getDiffMatrix(double[][] a, double[][] b) {
		if(a[0].length != b[0].length || a[1].length != b[1].length) return null;
		
		int rowCount = a[0].length;
		int colCount = a[1].length;
		double[][] res = new double[rowCount][colCount];
		
		for(int x = 0; x < rowCount; x ++) {
			for(int y = 0; y < colCount; y ++) {
				res[x][y] = a[x][y] - b[x][y];
			}
		}
		
		return res;
	}
}
