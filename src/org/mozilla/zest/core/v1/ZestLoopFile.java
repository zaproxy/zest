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
	
	/** The counter. */
	private static int counter = 0;

	/**
	 * Instantiates a new zest loop file.
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ZestLoopFile() throws FileNotFoundException, IOException {
		this(File.createTempFile("emptyfile", ".txt"));
	}

	/**
	 * Instantiates a new zest loop file.
	 *
	 * @param stmts the stmts
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ZestLoopFile(List<ZestStatement> stmts)
			throws FileNotFoundException, IOException {
		this(File.createTempFile("emptyfile", ".txt"), stmts);
	}

	/**
	 * Instantiates a new zest loop file.
	 *
	 * @param index the index
	 */
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

	/**
	 * Instantiates a new zest loop file.
	 *
	 * @param index the index
	 * @param name the name
	 * @param pathToFile the path to file
	 * @param statements the statements
	 * @throws FileNotFoundException the file not found exception
	 */
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

	/**
	 * Instantiates a new zest loop file.
	 *
	 * @param index the index
	 * @param name the name
	 * @param file the file
	 * @param statements the statements
	 * @throws FileNotFoundException the file not found exception
	 */
	public ZestLoopFile(int index, String name, File file,
			List<ZestStatement> statements) throws FileNotFoundException {
		super(index, name, new ZestLoopTokenFileSet(file.getAbsolutePath()), statements);
	}

	/**
	 * Instantiates a new zest loop file.
	 *
	 * @param name the name
	 * @param pathToFile the path to file
	 * @param statements the statements
	 * @throws FileNotFoundException the file not found exception
	 */
	public ZestLoopFile(String name, String pathToFile,
			List<ZestStatement> statements) throws FileNotFoundException {
		super(name, new ZestLoopTokenFileSet(pathToFile), statements);
	}

	/**
	 * Instantiates a new zest loop file.
	 *
	 * @param name the name
	 * @param file the file
	 * @param statements the statements
	 * @throws FileNotFoundException the file not found exception
	 */
	public ZestLoopFile(String name, File file, List<ZestStatement> statements)
			throws FileNotFoundException {
		super(name, new ZestLoopTokenFileSet(file.getAbsolutePath()), statements);
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoop#deepCopy()
	 */
	@Override
	public ZestLoopFile deepCopy() {
		ZestLoopFile copy = new ZestLoopFile(this.getIndex());
		copy.setCurrentState(this.getCurrentState().deepCopy());
		copy.setStatements(this.copyStatements());
		copy.setSet(this.getSet().deepCopy());
		return copy;
	}

	/**
	 * returns the file of this loop.
	 *
	 * @return the file of this loop
	 */
	public File getFile() {
		return this.getSet().getFile();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoop#getCurrentState()
	 */
	@Override
	public ZestLoopStateFile getCurrentState() {
		return (ZestLoopStateFile) super.getCurrentState();
	}

	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoop#getSet()
	 */
	@Override
	public ZestLoopTokenFileSet getSet() {
		return (ZestLoopTokenFileSet) super.getSet();
	}
	
	/* (non-Javadoc)
	 * @see org.mozilla.zest.core.v1.ZestLoop#setSet(org.mozilla.zest.core.v1.ZestLoopTokenSet)
	 */
	@Override
	public void setSet(ZestLoopTokenSet<String> set){
		if(set instanceof ZestLoopTokenFileSet){
			super.setSet(set);
		} else{
			System.err.println("The given set is not a "+this.getSet().getClass());
		}
	}
}
