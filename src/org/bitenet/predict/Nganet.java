package org.bitenet.predict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Nganet {
NNet producer;
NNet tester;
public static final String TEMP_DATA_PATH1 = "tempTraining111.txt";
public static final String TEMP_DATA_PATH2 = "tempTraining222.txt";
public static final String TEMP_DATA_PATH3 = "tempTraining333.txt";
	public Nganet() {
		
	}
	public void train(NDataSet goal,double learning) throws FileNotFoundException {
		//generate a noise file
		PrintWriter pw = new PrintWriter(new File(TEMP_DATA_PATH3));
		double entrycount = goal.numEntries();
		int entrysize = goal.nextSet().length;
		goal.reset();
		for (int i = 0; i < entrycount; i++) {
			pw.write(randCSVDoubles(entrysize,goal.max(),goal.min()));
		}
		//train the producer on minimal data set
		producer.train(new NDataSet(new File(TEMP_DATA_PATH3)), goal, learning, NModel.required_error, NModel.max_steps);
		//create output set with interspersed improper data
		pw.close();
		File f = new File(TEMP_DATA_PATH1);
		pw = new PrintWriter(f);
		int truEntries = 0;
		while(goal.hasNextSet()) {
			truEntries++;
			pw.write(goal.nextString());
		}
		 entrycount = goal.numEntries();
		entrysize = goal.nextSet().length;
		goal.reset();
		//write in the randomly generated data
		for (int i = 0; i < entrycount; i++) {
			pw.write(randCSVDoubles(entrysize,goal.max(),goal.min()));
		}
		//create a dataset representing probabilitites data is real vs. not real
		pw.close();
		pw = new PrintWriter(new File(TEMP_DATA_PATH2));
		for (int i = 0; i < truEntries; i++) {
			pw.print("1,");
		}
		for (int i = 0; i < truEntries; i++) {
			pw.print("0,");
		}
		//train discriminator
		tester.train(new NDataSet(new File(TEMP_DATA_PATH1)), new NDataSet(new File(TEMP_DATA_PATH2)), learning,NModel.required_error, NModel.max_steps);
		Still have to train them together so leaving this so it causes an error and i know to fix it
	}
	private String randCSVDoubles(int size,double max, double min) {
		String ret = "";
		for (int i = 0; i < size; i++) {
			ret+=(Math.random() * ((max - min) + 1))+",";
		}
		return ret;
	}

}
