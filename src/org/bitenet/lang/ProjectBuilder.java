package org.bitenet.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

import org.bitenet.predict.Predictor;

public class ProjectBuilder {
public static final String LAUNCHPAD_LOCATION = "www.bitenet.org/launchpad.jar";
	@SuppressWarnings("resource")
	public ProjectBuilder(int app_id) throws FileNotFoundException, IOException {
		//build a randomly generated predictor for the current project
		Predictor p = Predictor.buildRandom(app_id,nameOrd());
		//write predictor to file
		new ObjectOutputStream(new FileOutputStream(new File(Predictor.PREDICTOR_PATH+Predictor.FILE_ENDER))).writeObject(p);
		//download latest release of download latest release of launchpad and put it into the project
		ReadableByteChannel rbc = Channels.newChannel(new URL(LAUNCHPAD_LOCATION).openStream());
		FileOutputStream fos = new FileOutputStream("run.jar");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}

	public static void main(String[] args) {
		try {
			new ProjectBuilder(Integer.parseInt(args[0]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public String[] nameOrd(){
		File[] fs = new File(System.getProperty("user.dir")).listFiles();
		String[] names = new String[fs.length];
		for (int i = 0; i < fs.length; i++) {
			names[i] = fs[i].getName().split(".")[0];
		}
		Arrays.sort(names);
		return names;
	}

}
