package org.bitenet.predict.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
/*
 * Purpose: way to quickly access data for use in ml
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class LocalDataSet implements DataSet{
Scanner in;
File from;
	public LocalDataSet(File f) throws FileNotFoundException {
	in = new Scanner(f);
	from = f;
	}
	public boolean hasNextSet() {
		return in.hasNextLine();
	}
	public double max() {
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
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	public int numEntries(){
		in.close();
		try {
			int ret = countLines(from.getAbsolutePath());
			reset();
			return ret;
		} catch (IOException e) {
			reset();
			e.printStackTrace();
			return -1;
		
		}
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
	public void reset(){
		try {
			in.close();
			in = new Scanner(from);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public String nextString() {
		return in.nextLine();
	}
	public double min(){
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
