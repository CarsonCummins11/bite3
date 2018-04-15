package org.bitenet.predict.ActivationFunctions;

public class Sigmoid implements NActivationFunction {

	@Override
	public double activate(double in) {
		return 1/(1+Math.exp(-in));
	}

}
