package org.bitenet.predict.activationfunctions;
/*
 * Purpose: general form of activation function
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public interface NActivationFunction {
	public double activate(double in);
	public double derivative(double in);
}
