package LearningTest;

import java.io.File;
import java.io.FileNotFoundException;

import org.bitenet.predict.NDataSet;
import org.bitenet.predict.genetic.NEvolutionHandler;

public class quicktest {

	public quicktest() throws FileNotFoundException {
		System.out.println(NEvolutionHandler.train(new NDataSet(new File("inputs.txt")),new NDataSet(new File("inputs.txt")), 2048, 16384, 0.8f, 0.1f, 0.03f,.1, new qm("hello world")).def);
	}
public static void main(String[] args) {
	try {
		new quicktest();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
}
}
