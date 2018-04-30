package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;
/*
 * Purpose: implementation of Rectifier activation
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Rectifier implements NActivationFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6320488227237454403L;
	@Override
	public double activate(double in) {
		return Math.max(0,in);
	}
	@Override
	public double derivative(double in) {
		return in<0?0:1;
	}
	public static ArrayList<ArrayList<NActivationFunction>> buildActivationMatrix(int[] dimensions){
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < dimensions.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < dimensions[i]; j++) {
				ret.get(i).add(new Rectifier());
			}
		}
		return ret;
		
	}
}
