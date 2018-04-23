package org.bitenet.predict;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.bitenet.predict.activationfunctions.Identity;
import org.bitenet.predict.activationfunctions.NActivationFunction;
import org.bitenet.predict.errorfunctions.NCostFunction;

import com.rits.cloning.Cloner;

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
	NCostFunction costFunc;

	public void prune(double tolerance) {
		ArrayList<NLayer> ls = input.getLayers(new ArrayList<NLayer>());
		for (int i = 0; i < ls.size(); i++) {
			for (int j = 0; j < ls.get(i).nodes.size(); j++) {
				ls.get(i).nodes.get(j).bias = Math.abs(ls.get(i).nodes.get(j).bias) < tolerance ? 0
						: ls.get(i).nodes.get(j).bias;
				for (int k = 0; k < ls.get(i).nodes.get(j).weights.length; k++) {
					ls.get(i).nodes.get(j).weights[k] = Math.abs(ls.get(i).nodes.get(j).weights[k]) < tolerance ? 0
							: ls.get(i).nodes.get(j).weights[k];
				}
			}
		}
	}

	public NNet(int[] layersizes, NActivationFunction func, NCostFunction cfunc) {
		actFunc = func;
		costFunc = cfunc;
		dimensions = layersizes;
		// build the net
		NLayer prevv = null;
		for (int i = 0; i < layersizes.length; i++) {
			if (i == 0) {
				input = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
					input.nodes.add(new NNode(null,i,j,prevv,layersizes[i + 1], actFunc));
				}
				prevv = input;
			} else if (i == layersizes.length - 1) {
				output = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
					output.nodes.add(new NNode(null,i,j,prevv,1, new Identity()));
				}
				input.addNext(output);
			} else {
				NLayer toadd = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
					toadd.nodes.add(new NNode(null,i,j,prevv,layersizes[i + 1], actFunc));
				}
				input.addNext(toadd);
				prevv = toadd;
			}
		}
		ArrayList<NLayer> rep = input.getLayers(new ArrayList<NLayer>());
		int i = 0;
		for(NLayer l : rep) {
			for (NNode n:l.nodes) {
				n.connections = i+1<rep.size()?rep.get(i+1).nodes:null;
			}
			i++;
		}
	}

	public NNet(int[] dims, NLayer inLayer, NActivationFunction func, NCostFunction cfunc) {
		input = inLayer;
		dimensions = dims;
		actFunc = func;
		costFunc = cfunc;
		ArrayList<NLayer> def = inLayer.getLayers(new ArrayList<NLayer>());
		output = def.get(def.size() - 1);
	}

	public double[] activate(double[] in) {
		// set the input values
		for (int i = 0; i < in.length; i++) {
			input.nodes.get(i).value = in[i];
		}
		// activate the input layer
		input.activate();
		// return the result
		double[] ret = output.getValues();
		input.reset();
		return ret;
	}

	public void train(NDataSet in, NDataSet goal, double learning, double err, int time) throws FileNotFoundException {
		//get the start time of the training
		long start = System.currentTimeMillis();
		
		//initialize variable to hold the current score 
		double score_cur;
		
		/*start looping, breaking conditions
		 * current score is still greater than the error required
		 * the amount of time allowed for training is greater than the amount of time spent or time allowed = -1
		 */
		while ((score_cur = score(in, goal)) > err && ((System.currentTimeMillis() - start) < time || time == -1)) {
			
			//output the current score
			System.out.println("score = " + score_cur);
			
			//calculate the score using the gradient of the cost function
			double gScore = gradientCost(in,goal);
			
			//get the layers of the current network
			ArrayList<NLayer> lays = input.getLayers(new ArrayList<NLayer>());
			
			//create a new matrix of nodes representing the new network
			ArrayList<ArrayList<NNode>> nns = new ArrayList<ArrayList<NNode>>();
			
			//iterate through the old network
			for (int i = 0; i < lays.size(); i++) {
				
				//add a new layer to the matrix of new nodes being created
				nns.add(new ArrayList<NNode>());
				
				//the old layer list of nodes is encapsulated into a different var
				ArrayList<NNode> nodes = lays.get(i).nodes;
				
				
				//iterate over the old layers node list
				for (int j = 0; j < nodes.size(); j++) {
					
					//clone the current node in that list to edit it to add to the next level of network
					NNode toad = new Cloner().deepClone(nodes.get(j));
					
					//increment the variable to be derived
					nodes.get(j).bias+=DERIVATIVE_STEP;
					
					//rescore the network
					double xnn = score(in,goal);
					

					//undo the incrementing
					nodes.get(j).bias-=DERIVATIVE_STEP;
					
					//the variable representing the derivative
					double pDeriv = 0;
					
					//calculate the derivatives at all the input locations
					//while the input data set has another set of inputs
					while(in.hasNextSet()) {
						
						//for the output nodes
						for (int n = 0; n < output.nodes.size(); n++) {
							
							//add the derivative at each point to pderiv
								pDeriv+=output.nodes.get(n).derivative(input,toad.myLayer,toad.myIndex,in.nextSet());		
						}
					}
					
					//estimate the derivative by inputting the current score, incrementing the variable to be derived and calculating the slope using the result
					//xoo is the original score
					double xoo = score_cur;
					
					
					//chain rule for the gradient score
					pDeriv = pDeriv*gScore;
					
					//output results of derivative calculation
					System.out.println("calculated derivative: "+pDeriv);
					
					//output estimation
					System.out.println(xnn);
					System.out.println(xoo);
					System.out.println("estimated derivative: "+(xnn-xoo)/DERIVATIVE_STEP);
					
					//reset the dataset
					in.reset();
					
					//do gradient descent
					toad.bias = nodes.get(j).bias - (learning * pDeriv);
					
					//loop through all the weights
					for (int k = 0; k < (j-1>0?nodes.get(j-1).weights.length:0); k++) {
						
						//reset pderiv
						pDeriv = 0;
						
						//loop through inputs
						while(in.hasNextSet()) {
							
							//find derivative with resoect to each node
							for (int n = 0; n < output.nodes.size(); n++) {
								
								//add derivative of each node to pderiv
									pDeriv+=output.nodes.get(n).derivative(input,toad.myLayer,toad.myIndex,k,in.nextSet());		
							}
						}
						
						//multiply nodes by gradient score for chain rule
						pDeriv = pDeriv*gScore;
						
						//estimate derivative
						double xo = score_cur;
						
						//increment weight
						nodes.get(j).weights[k]+=DERIVATIVE_STEP;
						
						//calculate score at new point
						double xn = score(in,goal);
						
						//undo incrementing of that weight
						nodes.get(j).weights[k]-=DERIVATIVE_STEP;
						/*
						//output results of derivative calculation
						System.out.println("calculated derivative: "+pDeriv);
						
						//output estimation
						System.out.println("estimated derivative: "+(xn-xo)/DERIVATIVE_STEP);
						*/
						//reset the database
						in.reset();
							
						//do gradient descent
						toad.weights[k] = nodes.get(j).weights[k] - (learning * pDeriv);
					}
					
					//add in the newly calculated node
					nns.get(nns.size() - 1).add(toad);
				}
			}
			
			//build the new network
			for (int i = 0; i < lays.size(); i++) {
				lays.get(i).nodes = nns.get(i);
			}
		}
	}

	private double gradientCost(NDataSet in, NDataSet goal) throws FileNotFoundException {
		double errSum = 0;
		double i = 0;
		while (in.hasNextSet()) {
			i++;
			errSum += gradient(goal.nextSet(), activate(in.nextSet()));
		}
		in.reset();
		goal.reset();
		return errSum / i;
	}

	public double error(double[] goal, double[] pred) {
		return costFunc.error(goal, pred);
	}
	public double gradient(double[] goal, double[] pred) {
		return costFunc.gradient(goal, pred);
	}

	public double score(NDataSet in, NDataSet goal) throws FileNotFoundException {
		double errSum = 0;
		double i = 0;
		while (in.hasNextSet()) {
			i++;
			errSum += error(goal.nextSet(), activate(in.nextSet()));
		}
		in.reset();
		goal.reset();
		return errSum / i;
	}

	public void print() {
		ArrayList<NLayer> ls = input.getLayers(new ArrayList<>());
		for (int i = 0; i < ls.size(); i++) {
			ls.get(i).print();
		}

	}

}
