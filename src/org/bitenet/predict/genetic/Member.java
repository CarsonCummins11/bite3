package org.bitenet.predict.genetic;

import org.bitenet.predict.NDataSet;

public interface Member<T extends Member<T>> {
//should return 2 kids
public T[] breed(T m);
public double score(NDataSet in, NDataSet goal);
public T random();
public void mutate();
public void print();
}
