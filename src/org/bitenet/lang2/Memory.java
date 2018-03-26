package org.bitenet.lang2;

public class Memory {
int[] def;
	public Memory(int size) {
		def = new int[size];
	}
	public int get(int pos) {
		return def[pos];
	}
	public void set(int pos, int value) {
		def[pos] = value;
	}
	public static Memory deserialize(String s) {
		// TODO Auto-generated method stub
		
	}
}
