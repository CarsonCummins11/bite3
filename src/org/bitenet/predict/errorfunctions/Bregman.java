package org.bitenet.predict.errorfunctions;
/*
 * Purpose: implementation of bregman cost
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Bregman implements NCostFunction{

	@Override
	public double error(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum+=exp[i]*Math.log(exp[i]/act[i]);
		}
		for (int i = 0; i < exp.length; i++) {
			sum-=exp[i];
		}
		for (int i = 0; i < act.length; i++) {
			sum+=act[i];
		}
		return sum;
	}

	@Override
	public double gradient(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum+=((act[i]-exp[i])/act[i]);
		}
		return sum;
	}

}
