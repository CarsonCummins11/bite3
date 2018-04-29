package org.bitenet.lang2;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
/*
 * Purpose: the surface which all rendered images are drawn to
 * 
 * @author Carson Cummins
 * @version 0.0
 */
@SuppressWarnings("serial")
public class Surface extends JPanel{
BufferedImage img;
	public Surface() {
		
	}
@Override
public void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.drawImage(img, getX(), getY(), null);
}
public void setImage(BufferedImage in) {
	img = in;
}
}
