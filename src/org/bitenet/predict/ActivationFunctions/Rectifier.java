package org.bitenet.predict.activationfunctions;

import java.util.ArrayList;

public class Rectifier implements NActivationFunction {

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
