package org.bitenet.predict.activationfunctions;

public class PiecwiseLinear implements NActivationFunction {

	@Override
	public double activate(double in) {
		if(in<-1) return 0;
		if(in>1) return 1;
		return .5+(.5*in);
	}
	@Override
	public double derivative(double in) {
		if(in<-1||in>1) return 0;
		return .5;
	}
}
