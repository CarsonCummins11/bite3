package org.bitenet.predict.ActivationFunctions;

public class BipolarSigmoid implements NActivationFunction {

	@Override
	public double activate(double in) {
		return (1-Math.exp(-in))/(1+Math.exp(-in));
	}

}
