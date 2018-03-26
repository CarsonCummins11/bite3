package org.bitenet.predict;


public class NNode {
	double[] weights;
	double bias;
	double value = 0;
	public NNode(int nextsize) {
		weights = new double[nextsize];
		//randomize the node on creation
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 200*Math.random()-100;
		}
		bias = 200*Math.random()-100;
	}
	public double getWeight(int at) {
		return weights[at];
	}
	public void setWeight(int at, int set) {
		weights[at] = set;
	}
	public void activate() {
		value = sigmoid(bias+value);
	}
	private double sigmoid(double inn) {
		return 1/(1+Math.exp(-inn));
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
