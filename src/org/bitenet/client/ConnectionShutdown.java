package org.bitenet.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.SSLSocket;

import org.bitenet.lang.Memory;
import org.bitenet.predict.Predictor;

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
			out.writeObject(ts_dat);
			out.writeObject(clas_dat);
			out.writeObject(arg_dat);
			out.writeInt(app_id);
			sok.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
