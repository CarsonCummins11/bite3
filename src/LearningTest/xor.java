package LearningTest;

import java.io.File;
import java.io.FileNotFoundException;

import org.bitenet.predict.NDataSet;
import org.bitenet.predict.NNet;
import org.bitenet.predict.activationfunctions.*;
import org.bitenet.predict.errorfunctions.*;

public class xor {

	public xor() throws FileNotFoundException {
		/*NModel test = new NModel(2,1);
		test = NModel.train(new double[][] {{1,1},{0,1},{1,0},{0,0}}, new double[][] {{0},{1},{1},{0}} , .1);
		*/
		/*
		NPoly test = new NPoly(5);
		try {
			test.train(new NDataSet(new File("inputs.txt")), new NDataSet(new File("outputs.txt")), .0000001,-1, .03);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		test.print();
		*/
		NNet test = new NNet(new int[] {2,3,1}, new SmoothRectifier(),new Quadratic());
		test.train(new NDataSet(new File("inputs.txt")), new NDataSet(new File("outputs.txt")), .07, .126,10000);
		System.out.println("should be 0, actual result is: " + test.activate(new double[]{1,1})[0]);
		System.out.println("should be 1, actual result is: " + test.activate(new double[]{0,1})[0]);
		System.out.println("should be 1, actual result is: " + test.activate(new double[]{1,0})[0]);
		System.out.println("should be 0, actual result is: " + test.activate(new double[]{0,0})[0]);
	}

	public static void main(String[] args) {
		try {
			new xor();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
