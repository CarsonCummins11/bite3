package org.bitenet.lang2;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFrame;
import javax.swing.JWindow;

import org.bitenet.predict.Predictor;
/*
 * Purpose: connects client to server
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Remote implements Runnable {
public URL server;
public static final String url = "";
ObjectOutputStream out;
ObjectInputStream in;
TimeDecayMap<SlaveDefinition,Memory> predictions;
Predictor pred;
JFrame f;
public static final int WAIT_PERIOD = 5;
public static final String EXECUTION_ID = "EXECUTION_CON";
	public Remote(JFrame ff) throws IOException{
		pred = new Predictor();
		server = new URL(url);
		f = ff;
		URLConnection con = server.openConnection();
		out = new ObjectOutputStream(con.getOutputStream());
		out.writeChars(EXECUTION_ID);
		in = new ObjectInputStream(con.getInputStream());
	}

	public boolean contains(SlaveDefinition sd) {
		return predictions.containsKey(sd);
	}

	public Memory retrieve(SlaveDefinition sd) {
		return predictions.get(sd);
	}
	public BufferedImage screenShot(JFrame in) {
		int imgWidth = in.getWidth();
		int imgHeight= in.getHeight();
		GraphicsConfiguration gConf = new JWindow().getGraphicsConfiguration();
		BufferedImage img = gConf.createCompatibleImage(imgWidth, imgHeight);
		Graphics g = img.getGraphics();
		f.paint(g);
		g.dispose();
		return img;
	}
	@Override
	public void run() {
		while(true) {
			SlaveDefinition sd = pred.generate(screenShot(f),1)[0];
			try {
				predictions.put(sd,execute(sd));
				wait(WAIT_PERIOD);
			} catch (IOException | InterruptedException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}

	private Memory execute(SlaveDefinition sd) throws IOException, ClassNotFoundException {
		out.writeObject(sd);
		return (Memory)in.readObject();
	}

}
