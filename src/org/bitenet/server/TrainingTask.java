package org.bitenet.server;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.bitenet.lang.Memory;
import org.bitenet.predict.Predictor;
import org.bitenet.predict.data.MongoDataSet;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class TrainingTask implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7121340169034273423L;
	private static final String DATABASE_NAME = null;
	private static final String TS_COLLECTION = null;
	private static final String ARG_COLLECTION = null;
	private static final String CLASS_COLLECTION = null;
	private static final String PREDICTOR_COLLECTION = null;
	ArrayList<BufferedImage> ts_dat;
	ArrayList<String> order;
    HashMap<BufferedImage,String> clas_dat;
    HashMap<String,HashMap<BufferedImage,Memory>> arg_dat;
    int app_id;
	public TrainingTask(int app,ArrayList<BufferedImage> ts_datIn, HashMap<BufferedImage, String> clas_datIn, HashMap<String, HashMap<BufferedImage, Memory>> arg_datIn, ArrayList<String> execOrder) {
		ts_dat = ts_datIn;
		clas_dat = clas_datIn;
		arg_dat = arg_datIn;
		order = execOrder;
	}
	private String doubleArraytoString(double[] rep) {
		String q = "";
		for (int i = 0; i < rep.length; i++) {
		q+=rep[i]+",";	
		}
		return q;
	}
	public ArrayList<DBObject> DBConvertTS(){
		ArrayList<DBObject> ret = new ArrayList<>();
		for (BufferedImage b : ts_dat) {
			double[] rep = Predictor.represent(b);
			ret.add(new BasicDBObject(MongoDataSet.DATA_NAME,doubleArraytoString(rep)));
		}
		return ret;
	}
	public ArrayList<DBObject> DBConvertClass(){
		ArrayList<DBObject> ret = new ArrayList<>();
		for (BufferedImage b : clas_dat.keySet()) {
			ret.add(new BasicDBObject(MongoDataSet.DATA_NAME,clas_dat.get(b)));
		}
		return ret;
	}
	public ArrayList<DBObject> DBConvertArg(String key) throws IOException{
		ArrayList<DBObject> ret = new ArrayList<>();
		for (BufferedImage b : arg_dat.get(key).keySet()) {
			ret.add(new BasicDBObject(MongoDataSet.DATA_NAME,arg_dat.get(key).get(b).serialize()));
		}
		return ret;
	}
	public void execute(MongoClient c) throws ClassNotFoundException, IOException {
		//write data to mongoDB
		DBCollection datTS = c.getDB(DATABASE_NAME).getCollection(TS_COLLECTION+app_id);
		for (DBObject o: DBConvertTS()) {
			datTS.insert(o);
		}
		DBCollection datClass = c.getDB(DATABASE_NAME).getCollection(CLASS_COLLECTION+app_id);
		for (DBObject o: DBConvertClass()) {
			datClass.insert(o);
		}
		for (String s: arg_dat.keySet()) {
			DBCollection datArg = c.getDB(DATABASE_NAME).getCollection(ARG_COLLECTION+s+app_id);
			for(DBObject o:DBConvertArg(s)) {
				datArg.insert(o);
			}
		}
		//pull predictor from mongoDB
		Predictor pred = Predictor.deserialize((String)c.getDB(DATABASE_NAME).getCollection(PREDICTOR_COLLECTION).find(new BasicDBObject("_ID",Integer.toString(app_id))).one().get(MongoDataSet.DATA_NAME));
		if(pred == null) {
			pred = Predictor.buildRandom(app_id,order.toArray(new String[order.size()]));
		}
		//train predicton
		pred.train(c,app_id);
		//push to mongoDB
		c.getDB(DATABASE_NAME).getCollection(PREDICTOR_COLLECTION).insert(new BasicDBObject("_ID",Integer.toString(app_id)).append(MongoDataSet.DATA_NAME, pred.serialize()));
		}

}
