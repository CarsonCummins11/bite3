package org.bitenet.predict.errorfunctions;

public class Quadratic implements NCostFunction{
	@Override
	public double error(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum+=.5*Math.pow(act[i]-exp[i],2);
		}
		return sum/act.length;
	}

	@Override
	public double gradient(double[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			sum+=act[i]-exp[i];
		}
		return sum/act.length;
	}

}
