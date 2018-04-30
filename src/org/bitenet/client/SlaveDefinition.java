package org.bitenet.client;

import java.io.FileNotFoundException;
import java.io.Serializable;

import org.bitenet.lang.Memory;
import org.bitenet.lang.Slave;

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
	public Memory execute() throws FileNotFoundException {
		return new Slave(m,false,func,null,null).activate();
	}

}
