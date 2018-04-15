package org.bitenet.predict;

import java.util.ArrayList;

public class NLayer {
ArrayList<NNode> nodes;
NLayer next;
	public NLayer() {
		nodes = new ArrayList<NNode>();
	}
	public void activate() {
		if(next!=null) {
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.get(i).weights.length; j++) {
				double weight = nodes.get(i).weights[j];
				if(weight!=0||nodes.get(i).value == 0) {
				next.nodes.get(j).value+=weight*nodes.get(i).value;
				}
			}
		}
		if(next.next!=null) {
		for (int i = 0; i < next.nodes.size(); i++) {
			next.nodes.get(i).activate();
		}
		}
		next.activate();
		}
	}
	public double[] getValues() {
		ArrayList<Double> retu = new ArrayList<Double>();
		for (int i = 0; i < nodes.size(); i++) {
			retu.add(nodes.get(i).value);
		}
		double[] ret = new double[retu.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = retu.get(i);
		}
		return ret;
	}
	public void addNext(NLayer n) {
		if(next!=null) {
			next.addNext(n);
		}else {
			next = n;
		}
	}
	public ArrayList<NLayer> getLayers(ArrayList<NLayer> start){
		start.add(this);
		if(next!=null) {
			return next.getLayers(start);
		}else {
			return start;
		}
	}
	public void mutate() {
		int mutNum = (int)(Math.random()*nodes.size()/2);
		for (int i = 0; i < mutNum; i++) {
			int mutInd = (int)Math.round(Math.random()*(nodes.size()-1));
			nodes.get(mutInd).mutate();
		}
	}
	public void print() {
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).print();
		}
		
	}
	public void reset() {
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).value = 0;
		}
	if(next!=null) {
		next.reset();
	}
		
	}
}
