package org.bitenet.predict;
import java.util.ArrayList;

import org.bitenet.predict.activationfunctions.*;

public class NNode {
	double[] weights;
	double bias;
	double value = 0;
	NActivationFunction actFunc;
	NLayer prev;
	int myLayer;
	int myIndex;
	ArrayList<NNode> connections;
	public NNode(ArrayList<NNode> cons,int layerNum, int layerInd,NLayer previous, int nextsize, NActivationFunction activationFunction) {
		myLayer = layerNum;
		myIndex = layerInd;
		prev = previous;
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
	public void fire() {
		int i = 0;
		for(NNode con : connections) {
			con.value+=value*weights[i];
			i++;
		}
	}
	public void activation() {
		value = actFunc.activate(bias+value);
	}
	public double backProp(double[] inputs) {
		//the inside of L (weights*aprev + bias)
		if(prev!=null) {
		double sum = 0;
		for (int i = 0; i < prev.nodes.size(); i++) {
			NNode cur = prev.nodes.get(i);
			sum+=cur.weights[myIndex]*cur.backProp(inputs);
		}
		return sum+bias;
		}else {
			return inputs[myIndex];
		}
	}
	public double derivative(NLayer inputLayer, int layerNum, int layerInd, double[] inputs) {
		if(layerNum == myLayer&&layerInd == myIndex){
			double ret = actFunc.derivative(backProp(inputs));
			inputLayer.reset();
			return ret;
		}else if(prev == null){
			/*
			 * This might have to be 0 but idk??
			 * 
			 */
			inputLayer.reset();
			return inputs[myIndex];
		}else {
			double thisder =  actFunc.derivative(backProp(inputs));
			double sum = 0;
			for (int i = 0; i < prev.nodes.size(); i++) {
				NNode cur = prev.nodes.get(i);
				sum+=cur.weights[myIndex]*cur.derivative(inputLayer,layerNum, layerInd, inputs);
			}
			return thisder*sum;
		}
	}
	public double derivative(NLayer inputLayer,int layerNum, int layerInd, int weightNum, double[] inputs) {
		if(layerNum == myLayer&&layerInd == myIndex){
			double ret =  actFunc.derivative(backProp(inputs))*prev.nodes.get(weightNum).backProp(inputs);
			inputLayer.reset();
			return ret;
		}else if(prev == null){
			inputLayer.reset();
			/*
			 * This might have to be 0 but idk??
			 * 
			 */
			return inputs[myIndex];
		}else {
			double thisder = actFunc.derivative(backProp(inputs));
			double sum = 0;
			for (int i = 0; i < prev.nodes.size(); i++) {
				NNode cur = prev.nodes.get(i);
				sum+=cur.weights[myIndex]*cur.derivative(inputLayer,layerNum, layerInd, weightNum, inputs);
			}
			return thisder*sum;
		}
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
