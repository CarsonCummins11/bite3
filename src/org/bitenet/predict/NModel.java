package org.bitenet.predict;

import java.io.FileNotFoundException;

import org.bitenet.predict.genetic.Member;
import org.bitenet.predict.genetic.NEvolutionHandler;

public class NModel implements Member<NModel>{
public static final int MAX_WIDTH = 5;
public static final int MAX_HEIGHT = 5;
public static int GENERATIONS = 50;
public static int STARTINGPOOL = 20;
public static float ELITISM = 0.1f;
public static float CROSSOVER = 0.8f;
public static float MUTATION = 0.03f;
public static double required_error = .1;
public static int max_steps = 1000;
public int[] dimensions;
public double lr;
NNet contained;
	public NModel(int inputSize, int outputSize) {
		randomDimensions(inputSize,outputSize);
		lr = Math.random()/4;
	}
	public NModel(int[] dims, double lnr) {
		dimensions = dims;
		lr = lnr;
	}
public void randomDimensions(int inSize, int outSize) {
	int len = 2+(int)Math.round(Math.random()*(MAX_WIDTH-2));
	dimensions = new int[len];
	dimensions[0] = inSize;
	dimensions[dimensions.length-1] = outSize;
	for (int i = 1; i < dimensions.length-1; i++) {
		dimensions[i] = (int)Math.round((Math.random()*MAX_HEIGHT));
	}
	
}
	@Override
	public void mutate() {
		int mutind = (int)Math.round(Math.random()*(dimensions.length-1));
		dimensions[mutind] = dimensions[mutind]+(int)Math.round((2*Math.random())-1);
		if(.9<Math.random()) {
			if(lr>.01) {
			lr+=(Math.random()*.02)-.01;
			}
		}
	}

	@Override
	public NModel[] breed(NModel m) {
		return new NModel[] {breed1(m),breed1(m)};
	}
	public NModel breed1(NModel m) {
		int len = .5<Math.random()?dimensions.length:m.dimensions.length;
		int[] ret = new int[len];
		ret[0] = dimensions[0];
		ret[ret.length-1] = dimensions[dimensions.length-1];
		for (int i = 1; i < ret.length-1; i++) {
			if(i<dimensions.length&&i<m.dimensions.length) {
				ret[i] = .5<Math.random()?m.dimensions[i]:dimensions[i];
			}else if(i<dimensions.length) {
				ret[i] = dimensions[i];
			}else if(i<m.dimensions.length) {
				ret[i] = m.dimensions[i];
			}
		}
		double lnr = .5<Math.random()?lr:m.lr;
		return new NModel(ret,lnr);
	}
	public static NModel train(NDataSet in, NDataSet goal, double err) throws FileNotFoundException {
		int inLength = in.nextSet().length;
		in.reset();
		int goalLength = goal.nextSet().length;
		goal.reset();
		return NEvolutionHandler.train(in,goal,STARTINGPOOL,GENERATIONS,CROSSOVER,ELITISM,MUTATION,err,new NModel(inLength,goalLength));
	}
	@Override
	public NModel random() {
		return new NModel(dimensions[0],dimensions[dimensions.length-1]);
	}
	public double[] calculate(double[] in) {
		return contained.activate(in);
	}
	@Override
	public double score(NDataSet in, NDataSet goal) {
		try {
			contained = new NNet(dimensions);
			contained.train(in, goal, lr,required_error,max_steps);
			return contained.score(in,goal);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
	@Override
	public void print() {
		for (int i = 0; i < dimensions.length; i++) {
			System.out.print(dimensions[i]+",");
		}
		System.out.println();
	}

}
