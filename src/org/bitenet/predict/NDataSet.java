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

}
