/**
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * @author Alessandro Secco: seccoale@gmail.com
 */
package org.mozilla.zest.core.v1;

import java.io.FileNotFoundException;

/**
 * The Class ZestLoopStateFile.
 */
public class ZestLoopStateFile extends ZestLoopState<String> {
	
//	/** The path to file. */
//	private String pathToFile;
	
	/** The converted state. */
	private transient ZestLoopStateString convertedState;
	
//	/** The file. */
//	private transient File file;

	/**
	 * Instantiates a new zest loop state file.
	 */
	public ZestLoopStateFile() {
		super();
	}

	/**
	 * Instantiates a new zest loop state file.
	 *
	 * @param pathToFile the path to file
	 * @throws FileNotFoundException the file not found exception
	 */
	public ZestLoopStateFile(ZestLoopTokenStringSet set){
		super(set);
	}

	

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#increase()
	 */
	

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#toLastState()
	 */
//	@Override
//	public void toLastState(ZestLoopTokenStringSet set) {
//		convertedState.toLastState(set);
//	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#deepCopy()
	 */
	@Override
	public ZestLoopState<String> deepCopy() {
		ZestLoopStateFile copy = new ZestLoopStateFile();
		copy.convertedState = (ZestLoopStateString) this.convertedState
				.deepCopy();
//		copy.file = this.file;
//		copy.pathToFile = this.pathToFile;
		return copy;
	}

	@Override
	public boolean increase(int step, ZestLoopTokenSet<String> set) {
		return this.convertedState.increase(step, set);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopState#isLastState()
	 */
//	@Override
//	public boolean isLastState(ZestLoopTokenStringSet set) {
//		return convertedState.isLastState();
//	}

//	/**
//	 * Gets the file.
//	 *
//	 * @return the file
//	 */
//	public File getFile() {
//		return this.file;
//	}

//	/**
//	 * Sets the file.
//	 *
//	 * @param newFile the new file
//	 * @return the file
//	 * @throws FileNotFoundException the file not found exception
//	 */
//	public File setFile(File newFile) throws FileNotFoundException {
//		File oldFile = this.file;
//		this.file = newFile;
//		this.pathToFile = newFile.getAbsolutePath();
//		this.convertedState = new ZestLoopStateString();
//		init();
//		return oldFile;
//	}

//	@Override
//	public ZestLoopTokenStringSet getSet() {
//		return this.convertedState.getSet();
//	}
}
