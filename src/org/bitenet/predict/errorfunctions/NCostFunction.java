package org.bitenet.predict.errorfunctions;
/*
 * Purpose: This is the general form for a cost function
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public interface NCostFunction {
	public double error(double[] exp, double[] act);
	public double gradient(double[] exp,double[] act);
}
