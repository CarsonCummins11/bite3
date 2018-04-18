package org.bitenet.predict.ActivationFunctions;

public interface NActivationFunction {
	public double activate(double in);
	public double derivative(double in);
}
