/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ZestLoopStateFile extends ZestLoopState<File> {
	private String pathToFile;
	private transient ZestLoopStateString convertedState;
	private transient File file;

	private ZestLoopStateFile() {
	}

	public ZestLoopStateFile(String pathToFile) throws FileNotFoundException {
		this(new File(pathToFile));
	}

	public ZestLoopStateFile(File file) throws FileNotFoundException {
		super();
		this.file = file;
		this.pathToFile = file.getAbsolutePath();
		this.init();
	}

	/**
	 * private method for initialization of the loop (TokenSet & first state)
	 * 
	 * @throws FileNotFoundException
	 *             if the file does not exist
	 */
	private void init() throws FileNotFoundException {
		Scanner in = new Scanner(this.file);
		ZestLoopTokenStringSet initializationSet = new ZestLoopTokenStringSet();
		String line;
		while (in.hasNextLine()) {
			line = in.nextLine();
			if (!line.startsWith("#")) {
				initializationSet.addToken(in.nextLine());
			}
		}
		this.convertedState = new ZestLoopStateString(initializationSet);
		in.close();
	}

	@Override
	public boolean increase() {
		return convertedState.increase();
	}

	@Override
	public void toLastState() {
		convertedState.toLastState();
	}

	@Override
	public ZestLoopState<File> deepCopy() {
		ZestLoopStateFile copy = new ZestLoopStateFile();
		copy.convertedState = (ZestLoopStateString) this.convertedState
				.deepCopy();
		copy.file = this.file;
		copy.pathToFile = this.pathToFile;
		return copy;
	}

	@Override
	public boolean isLastState() {
		return convertedState.isLastState();
	}

	public File getFile() {
		return this.file;
	}

	public File setFile(File newFile) throws FileNotFoundException {
		File oldFile = this.file;
		this.file = newFile;
		this.pathToFile = newFile.getAbsolutePath();
		this.convertedState = new ZestLoopStateString();
		init();
		return oldFile;
	}
}
