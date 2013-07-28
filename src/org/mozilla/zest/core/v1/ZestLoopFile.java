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
import java.util.LinkedList;
import java.util.List;
/**
 * This class represent a loop through a list of strings given in input through a file
 */
public class ZestLoopFile extends ZestLoop<File>{
	public ZestLoopFile(File file) throws FileNotFoundException {
		this(file, new LinkedList<ZestStatement>());
	}
	public ZestLoopFile(int index, File file) throws FileNotFoundException{
		this(index, file, new LinkedList<ZestStatement>());
	}
	public ZestLoopFile(String pathToFile) throws FileNotFoundException{
		this(pathToFile, new LinkedList<ZestStatement>());
	}
	public ZestLoopFile(int index, String pathToFile) throws FileNotFoundException{
		this(index, pathToFile, new LinkedList<ZestStatement>());
	}
	public ZestLoopFile(String pathToFile, List<ZestStatement> statements) throws FileNotFoundException{
		super(new ZestLoopStateFile(pathToFile), statements);
	}
	public ZestLoopFile(File file, List<ZestStatement> statements) throws FileNotFoundException{
		super(new ZestLoopStateFile(file), statements);
	}
	public ZestLoopFile(int index, String pathToFile, List<ZestStatement> statements) throws FileNotFoundException{
		super(new ZestLoopStateFile(pathToFile), statements);
	}
	public ZestLoopFile( int index, File file, List<ZestStatement> statements) throws FileNotFoundException{
		super(index, new ZestLoopStateFile(file), statements);
	}
}
