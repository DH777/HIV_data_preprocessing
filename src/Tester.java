import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class Tester {
	
	public static void main(String[] args) throws FileNotFoundException {
		JSWriter js = new JSWriter();
		js.openDir("clust04142018");
//		js.printOutJS("restudentprogrammer/Clust18_pairs_withDist1.5.txt");
//		JSWriterSequence seq = new JSWriterSequence();
//		seq.printOutJS("sequences_02012018.txt");
		ReadSequence rs = new ReadSequence();
		rs.combine("Clusters_Auxinfo");
		rs.printOutJS("all_sequence.txt");
	}
}
