package org.bitenet.predict.activationfunctions;

public class Tanh implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.tanh(in);
	}
	public double derivative(double in) {
		return Math.pow((1/Math.cosh(in)),2);
	}
}
