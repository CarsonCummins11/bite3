package org.bitenet.predict.errorfunctions;
/*
 * Purpose: Implementation of ItakuraSaito cost function
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class ItakuraSaito implements NCostFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5298727568415609915L;

	@Override
	public double error(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			double div = exp[i]/act[i];
			sum+=(div-Math.log(div)-1);
		}
		return sum;
	}

	@Override
	public double gradient(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum+=(act[i]-exp[i])/Math.pow(act[i], 2);
		}
		return sum;
	}


}
