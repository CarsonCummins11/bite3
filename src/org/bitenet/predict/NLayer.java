package org.bitenet.predict;

import java.util.ArrayList;
/*
 * Purpose: Layer for usage in a neural network
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class NLayer {
	public ArrayList<NNode> nodes;
	NLayer next;

	public NLayer() {
		nodes = new ArrayList<NNode>();
	}

	public void activate() {
		if(next!=null) {
			for (NNode n : nodes) {
				n.fire();
			}
			for (NNode n : next.nodes) {
				n.activation();
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
		if (next != null) {
			next.addNext(n);
		} else {
			next = n;
		}
	}

	public ArrayList<NLayer> getLayers(ArrayList<NLayer> start) {
		start.add(this);
		if (next != null) {
			return next.getLayers(start);
		} else {
			return start;
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
		if (next != null) {
			next.reset();
		}

	}
}
