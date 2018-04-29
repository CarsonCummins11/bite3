package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;
/*
 * Purpose: implementation of Piecewise linear activation
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class PiecewiseLinear implements NActivationFunction {

	@Override
	public double activate(double in) {
		if(in<-1) return 0;
		if(in>1) return 1;
		return .5+(.5*in);
	}
	@Override
	public double derivative(double in) {
		if(in<-1||in>1) return 0;
		return .5;
	}
	public static ArrayList<ArrayList<NActivationFunction>> buildActivationMatrix(int[] dimensions){
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < dimensions.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < dimensions[i]; j++) {
				ret.get(i).add(new PiecewiseLinear());
			}
		}
		return ret;
		
	}
}
