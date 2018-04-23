package org.bitenet.predict.activationfunctions;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.bitenet.predict.NDataSet;
import org.bitenet.predict.NNet;
import org.bitenet.predict.errorfunctions.NCostFunction;

public class NNetTrainer {
public static NNet train(NDataSet in, NDataSet goal, double learning, double err, int time, int maxepochs, ArrayList<ArrayList<NActivationFunction>> actfunc, NCostFunction cf, int[] dims) throws FileNotFoundException {
	int i = 0;
	double error = Double.MAX_VALUE;
	NNet cur = null;
	while(i<maxepochs&&error>err) {
	cur = new NNet(dims,actfunc,cf);
	cur.train(in, goal, learning, err, time);
	error = cur.score(in, goal);
	i++;
	}
	return cur;
}
}
