package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;
/*
 * Purpose: implementation of LeCun Tanh activation
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class LeCunTanh implements NActivationFunction {

	@Override
	public double activate(double in) {
		return 1.7159*Math.tanh(.6666666666666666666*in);
	}

	@Override
	public double derivative(double in) {
		return 1.7159*.6666666666666666666666666666*Math.pow(1/Math.cosh(.666666666666666666666666666666*in),2);
	}
	public static ArrayList<ArrayList<NActivationFunction>> buildActivationMatrix(int[] dimensions){
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < dimensions.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < dimensions[i]; j++) {
				ret.get(i).add(new LeCunTanh());
			}
		}
		return ret;
		
	}
}
