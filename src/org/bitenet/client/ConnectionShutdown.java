package org.bitenet.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.net.ssl.SSLSocket;

import org.bitenet.lang.Memory;
import org.bitenet.predict.Predictor;
import org.bitenet.server.TrainingTask;

public class ConnectionShutdown implements Runnable{
	ArrayList<BufferedImage> ts_dat;
	ObjectOutputStream out;
	HashMap<BufferedImage,String> clas_dat;
	SSLSocket sok;
	Thread myThread;
	HashMap<String,HashMap<BufferedImage,Memory>> arg_dat;
	public static final int MAX_SIZE = 50;
	int app_id;
	public ConnectionShutdown(int app,Thread m,SSLSocket mySock) {
		ts_dat = new ArrayList<>();
		myThread = m;
		sok = mySock;
		app_id = app;
		clas_dat = new HashMap<>();
		arg_dat = new HashMap<>();
	}

	@Override
	public void run() {
		try {
			out.writeObject(null);
			out.writeObject(new TrainingTask(app_id,ts_dat,clas_dat,arg_dat,genExecOrder()));
			sok.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private ArrayList<String> genExecOrder() {
		File[] fs = new File(System.getProperty("user.dir")).listFiles();
		String[] names = new String[fs.length];
		for (int i = 0; i < fs.length; i++) {
			names[i] = fs[i].getName().split(".")[0];
		}
		Arrays.sort(names);
		ArrayList<String> ret = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			ret.add(names[i]);
		}
		return ret;
	}

	public void dataAdd(BufferedImage img, SlaveDefinition sd) {
		if(ts_dat.size()<MAX_SIZE) {
		BufferedImage qq = Predictor.reduce(img);
		ts_dat.add(qq);
		clas_dat.put(qq,sd.func);
		if(arg_dat.containsKey(sd.func)) {
			arg_dat.get(sd.func).put(qq, sd.m);
		}else {
			HashMap<BufferedImage,Memory> temp = new HashMap<>();
			temp.put(qq, sd.m);
			arg_dat.put(sd.func, temp);
		}
		}else {
			Runtime.getRuntime().removeShutdownHook(myThread);
			myThread.start();
		}
		
		
	}

}
