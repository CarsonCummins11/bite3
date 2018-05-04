package org.bitenet.predict.data;

import java.net.UnknownHostException;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDataSet implements DataSet{
	public static final String MONGO_URL = "";
	public static final String DATABASE_NAME = "BiteNet";
	public static final String TRAINING_COLLECTION = "Training";
	public static final String DATA_NAME = "data";
	public static final String PREDICTOR_COLLECTION = "Predictor";
	public static final String PREDICTOR_ID_SIGNIFIER = "_ID";
	private DBCollection dat;
	private DBCursor curs;
	public MongoDataSet(int id) throws UnknownHostException {
		MongoClient mc = new MongoClient(new MongoClientURI(MONGO_URL));
		dat = mc.getDB(DATABASE_NAME).getCollection(TRAINING_COLLECTION+id);
		curs = dat.find();
	}

	@Override
	public int numEntries() {
		return (int) dat.count();
		
	}

	@Override
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

	private double arrMax(double[] nums) {
		double max = -Double.MAX_VALUE;
		for (int i = 0; i < nums.length; i++) {
			max = nums[i]>max?nums[i]:max;
		}
		return max;
	}

	@Override
	public boolean hasNextSet() {
		return curs.hasNext();
	}

	@Override
	public double[] nextSet() {
		return toDoubleArray(curs.next().get(DATA_NAME));
	
	}

	private double[] toDoubleArray(Object o) {
		return (double[])o;
	}
	private double arrMin(double[] nums) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < nums.length; i++) {
			min = nums[i]>min?nums[i]:min;
		}
		return min;
	}
	@Override
	public double min() {
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

	@Override
	public void reset() {
		curs = dat.find();
		
	}

}
