package org.bitenet.predict.errorfunctions;
/*
 * Purpose: Implementation of Hellinger cost function
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Hellinger implements NCostFunction{
/**
	 * 
	 */
	private static final long serialVersionUID = 8331355842334736805L;
public static final double LEADING_CO = 1/Math.sqrt(2);
	@Override
	public double error(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum+=Math.pow(Math.sqrt(act[i])-Math.sqrt(exp[i]), 2);
		}
		return (LEADING_CO*sum)/exp.length;
	}

	@Override
	public double gradient(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			double actsqrt = Math.sqrt(act[i]);
			sum+=(actsqrt-Math.sqrt(exp[i]))/actsqrt;
		}
		return (LEADING_CO*sum)/exp.length;
	}


}
