package org.bitenet.predict;

import java.io.File;
import java.io.FileNotFoundException;

public class NPoly {
double[] coes;
public static final double MAX_COEFFICIENT = 100;
public static final double DERIVATIVE_STEP = .0001;
	public NPoly(int size) {
		coes = randomCoefficients(size);
		for (int i = 0; i < coes.length; i++) {
			System.out.print(coes[i]+",");
		}
		System.out.println();
		System.out.println("calculated w/ 1 = "+calculate(1));
		try {
			System.out.println("scored = " +score(new NDataSet(new File("inputs.txt")),new NDataSet(new File("inputs.txt"))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private double[] randomCoefficients(int size) {
		double[] ret = new double[size];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (2*Math.random()*MAX_COEFFICIENT)-MAX_COEFFICIENT;
		}
		return ret;
	}
	public void train(NDataSet inputs, NDataSet outputs, double lr, int steps, double err) {
		double curScore;
		int u = 0;
		while((curScore=score(inputs,outputs))>err&&u<steps) {
			double[] nCoes = new double[coes.length];
			for (int i = 0; i < nCoes.length; i++) {
				double kk = coes[i];
				coes[i]+=DERIVATIVE_STEP;
				double nScore = score(inputs,outputs);
				System.out.println("nscore = "+nScore);
				coes[i]=kk;
				nCoes[i] = coes[i]- (lr*((nScore-curScore)/DERIVATIVE_STEP));
			}
			coes = nCoes;
		}
	}
	public double error(double goal,double pred) {
		return Math.abs(goal-pred);
	}
	public double score(NDataSet inputs, NDataSet outputs) {
		double sum = 0;
		double k = 0;
		while(inputs.hasNextSet()) {
			k+=1;
			sum+=error(outputs.nextSet()[0],calculate(inputs.nextSet()[0]));
		}
		System.out.println("sum = "+sum);
		return sum/k;
	}
	public double calculate(double in) {
		double ret = 0;
		for (int i = 0; i < coes.length; i++) {
			ret+=Math.pow(in, i)*coes[i];
		}
		return ret;
	}

}
