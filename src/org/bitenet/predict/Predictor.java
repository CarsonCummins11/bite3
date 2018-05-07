package org.bitenet.predict;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bitenet.client.SlaveDefinition;
import org.bitenet.lang.Memory;

import com.mongodb.MongoClient;

/*
 * Purpose: usage of ML libraries to apply to overall application
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Predictor implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 8329351451013861958L;
public static final int REDUCED_WIDTH = 50;
public static final int REDUCED_AREA = (int)Math.round(Math.pow(REDUCED_WIDTH, 2));
public static final String PREDICTOR_PATH = "BN_PL";
public static final String FILE_ENDER = ".nnet";
NModel classifier;
NModel predict;
HashMap<String,NModel> argBuilder;
String[] funcs;
public int app_id;
//required confidence for a prediction to be executed
public static final double REQ_CONF =  .95;
//has to be in top N_LIMIT in order to be executed
public static final int N_LIMIT = 4;
	@SuppressWarnings("resource")
	public static Predictor build() throws ClassNotFoundException, FileNotFoundException, IOException {
	return (Predictor)new ObjectInputStream(new FileInputStream(new File(PREDICTOR_PATH+FILE_ENDER))).readObject();
	}
	public Predictor() {
		
	}
	public SlaveDefinition[] generate(BufferedImage img) {
		double[] input = represent(img);
		double[] predImage = predict.activate(input);
		double[] funcPred = classifier.activate(predImage);
		return buildSlaveDefs(funcPred, predImage);
	}
	private SlaveDefinition[] buildSlaveDefs(double[] funcPred,double[] predImage) {
		ArrayList<String> funcRet = new ArrayList<>();
		double nth = getNthHighest(funcPred,N_LIMIT);
		for (int i = 0; i < funcPred.length; i++) {
			if(funcPred[i]>REQ_CONF||funcPred[i]>nth) {
				funcRet.add(funcs[i]);
			}
		}
		ArrayList<SlaveDefinition> ret = new ArrayList<>();
		for (String s : funcRet) {
			ret.add(new SlaveDefinition(s,new Memory(argBuilder.get(s).activate(predImage))));
		}
		return ret.toArray(new SlaveDefinition[ret.size()]);
	}
	private double getNthHighest(double[] funcPred, int nLimit) {
		double[] q = funcPred.clone();
		Arrays.sort(q);
		return q[q.length-1-nLimit];
	}
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

	public static BufferedImage reduce(BufferedImage img) {
		//shrink image
		Image k = img.getScaledInstance(REDUCED_WIDTH, REDUCED_WIDTH, Image.SCALE_FAST);
		BufferedImage step1 = toBufferedImage(k);
		BufferedImage ret = new BufferedImage(REDUCED_WIDTH, REDUCED_WIDTH,BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < REDUCED_WIDTH; i++) {
			for (int j = 0; j < REDUCED_WIDTH; j++) {
				int rgb = step1.getRGB(i, j);
				int r = (rgb>>16)&0x0ff;
				int g=(rgb>>8) &0x0ff;
				int b= (rgb)   &0x0ff;
				r = g = b = (r+g+b)/3;
				ret.setRGB(i, j,  ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff));
			}
		}
		return ret;
	}
	public static double[] represent(BufferedImage img) {
		BufferedImage rett = reduce(img);
		double[] ret = new double[REDUCED_AREA];
		for (int i = 0; i < REDUCED_WIDTH; i++) {
			for (int j = 0; j < REDUCED_WIDTH; j++) {
				//gets r value of rgb
				ret[(i*REDUCED_WIDTH)+j] = rett.getRGB(i, j)>>16&0x0ff;
			}
		}
		return ret;
	}
	public static Predictor buildRandom(int app_id,String[] names) {
		NModel clas = new NModel(REDUCED_AREA,names.length,3,3,1);
		NModel pred = new NModel(REDUCED_AREA,REDUCED_AREA,3,3,1);
		HashMap<String,NModel> argmake = new HashMap<>();
		for (int i = 0; i < names.length; i++) {
			argmake.put(names[i], new NModel(REDUCED_AREA,100,3,3,1));
		}
		Predictor ret = new Predictor();
		ret.funcs = names;
		ret.app_id = app_id;
		ret.classifier = clas;
		ret.predict = pred;
		ret.argBuilder = argmake;
		return ret;
	}
	public static Predictor deserialize(String s) throws ClassNotFoundException, IOException {
		 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(s.getBytes()));
		 Object o  = ois.readObject();
		 ois.close();
		 return (Predictor)o;
	}
	public String serialize() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        oos.close();
        return new String(baos.toByteArray());
	}
	public void train(MongoClient c, int id) {
		
		
	}
}
