import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
//		String trainingPath = null;
//		String testPath = null;
//		if(args.length < 2) {
//			System.out.println("Please enter training data and test data");
//			System.exit(0);
//		} else {
//			trainingPath = args[0];
//			testPath = args[1];
//		}
		
		// get path and create input stream
		NaiveBayes bayes = new NaiveBayes("train.dat");
		
		Test test1 = new Test("training", "train.dat", bayes);
		test1.runTest();
		Test test2 = new Test("test", "test.dat", bayes);
		test2.runTest();
	}

}
