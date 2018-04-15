package org.bitenet.predict;
import org.bitenet.predict.ActivationFunctions.*;

public class NNode {
	double[] weights;
	double bias;
	double value = 0;
	NActivationFunction actFunc;
	public NNode(int nextsize, int activationFunction) {
		weights = new double[nextsize];
		//randomize the node on creation
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 200*Math.random()-100;
		}
		bias = 200*Math.random()-100;
		switch(activationFunction) {
		case 0:
		
			actFunc = new Identity();
			break;
		case 1:
			
			actFunc = new Step();
			break;
		case 2:
			
			actFunc = new PiecwiseLinear();
			break;
		case 3:
			
			actFunc = new Sigmoid();
			break;
		case 4:
			
			actFunc =new  ComplementaryLogLog();
			break;
		case 5:
			
			actFunc = new Bipolar();
			break;
		case 6:
			
			actFunc = new BipolarSigmoid();
			break;
		case 7:
			
			actFunc = new Tanh();
			break;
		case 8:
			
			actFunc = new LeCunTanh();
			break;
		case 9:
			
			actFunc = new HardTanh();
			break;
		case 10:
			
			actFunc = new Absolute();
			break;
		case 11:
			
			actFunc = new Rectifier();
			break;
		case 12:
			
			actFunc = new SmoothRectifier();
			break;
		case 13:
			
			actFunc = new Logit();
			break;
		case 14:
			
			actFunc = new Cosine();
			break;
		}
	}
	public NNode(int nextsize, NActivationFunction activationFunction) {
		weights = new double[nextsize];
		//randomize the node on creation
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 200*Math.random()-100;
		}
		bias = 200*Math.random()-100;
		actFunc = activationFunction;
	}
	public double getWeight(int at) {
		return weights[at];
	}
	public void setWeight(int at, int set) {
		weights[at] = set;
	}
	public void activate() {
		value = actFunc.activate(bias+value);
	}
	public void mutate() {
		boolean mutBias = .1>Math.random();
		if(mutBias) {
			bias = Math.random()*(bias*3)-bias;
		}
		int mutNum = (int)(Math.random()*weights.length/2);
		for (int i = 0; i < mutNum; i++) {
			int mutInd = (int)(Math.random()*weights.length-1);
			weights[mutInd] = Math.random()*(bias*3)-bias;
		}
	
	}
	public void print() {
		System.out.print("bias = "+bias+", # of connections = "+weights.length);
		for (int i = 0; i < weights.length; i++) {
			System.out.print(", weights["+i+"] = "+weights[i]);
		}
	System.out.println("");
	}
}
