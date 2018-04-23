package org.bitenet.predict.activationfunctions;

public class Step implements NActivationFunction {

	@Override
	public double activate(double in) {
		return in>0?1:0;
	}
	public double derivative(double in) {
		return 0;
	}

}
