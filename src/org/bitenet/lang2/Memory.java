package org.bitenet.lang2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
/*
 * Purpose: virtual memory used by the bitenet container
 * 
 * @author Carson Cummins
 * @version 0.0
 */
public class Memory {
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
	public static Memory deserialize(String s) throws IOException {
		GZIPInputStream outZip = new GZIPInputStream(new ByteArrayInputStream(s.getBytes("UTF-8")));
		BufferedReader ret = new BufferedReader(new InputStreamReader(outZip,"UTF-8"));
		String[] intsStr = ret.readLine().split(",");
		int[] ints = new int[intsStr.length];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = Integer.parseInt(intsStr[i]);
		}
		Memory rett = new Memory(ints.length);
		rett.def = ints;
		return rett;
		
	}
	public String serialize () throws IOException {
		String r = toStr();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream inzip = new GZIPOutputStream(bos);
		inzip.write(r.getBytes("UTF-8"));
		inzip.close();
		return bos.toString("UTF-8");
	}
	private String toStr() {
		String ret = "";
		for (int i = 0; i < def.length; i++) {
			ret+=i+",";
			
		}
		return ret;
	}
}
