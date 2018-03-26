package org.bitenet.predict.genetic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.bitenet.predict.NDataSet;

import com.rits.cloning.Cloner;

public class NPopulation <T extends Member<T>>{

	/** The size of the tournament. */
	private static final int TOURNAMENT_SIZE = 3;
	private static final Random rand = new Random(System.currentTimeMillis());
	private float elitism;
	private float mutation;
	private float crossover;
	private ArrayList<Holder<T>> popArr;
	NDataSet inDat;
	NDataSet outDat;
	public NPopulation(int size, float crossoverRatio, float elitismRatio, float mutationRatio, T parent,NDataSet inputs, NDataSet outputs) {
		
		this.crossover = crossoverRatio;
		this.elitism = elitismRatio;
		this.mutation = mutationRatio;
		this.inDat = inputs;
		this.outDat = outputs;
		// Generate an initial population
		this.popArr = new ArrayList<Holder<T>>();
		for (int i = 0; i < size; i++) {
			this.popArr.add(new Holder<T>(parent.random(),inDat,outDat));
		}
		long strt = System.currentTimeMillis();
		System.out.println("starting initial score");
		popArr = sort(popArr);
		System.out.println("finished initial sort time = "+(System.currentTimeMillis()-strt));
	}

	/**
	 * Method used to evolve the population.
	 */
	public void evolve() {
		long strt = System.currentTimeMillis();
		// Create a buffer for the new generation
		ArrayList<Holder<T>> buffer = new ArrayList<Holder<T>>();
		for (int i = 0; i < popArr.size(); i++) {
			buffer.add(null);
		}
		// Copy over a portion of the population unchanged, based on 
		// the elitism ratio.
		int idx = Math.round(popArr.size() * elitism);
		for (int i = 0; i < idx+1; i++) {
			buffer.set(i, popArr.get(i));
		}
		// Iterate over the remainder of the population and evolve as 
		// appropriate.
		while (idx < buffer.size()) {
			// Check to see if we should perform a crossover. 
			if (rand.nextFloat() <= crossover) {
				
				// Select the parents and mate to get their children
				ArrayList<Holder<T>> parents = selectParents();
				ArrayList<Holder<T>> children = parents.get(0).breed(parents.get(1));
				
				// Check to see if the first child should be mutated.
				if (rand.nextFloat() <= mutation) {
					buffer.set(idx++,children.get(0).mutate());
				} else {
					buffer.set(idx++,children.get(0));
				}
				
				// Repeat for the second child, if there is room.
				if (idx < buffer.size()) {
					if (rand.nextFloat() <= mutation) {
						buffer.set(idx,children.get(1).mutate());
					} else {
						buffer.set(idx,children.get(1));
					}
				}
			} else { // No crossover, so copy verbatium.
				// Determine if mutation should occur.
				if (rand.nextFloat() <= mutation) {
					buffer.set(idx,popArr.get(idx).mutate());
				} else {
					buffer.set(idx,popArr.get(idx));
				}
			}
			
			// Increase our counter
			++idx;
		}
		System.out.println("breeding/mutation time = " + (System.currentTimeMillis()-strt));
strt = System.currentTimeMillis();
		// Sort the buffer based on fitness.
		buffer = sort(buffer);
		
		// Reset the population
		popArr = buffer;
	}
	

	private ArrayList<Holder<T>> sort(ArrayList<Holder<T>> in) {
		//score the current population
				for (int i = 0; i < popArr.size(); i++) {
					popArr.get(i).score();
				}
		Cloner c = new Cloner();
		ArrayList<Holder<T>> ret = c.deepClone(in);
	    /*    
		int n = ret.size();
	        for (int i = 0; i < n-1; i++) {
	            for (int j = 0; j < n-i-1; j++) {
	                if (ret.get(j).getScore() > ret.get(j+1).getScore())
	                {
	                    Holder<T> temp = ret.get(j);
	                    ret.set(j,ret.get(j+1));
	                    ret.set(j+1,temp);
	                }
	            }
	        }
	        */
		Collections.sort(ret);
		return ret;
	}

	public ArrayList<Holder<T>> getPopulation() {
		Cloner cloner=new Cloner();
		return cloner.deepClone(popArr);
	}
	

	public float getElitism() {
		return elitism;
	}

	
	public float getCrossover() {
		return crossover;
	}

	
	public float getMutation() {
		return mutation;
	}

	private ArrayList<Holder<T>> selectParents() {
		ArrayList<Holder<T>> parents = new ArrayList<Holder<T>>();
for (int i = 0; i < 2; i++) {
	parents.add(null);
}
		// Randomly select two parents via tournament selection.
		for (int i = 0; i < 2; i++) {
			parents.set(i, popArr.get(rand.nextInt(popArr.size())));
			for (int j = 0; j < TOURNAMENT_SIZE; j++) {
				int idx = rand.nextInt(popArr.size());
				if (popArr.get(idx).compareTo(parents.get(i)) < 0) {
					parents.set(i,popArr.get(idx));
				}
			}
		}
		
		return parents;
	}
}