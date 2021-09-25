import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class JointDistributionTable {
	
	// attribute headers, wesley, romulan, etc.
	String[] attributes = null;
	
	// joint distribution table for test data
	List<TreeMap<String, Integer>> table = null;
	List<Integer> classData = null;
		
	public JointDistributionTable() {
		table = new ArrayList<>();
		classData = new ArrayList<>();
	}
	
	public void putInRow(String key, Integer value, int row) {
		getRow(row).put(key, value);
	}
	
	public String getAttributeAt(int col) {
		return attributes[col];
	}
	
	public int getValueAt(int row, String attribute) {
		return getRow(row).get(attribute);
	}
	
	public String getKeyAt(int r, int c) {
		TreeMap<String, Integer> row = getRow(r);
		List<Entry<String, Integer>> entries = new ArrayList<>(row.entrySet());
		return entries.get(c).getKey();
	}
	
	public int getClassValue(int row) {
		return classData.get(row);
	}
	
	public void addClassValue(Integer value) {
		classData.add(value);
	}
	
	public void addRow(TreeMap<String, Integer> treemap) {
		table.add(treemap);
	}
	
	public TreeMap<String, Integer> getRow(int row) {
		return table.get(row);
	}
	
	public void addAttributes(String[] attributes) {
		this.attributes = attributes;
	}
	
	public String[] getAttributes() {
		return attributes;
	}
	
	public int columnSize() {
		return attributes.length - 1;
	}
	
	public int rowSize() {
		return classData.size();
	}
	
	public void print() {
		int borderCt = 0;
		for(String attribute : table.get(0).keySet()) 
			borderCt += (attribute.length() + 3);
		
		for(int i = 0; i < borderCt + 1; i++)
			System.out.print("-");
		System.out.print("\n|");
		for(String attribute : table.get(0).keySet()) {
			System.out.printf(" %s |", attribute);
		}
		System.out.print("\n");
		for(int i = 0; i < borderCt + 1; i++)
			System.out.print("-");
		
		for(int i = 0; i < rowSize(); i++) {
			System.out.print("\n|");
			for(Map.Entry<String, Integer> entry : table.get(i).entrySet()) {
				String attribute = entry.getKey();
				int value = entry.getValue();
				for(int s = 0; s < attribute.length(); s++) System.out.print(" ");
				System.out.printf("%d |", value);
				
			}
		}
		System.out.print("\n");
		for(int i = 0; i < borderCt + 1; i++)
			System.out.print("-");
	}

	public void createTable(String filepath) throws IOException {
		FileInputStream file = new FileInputStream(filepath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(file));
		
		// store attribute headers into array
		// attributes[0] = "wesley", etc.
		String attributeStr = reader.readLine();
		addAttributes(attributeStr.split("\t"));
		
		// loop each row of data line, and row tracker
		String line = null; int ln = 0;
		while((line = reader.readLine()) != null) {
			
			// skip whitespace/empty lines
			if(line.isEmpty() || Character.isWhitespace(line.charAt(0))) 
				continue;

			// split each row of data string by column
			String[] dataLn = line.split("\t");
			
			// add value at last column, "class", to classData
			// line 0: class[0] = 0/1
			int classValue = Integer.parseInt(dataLn[dataLn.length - 1]);
			addClassValue(classValue);
			
			// add a new treemap for every row of data (line)
			addRow(new TreeMap<>());
			for(int i = 0; i < dataLn.length - 1; i++) {
				
				// attribute[0] = Wesley
				// dataLn[0] = corresponding value for wesley
				String attribute = getAttributeAt(i);
				int dataValue = Integer.parseInt(dataLn[i]);
				
				// line 0: table.get(0) = [wesley=0, romulan=1, ... ]
				putInRow(attribute, dataValue, ln);
			}
			
			// increment to next row
			ln++;
		}
		
		reader.close();
	}
}
