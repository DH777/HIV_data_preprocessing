import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Read in data and transfer to JavaScript format
 * @author donghanz
 *
 */
public class JSWriter {
	protected static HashMap<String, String> nodeMap;
	protected static HashMap<String, Integer> degreeMap;
	protected static HashMap<String, Double> nDisMap;
	protected static HashSet<String> idSet1;
	protected static HashSet<String> idSet2;
	protected static HashSet<String> idSet3;
	
	public void openDir(String s) throws FileNotFoundException {
		nodeMap = new HashMap<String, String>();
		degreeMap = new HashMap<String, Integer>();
		nDisMap = new HashMap<String, Double>();
		idSet1 = new HashSet<String>();
		idSet2 = new HashSet<String>();
		idSet3 = new HashSet<String>();
		
		File[] dir = new File(s).listFiles();
		for (File f : dir) {
			if (f.getName().endsWith(".txt")) 
				printOutJS(s + "/" + f.getName(), nodeMap, degreeMap, nDisMap, idSet1, idSet2, idSet3);
		}

//		getIdEachCluster(nodeMap);
	}
	public void printOutJS(String s, HashMap<String, String> nodeMap, HashMap<String, Integer> degreeMap, HashMap<String, Double> nDisMap, HashSet<String> idSet1, HashSet<String> idSet2, HashSet<String> idSet3) {
		try (Scanner sc = new Scanner(new File(s));
				FileWriter fw = new FileWriter("edgeOut.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter edgeOut = new PrintWriter(bw);
				FileWriter fw1 = new FileWriter("idOutWithin0.5.csv", true);
				FileWriter fw2 = new FileWriter("idOutWithin1.csv", true);
				FileWriter fw3 = new FileWriter("idOutWithin1.5.csv", true);
				BufferedWriter bw1 = new BufferedWriter(fw1);
				BufferedWriter bw2 = new BufferedWriter(fw2);
				BufferedWriter bw3 = new BufferedWriter(fw3);
				PrintWriter idOut1 = new PrintWriter(bw1);
				PrintWriter idOut2 = new PrintWriter(bw2);
				PrintWriter idOut3 = new PrintWriter(bw3);){
//			nodeSet = new HashSet<String>();
//			degreeMap = new HashMap<String, Integer>();
			HashMap<String, Double> singleNodeMap = new HashMap<>();
			String cluster = s.split("/")[1].split("_")[0];
//			idOut1.println(cluster+":");
//			idOut2.println(cluster+":");
//			idOut3.println(cluster+":");
			while (sc.hasNextLine()) {
				String[] rawData = sc.nextLine().split("\\t");
				nodeMap.put(rawData[0], cluster);
				nodeMap.put(rawData[1], cluster);
				degreeMap.put(rawData[0], degreeMap.getOrDefault(rawData[0], 0) + 1);
				degreeMap.put(rawData[1], degreeMap.getOrDefault(rawData[1], 0) + 1);
				Double dis = Double.parseDouble(rawData[2]);
				if (dis <= 0.5) {
//					idSet1.add(rawData[0] +"\t"+ rawData[1]);
					singleNodeMap.put(rawData[0], 0.5);
					singleNodeMap.put(rawData[1], 0.5);
				}else if (dis <= 1) {
//					idSet2.add(rawData[0] +"\t"+ rawData[1]);
					if (!singleNodeMap.containsKey(rawData[0]) || singleNodeMap.get(rawData[0]) == 1.5) {
						singleNodeMap.put(rawData[0], 1.0);
					}
					if (!singleNodeMap.containsKey(rawData[1]) || singleNodeMap.get(rawData[1]) == 1.5) {
						singleNodeMap.put(rawData[1], 1.0);
					}
				}else {
					if (!singleNodeMap.containsKey(rawData[0])) singleNodeMap.put(rawData[0], 1.5);
					if (!singleNodeMap.containsKey(rawData[1])) singleNodeMap.put(rawData[1], 1.5);
				}
				
				nDisMap.put(rawData[0], Math.min(nDisMap.getOrDefault(rawData[0], 1.5), dis));
				nDisMap.put(rawData[1], Math.min(nDisMap.getOrDefault(rawData[1], 1.5), dis));
				String weight = "";
				if (dis < 0.5) {
					weight = "0.7";
				}else if (dis >= 0.5 && dis < 0.7 ) {
					weight = "0.5";
				}else if (dis >= 0.7 && dis < 1.0 ) {
					weight = "0.3";
				}else if (dis >= 1.0 && dis <= 1.5 ) {
					weight = "0.1";
				}
				
				//{ data: { source: '1', target: '2', weight: 1 } },
				String edge = "{ data: { source: '"+ rawData[0] +"', target: '" + rawData[1] + "', weight: " + weight + ", cluster: '"+ cluster +"', distance: " + dis + "} },";
				edgeOut.println(edge);
			}
			for (String key : singleNodeMap.keySet()) {
				double group = singleNodeMap.get(key);
				if (group == 0.5) {
					idOut1.println(key + "," + cluster.substring(5));
					idOut2.println(key + "," + cluster.substring(5));
					idOut3.println(key + "," + cluster.substring(5));
				}else if (group == 1.0) {
					idOut2.println(key + "," + cluster.substring(5));
					idOut3.println(key + "," + cluster.substring(5));
				}else idOut3.println(key + "," + cluster.substring(5));
			}
//			for (String str : idSet1) idOut1.println(str);
//			for (String str : idSet2) idOut2.println(str);
//			for (String str : idSet3) idOut3.println(str);
//			for (String n : nodeSet) {
//				//{ data: { id: '1'} },
//				String node = "{ data: { id: '"+ n +"'} },";
//				nodeOut.println(node);
//			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void getIdEachCluster(HashMap<String, String> nodeMap) {
		try (PrintWriter idOut = new PrintWriter(new File("idOut.txt"));) {
			List<HashSet<String>> clusters = new ArrayList<HashSet<String>>();
			for (int i = 0; i < 19; i++) {
				clusters.add(new HashSet<String>());
			}
			for (String id : nodeMap.keySet()) {
				int clusterNo = Integer.parseInt(nodeMap.get(id).substring(5));
				HashSet<String> set = clusters.get(clusterNo-1);
				set.add(id);
			}
			for (int i = 1; i <= 19; i++) {
				idOut.println("Cluster" + i + ":");
				idOut.println(clusters.get(i-1));
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
