package LearningTest;

import java.io.File;
import java.io.FileNotFoundException;

import org.bitenet.predict.NDataSet;
import org.bitenet.predict.NNet;
import org.bitenet.predict.genetic.NEvolutionHandler;

public class quicktest {

	public quicktest() throws FileNotFoundException {
		NNet fit = new NNet(new int[] { 70, 55, 40, 20, 10, 1 }, NNet.BIPOLAR_SIGMOID);
		fit.train(new NDataSet(new File("textIDin.txt")), new NDataSet(new File("textIDout.txt")), .01, .1, 5000);
		System.out.println(NEvolutionHandler.train(new NDataSet(new File("inputs.txt")),
				new NDataSet(new File("inputs.txt")), 2048, 16384, 0.8f, 0.1f, 0.03f, .1,
				new qm("asdfghjilkefisjkldiwn 482;dlfnti4920d,g kiels flsid95k5o40gj704jdl;ski3")).def);
	}

	public static void main(String[] args) {
		try {
			new quicktest();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
