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

public class ZestLoopFile extends ZestLoopValues {
	private final File file;

	public ZestLoopFile(File file) throws FileNotFoundException {
		super();
		this.file = file;
		init();
	}

	public ZestLoopFile(File file, List<ZestStatement> statements)
			throws FileNotFoundException {
		super();
		this.file = file;
		for (ZestStatement stmt : statements) {
			this.addStatement(stmt);
		}
		init();
	}

	private void init() throws FileNotFoundException {
		Scanner in = new Scanner(this.file);
		ZestLoopState<String> initializationState;
		ZestLoopTokenSet<String> initializationTokenSet = new ZestLoopTokenSet<>();
		String line;
		while (in.hasNextLine()) {
			line = in.nextLine();
			if (!line.startsWith("#")) {
				initializationTokenSet.addToken(new ZestLoopTokenValue(in
						.nextLine()));
			}
		}
		initializationState = new ZestLoopState<>(initializationTokenSet);
		in.close();
		this.setState(initializationState);
	}
}
