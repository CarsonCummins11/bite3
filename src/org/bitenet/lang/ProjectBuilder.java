package org.bitenet.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.bitenet.predict.Predictor;
import org.bitenet.predict.data.MongoDataSet;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class ProjectBuilder {
public static final String LAUNCHPAD_LOCATION = "www.bitenet.org/launchpad.jar";
	@SuppressWarnings("resource")
	public ProjectBuilder(int app_id) throws FileNotFoundException, IOException {
		//build a randomly generated predictor for the current project
		Predictor p = Predictor.buildRandom(app_id);
		//upload predictor to mongo db
		MongoClient mc = new MongoClient(new MongoClientURI(MongoDataSet.MONGO_URL));
		DBCollection dat = mc.getDB(MongoDataSet.DATABASE_NAME).getCollection(MongoDataSet.PREDICTOR_COLLECTION);
		dat.insert(new BasicDBObject(MongoDataSet.PREDICTOR_ID_SIGNIFIER,Integer.toString(app_id)).append(MongoDataSet.DATA_NAME, p.serialize()));
		//write predictor to file
		new ObjectOutputStream(new FileOutputStream(new File(Predictor.PREDICTOR_PATH+Predictor.FILE_ENDER))).writeObject(p);
		//download latest release of download latest release of launchpad and put it into the project
		ReadableByteChannel rbc = Channels.newChannel(new URL(LAUNCHPAD_LOCATION).openStream());
		FileOutputStream fos = new FileOutputStream("run.jar");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}

	public static void main(String[] args) {
		try {
			new ProjectBuilder(Integer.parseInt(args[0]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
