package org.bitenet.lang2;

import java.io.Serializable;

/*
 * Purpose: definition of function to be executed remotely
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class SlaveDefinition implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -6652838725207635273L;
String func;
Memory m;
	public SlaveDefinition(String def, Memory nMem) {
		func = def;
		m = nMem;
		}

}
