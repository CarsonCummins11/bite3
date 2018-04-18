package org.bitenet.predict.ActivationFunctions;

public class Logit implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.log(in/(1-in));
	}

	@Override
	public double derivative(double in) {
		return (1-in)/(Math.pow(in,3));
	}

}
