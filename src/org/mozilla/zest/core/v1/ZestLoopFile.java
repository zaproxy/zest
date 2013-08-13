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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * This class represent a loop through a list of strings given in input through
 * a file.
 */
public class ZestLoopFile extends ZestLoop<String> {
	private static int counter = 0;

	public ZestLoopFile() throws FileNotFoundException, IOException {
		this(File.createTempFile("emptyfile", ".txt"));
	}

	public ZestLoopFile(List<ZestStatement> stmts)
			throws FileNotFoundException, IOException {
		this(File.createTempFile("emptyfile", ".txt"), stmts);
	}

	private ZestLoopFile(int index) {
		super(index, "LoopFile" + counter++, new ZestLoopTokenStringSet(), new LinkedList<ZestStatement>());
	}

	/**
	 * Instantiates a new zest loop file.
	 * 
	 * @param file
	 *            the file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public ZestLoopFile(File file) throws FileNotFoundException {
		this("LoopFile" + counter++, file, new LinkedList<ZestStatement>());
	}

	/**
	 * Instantiates a new zest loop file.
	 * 
	 * @param index
	 *            the index
	 * @param file
	 *            the file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public ZestLoopFile(int index, File file) throws FileNotFoundException {
		this(index, "LoopFile" + counter++, file,
				new LinkedList<ZestStatement>());
	}

	/**
	 * Instantiates a new zest loop file.
	 * 
	 * @param pathToFile
	 *            the path to file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public ZestLoopFile(String pathToFile) throws FileNotFoundException {
		this("LoopFile" + counter++, pathToFile,
				new LinkedList<ZestStatement>());
	}

	/**
	 * Instantiates a new zest loop file.
	 * 
	 * @param index
	 *            the index
	 * @param pathToFile
	 *            the path to file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public ZestLoopFile(int index, String pathToFile)
			throws FileNotFoundException {
		this(index, "LoopFile" + counter++, pathToFile,
				new LinkedList<ZestStatement>());
	}

	/**
	 * Instantiates a new zest loop file.
	 * 
	 * @param pathToFile
	 *            the path to file
	 * @param statements
	 *            the statements
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public ZestLoopFile(String pathToFile, List<ZestStatement> statements)
			throws FileNotFoundException {
		super("LoopFile" + counter++, new ZestLoopTokenFileSet(pathToFile),
				statements);
	}

	/**
	 * Instantiates a new zest loop file.
	 * 
	 * @param file
	 *            the file
	 * @param statements
	 *            the statements
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public ZestLoopFile(File file, List<ZestStatement> statements)
			throws FileNotFoundException {
		super("LoopFile" + counter++, new ZestLoopTokenFileSet(file.getAbsolutePath()), statements);
	}

	/**
	 * Instantiates a new zest loop file.
	 * 
	 * @param index
	 *            the index
	 * @param pathToFile
	 *            the path to file
	 * @param statements
	 *            the statements
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public ZestLoopFile(int index, String pathToFile,
			List<ZestStatement> statements) throws FileNotFoundException {
		super("LoopFile" + counter++, new ZestLoopTokenFileSet(pathToFile),
				statements);
	}

	public ZestLoopFile(int index, String name, String pathToFile,
			List<ZestStatement> statements) throws FileNotFoundException {
		super(index, name, new ZestLoopTokenFileSet(pathToFile), statements);
	}

	/**
	 * Instantiates a new zest loop file.
	 * 
	 * @param index
	 *            the index
	 * @param file
	 *            the file
	 * @param statements
	 *            the statements
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public ZestLoopFile(int index, File file, List<ZestStatement> statements)
			throws FileNotFoundException {
		super(index, "LoopFile" + counter++, new ZestLoopTokenFileSet(file.getAbsolutePath()),
				statements);
	}

	public ZestLoopFile(int index, String name, File file,
			List<ZestStatement> statements) throws FileNotFoundException {
		super(index, name, new ZestLoopTokenFileSet(file.getAbsolutePath()), statements);
	}

	public ZestLoopFile(String name, String pathToFile,
			List<ZestStatement> statements) throws FileNotFoundException {
		super(name, new ZestLoopTokenFileSet(pathToFile), statements);
	}

	public ZestLoopFile(String name, File file, List<ZestStatement> statements)
			throws FileNotFoundException {
		super(name, new ZestLoopTokenFileSet(file.getAbsolutePath()), statements);
	}

	@Override
	public ZestLoopFile deepCopy() {
		ZestLoopFile copy = new ZestLoopFile(this.getIndex());
		copy.setCurrentState(this.getCurrentState().deepCopy());
		copy.setStatements(this.copyStatements());
		copy.setSet(this.getSet().deepCopy());
		return copy;
	}

	/**
	 * returns the file of this loop
	 * 
	 * @return the file of this loop
	 */
	public File getFile() {
		return this.getSet().getFile();
	}

	@Override
	public ZestLoopStateFile getCurrentState() {
		return (ZestLoopStateFile) super.getCurrentState();
	}

	@Override
	public ZestLoopTokenFileSet getSet() {
		return (ZestLoopTokenFileSet) super.getSet();
	}
	@Override
	public void setSet(ZestLoopTokenSet<String> set){
		if(set instanceof ZestLoopTokenFileSet){
			super.setSet(set);
		} else{
			System.err.println("The given set is not a "+this.getSet().getClass());
		}
	}
}
