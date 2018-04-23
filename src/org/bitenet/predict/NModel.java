package org.bitenet.predict;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.bitenet.predict.activationfunctions.NActivationFunction;
import org.bitenet.predict.errorfunctions.NCostFunction;
import org.bitenet.predict.genetic.Member;
import org.bitenet.predict.genetic.NEvolutionHandler;
import org.bitenet.predict.activationfunctions.*;
import org.bitenet.predict.errorfunctions.*;

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
public static int MAXEPOCHS = 10;
public int[] dimensions;
public double lr;
public ArrayList<ArrayList<NActivationFunction>> actFunc;
public NCostFunction costFunc;
NNet contained;
	public NModel(int inputSize, int outputSize) {
		randomDimensions(inputSize,outputSize);
		lr = Math.random()/4;
		actFunc = randomActFuncs(dimensions);
		costFunc = randomCostFunc();
	}
	public NCostFunction randomCostFunc() {
		int choice = (int)Math.floor(Math.random()*6);
		switch(choice) {
		case 0:
			return new Bregman();
		case 1:
			return new CrossEntropy();
		case 2:
			return new Hellinger();
		case 3:
			return new ItakuraSaito();
		case 4:
			return new KullbackLeibler();
		case 5:
			return new Quadratic();
		default: 
			return null;
		}
		
	}
	public static ArrayList<ArrayList<NActivationFunction>> randomActFuncs(int[] nums) {
		ArrayList<ArrayList<NActivationFunction>> ret = new ArrayList<>();
		for (int i = 0; i < nums.length; i++) {
			ret.add(new ArrayList<NActivationFunction>());
			for (int j = 0; j < nums[i]; j++) {
			ret.get(i).add(randomActFunc());
			}
		}
		return ret;
	}
	public static NActivationFunction randomActFunc() {
		int choice = (int)Math.floor(Math.random()*15);
		switch(choice) {
		case 0:
			return new Identity();
		case 1:
			return new Step();
		case 2:
			return new PiecewiseLinear();
		case 3:
			return new Sigmoid();
		case 4:
			return new  ComplementaryLogLog();
		case 5:
			return new Bipolar();
		case 6:
			return new BipolarSigmoid();
		case 7:
			return new Tanh();
		case 8:
			return new LeCunTanh();
		case 9:
			return new HardTanh();
		case 10:
			return new Absolute();
		case 11:
			return new Rectifier();
		case 12:
			return new SmoothRectifier();
		case 13:
			return new Logit();
		case 14:
			return new Cosine();
		default:
			return null;
		}
	}
	public NModel(int[] dims, double lnr, ArrayList<ArrayList<NActivationFunction>> af,NCostFunction cf) {
		dimensions = dims;
		lr = lnr;
		actFunc = af;
		costFunc = cf;
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
		double weights= 2*Math.random();
		ArrayList<ArrayList<NActivationFunction>> af = new ArrayList<>();
		int ii = 0;
		for(ArrayList<NActivationFunction> q : actFunc) {
			af.add(new ArrayList<NActivationFunction>());
			int jj = 0;
			for (NActivationFunction r : q) {
				af.get(ii).add(.5<Math.random()?r:m.actFunc.get(ii).get(jj));
				jj++;
			}
			ii++;
		}
		return new NModel(ret,(weights*lr+(2-weights)*lr)/2,af,.5<Math.random()?costFunc:m.costFunc);
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
	public double[] activate(double[] in) {
		return contained.activate(in);
	}
	@Override
	public double score(NDataSet in, NDataSet goal) {
		try {
			contained = NNetTrainer.train(in, goal, lr, required_error, max_steps,10, actFunc, costFunc, dimensions);
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
