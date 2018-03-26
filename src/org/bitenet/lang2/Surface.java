package org.bitenet.lang2;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

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
