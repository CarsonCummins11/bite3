package org.bitenet.predict;

import java.io.FileNotFoundException;

public class NPoly {
double[] coes;
public double MAX_COEFFICIENT;
public static final double DERIVATIVE_STEP = .00001;
	public NPoly(int size) {
		coes = randomCoefficients(size);
	}
	private double[] randomCoefficients(int size) {
		double[] ret = new double[size];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (2*Math.random()*MAX_COEFFICIENT)-MAX_COEFFICIENT;
		}
		return ret;
	}
	public void train(NDataSet inputs, NDataSet outputs, double lr, int time, double err) throws FileNotFoundException {
		double curScore;
		MAX_COEFFICIENT = outputs.max();
		long start = System.currentTimeMillis();
		while((curScore=score(inputs,outputs))>err&&((System.currentTimeMillis()-start)<time||time==-1)) {
			System.out.println("score = "+curScore);
			double[] nCoes = new double[coes.length];
			for (int i = 0; i < nCoes.length; i++) {
				double kk = coes[i];
				coes[i]+=DERIVATIVE_STEP;
				double nScore = score(inputs,outputs);
				coes[i]=kk;
				nCoes[i] = coes[i]- (lr*((nScore-curScore)/DERIVATIVE_STEP));
			}
			coes = nCoes;
		}
	}
	public double error(double goal,double pred) {
		return Math.abs(goal-pred);
	}
	public double score(NDataSet inputs, NDataSet outputs) throws FileNotFoundException {
		double sum = 0;
		double k = 0;
		while(inputs.hasNextSet()) {
			k+=1;
			sum+=error(outputs.nextSet()[0],calculate(inputs.nextSet()[0]));
		}
		inputs.reset();
		outputs.reset();
		return sum/k;
	}
	public double calculate(double in) {
		double ret = 0;
		for (int i = 0; i < coes.length; i++) {
			ret+=Math.pow(in, i)*coes[i];
		}
		return ret;
	}
	public void print() {
		System.out.print("y = ");
		for (int i = coes.length-1; i >-1; i--) {
			System.out.print(coes[i]+"(x^"+i+") +");
		}
		System.out.println();
	}

}
