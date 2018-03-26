package LearningTest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.bitenet.predict.NModel;
import org.bitenet.predict.NNet;
@SuppressWarnings("serial")
public class go extends JPanel implements KeyListener, Runnable{
int y = 0;
double x = 0;
public static final int px = 50;
double py = 250;
double pmov = 0;
public static final int DIMENSION = 50;
public final static double speed = .125;
public static final double pspeed = .25;
ArrayList<ArrayList<Double>> inDat;
ArrayList<ArrayList<Double>> outDat;
JFrame f;
sdHook sd;
public static final int GENERATIONS = 500;
public static final int STARTINGPOOL = 250;
public final static boolean NEURAL_PLAY = true;
	public go() throws FileNotFoundException{
		super();
		f = new JFrame();
		if(NEURAL_PLAY) {
			//get collected data
			Scanner br = new Scanner(new File("outdat.txt"));
			ArrayList<double[]> outdat = new ArrayList<>();
			while(br.hasNextLine()) {
			String[] inn = br.nextLine().split(",");
			double[] k = new double[inn.length];
			for (int i = 0; i < inn.length; i++) {
				k[i] = Double.parseDouble(inn[i]);
			}
			outdat.add(k);
			}
			br.close();
			Scanner brr = new Scanner(new File("outdat.txt"));
			ArrayList<double[]> indat = new ArrayList<>();
			while(brr.hasNextLine()) {
			String[] innn = brr.nextLine().split(",");
			double[] kk = new double[innn.length];
			for (int i = 0; i < innn.length; i++) {
				kk[i] = Double.parseDouble(innn[i]);
			}
			indat.add(kk);
			}
			brr.close();
			double[][] intrain = new double[indat.size()][indat.get(0).length];
			for (int i = 0; i < intrain.length; i++) {
				intrain[i] = indat.get(i);
			}
			double[][] outtrain = new double[outdat.size()][outdat.get(0).length];
			for (int i = 0; i < outtrain.length; i++) {
				outtrain[i] = outdat.get(i);
			}
			//train from data
			NModel m = NModel.train(intrain, outtrain, .1);
			f.setSize(500,500);
			f.setLayout(new GridLayout(1,1));
			f.add(this);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);
			new Thread(this).start();
			while(true) {
				System.out.println("activating neural net with angle = "+getAngle()+" and dist = "+getDist());
				double guess = m.calculate(new double[]{getAngle(),getDist()})[0];
				System.out.println("guessed pmov should be " +guess);
				if(guess>.125) {
					pmov = .25;
				}else if(guess<-.125) {
					pmov = -.25;
				}else {
					pmov = 0;
				}
				long t = System.currentTimeMillis();
				while(System.currentTimeMillis()-t<500) {
					//wait 500 milliseconds to simulate human reaction time
				}
			}
		}else{
			inDat = new ArrayList<>();
			outDat = new ArrayList<>();
			f.addKeyListener(this);
			sd = new sdHook();
			Runtime.getRuntime().addShutdownHook(new Thread(sd));
			f.setSize(500,500);
			f.setLayout(new GridLayout(1,1));
			f.add(this);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setVisible(true);
			new Thread(this).start();
		}
	}

	public static void main(String[] args) {
		try {
			new go();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//move the enemy
		x-=speed;
		if(x<0) {
			x = 500;
			y = (int)(Math.random()*400)+50;
		}
		g.setColor(Color.BLACK);
		g.fillRect((int)Math.floor(x),y,DIMENSION,DIMENSION);
		//move the character
		py+=pmov;
		g.setColor(Color.red);
		g.fillRect(px,(int)Math.floor(py),DIMENSION/2,DIMENSION/2);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			pmov = pspeed;
		}else if(e.getKeyCode()==KeyEvent.VK_UP){
			pmov = -pspeed;
		}
		ArrayList<Double> inn = new ArrayList<>();
		inn.add(getAngle());
		inn.add(getDist());
		inDat.add(inn);
		sd.inDat.add(inn);
		ArrayList<Double> outt = new ArrayList<>();
		outt.add(pmov);
		outDat.add(outt);
		sd.outDat.add(outt);
	}

	private Double getDist() {
		return Point.distance(x, y, px, py);
	}


	public double getAngle()
	{
	    double theta = Math.atan2(y - py, x - px);
	    theta += Math.PI/2.0;
	    double angle = Math.toDegrees(theta);
	    if (angle < 0) {
	        angle += 360;
	    }
	    return angle;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pmov = 0;
		//update neural net data
		ArrayList<Double> inn = new ArrayList<>();
		inn.add(getAngle());
		inn.add(getDist());
		inDat.add(inn);
		sd.inDat.add(inn);
		ArrayList<Double> outt = new ArrayList<>();
		outt.add(pmov);
		outDat.add(outt);
		sd.outDat.add(outt);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		while(true) {
		f.repaint();
		}
		
	}
	
}
