package org.bitenet.predict;


import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.bitenet.predict.ActivationFunctions.Absolute;
import org.bitenet.predict.ActivationFunctions.Bipolar;
import org.bitenet.predict.ActivationFunctions.BipolarSigmoid;
import org.bitenet.predict.ActivationFunctions.ComplementaryLogLog;
import org.bitenet.predict.ActivationFunctions.Cosine;
import org.bitenet.predict.ActivationFunctions.HardTanh;
import org.bitenet.predict.ActivationFunctions.Identity;
import org.bitenet.predict.ActivationFunctions.LeCunTanh;
import org.bitenet.predict.ActivationFunctions.Logit;
import org.bitenet.predict.ActivationFunctions.NActivationFunction;
import org.bitenet.predict.ActivationFunctions.PiecwiseLinear;
import org.bitenet.predict.ActivationFunctions.Rectifier;
import org.bitenet.predict.ActivationFunctions.Sigmoid;
import org.bitenet.predict.ActivationFunctions.SmoothRectifier;
import org.bitenet.predict.ActivationFunctions.Step;
import org.bitenet.predict.ActivationFunctions.Tanh;


public class NNet {
	public static final int IDENTITY = 0;
	public static final int STEP = 1;
	public static final int PIECEWISE_LINEAR = 2;
	public static final int SIGMOID = 3;
	public static final int COMPLEMENTARY_LOG_LOG = 4;
	public static final int BIPOLAR = 5;
	public static final int BIPOLAR_SIGMOID = 6;
	public static final int TANH = 7;
	public static final int LECUN_TANH = 8;
	public static final int HARD_TANH = 9;
	public static final int ABSOLUTE = 10;
	public static final int RECTIFIER = 11;
	public static final int SMOOTH_RECTIFIER = 12;
	public static final int LOGIT = 13;
	public static final int COSINE = 14;
private static final double DERIVATIVE_STEP = NPoly.DERIVATIVE_STEP;
NLayer input;
NLayer output;
int[] dimensions;
NActivationFunction actFunc;
	public void prune(double tolerance) {
		ArrayList<NLayer> ls = input.getLayers(new ArrayList<NLayer>());
		for (int i = 0; i < ls.size(); i++) {
			for (int j = 0; j < ls.get(i).nodes.size(); j++) {
				ls.get(i).nodes.get(j).bias = Math.abs(ls.get(i).nodes.get(j).bias)<tolerance?0:ls.get(i).nodes.get(j).bias;
				for (int k = 0; k < ls.get(i).nodes.get(j).weights.length; k++) {
					ls.get(i).nodes.get(j).weights[k] = Math.abs(ls.get(i).nodes.get(j).weights[k])<tolerance?0:ls.get(i).nodes.get(j).weights[k];
				}
			}
		}
	}
	public NNet(int[] layersizes, int activation_function) {
		switch(activation_function) {
		case 0:
		
			actFunc = new Identity();
			break;
		case 1:
			
			actFunc = new Step();
			break;
		case 2:
			
			actFunc = new PiecwiseLinear();
			break;
		case 3:
			
			actFunc = new Sigmoid();
			break;
		case 4:
			
			actFunc =new  ComplementaryLogLog();
			break;
		case 5:
			
			actFunc = new Bipolar();
			break;
		case 6:
			
			actFunc = new BipolarSigmoid();
			break;
		case 7:
			
			actFunc = new Tanh();
			break;
		case 8:
			
			actFunc = new LeCunTanh();
			break;
		case 9:
			
			actFunc = new HardTanh();
			break;
		case 10:
			
			actFunc = new Absolute();
			break;
		case 11:
			
			actFunc = new Rectifier();
			break;
		case 12:
			
			actFunc = new SmoothRectifier();
			break;
		case 13:
			
			actFunc = new Logit();
			break;
		case 14:
			
			actFunc = new Cosine();
			break;
		}
		dimensions = layersizes;
		//build the net
		for (int i = 0; i < layersizes.length; i++) {
			if(i==0) {
				input = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
				input.nodes.add(new NNode(layersizes[i+1],actFunc));
				}
			}else if(i==layersizes.length-1) {
				output = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
				output.nodes.add(new NNode(1,new Identity()));
				}
				input.addNext(output);
			}else {
				NLayer toadd = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
					toadd.nodes.add(new NNode(layersizes[i+1],actFunc));
				}
				input.addNext(toadd);
			}
		}
	}
	public NNet(int[] dims, NLayer inLayer) {
		input = inLayer;
		dimensions = dims;
		ArrayList<NLayer> def =inLayer.getLayers(new ArrayList<NLayer>());
		output = def.get(def.size()-1);
	}
	public double[] activate(double[] in) {
		//set the input values
		for (int i = 0; i < in.length; i++) {
			input.nodes.get(i).value = in[i];
		}
		//activate the input layer
		input.activate();
		//return the result
		double[] ret = output.getValues();
		input.reset();
		return ret;
	}
	public void train(NDataSet in, NDataSet goal, double learning,double err,int time) throws FileNotFoundException {
		long start = System.currentTimeMillis();
		double score_cur;
		while((score_cur = score(in,goal))>err&&((System.currentTimeMillis()-start)<time||time==-1)) {
			System.out.println("score = "+score_cur);
		ArrayList<NLayer> lays = input.getLayers(new ArrayList<NLayer>());
		ArrayList<ArrayList<NNode>> nns = new ArrayList<ArrayList<NNode>>();
		for (int i = 0; i < lays.size(); i++) {
			nns.add(new ArrayList<NNode>());
			ArrayList<NNode> nodes = lays.get(i).nodes;
			for (int j = 0; j < nodes.size(); j++) {
				NNode toad = new NNode(nodes.get(j).weights.length,actFunc);
				double xoo = score_cur;
				nodes.get(j).bias+=DERIVATIVE_STEP;
				double xnn = score(in,goal);
				nodes.get(j).bias-=DERIVATIVE_STEP;
				toad.bias = nodes.get(j).bias-(learning*((xnn-xoo)/DERIVATIVE_STEP));
				for (int k = 0; k < nodes.get(j).weights.length; k++) {
					double xo = score_cur;
					nodes.get(j).weights[k]+=DERIVATIVE_STEP;
					double xn = score(in,goal);
					nodes.get(j).weights[k]-=DERIVATIVE_STEP;
					toad.weights[k] = nodes.get(j).weights[k]-(learning*((xn-xo)/DERIVATIVE_STEP));
				}
				nns.get(nns.size()-1).add(toad);
			}
		}
		for (int i = 0; i < lays.size(); i++) {
			lays.get(i).nodes = nns.get(i);
		}
		//System.out.println("finsihed "+u+"epochs and score = "+score(in,goal));
		}
	}
	public double error(double[] goal,double[] pred) {
		double sum = 0;
		for (int i = 0; i < pred.length; i++) {
				sum+=Math.pow(goal[i]-pred[i],2);
		}
		return sum/(goal.length);
	}

	public double score(NDataSet in, NDataSet goal) throws FileNotFoundException {
		double errSum = 0;
		double i = 0;
		while(in.hasNextSet()) {
			i++;
			errSum += error(goal.nextSet(),activate(in.nextSet()));
		}
		in.reset();
		goal.reset();
		return errSum/i;
	}
	public void print() {
		ArrayList<NLayer> ls = input.getLayers(new ArrayList<>());
		for (int i = 0; i < ls.size(); i++) {
			ls.get(i).print();
		}
		
	}


}
