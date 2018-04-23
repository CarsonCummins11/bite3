package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;

public class Logit implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.log(in/(1-in));
	}

	@Override
	public double derivative(double in) {
		return (1-in)/(Math.pow(in,3));
	}
	public static ArrayList<ArrayList<NActivationFunction>> buildActivationMatrix(int[] dimensions){
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < dimensions.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < dimensions[i]; j++) {
				ret.get(i).add(new Logit());
			}
		}
		return ret;
		
	}
}
