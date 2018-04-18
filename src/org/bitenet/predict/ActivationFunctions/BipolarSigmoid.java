package org.bitenet.predict.ActivationFunctions;

public class BipolarSigmoid implements NActivationFunction {

	@Override
	public double activate(double in) {
		return (1-Math.exp(-in))/(1+Math.exp(-in));
	}

	@Override
	public double derivative(double in) {
		return 2*Math.exp(-in)/((1+Math.exp(-2))*(1+Math.exp(-2)));
	}

}
