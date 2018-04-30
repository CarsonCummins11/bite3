package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;
/*
 * Purpose: implementation of Bipolar Sigmoid activation
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class BipolarSigmoid implements NActivationFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8886769462681558325L;
	@Override
	public double activate(double in) {
		return (1-Math.exp(-in))/(1+Math.exp(-in));
	}

	@Override
	public double derivative(double in) {
		return 2*Math.exp(-in)/((1+Math.exp(-2))*(1+Math.exp(-2)));
	}
	public static ArrayList<ArrayList<NActivationFunction>> buildActivationMatrix(int[] dimensions){
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < dimensions.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < dimensions[i]; j++) {
				ret.get(i).add(new BipolarSigmoid());
			}
		}
		return ret;
		
	}
}
