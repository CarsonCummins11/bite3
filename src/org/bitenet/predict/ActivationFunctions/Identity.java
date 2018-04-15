package org.bitenet.predict.ActivationFunctions;

public class Identity implements NActivationFunction {

	@Override
	public double activate(double in) {
		return in;
	}

}
