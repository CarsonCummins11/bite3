package org.bitenet.predict.ActivationFunctions;

public class LeCunTanh implements NActivationFunction {

	@Override
	public double activate(double in) {
		return 1.7159*Math.tanh(.6666666666666666666*in);
	}

	@Override
	public double derivative(double in) {
		return 1.7159*.6666666666666666666666666666*Math.pow(1/Math.cosh(.666666666666666666666666666666*in),2);
	}

}
