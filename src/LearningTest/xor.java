package LearningTest;

import java.io.File;
import java.io.FileNotFoundException;

import org.bitenet.predict.NDataSet;
import org.bitenet.predict.NModel;
import org.bitenet.predict.NNet;
import org.bitenet.predict.NNetTrainer;
import org.bitenet.predict.activationfunctions.*;
import org.bitenet.predict.errorfunctions.*;

public class xor {

	public xor() throws FileNotFoundException {
		NModel test = NModel.train(new NDataSet(new File("inputs.txt")), new NDataSet(new File("outputs.txt")));
		/*
		NPoly test = new NPoly(5);
		try {
			test.train(new NDataSet(new File("inputs.txt")), new NDataSet(new File("outputs.txt")), .0000001,-1, .03);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		test.print();
		*/
		//NNet test = NNetTrainer.train(new NDataSet(new File("inputs.txt")), new NDataSet(new File("outputs.txt")), .07, .1,10000, 30,Cosine.buildActivationMatrix(new int[] {2,3,1}), new Quadratic(), new int[] {2,3,1});
		System.out.println("should be 0, actual result is: " + test.activate(new double[]{1,1})[0]);
		System.out.println("should be 1, actual result is: " + test.activate(new double[]{0,1})[0]);
		System.out.println("should be 1, actual result is: " + test.activate(new double[]{1,0})[0]);
		System.out.println("should be 0, actual result is: " + test.activate(new double[]{0,0})[0]);
		test.print();
		/*
		NNet test = new NNet(new int[] {2,3,2}, new SmoothRectifier(),new Quadratic());
		test.print();
		double[] out = test.activate(new double[]{5,10});
		System.out.println(out[0]+","+out[1]);
		System.out.println(test.output.nodes.get(0).backActivate(new double[] {5,10})+","+test.output.nodes.get(1).backActivate(new double[] {5,10}));
		*/
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
