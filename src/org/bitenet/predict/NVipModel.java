package org.bitenet.predict;

import java.awt.Point;
import java.io.FileNotFoundException;

import org.bitenet.predict.genetic.Member;
import org.bitenet.predict.genetic.NEvolutionHandler;

public class NVipModel implements Member<NVipModel>{
Point[] inputPositions;
int[] dimensions;
Nvipnet net;
public static final double err = .1;
public static final double learning = .01;
double MUTATION_CHANCE = .3;
	public NVipModel(int inSize, int outSize) {
		dimensions = randDims(inSize,outSize);
		inputPositions = randPoints(inSize);
		
	}
	public static NVipModel train(NDataSet in, NDataSet goal, double err) throws FileNotFoundException {
		int inLength = in.nextSet().length;
		in.reset();
		int goalLength = goal.nextSet().length;
		goal.reset();
		return NEvolutionHandler.train(in,goal,NModel.STARTINGPOOL,NModel.GENERATIONS,NModel.CROSSOVER,NModel.ELITISM,NModel.MUTATION,err,new NVipModel(inLength,goalLength));
	}
	public NVipModel(Point[] inPos, int[] dims) {
		inputPositions = inPos;
		dimensions = dims;
	}
	public Point[] randPoints(int inputSize) {
		int maxX = dimensions.length-2;
		Point[] ps = new Point[inputSize];
		for (int i = 0; i < inputSize; i++) {
		Point p = new Point();
		p.x = (int)Math.round(Math.random()*maxX);
		/*
		 * Might have to add a -1 at the end of this line to ensure we stay within array boundaries
		 */
		p.y = (int)Math.round(Math.random()*dimensions[p.x]);
		}
		return ps;
	}
	public int[] randDims(int inSize, int outSize) {
		int retSum = -1;
		int[] ret = null;
		while(retSum<inSize) {
		retSum = 0;
		ret =new int[(int)Math.round(NModel.MAX_WIDTH*Math.random())];
		for (int i = 0; i < ret.length-1; i++) {
			ret[i] = (int)Math.round(NModel.MAX_HEIGHT*Math.random());
			retSum+=ret[i];
		}
		}
		ret[ret.length-1] = outSize;
		return ret;
	}
	@Override
	public NVipModel[] breed(NVipModel m) {
		Point[] p1 = new Point[inputPositions.length];
		Point[] p2 = new Point[inputPositions.length];
		double we = 2*Math.random();
		double we2 = 2-we;
		int[] d1 = new int[(int)Math.round((we*dimensions.length+we2*m.dimensions.length)/2)];
		int[] d2 = new int[(int)Math.round((we2*dimensions.length+we*m.dimensions.length)/2)];
		for (int i = 0; i < p1.length; i++) {
			we = 2*Math.random();
			we2 = 2-we;
			p1[i] = new Point();
			p1[i].x = (int)Math.round((we*inputPositions[i].x+we2*m.inputPositions[i].x)/2);
			p2[i].x = (int)Math.round((we2*inputPositions[i].x+we*m.inputPositions[i].x)/2);
			p1[i].y = (int)Math.round((we*inputPositions[i].y+we2*m.inputPositions[i].y)/2);
			p2[i].y = (int)Math.round((we2*inputPositions[i].y+we*m.inputPositions[i].y)/2);
			}
		for (int i = 0; i < (d2.length>d1.length?d2.length:d1.length); i++) {
			we = 2*Math.random();
			we2 = 2-we;
			if(i<d2.length&&i<d1.length) {
				if(i<dimensions.length&&i<m.dimensions.length) {
					d2[i] = (int)Math.round((we*dimensions[i]+we2*m.dimensions[i])/2);
					d1[i] = (int)Math.round((we2*dimensions[i]+we*m.dimensions[i])/2);
				}else if(i<dimensions.length) {
					d2[i] = dimensions[i];
					d1[i] = dimensions[i];
				}else if(i<m.dimensions.length) {
					d2[i] = m.dimensions[i];
					d1[i] = m.dimensions[i];
				}
			}else if(i<d2.length) {
				if(i<dimensions.length&&i<m.dimensions.length) {
					d2[i] = (int)Math.round((we*dimensions[i]+we2*m.dimensions[i])/2);
				}else if(i<dimensions.length) {
					d2[i] = dimensions[i];
				}else if(i<m.dimensions.length) {
					d2[i] = m.dimensions[i];
				}
			}else if(i<d1.length) {
				if(i<dimensions.length&&i<m.dimensions.length) {
					d1[i] = (int)Math.round((we2*dimensions[i]+we*m.dimensions[i])/2);
				}else if(i<dimensions.length) {
					d1[i] = dimensions[i];
				}else if(i<m.dimensions.length) {
					d1[i] = m.dimensions[i];
				}
			}
		}
		return new NVipModel[] {new NVipModel(p1, d1),new NVipModel(p2,d2)};
	}

	@Override
	public double score(NDataSet in, NDataSet goal) {
		net = new Nvipnet(dimensions,inputPositions);
		try {
			net.train(in, goal, learning, err, NModel.max_steps);
			return net.score(in, goal);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return Double.MAX_VALUE;
		}
	}

	@Override
	public NVipModel random() {
		return new NVipModel(inputPositions.length,dimensions[dimensions.length-1]);
	}

	@Override
	public void mutate() {
		for (int i = 0; i < dimensions.length-1; i++) {
			if(Math.random()<MUTATION_CHANCE) {
				dimensions[i] = (int)Math.round(((Math.random()*2)-1)+dimensions[i]);
			}
		}
		for (int i = 0; i < inputPositions.length; i++) {
			if(Math.random()<MUTATION_CHANCE) {
				int nex = (int)Math.round(((Math.random()*2)-1)+inputPositions[i].x);
				inputPositions[i].x =nex>0&&nex<dimensions[i]?nex:inputPositions[i].x;
			}
			if(Math.random()<MUTATION_CHANCE) {
				int nex = (int)Math.round(((Math.random()*2)-1)+inputPositions[i].y);
				inputPositions[i].y =nex>0&&nex<dimensions[i]?nex:inputPositions[i].y;
			}
		}
		
	}

	@Override
	public void print() {
		System.out.println("the required print method was implemented for a vipnet model");
		
	}

}
