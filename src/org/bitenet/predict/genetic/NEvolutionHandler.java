package org.bitenet.predict.genetic;

import org.bitenet.predict.data.DataSet;
/*
 * Purpose: This runs a genetic algorithm
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class NEvolutionHandler {

public static <T extends Member<T>> T train(DataSet inDat, DataSet outDat,int populationSize, int maxGenerations,float crossoverRatio,float elitismRatio,float mutationRatio, double err, T parent){
	NPopulation<T> pop = new NPopulation<>(populationSize, crossoverRatio, elitismRatio, mutationRatio,parent,inDat,outDat);
	// Start evolving the population, stopping when the maximum number of
	// generations is reached, or when we find a solution.
	int i = 0;
	Holder<T> best = pop.getPopulation().get(0);
	long sum = 0;
	while ((i++ <= maxGenerations) && (best.getScore() != 0)) {
		System.out.println("Generation " + i + ": " + best.getScore());
		if(best.getScore()<err) {
			break;
		}
		best.getMember().print();
		long start = System.currentTimeMillis();
		pop.evolve();
		best = pop.getPopulation().get(0);
		sum+=System.currentTimeMillis()-start;
	}
	System.out.println("average generation time = "+sum/i);
	return best.getMember();
}

}
