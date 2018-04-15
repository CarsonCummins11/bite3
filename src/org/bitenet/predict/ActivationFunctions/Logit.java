package org.bitenet.predict.ActivationFunctions;

public class Logit implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.log(in/(1-in));
	}

}
