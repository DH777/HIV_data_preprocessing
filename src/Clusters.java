import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Clusters {
	private static final Pattern p0 = Pattern.compile("(.+\\s)(\\d+)(:.+)");
	private static final Pattern p1 = Pattern.compile("(.+\\s)(\\d+)(\\s)(.+)");
	private static final Pattern p2 = Pattern.compile("(.+\\s)(\\d+)(_.+)");
	HashMap<String, String> clust0 = new HashMap<String, String>();
	HashMap<String, String> clust1 = new HashMap<String, String>();
	HashMap<String, String> clust2 = new HashMap<String, String>();
	HashMap<String, String> clust3 = new HashMap<String, String>();
	HashMap<String, String> clust4 = new HashMap<String, String>();
	HashMap<String, String> clust5 = new HashMap<String, String>();
	public double[][] matrix = new double[273][273];
	
	public void formClusters(String s) {
		try (Scanner sc = new Scanner(new File(s));) {
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				Matcher m0 = p0.matcher(line);
				Matcher m1 = p1.matcher(line);
				Matcher m2 = p2.matcher(line);
				if (m0.find() && m1.find() && m2.find()) {
					switch(m0.group(2)) {
						case "0":
							clust0.put(m1.group(2), m2.group(2));
							break;
						case "1":
							clust1.put(m1.group(2), m2.group(2));
							break;
						case "2":
							clust2.put(m1.group(2), m2.group(2));
							break;
						case "3":
							clust3.put(m1.group(2), m2.group(2));
							break;
						case "4":
							clust4.put(m1.group(2), m2.group(2));
							break;
						case "5":
							clust5.put(m1.group(2), m2.group(2));
							break;
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void divide(String fileName) {
		try (PrintWriter out = new PrintWriter(new File("testClusters/" + fileName));) {
			StringBuilder sb;
			switch(fileName) {
				case "clust0.txt":
					sb = printClust(clust0);
					out.print(sb.toString());
					break;
				case "clust1.txt":
					sb = printClust(clust1);
					out.print(sb.toString());
					break;
				case "clust2.txt":
					sb = printClust(clust2);
					out.print(sb.toString());
					break;
				case "clust3.txt":
					sb = printClust(clust3);
					out.print(sb.toString());
					break;
				case "clust4.txt":
					sb = printClust(clust4);
					out.print(sb.toString());
					break;
				case "clust5.txt":
					sb = printClust(clust5);
					out.print(sb.toString());
					break;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private StringBuilder printClust(HashMap<String, String> clust) {
		StringBuilder sb = new StringBuilder();
		for (String key1 : clust.keySet()) {
			for (String key2 : clust.keySet()) {
				int x = Integer.parseInt(key1);
				int y = Integer.parseInt(key2);
				if (x >= y) continue;
				String s = clust.get(key1) + "\t" + clust.get(key2) + "\t" + matrix[x][y+1] + '\n';
				sb.append(s);
			}
		}
		return sb;
	}
	
	public void readMatrix(String s) {
		try (Scanner sc = new Scanner(new File(s));) {
			sc.nextLine();
			int count = 0;
			while (sc.hasNextLine()) {
				String[] line = sc.nextLine().split(" ");
				if (line[1].isEmpty()) {
					for (int i = 1; i <= 272; i++) {
						matrix[count][i] = Double.parseDouble(line[i+1]);
					}
				}else {
					for (int i = 1; i <= 272; i++) {
						matrix[count][i] = Double.parseDouble(line[i]);
					}
				}

				count++;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Clusters c = new Clusters();
		c.formClusters("sequence_newClust.aux");
		c.readMatrix("sequences_new.mat");
		for (int i = 0; i < 6; i++) {
			c.divide("clust" + i + ".txt");
		}
	}

}
