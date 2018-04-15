package org.bitenet.predict.ActivationFunctions;

public class Rectifier implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.max(0,in);
	}

}
