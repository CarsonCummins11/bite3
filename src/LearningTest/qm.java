package LearningTest;

import java.util.Random;

import org.bitenet.predict.genetic.Member;

public class qm implements Member<qm>{
String def = "";
String goal;
	public qm(String gol) {
		goal = gol;
		def = rString(goal.length());
	}

	private String rString(int length) {
		String ret = "";
		for (int i = 0; i < length; i++) {
			Random rand = new Random();
			ret+= (char) (rand.nextInt(90) + 32);
		}
		return ret;
	}

	@Override
	public qm[] breed(qm m) {
		Random rand = new Random();
		int pivot = rand.nextInt(def.length());
		String c1 = def.substring(0, pivot) + m.def.substring(pivot);
		String c2 = m.def.substring(0,pivot) + def.substring(pivot);
		qm t1 = new qm(goal);
		qm t2 = new qm(goal);
		t1.def = c1;
		t2.def = c2;
		return new qm[] {t1,t2};
		
	}

	@Override
	public double score(double[][] in, double[][] gol) {
		double ret = 0;
		for (int i = 0; i < goal.length(); i++) {
			ret += Math.abs(def.charAt(i)-goal.charAt(i));
		}
		return ret;
	}

	@Override
	public qm random() {
		return new qm(goal);
	}

	@Override
	public void mutate() {
		int mutCount = (int)(Math.random()*goal.length())/2;
		for (int i = 0; i < mutCount; i++) {
			int mutInd = (int)(Math.random()*(goal.length()-1));
			Random rand = new Random();
			char x = (char) (rand.nextInt(90) + 32);
			def = def.substring(0,mutInd)+x+def.substring(mutInd+1);
		}
	}

	@Override
	public void print() {
		System.out.println(def);
		
	}

}
