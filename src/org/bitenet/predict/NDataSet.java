package org.bitenet.predict;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NDataSet {
Scanner in;
File from;
	public NDataSet(File f) throws FileNotFoundException {
	in = new Scanner(f);
	from = f;
	}
	public boolean hasNextSet() {
		return in.hasNextLine();
	}
	public double max() throws FileNotFoundException {
		reset();
		double max = -Double.MAX_VALUE;
		while(hasNextSet()) {
			double maxx = arrMax(nextSet());
		if(max<maxx) {
			max = maxx;
		}
		}
	reset();
	return max;
	}
	public double numEntries() throws FileNotFoundException {
		double ret = 0;
		reset();
		while(hasNextSet()) {
			ret++;
		}
		reset();
		return ret;
	}
	private double arrMax(double[] nums) {
		double max = -Double.MAX_VALUE;
		for (int i = 0; i < nums.length; i++) {
			max = nums[i]>max?nums[i]:max;
		}
		return max;
	}
	public double[] nextSet() {
		if(in.hasNextLine()) {
		String[] k = in.nextLine().split(",");
		double[] ret = new double[k.length];
		for (int i = 0; i < k.length; i++) {
			ret[i] = Double.parseDouble(k[i]);
		}
		return ret;
		}else {
			return null;
		}
	}
	public void reset() throws FileNotFoundException {
		in = new Scanner(from);
	}
	public String nextString() {
		return in.nextLine();
	}
	public double min() throws FileNotFoundException {
		reset();
		double min = Double.MAX_VALUE;
		while(hasNextSet()) {
			double minn = arrMin(nextSet());
		if(min>minn) {
			min = minn;
		}
		}
	reset();
	return min;
	}
	private double arrMin(double[] nums) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < nums.length; i++) {
			min = nums[i]>min?nums[i]:min;
		}
		return min;
	}

}
