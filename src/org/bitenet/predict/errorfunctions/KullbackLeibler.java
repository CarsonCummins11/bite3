package org.bitenet.predict.errorfunctions;
/*
 * Purpose: implementation of KullbackLeibler cost
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class KullbackLeibler implements NCostFunction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2298460642716140705L;

	@Override
	public double error(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum+=exp[i]*Math.log(exp[i]/act[i]);
		}
		return sum;
	}

	@Override
	public double gradient(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum+=-exp[i]/act[i];
		}
		return sum;
	}

}
