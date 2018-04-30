package org.bitenet.predict;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

import org.bitenet.predict.activationfunctions.NActivationFunction;
import org.bitenet.predict.errorfunctions.NCostFunction;

import com.rits.cloning.Cloner;
/*
 * Purpose: implementation of Neural Net
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class NNet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7580444509262611233L;
	NLayer input;
	public NLayer output;
	int[] dimensions;
	NCostFunction costFunc;
	public double testPer;
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

	public NNet(int[] layersizes, ArrayList<ArrayList<NActivationFunction>> func, NCostFunction cfunc, double testRetention) {
		costFunc = cfunc;
		dimensions = layersizes;
		testPer = testRetention;
		// build the net
		NLayer prevv = null;
		for (int i = 0; i < layersizes.length; i++) {
			if (i == 0) {
				input = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
					input.nodes.add(new NNode(null, i, j, prevv, layersizes[i + 1], func.get(i).get(j)));
				}
				prevv = input;
			} else if (i == layersizes.length - 1) {
				output = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
					output.nodes.add(new NNode(null, i, j, prevv, -1, func.get(i).get(j)));
				}
				input.addNext(output);
			} else {
				NLayer toadd = new NLayer();
				for (int j = 0; j < layersizes[i]; j++) {
					toadd.nodes.add(new NNode(null, i, j, prevv, layersizes[i + 1], func.get(i).get(j)));
				}
				input.addNext(toadd);
				prevv = toadd;
			}
		}
		ArrayList<NLayer> rep = input.getLayers(new ArrayList<NLayer>());
		int i = 0;
		for (NLayer l : rep) {
			for (NNode n : l.nodes) {
				n.connections = i + 1 < rep.size() ? rep.get(i + 1).nodes : null;
			}
			i++;
		}
	}

	public NNet(int[] dims, NLayer inLayer, NCostFunction cfunc) {
		input = inLayer;
		dimensions = dims;
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
		// get the start time of the training
		long start = System.currentTimeMillis();
		int size = (int) in.numEntries();
		// initialize variable to hold the current score
		double score_cur;
		double score_last = -1;
		/*
		 * start looping, breaking conditions current score is still greater than the
		 * error required the amount of time allowed for training is greater than the
		 * amount of time spent or time allowed = -1
		 */
		while ((score_cur = score(in, goal)) > err && ((System.currentTimeMillis() - start) < time || time == -1)) {
			if (score_cur == score_last) {
				break;
			}
			// output the current score
			System.out.println("score = " + score_cur);

			// calculate the score using the gradient of the cost function
			double gScore = gradientCost(in, goal);

			// get the layers of the current network
			ArrayList<NLayer> lays = input.getLayers(new ArrayList<NLayer>());

			// create a new matrix of nodes representing the new network
			ArrayList<ArrayList<NNode>> nns = new ArrayList<ArrayList<NNode>>();

			// iterate through the old network
			for (int i = 0; i < lays.size(); i++) {

				// add a new layer to the matrix of new nodes being created
				nns.add(new ArrayList<NNode>());

				// the old layer list of nodes is encapsulated into a different var
				ArrayList<NNode> nodes = lays.get(i).nodes;

				// iterate over the old layers node list
				for (int j = 0; j < nodes.size(); j++) {

					// clone the current node in that list to edit it to add to the next level of
					// network
					NNode toad = new Cloner().deepClone(nodes.get(j));

					// the variable representing the derivative
					double pDeriv = 0;

					// calculate the derivatives at all the input locations
					// while the input data set has another set of inputs
					int qq = 0;
					while (in.hasNextSet()&&qq<(testPer*size)) {

						// for the output nodes
						for (int n = 0; n < output.nodes.size(); n++) {

							// add the derivative at each point to pderiv
							pDeriv += output.nodes.get(n).derivative(toad.myLayer, toad.myIndex, in.nextSet());
						}
					}

					// chain rule for the gradient score
					pDeriv = pDeriv * gScore;

					// reset the dataset
					in.reset();

					// do gradient descent
					toad.bias = nodes.get(j).bias - (learning * pDeriv);

					// loop through all the weights
					for (int k = 0; k < (j - 1 > 0 ? nodes.get(j - 1).weights.length : 0); k++) {

						// reset pderiv
						pDeriv = 0;

						// loop through inputs
						qq = 0;
						while (in.hasNextSet()&&qq<(testPer*size)) {

							// find derivative with respect to each node
							for (int n = 0; n < output.nodes.size(); n++) {

								// add derivative of each node to pderiv
								pDeriv += output.nodes.get(n).derivative(toad.myLayer, toad.myIndex, k, in.nextSet());
							}
						}

						// multiply nodes by gradient score for chain rule
						pDeriv = pDeriv * gScore;

						// reset the database
						in.reset();

						// do gradient descent
						toad.weights[k] = nodes.get(j).weights[k] - (learning * pDeriv);
					}

					// add in the newly calculated node
					nns.get(nns.size() - 1).add(toad);
				}
			}

			// build the new network
			for (int i = 0; i < lays.size(); i++) {
				lays.get(i).nodes = nns.get(i);
			}
			score_last = score_cur;
		}
	}

	private double gradientCost(NDataSet in, NDataSet goal) throws FileNotFoundException {
		in.reset();
		goal.reset();
		double errSum = 0;
		double i = 0;
		while(in.hasNextSet()&&i<in.numEntries()*testPer&&i<goal.numEntries()*testPer) {
			i++;
			in.nextSet();
		}
		i=0;
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
