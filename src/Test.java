import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
	
	// test name
	String name;
	
	// trained bayesian learner
	NaiveBayes bayes;
	
	// test data
	JointDistributionTable dataTable;
	
	// class values using bayes classification
	List<Integer> bayesClassification = new ArrayList<>();
	
	public Test(String name, String filepath, NaiveBayes bayes) throws IOException {
		this.bayes = bayes;
		dataTable = new JointDistributionTable();
		this.name = name;
		dataTable.createTable(filepath);
	}
	
	public void runTest() {
		
		// loop through every row of data
		for(int r = 0; r < dataTable.rowSize(); r++) {
			
			// probability of attributes given 0/1
			// e.g. P(barclay=0 && ... && wesley=1|0)
			double p_a_0 = 1, p_a_1 = 1;
			
			// loop through every entry, attribute=value, 
			// in the data table, e.g. poetry=0
			for(int c = 0; c < dataTable.columnSize(); c++) {
				String attribute = dataTable.getKeyAt(r, c);
				int value = dataTable.getValueAt(r, attribute);
				
				// P(attribute[0]=value[0]|0) * ... * P(attribute[n]=value[n]|0)
				// e.g. P(barclay=0|0) * ... * P(wesley=1|0)
				if(value == 0) {
					p_a_0 *= bayes.p_a0_0.get(attribute);
					p_a_1 *= bayes.p_a0_1.get(attribute);
				} else if (value == 1) {
					p_a_0 *= bayes.p_a1_0.get(attribute);
					p_a_1 *= bayes.p_a1_1.get(attribute);
				}
			}
			
			// e.g. P(barclay=0 && ... && wesley=1|0)P(0)
			// e.g. P(barclay=0 && ... && wesley=1|1)P(1)
			double p_0_a = p_a_0 * bayes.p_0;
			double p_1_a = p_a_1 * bayes.p_1;
			
			if(p_0_a >= p_1_a) bayesClassification.add(0);
			else bayesClassification.add(1);
			
		}
		
		System.out.printf("\nAccuracy on %s set (%d instances): %.2f%%\n", name, dataTable.rowSize(), calculateAccuracy());

	}

	
	public double calculateAccuracy() {
		int similar = 0;
		for(int i = 0; i < dataTable.rowSize(); i++) 
			if(bayesClassification.get(i) == dataTable.getClassValue(i)) 
				similar++;
		
		return (double)similar/(double)dataTable.rowSize() * 100;
	}

}
