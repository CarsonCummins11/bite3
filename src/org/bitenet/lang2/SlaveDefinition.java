package org.bitenet.lang2;
/*
 * Purpose: definition of function to be executed remotely
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class SlaveDefinition {
String func;
Memory m;
	public SlaveDefinition(String def, Memory nMem) {
		func = def;
		m = nMem;
		}

}
