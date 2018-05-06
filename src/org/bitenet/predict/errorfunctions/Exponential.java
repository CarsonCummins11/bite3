package org.bitenet.predict.errorfunctions;
/*
 * Purpose: Implementation of exponential cost function
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Exponential implements NCostFunction {
/**
	 * 
	 */
	private static final long serialVersionUID = -8326788467173574614L;
public double tau;
	public Exponential(double tau) {
		this.tau = tau;
	}
	@Override
	public double error(double[] exp, double[] act) {
		double sum = 0;
			for (int i = 0; i < act.length; i++) {
				sum+=Math.pow(exp[i]-act[i],2);
			}
		sum = tau*Math.exp((1/tau)*sum);
		return sum/exp.length;
	}

	@Override
	public double gradient(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum+=Math.pow(exp[i]-act[i],2);
		}
		sum = tau*Math.exp(sum/tau);
		double summ = 0;
		double tt = 2/tau;
		for (int i = 0; i < act.length; i++) {
			summ+=tt*(act[i]-exp[i]);
		}
		return (sum*summ)/exp.length;
	}


}
