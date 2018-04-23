package org.bitenet.predict.activationfunctions;

public class Rectifier implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.max(0,in);
	}
	@Override
	public double derivative(double in) {
		return in<0?0:1;
	}

}
