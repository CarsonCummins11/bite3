package LearningTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class sdHook implements Runnable{
ArrayList<ArrayList<Double>> inDat;
ArrayList<ArrayList<Double>> outDat;
	public sdHook() {
	inDat = new ArrayList<>();
	outDat = new ArrayList<>();
}

@Override
public void run() {
	try {
		System.out.println("shutdown hook initiated");
		PrintWriter outpw = new PrintWriter(new File("outdat.txt"));
		PrintWriter inpw = new PrintWriter(new File("indat.txt"));
		String od = "";
		String id = "";
		for (int i = 0; i < inDat.size(); i++) {
			for (int j = 0; j < inDat.get(i).size(); j++) {
				id += inDat.get(i).get(j)+",";
			}
			id+="\n";
			for (int j = 0; j < outDat.get(i).size(); j++) {
				od += outDat.get(i).get(j)+",";
			}
			od+="\n";
			
		}
		outpw.write(od);
		inpw.write(id);
		outpw.close();
		inpw.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
}

}
