package org.bitenet.predict.genetic;

import java.util.ArrayList;

import org.bitenet.predict.NDataSet;

import com.rits.cloning.Cloner;

public class Holder<T extends Member<T>> implements Comparable<Holder<T>>{
protected T myMem;
protected double myScore;
NDataSet inDat;
NDataSet outDat;
public boolean complete = false;
	public Holder( T mem,NDataSet inputs, NDataSet outputs) {
		myMem = mem;
		inDat = inputs;
		outDat = outputs;
	}
public T getMember(){
	return myMem;
}
public double getScore() {
	return myScore;
}
public ArrayList<Holder<T>> breed(Holder<T> in) {
	ArrayList<Holder<T>> ret = new ArrayList<Holder<T>>();
	T[] p = myMem.breed(in.getMember());
	for (int i = 0; i < p.length; i++) {
		ret.add(new Holder<T>(p[i],inDat,outDat));
	}
	return ret;
}
public Holder<T> mutate() {
	Cloner cloner=new Cloner();
	Holder<T> clone = cloner.deepClone(this);
	Holder<T> temp = new Holder<T>(clone.getMember(),inDat,outDat);
	temp.myMem.mutate();
	return temp;
}
public int compareTo(Holder<T> in) {
	if (getScore() < in.getScore()) {
		return -1;
	} else if (getScore() > in.getScore()) {
		return 1;
	}
	
	return 0;
}
public void score() {
	myScore = myMem.score(inDat, outDat);
	
}

}
