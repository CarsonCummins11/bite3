package LearningTest;

import java.io.File;
import java.io.FileNotFoundException;

import org.bitenet.predict.NDataSet;
import org.bitenet.predict.NModel;
import org.bitenet.predict.NPoly;

public class xor {

	public xor() {
		/*NModel test = new NModel(2,1);
		test = NModel.train(new double[][] {{1,1},{0,1},{1,0},{0,0}}, new double[][] {{0},{1},{1},{0}} , .1);
		*/
		NPoly test = new NPoly(5);
		try {
			test.train(new NDataSet(new File("inputs.txt")), new NDataSet(new File("outputs.txt")), .0000001,-1, .03);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		test.print();
		/*
		NNet test = new NNet(new int[] {2,3,1});
		test.train(new double[][] {{1,1},{0,1},{1,0},{0,0}}, new double[][] {{0},{1},{1},{0}}, .07, .1,10000);
		System.out.println("should be 0, actual result is: " + test.activate(new double[]{1,1})[0]);
		System.out.println("should be 1, actual result is: " + test.activate(new double[]{0,1})[0]);
		System.out.println("should be 1, actual result is: " + test.activate(new double[]{1,0})[0]);
		System.out.println("should be 0, actual result is: " + test.activate(new double[]{0,0})[0]);
		*/
	}

	public static void main(String[] args) {
		new xor();

	}

}
