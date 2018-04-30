package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;
/*
 * Purpose: implementation of Tanh activation
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Tanh implements NActivationFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4970810997084539852L;
	@Override
	public double activate(double in) {
		return Math.tanh(in);
	}
	public double derivative(double in) {
		return Math.pow((1/Math.cosh(in)),2);
	}
	public static ArrayList<ArrayList<NActivationFunction>> buildActivationMatrix(int[] dimensions){
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < dimensions.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < dimensions[i]; j++) {
				ret.get(i).add(new Tanh());
			}
		}
		return ret;
		
	}
}
