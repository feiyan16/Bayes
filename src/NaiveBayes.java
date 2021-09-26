
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class NaiveBayes {

	JointDistributionTable dataTable = null;

	// size of all data, 0s, and 1s
	double u, n0, n1, p_0, p_1;

	// probabilities of 0 given 0
	TreeMap<String, Double> p_a0_0 = null,

			// probabilities of 1 given 0
			p_a1_0 = null,

			// probabilities of 0 given 1
			p_a0_1 = null,

			// probabilities of 1 given 1
			p_a1_1 = null;

	public NaiveBayes(String filepath) throws FileNotFoundException, IOException {
		
		// initialize everything to avoid NullPointerException
		dataTable = new JointDistributionTable();
		p_a0_0 = new TreeMap<>();
		p_a1_0 = new TreeMap<>();
		p_a0_1 = new TreeMap<>();
		p_a1_1 = new TreeMap<>();
		
		// parse and store attributes into data structures
		dataTable.createTable(filepath);
		
		// get # of data, # of 0s, # of 1s
		getSizes();
		
		// get probabilities and sizes 
		getProbabilities();
		
		// print calculated probabilities
		printProbabilities();
	}

	public void getProbabilities() {

		// calculate P(0) and P(1)
		p_0 = n0 / u;
		p_1 = n1 / u;

		// get row indexes of zeroes and ones
		List<Integer> zeroes = new ArrayList<>();
		List<Integer> ones = new ArrayList<>();
		for(int i = 0; i < dataTable.rowSize(); i++) {
			int c = dataTable.getClassValue(i);
			if(c == 0) zeroes.add(i);
			else if (c == 1) ones.add(i);
		}

		// for every attribute, find number of 0s and 1s 
		// within the rows that are class = 0 or class = 1
		for(int i = 0; i < dataTable.columnSize(); i++) {
			
			String attribute = dataTable.getAttributeAt(i);
			int ct_0 = 0, ct_1 = 0;

			// for all the rows with class = 0
			for(Integer z : zeroes) {
				int v = dataTable.getValueAt(z, attribute);
				if(v == 0) ct_0++; 
				else if (v == 1) ct_1++;
			}
			p_a0_0.put(attribute, (double) ct_0 / n0);
			p_a1_0.put(attribute, (double) ct_1 / n0);

			ct_0 = 0; ct_1 = 0;
			// for all the rows with class = 1
			for(Integer o : ones) {
				int v = dataTable.getValueAt(o, attribute);
				if(v == 0) ct_0++; 
				else if (v == 1) ct_1++;
			}
			p_a0_1.put(attribute, (double) ct_0 / n1);
			p_a1_1.put(attribute, (double) ct_1 / n1);
		}
		
	}

	public void getSizes() {
		
		// universe is the number of class data
		this.u = dataTable.rowSize();

		// copy table data to a copy array
		List<Integer> sortedData = Arrays.asList(new Integer[(int)u]);
		Collections.copy(sortedData, dataTable.classData);

		// sort them so it's 0000...01...1111, easier for search
		Collections.sort(sortedData);

		// Calculate number of 0s and 1s
		for(int i = 0; i < u-1; i++) {

			// # of 0s will be the index right before there is a 1
			// # of 1s will be the remaining 'half'
			if(sortedData.get(i) != sortedData.get(i + 1)) {
				n0 = i + 1;
				n1 = u - n0;
				break;
			}
		}
	}

	public void printProbabilities() {
		final String classf = "P(class=%d)=%.2f ", 
				attributef = "P(%s=%d|%d)=%.2f ";
		
		System.out.printf(classf, 0, p_0);
		for(int i = 0; i < dataTable.columnSize(); i++) {
			String attribute = dataTable.getAttributeAt(i);
			System.out.printf(attributef, attribute, 0, 0, p_a0_0.get(attribute));
			System.out.printf(attributef, attribute, 1, 0, p_a1_0.get(attribute));
		}
		
		System.out.print("\n");
		
		System.out.printf(classf, 1, p_1);
		for(int i = 0; i < dataTable.columnSize(); i++) {
			String attribute = dataTable.getAttributeAt(i);
			System.out.printf(attributef, attribute, 0, 1, p_a0_1.get(attribute));
			System.out.printf(attributef, attribute, 1, 1, p_a1_1.get(attribute));
		}
		
		System.out.print("\n");
	}
}
