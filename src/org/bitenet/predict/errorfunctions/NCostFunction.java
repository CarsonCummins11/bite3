package org.bitenet.predict.errorfunctions;

public interface NCostFunction {
	public double error(double[] exp, double[] act);
	public double gradient(double[] exp,double[] act);
}
