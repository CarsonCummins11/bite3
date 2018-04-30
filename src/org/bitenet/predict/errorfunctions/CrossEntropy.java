package org.bitenet.predict.errorfunctions;
/*
 * Purpose: Implementation of cross entropy cost
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class CrossEntropy implements NCostFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8915955859933722739L;

	@Override
	public double error(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum+=exp[i]*Math.log(act[i]);
		}
		return -sum;
	}

	@Override
	public double gradient(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum-=exp[i]/act[i];
		}
		return sum;
	}


}
