package LearningTest;

import org.bitenet.predict.genetic.NEvolutionHandler;

public class quicktest {

	public quicktest() {
		System.out.println(NEvolutionHandler.train(new double[][] {}, new double[][] {}, 2048, 16384, 0.8f, 0.1f, 0.03f,.1, new qm("hello world")).def);
	}
public static void main(String[] args) {
	new quicktest();
}
}
