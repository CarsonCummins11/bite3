package org.bitenet.predict;

import java.util.ArrayList;

import org.bitenet.predict.activationfunctions.BipolarSigmoid;
import org.bitenet.predict.activationfunctions.Tanh;
import org.bitenet.predict.data.DataSet;

import com.rits.cloning.Cloner;

public class NBNet {
	ArrayList<ArrayList<node>> net;

	public NBNet(int[] sizes) {
		net = new ArrayList<>();
		for (int i = 0; i < sizes.length; i++) {
			net.add(new ArrayList<>());
			for (int j = 0; j < sizes[i]; j++) {
				net.get(net.size() - 1).add(new node(i + 1 < sizes.length ? sizes[i + 1] : 0));
			}
		}
	}

	public int[] activate(double[] in) {
		ArrayList<node> k = net.get(0);
		int i = 0;
		for (node n : k) {
			n.val = (int)in[i];
			i++;
		}
		i = 0;
		for (ArrayList<node> o : net) {
			if (i + 1 < net.size()) {
				for (node n : o) {
					n.activate();
					ArrayList<node> h = net.get(i + 1);
					int j = 0;
					for (node next : h) {
						next.val += (n.val * n.weights[j]);
						j++;
					}
				}
			}
			i++;
		}
		int[] ret = new int[net.get(net.size()-1).size()];
		for (int j = 0; j < ret.length; j++) {
			ret[j] = net.get(net.size()-1).get(j).val;
		}
		reset();
		return ret;
	}
	public void reset() {
		for (ArrayList<node> a : net) {
			for (node b : a) {
				b.val = 0;
			}
		}
	}
	public double scoreDiff(int[] exp, double[] act) {
		double sum = 0;
		for (int i = 0; i < act.length; i++) {
			if(exp[i]-(int)act[i]!=0) {
			sum+=Math.log(Math.abs(exp[i]-(int)act[i]));
			}
		}
		return sum/(double)exp.length;
	}
	public double score(DataSet in, DataSet out) {
		double sum = 0;
		in.reset();
		out.reset();
		int k = 0;
		while(in.hasNextSet()) {
			k++;
			sum+=1+scoreDiff(activate(in.nextSet()),out.nextSet());
		}
		out.reset();
		in.reset();
		return sum/(double)k;
	}
	public void train(DataSet in, DataSet out, double error) {
		boolean copy = false;
		double err = Double.MAX_VALUE;
		while(err>error) {
			err = score(in,out);
			System.out.println(err);
			for (ArrayList<node> a : net) {
				for (node n : a) {
					n.bias = n.bias*-1;
					double nscore = score(in,out);
					boolean shouldprune = true;
					if(nscore != err) {
						shouldprune = false;
					}
					if(nscore>err) {
						n.bias = n.bias*-1;
					}
					for (int i = 0; i < n.weights.length; i++) {
						n.weights[i] = n.weights[i]*-1;
						nscore = score(in,out);
						if(nscore == err) {
							prune(n);
						}
						if(nscore>err) {
							n.weights[i] = n.weights[i]*-1;
						}
					}
				}
			}
			double errr = score(in,out);
			if(errr == err&&err!=0) {
				System.out.println("local minimum reached, mutating");
				//mutate();
			}else {
				err = errr;
			}
		}
	}



	private void prune(node n) {
		ArrayList<node> prev = null;
		for (ArrayList<node> a : net) {
			for (node b : a) {
				if(n==b) {
					if(prev!=null) {
						for (node c : prev) {
							c = new node(a.size()-1);
						}
					}else {
						a.remove(b);
					}
				}
			}
			prev = a;
		}
		
	}

	private void mutate() {
		for (ArrayList<node> a : net) {
			for (node n : a) {
				if(Math.random()<.1) {
					if(Math.random()<.1) {
						n.bias = n.bias*-1;
					}
					for (int i = 0; i < n.weights.length; i++) {
						if(Math.random()<.1) {
							n.weights[i] = n.weights[i]*-1;
						}
					}
				}
			}
		}
		
	}

	public static class node {
		int[] weights;
		int bias;
		int val;
		public void activate() {
			val +=bias;
			val = (int) new Tanh().activate(val);
		}
		public node(int nextSize) {
			val = 0;
			weights = new int[nextSize];
			bias = Math.random() > .5 ? -1 : 1;
			for (int i = 0; i < weights.length; i++) {
				weights[i] = Math.random() > .5 ? -1 : 1;
			}
		}
	}
}
