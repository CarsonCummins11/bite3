package org.bitenet.predict.ActivationFunctions;

public class SmoothRectifier implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.log(1+Math.exp(in));
	}
	@Override
	public double derivative(double in) {
		double qq = Math.exp(in);
		return qq/(1+qq);
	}

}
