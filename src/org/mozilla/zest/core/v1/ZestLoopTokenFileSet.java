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

public class ZestLoopTokenFileSet extends ZestElement implements ZestLoopTokenSet<String> {
	private String pathToFile = null;
	private transient ZestLoopTokenStringSet convertedSet = null;
	
	public ZestLoopTokenFileSet(String pathToFile) throws FileNotFoundException {
		super();
		this.pathToFile = pathToFile;
		this.convertedSet = this.init(new File(pathToFile));
	}

	/**
	 * private method for initialization of the loop (TokenSet & first state).
	 * 
	 * @throws FileNotFoundException
	 *             if the file does not exist
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

	@Override
	public void addToken(String token) {
		throw new IllegalArgumentException("Operation not allowed for "
				+ this.getClass().getName());
	}

	@Override
	public String getToken(int index) {
		return convertedSet.getToken(index);
	}

	@Override
	public List<String> getTokens() {
		return convertedSet.getTokens();
	}

	@Override
	public int indexOf(String token) {
		return convertedSet.indexOf(token);
	}

	@Override
	public String getLastToken() {
		return convertedSet.getLastToken();
	}

	@Override
	public int size() {
		return convertedSet.size();
	}

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
	public File getFile(){
		return new File(pathToFile);
	}

	public ZestLoopTokenStringSet getConvertedSet() {
		return this.convertedSet;
	}
	@Override
	public ZestLoopStateFile getFirstState(){
			ZestLoopStateFile stateFile=new ZestLoopStateFile(convertedSet);
			return stateFile;
	}

}
