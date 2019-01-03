import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class JSWriterSequence {
	
	public void printOutJS(String s) {
		Set<String> idSet = new HashSet<String>();
		Map<String, Integer> race_grpMap = new HashMap<String, Integer>();
		Map<String, Integer> curr_genMap = new HashMap<String, Integer>();
		Map<String, Integer> dx_statMap = new HashMap<String, Integer>();
		Map<String, Integer> trans_grpMap = new HashMap<String, Integer>();
		Map<String, Integer> sex_at_birthMap = new HashMap<String, Integer>();
		Map<String, Integer> age_grpMap = new HashMap<String, Integer>();
		Map<String, Integer> acuteMap = new HashMap<String, Integer>();
		Map<String, Integer> dx_yearMap = new HashMap<String, Integer>();
		Map<String, Integer> care_statusMap = new HashMap<String, Integer>();
		Map<String, Integer> viral_suppressionMap = new HashMap<String, Integer>();
		
		try (Scanner sc = new Scanner(new File(s));
				PrintWriter nodeOut = new PrintWriter(new File("nodeOut.txt"));
				PrintWriter sequenceOut = new PrintWriter(new File("sequenceOut.txt"));){
//			sc.nextLine();
			int count = 0;
			while (sc.hasNextLine()) {
				String input = sc.nextLine();
				if (input.equals("\n") || input.equals("")) continue;
				String[] data = input.split("\\t");
				if (!JSWriter.nodeMap.containsKey(data[12]) || idSet.contains(data[12])) continue;
				String GENOTYPE_SEQUENCE = data[0];
				String race_grp = data[1];
				String curr_gen = data[2];
				String dx_stat = data[3];
				String trans_grp = data[4];
				String sex_at_birth = data[5];
				String age_grp = data[6];
				String acute = data[7];
				String dx_year = data[8];
				String care_status = data[9];
				String viral_suppression = data[10];
				String tract_id = data[11];
				String ID = data[12];
				String id_seq = data[13];
				idSet.add(ID);
				race_grpMap.put(race_grp, race_grpMap.getOrDefault(race_grp, 0) + 1);
				curr_genMap.put(curr_gen, curr_genMap.getOrDefault(curr_gen, 0) + 1);
				dx_statMap.put(dx_stat, dx_statMap.getOrDefault(dx_stat, 0) + 1);
				trans_grpMap.put(trans_grp, trans_grpMap.getOrDefault(trans_grp, 0) + 1);
				sex_at_birthMap.put(sex_at_birth, sex_at_birthMap.getOrDefault(sex_at_birth, 0) + 1);
				age_grpMap.put(age_grp, age_grpMap.getOrDefault(age_grp, 0) + 1);
				acuteMap.put(acute, acuteMap.getOrDefault(acute, 0) + 1);
				dx_yearMap.put(dx_year, dx_yearMap.getOrDefault(dx_year, 0) + 1);
				care_statusMap.put(care_status, care_statusMap.getOrDefault(care_status, 0) + 1);
				viral_suppressionMap.put(viral_suppression, viral_suppressionMap.getOrDefault(viral_suppression, 0) + 1);
				String res = "{ data: { id: '"+ ID +"', cluster: '" + JSWriter.nodeMap.get(data[12]) + "', degree: " + JSWriter.degreeMap.get(ID) + ", nDis: " + JSWriter.nDisMap.get(ID) + ", race_grp: '"+ race_grp + "', curr_gen: '" + curr_gen + "', dx_stat: '" + dx_stat +"', trans_grp: '"+ trans_grp +"', sex_at_birth: '"+ sex_at_birth + "', age_grp: '" + age_grp + "', acute: '" + acute + "', dx_year: '" + dx_year + "', care_status: '"+ care_status + "', viral_suppression: '" + viral_suppression + "', tract_id: '" + tract_id +"', id_seq: '" + id_seq + "'} },";
				nodeOut.println(res);
				count++;
				String seq = ">"+ id_seq + "\n" +GENOTYPE_SEQUENCE;
				sequenceOut.println(seq);
			}
			printMap(race_grpMap);
			System.out.println();
			printMap(curr_genMap);
			System.out.println();
			printMap(dx_statMap);
			System.out.println();
			printMap(trans_grpMap);
			System.out.println();
			printMap(sex_at_birthMap);
			System.out.println();
			printMap(age_grpMap);
			System.out.println();
			printMap(acuteMap);
			System.out.println();
			printMap(dx_yearMap);
			System.out.println();
			printMap(care_statusMap);
			System.out.println();
			printMap(viral_suppressionMap);
			System.out.println(count);
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void printMap(Map<String, Integer> map) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println("['" + entry.getKey() + "'," + entry.getValue() + "],");
		}
	}
}
