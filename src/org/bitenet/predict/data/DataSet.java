package org.bitenet.predict.data;

public interface DataSet {
//the number of entries in the dataset
public int numEntries();
public double max();
public boolean hasNextSet();
public double[] nextSet();
public double min();
public void reset();


}
