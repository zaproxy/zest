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
import java.util.List;
import java.util.Scanner;

// TODO: Auto-generated Javadoc
/**
 * The Class ZestLoopTokenFileSet.
 */
public class ZestLoopTokenFileSet extends ZestElement implements ZestLoopTokenSet<String> {
	
	/** The path to file. */
	private String pathToFile = null;
	
	/** The converted set. */
	private transient ZestLoopTokenStringSet convertedSet = null;
	
	/**
	 * Instantiates a new zest loop token file set.
	 *
	 * @param pathToFile the path to file
	 * @throws FileNotFoundException the file not found exception
	 */
	public ZestLoopTokenFileSet(String pathToFile) throws FileNotFoundException {
		super();
		this.pathToFile = pathToFile;
		this.convertedSet = this.init(new File(pathToFile));
	}

	/**
	 * private method for initialization of the loop (TokenSet & first state).
	 *
	 * @param file the file
	 * @return the zest loop token string set
	 * @throws FileNotFoundException if the file does not exist
	 */
	private ZestLoopTokenStringSet init(File file) throws FileNotFoundException {
		Scanner in = new Scanner(file);
		ZestLoopTokenStringSet initializationSet = new ZestLoopTokenStringSet();
		String line;
		while (in.hasNextLine()) {
			line = in.nextLine();
			if (!line.startsWith("#")) {
				initializationSet.addToken(line);
			}
		}
		in.close();
		return initializationSet;
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#addToken(java.lang.Object)
	 */
	@Override
	public void addToken(String token) {
		throw new IllegalArgumentException("Operation not allowed for "
				+ this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getToken(int)
	 */
	@Override
	public String getToken(int index) {
		return convertedSet.getToken(index);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getTokens()
	 */
	@Override
	public List<String> getTokens() {
		return convertedSet.getTokens();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(String token) {
		return convertedSet.indexOf(token);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getLastToken()
	 */
	@Override
	public String getLastToken() {
		return convertedSet.getLastToken();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#size()
	 */
	@Override
	public int size() {
		return convertedSet.size();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestElement#deepCopy()
	 */
	@Override
	public ZestLoopTokenFileSet deepCopy() {
		try {
			ZestLoopTokenFileSet copy = new ZestLoopTokenFileSet(pathToFile);
			copy.convertedSet = convertedSet.deepCopy();
			return copy;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile(){
		return new File(pathToFile);
	}

	/**
	 * Gets the converted set.
	 *
	 * @return the converted set
	 */
	public ZestLoopTokenStringSet getConvertedSet() {
		return this.convertedSet;
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoopTokenSet#getFirstState()
	 */
	@Override
	public ZestLoopStateFile getFirstState(){
			ZestLoopStateFile stateFile=new ZestLoopStateFile(convertedSet);
			return stateFile;
	}

}
