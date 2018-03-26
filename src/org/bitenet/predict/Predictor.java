package org.bitenet.predict;

import java.awt.image.BufferedImage;
import java.io.File;
import org.bitenet.lang2.SlaveDefinition;


public class Predictor {
public static final int REDUCED_WIDTH = 50;
NModel classifier;
NModel predict;
NModel argBuilder;
String[] funcs;
	public Predictor(File f) {
		// TODO Auto-generated constructor stub
	}
	public Predictor() {
		
	}
	public SlaveDefinition[] generate(BufferedImage img, int predictionSize) {
		double[] input = represent(img);
		double[] predImage = predict.calculate(input);
		double[] funcPred = classifier.calculate(predImage);
		for (int i = 0; i < funcPred.length; i++) {
			
		}
	}

	public void train(NDataSet inputs, NDataSet outputs) {
		
	}
	public static BufferedImage reduce(BufferedImage img) {
		
	}
	public static double[] represent(BufferedImage img) {
		
	}
}
