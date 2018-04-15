package org.bitenet.predict.ActivationFunctions;

public class ComplementaryLogLog implements NActivationFunction {

	@Override
	public double activate(double in) {
		
		return 1-Math.exp(-Math.exp(in));
	}

}
