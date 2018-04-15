package org.bitenet.predict.ActivationFunctions;

public class HardTanh implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.max(-1, Math.min(1, in));
	}

}
