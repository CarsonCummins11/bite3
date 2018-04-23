package org.bitenet.predict.activationfunctions;

public interface NActivationFunction {
	public double activate(double in);
	public double derivative(double in);
}
