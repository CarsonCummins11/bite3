package org.bitenet.predict;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.bitenet.predict.activationfunctions.NActivationFunction;
import org.bitenet.predict.errorfunctions.NCostFunction;
/*
 * Purpose: This trains a neural network
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class NNetTrainer {
public static NNet train(double dataUsage,NDataSet in, NDataSet goal, double learning, double err, int time, int maxepochs, ArrayList<ArrayList<NActivationFunction>> actfunc, NCostFunction cf, int[] dims) throws FileNotFoundException {
	int i = 0;
	double error = Double.MAX_VALUE;
	NNet cur = null;
	NNet best = null;
	while(i<maxepochs&&error>err) {
	cur = new NNet(dims,actfunc,cf,dataUsage);
	cur.train(in, goal, learning, err, time);
	double q = cur.score(in, goal);
	if(error>q) {
		best = cur;
		error = q;
	}
	i++;
	}
	return best;
}
}
