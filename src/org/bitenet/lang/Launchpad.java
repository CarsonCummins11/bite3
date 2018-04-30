package org.bitenet.lang;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import org.bitenet.client.Remote;
/*
 * Purpose: launches the application in the current directory
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Launchpad implements GraphicUpdater{
JFrame f;
Surface s;
	public Launchpad() throws IOException, ClassNotFoundException {
		f = new JFrame();
		f.setSize(500,500);
		f.setLayout(new GridLayout(1,1));
		f.add(s);
		f.setVisible(true);
		Slave s = new Slave(new Memory(Integer.MAX_VALUE-1),true,"main",new Remote(f),new BufferedImage(500,500,BufferedImage.TYPE_INT_ARGB));
		s.imagerec = this;
		f.addKeyListener(s);
		f.addMouseListener(s);
		f.addMouseMotionListener(s);
		s.activate();
	}
public static void main(String[] args) {
	try {
		new Launchpad();
	} catch ( IOException | ClassNotFoundException e) {
	System.out.println("a file is missing: "+e.getMessage());
	}
}
@Override
public void onUpdate(BufferedImage b) {
	s.setImage(b);
	f.repaint();
	
}
}
