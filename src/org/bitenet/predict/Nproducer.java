package org.bitenet.predict;

public class Nproducer extends NNet{
	NNet tester;
public Nproducer(int[] structure, int actFunc, NNet discriminator) {
	super(structure, actFunc);
	tester = discriminator;
}
@Override
public double score(NDataSet in, NDataSet goal) {
	
}
}
