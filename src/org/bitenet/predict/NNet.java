package org.bitenet.predict;


import java.io.FileNotFoundException;
import java.util.ArrayList;


public class NNet {
private static final double DERIVATIVE_STEP = .000001;
NLayer input;
NLayer output;
int[] dimensions;
	public NNet(int[] layersizes) {
		dimensions = layersizes;
		//build the net
		for (int i = 0; i < layersizes.length; i++) {
			if(i==0) {
				input = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
				input.nodes.add(new NNode(layersizes[i+1]));
				}
			}else if(i==layersizes.length-1) {
				output = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
				output.nodes.add(new NNode(1));
				}
				input.addNext(output);
			}else {
				NLayer toadd = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
					toadd.nodes.add(new NNode(layersizes[i+1]));
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
	public void train(NDataSet in, NDataSet goal, double learning,double err,int steps) throws FileNotFoundException {
		int u = 0;
		double score_cur;
		while((score_cur = score(in,goal))>err&&u<steps) {
		u++;
		ArrayList<NLayer> lays = input.getLayers(new ArrayList<NLayer>());
		ArrayList<ArrayList<NNode>> nns = new ArrayList<ArrayList<NNode>>();
		for (int i = 0; i < lays.size(); i++) {
			nns.add(new ArrayList<NNode>());
			ArrayList<NNode> nodes = lays.get(i).nodes;
			for (int j = 0; j < nodes.size(); j++) {
				NNode toad = new NNode(nodes.get(j).weights.length);
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
