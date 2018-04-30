package org.bitenet.predict.activationfunctions;

import java.io.Serializable;

/*
 * Purpose: general form of activation function
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public interface NActivationFunction extends Serializable{
	public double activate(double in);
	public double derivative(double in);
}
