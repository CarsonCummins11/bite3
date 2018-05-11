package LearnTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import org.bitenet.predict.NBNet;
import org.bitenet.predict.NModel;
import org.bitenet.predict.data.LocalDataSet;

public class XOR {

	public XOR() throws FileNotFoundException {

		//NModel nm = NModel.train(new LocalDataSet(new File("Test_data/XORInputs.txt")), new LocalDataSet(new File("Test_data/XOROutputs.txt")),.06);
		NBNet nm = new NBNet(new int[] {2,2,2,2,1});
		nm.train(new LocalDataSet(new File("Test_data/XORInputs.txt")), new LocalDataSet(new File("Test_data/XOROutputs.txt")),.01);
		System.out.println("should be 0, actual "+Arrays.toString(nm.activate(new double[] {0,0})));
		System.out.println("should be 0, actual "+Arrays.toString(nm.activate(new double[] {1,1})));
		System.out.println("should be 1, actual "+Arrays.toString(nm.activate(new double[] {1,0})));
		System.out.println("should be 1, actual "+Arrays.toString(nm.activate(new double[] {0,1})));
	}

	public static void main(String[] args) {
	try {
		new XOR();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}

}
