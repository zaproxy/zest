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

/**
 * The Class ZestLoopStateFile.
 */
public class ZestLoopStateFile extends ZestLoopState<String> {
	
	/** The path to file. */
	private String pathToFile;
	
	/** The converted state. */
	private transient ZestLoopStateString convertedState;
	
	/** The file. */
	private transient File file;

	/**
	 * Instantiates a new zest loop state file.
	 */
	private ZestLoopStateFile() {
		super();
	}

	/**
	 * Instantiates a new zest loop state file.
	 *
	 * @param pathToFile the path to file
	 * @throws FileNotFoundException the file not found exception
	 */
	public ZestLoopStateFile(String pathToFile) throws FileNotFoundException {
		this(new File(pathToFile));
	}

	/**
	 * Instantiates a new zest loop state file.
	 *
	 * @param file the file
	 * @throws FileNotFoundException the file not found exception
	 */
	public ZestLoopStateFile(File file) throws FileNotFoundException {
		super();
		this.file = file;
		this.pathToFile = file.getAbsolutePath();
		this.init();
	}

	/**
	 * private method for initialization of the loop (TokenSet & first state).
	 *
	 * @throws FileNotFoundException if the file does not exist
	 */
	private void init() throws FileNotFoundException {
		Scanner in = new Scanner(this.file);
		ZestLoopTokenStringSet initializationSet = new ZestLoopTokenStringSet();
		String line;
		while (in.hasNextLine()) {
			line = in.nextLine();
			if (!line.startsWith("#")) {
				initializationSet.addToken(line);
			}
		}
		this.convertedState = new ZestLoopStateString(initializationSet);
		in.close();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#increase()
	 */
	@Override
	public boolean increase() {
		return convertedState.increase();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#toLastState()
	 */
	@Override
	public void toLastState() {
		convertedState.toLastState();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#deepCopy()
	 */
	@Override
	public ZestLoopState<String> deepCopy() {
		ZestLoopStateFile copy = new ZestLoopStateFile();
		copy.convertedState = (ZestLoopStateString) this.convertedState
				.deepCopy();
		copy.file = this.file;
		copy.pathToFile = this.pathToFile;
		return copy;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#isLastState()
	 */
	@Override
	public boolean isLastState() {
		return convertedState.isLastState();
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * Sets the file.
	 *
	 * @param newFile the new file
	 * @return the file
	 * @throws FileNotFoundException the file not found exception
	 */
	public File setFile(File newFile) throws FileNotFoundException {
		File oldFile = this.file;
		this.file = newFile;
		this.pathToFile = newFile.getAbsolutePath();
		this.convertedState = new ZestLoopStateString();
		init();
		return oldFile;
	}

	@Override
	public ZestLoopTokenStringSet getSet() {
		return this.convertedState.getSet();
	}
}
