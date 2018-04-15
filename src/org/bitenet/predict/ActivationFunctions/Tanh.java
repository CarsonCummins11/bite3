package org.bitenet.predict.ActivationFunctions;

public class Tanh implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.tanh(in);
	}

}
