package org.bitenet.predict.ActivationFunctions;

public class ComplementaryLogLog implements NActivationFunction {

	@Override
	public double activate(double in) {
		
		return 1-Math.exp(-Math.exp(in));
	}

	@Override
	public double derivative(double in) {
		return Math.exp(-Math.exp(in)+in);
	}

}
