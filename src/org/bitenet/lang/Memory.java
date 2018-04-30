package org.bitenet.lang;

import java.io.Serializable;
/*
 * Purpose: virtual memory used by the bitenet container
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Memory implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -4206896067637995935L;
int[] def;
	public Memory(int size) {
		def = new int[size];
	}
	public Memory(double[] definition) {
		def = new int[definition.length];
		for (int i = 0; i < definition.length; i++) {
			def[i] = (int)Math.round(definition[i]);
		}
	}
	public int get(int pos) {
		return def[pos];
	}
	public void set(int pos, int value) {
		def[pos] = value;
	}
}
