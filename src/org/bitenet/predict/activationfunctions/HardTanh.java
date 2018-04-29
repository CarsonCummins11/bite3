package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;
/*
 * Purpose: implementation of Hard Tanh activation
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class HardTanh implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.max(-1, Math.min(1, in));
	}
	@Override
	public double derivative(double in) {
		if(in<1&&in>-1) {
			return 1;
		}else {
			return 0;
		}
	}
	public static ArrayList<ArrayList<NActivationFunction>> buildActivationMatrix(int[] dimensions){
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < dimensions.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < dimensions[i]; j++) {
				ret.get(i).add(new HardTanh());
			}
		}
		return ret;
		
	}
}
