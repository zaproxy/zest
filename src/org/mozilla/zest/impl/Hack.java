package org.mozilla.zest.impl;

import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class Hack {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ZestBasicRunner zbr = new ZestBasicRunner();
		zbr.setOutputWriter(new OutputStreamWriter(System.out));
		zbr.setStopOnAssertFail(false);
		try {
			Reader reader = new FileReader(new File("/home/simon/tmp/cccccc.zst"));;
			zbr.runScript(reader, null);
		} catch (Exception e) {
			System.out.println("Error running script: " + e);
			e.printStackTrace();
		}

	}

}
