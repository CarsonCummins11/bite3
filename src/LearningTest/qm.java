package LearningTest;

import java.util.Random;

import org.bitenet.predict.NDataSet;
import org.bitenet.predict.NNet;
import org.bitenet.predict.genetic.Member;

public class qm implements Member<qm>{
String def = "";
String goal;
NNet fitness;
	public qm(String gol, NNet fitnessCalc) {
		goal = gol;
		fitness = fitnessCalc;
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
		qm t1 = new qm(goal,fitness);
		qm t2 = new qm(goal,fitness);
		t1.def = c1;
		t2.def = c2;
		return new qm[] {t1,t2};
		
	}


	@Override
	public qm random() {
		return new qm(goal,fitness);
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

	@Override
	public double score(NDataSet in, NDataSet go) {
		fitness.activate(stringToDouble(def));
	}

}
