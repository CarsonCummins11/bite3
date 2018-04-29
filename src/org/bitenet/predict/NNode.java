package org.bitenet.predict;
import java.util.ArrayList;

import org.bitenet.predict.activationfunctions.*;
/*
 * Purpose: Node for usage in a neural net
 * 
 * @author Carson Cummins
 * @version 0.0
 */
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
		
		weights = (nextsize==-1?null:new double[nextsize]);
		//randomize the node on creation
		for (int i = 0; i < nextsize; i++) {
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
		//System.out.println("layer num: "+myLayer+" index: "+myIndex+" got fired");
		int i = 0;
		for(NNode con : connections) {
			con.value+=value*weights[i];
			i++;
		}
	}
	public void activation() {
		//System.out.println("layer num: "+myLayer+" index: "+myIndex+" got activated");
		value = actFunc.activate(bias+value);
	}
	public double backActivate(double[] inputs) {
		//calculate the value of any node given input values
		if(prev!=null) {
		double sum = 0;
		for (NNode cur : prev.nodes) {
			sum+=cur.weights[myIndex]*cur.backActivate(inputs);
		}
		return actFunc.activate(sum+bias);
		}else {
			return inputs[myIndex];
		}
	}
	public double backActivateOthers(double[] inputs) {
		//calculate the value of any node given input values
				if(prev!=null) {
				double sum = 0;
				for (NNode cur : prev.nodes) {
					sum+=cur.weights[myIndex]*cur.backActivate(inputs);
				}
				return sum+bias;
				}else {
					return inputs[myIndex];
				}
	}
	public double derivative(int layerNum, int layerInd, double[] inputs) {
		if(layerNum == myLayer&&layerInd == myIndex){
			return actFunc.derivative(backActivateOthers(inputs));
		}else if(prev == null){
		return 0;
		}else {
			double pderiv = 0;
			for(NNode c : prev.nodes) {
				pderiv+=c.weights[myIndex]*c.derivative(layerNum, layerInd, inputs);
			}
			return actFunc.derivative(backActivateOthers(inputs))*pderiv;
		}
	}
	public double derivative(int layerNum, int layerInd, int weightNum, double[] inputs) {
		if(layerNum == myLayer&&layerInd == myIndex){
		return actFunc.derivative(backActivateOthers(inputs))*prev.nodes.get(weightNum).backActivate(inputs);
		}else if(prev == null){
		return 0;
		}else {
			double pderiv = 0;
			for(NNode c : prev.nodes) {
				pderiv+=c.weights[myIndex]*c.derivative(layerNum, layerInd,weightNum,inputs);
			}
			return actFunc.derivative(backActivateOthers(inputs))*pderiv;
		}
	}
	
	public void print() {
		System.out.print("bias = "+bias+", # of connections = "+(connections==null?0:weights.length));
		for (int i = 0; i < (connections==null?-1:weights.length); i++) {
			System.out.print(", weights["+i+"] = "+weights[i]);
		}
	System.out.println("");
	}
}
