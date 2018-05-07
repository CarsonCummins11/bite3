package org.bitenet.predict.data;

import java.net.UnknownHostException;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDataSet implements DataSet{
	public static final String DATA_NAME = "data";
	public static final String PREDICTOR_ID_SIGNIFIER = "_ID";
	private DBCollection dat;
	private DBCursor curs;
	public MongoDataSet(String database_name, String collection_name, String mongo_url) throws UnknownHostException {
		MongoClient mc = new MongoClient(new MongoClientURI(mongo_url));
		dat = mc.getDB(database_name).getCollection(collection_name);
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
		String[] k = ((String)o).split(",");
		double[] ret = new double[k.length];
		for (int i = 0; i < k.length; i++) {
			ret[i] = Double.parseDouble(k[i]);
		}
		return ret;
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
