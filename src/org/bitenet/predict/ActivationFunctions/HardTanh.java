package org.bitenet.predict.activationfunctions;

public class HardTanh implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.max(-1, Math.min(1, in));
	}
	@Override
	public double derivative(double in) {
		if(in<1&&in>-1) {
			return 1;
		}else {
			return 0;
		}
	}

}
