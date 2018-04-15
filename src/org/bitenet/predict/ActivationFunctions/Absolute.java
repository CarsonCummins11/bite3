package org.bitenet.predict.ActivationFunctions;

public class Absolute implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.abs(in);
	}

}
