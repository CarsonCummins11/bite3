package org.bitenet.lang2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LineReader {
String[] lines;
int pos;
	public LineReader(String path) throws FileNotFoundException {
		pos = -1;
		Scanner q = new Scanner(new File(path));
		ArrayList<String> linTemp = new ArrayList<String>();
		while(q.hasNextLine()) {
			linTemp.add(q.nextLine());
		}
		q.close();
		lines = linTemp.toArray(new String[linTemp.size()]);
	}
	public String nextLine() {
		pos++;
		if(pos==lines.length) {
			return null;
		}
		return lines[pos];
	}
	public void setLine(int position) {
		pos = position;
	}
	

}
