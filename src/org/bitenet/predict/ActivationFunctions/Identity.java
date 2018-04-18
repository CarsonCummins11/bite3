package org.bitenet.predict.ActivationFunctions;

public class Identity implements NActivationFunction {

	@Override
	public double activate(double in) {
		return in;
	}

	@Override
	public double derivative(double in) {
		return 1;
	}

}
