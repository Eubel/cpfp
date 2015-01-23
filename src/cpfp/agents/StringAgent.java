package cpfp.agents;

/**
 * Extention methods for Converting String[] to String and vice versa
 * @author daniel
 *
 */
public class StringAgent {
	public static String concatenateStringArray(String[] strings) {
		String res = "";
		for (String s : strings) res += s + "\n";
		
		// remove last \n = trim end
		res = res.replaceAll("\\s+$", "");
		return res;
	}

	public static String[] getLinesOfString(String string) {
		return string.split("\n");
	}
}
