package org.bitenet.server;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bitenet.lang.Memory;
import org.bitenet.predict.Predictor;
import org.bitenet.predict.data.MongoDataSet;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;


public class TrainingTask {
	ArrayList<BufferedImage> ts_dat;
    HashMap<BufferedImage,String> clas_dat;
    HashMap<String,HashMap<BufferedImage,Memory>> arg_dat;
    int app_id;
	public TrainingTask(int app,ArrayList<BufferedImage> ts_datIn, HashMap<BufferedImage, String> clas_datIn, HashMap<String, HashMap<BufferedImage, Memory>> arg_datIn) {
		ts_dat = ts_datIn;
		clas_dat = clas_datIn;
		arg_dat = arg_datIn;
	}
	public ArrayList<DBObject> DBConvert(){
		
	}
	public void execute() throws ClassNotFoundException, IOException {
		//write data to mongoDB
		MongoClient mc = new MongoClient(new MongoClientURI(MongoDataSet.MONGO_URL));
		DBCollection dat = mc.getDB(MongoDataSet.DATABASE_NAME).getCollection(MongoDataSet.TRAINING_COLLECTION+app_id);
		for (DBObject o: DBConvert()) {
			dat.insert(o);
		}
		mc.close();
		//pull predictor from mongoDB
		Predictor pred = Predictor.deserialize(mc.getDB(MongoDataSet.DATABASE_NAME).getCollection(MongoDataSet.PREDICTOR_COLLECTION).find(new BasicDBObject("_ID",Integer.toString(app_id))).one().get(MongoDataSet.DATA_NAME));
		//train predictor
		pred.train(new MongoDataSet(app_id));
		//push to mongoDB
		
	}

}
