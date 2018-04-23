package org.bitenet.predict.errorfunctions;

public class KullbackLeibler implements NCostFunction{

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
