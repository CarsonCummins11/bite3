package org.bitenet.predict.ActivationFunctions;

public class Step implements NActivationFunction {

	@Override
	public double activate(double in) {
		return in>0?1:0;
	}

}
