package org.bitenet.predict;

import java.awt.Point;
import java.util.ArrayList;

public class Nvipnet extends NNet{
Point[] inPos;
	public Nvipnet(int[] layersizes, Point[] inputPos, int actFunc) {
		super(layersizes,actFunc);
		inPos = inputPos;
	}
	@Override
	public double[] activate(double[] in) {
		ArrayList<NLayer> ls = input.getLayers(new ArrayList<NLayer>());
		for (int i = 0; i < in.length; i++) {
			Point p = inPos[i];
			ls.get(p.x).nodes.get(p.y).value = in[i];
		}
		input.activate();
		double[] ret = output.getValues();
		input.reset();
		return ret;
	}
	

}
