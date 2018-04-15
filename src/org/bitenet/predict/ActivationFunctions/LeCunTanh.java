package org.bitenet.predict.ActivationFunctions;

public class LeCunTanh implements NActivationFunction {

	@Override
	public double activate(double in) {
		return 1.7159*Math.tanh(.6666666666666666666*in);
	}

}
