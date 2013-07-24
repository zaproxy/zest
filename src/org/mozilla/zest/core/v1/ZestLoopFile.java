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
/**
 * This class represent a loop through a list of strings given in input through a file
 */
public class ZestLoopFile extends ZestLoopString {
	/**
	 * the input file
	 */
	private final File file;
/**
 * The main construptor
 * @param file the file in input
 * @throws FileNotFoundException if the file does not exist
 */
	public ZestLoopFile(File file) throws FileNotFoundException {
		super();
		this.file = file;
		init();
	}
/**
 * Construptor
 * @param file the input file
 * @param statements the list of statements inside the loop
 * @throws FileNotFoundException if the file does not exist
 */
	public ZestLoopFile(File file, List<ZestStatement> statements)
			throws FileNotFoundException {
		super();
		this.file = file;
		for (ZestStatement stmt : statements) {
			this.addStatement(stmt);
		}
		init();
	}
/**
 * private method for initialization of the loop (TokenSet & first state)
 * @throws FileNotFoundException if the file does not exist
 */
	private void init() throws FileNotFoundException {
		Scanner in = new Scanner(this.file);
		ZestLoopState<String> initializationState;
		ZestLoopTokenSet<String> initializationTokenSet = new ZestLoopTokenSet<>();
		String line;
		while (in.hasNextLine()) {
			line = in.nextLine();
			if (!line.startsWith("#")) {
				initializationTokenSet.addToken(new ZestLoopToken<String>(in
						.nextLine()));
			}
		}
		initializationState = new ZestLoopState<>(initializationTokenSet);
		in.close();
		this.setState(initializationState);
	}
}
