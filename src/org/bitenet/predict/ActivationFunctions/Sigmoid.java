package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;
/*
 * Purpose: implementation of Sigmoid activation
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Sigmoid implements NActivationFunction {
	@Override
	public double activate(double in) {
		return 1/(1+Math.exp(-in));
	}
	@Override
	public double derivative(double in) {
		double qq = activate(in);
		return qq*(1-qq);
		
	}
	public static ArrayList<ArrayList<NActivationFunction>> buildActivationMatrix(int[] dimensions){
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < dimensions.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < dimensions[i]; j++) {
				ret.get(i).add(new Sigmoid());
			}
		}
		return ret;
		
	}

}
