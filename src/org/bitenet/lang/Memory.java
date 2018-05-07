package org.bitenet.lang;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.io.output.ByteArrayOutputStream;
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
	public static Memory deserialize(String s) throws ClassNotFoundException, IOException {
		 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(s.getBytes()));
		 Object o  = ois.readObject();
		 ois.close();
		 return (Memory)o;
	}
	public String serialize() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
       ObjectOutputStream oos = new ObjectOutputStream(baos);
       oos.writeObject(this);
       oos.close();
       return new String(baos.toByteArray());
	}
}
