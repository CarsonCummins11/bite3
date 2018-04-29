package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;
/*
 * Purpose: implementation of Smooth Rectifier activation
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class SmoothRectifier implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.log(1+Math.exp(in));
	}
	@Override
	public double derivative(double in) {
		double qq = Math.exp(in);
		return qq/(1+qq);
	}
	public static ArrayList<ArrayList<NActivationFunction>> buildActivationMatrix(int[] dimensions){
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < dimensions.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < dimensions[i]; j++) {
				ret.get(i).add(new SmoothRectifier());
			}
		}
		return ret;
		
	}
}
