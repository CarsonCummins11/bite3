package org.bitenet.predict;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bitenet.client.SlaveDefinition;
import org.bitenet.lang.Memory;

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
//required confidence for a prediction to be executed
public static final double REQ_CONF =  .7;
	@SuppressWarnings("resource")
	public static Predictor build() throws ClassNotFoundException, FileNotFoundException, IOException {
		Predictor p =  (Predictor)new ObjectInputStream(new FileInputStream(new File(PREDICTOR_PATH+FILE_ENDER))).readObject();
		p.funcs = buildFunctionOrder();
		return p;
	}
	private static String[] buildFunctionOrder() {
		File[] fs = new File(System.getProperty("user.dir")).listFiles();
		String[] names = new String[fs.length];
		for (int i = 0; i < fs.length; i++) {
			names[i] = fs[i].getName().split(".")[0];
		}
		Arrays.sort(names);
		return names;
	}
	public Predictor(int n) {
		
	}
	public SlaveDefinition[] generate(BufferedImage img, int predictionSize) {
		double[] input = represent(img);
		double[] predImage = predict.activate(input);
		double[] funcPred = classifier.activate(predImage);
		return buildSlaveDefs(funcPred, predImage);
	}
	private SlaveDefinition[] buildSlaveDefs(double[] funcPred,double[] predImage) {
		ArrayList<String> funcRet = new ArrayList<>();
		for (int i = 0; i < funcPred.length; i++) {
			if(funcPred[i]>REQ_CONF) {
				funcRet.add(funcs[i]);
			}
		}
		ArrayList<SlaveDefinition> ret = new ArrayList<>();
		for (String s : funcRet) {
			ret.add(new SlaveDefinition(s,new Memory(argBuilder.get(s).activate(predImage))));
		}
		return ret.toArray(new SlaveDefinition[ret.size()]);
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

	private static BufferedImage reduce(BufferedImage img) {
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
}
