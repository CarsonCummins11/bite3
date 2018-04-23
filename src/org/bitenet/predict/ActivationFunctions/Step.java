package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;

public class Step implements NActivationFunction {

	@Override
	public double activate(double in) {
		return in>0?1:0;
	}
	public double derivative(double in) {
		return 0;
	}
	public static ArrayList<ArrayList<NActivationFunction>> buildActivationMatrix(int[] dimensions){
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < dimensions.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < dimensions[i]; j++) {
				ret.get(i).add(new Step());
			}
		}
		return ret;
		
	}
}
