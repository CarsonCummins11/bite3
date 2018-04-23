package org.bitenet.predict.errorfunctions;

public class ItakuraSaito implements NCostFunction {

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
