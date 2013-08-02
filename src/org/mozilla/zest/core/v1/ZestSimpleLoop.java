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
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class ZestSimpleLoop extends ZestStatement implements ZestLoop {
	private ZestRunner runner = null;
	private ZestScript script=null;
	private List<ZestStatement> loopStatements = new LinkedList<>(); // Contained
																		// as
																		// ZestContainer
	private ZestResponse tmpResponse = null;
	private List<ZestToken> tokens = null;
	private File inputFile = null;
	private int loopIndex;
	private ZestToken tmpToken = null;
	
	private ZestSimpleLoop(int stmtIndex){
		super(stmtIndex);
	}
	public ZestSimpleLoop(ZestRunner runner, ZestScript script,
			InitializationToken initializationToken)
			throws FileNotFoundException {
		super();
		init(runner, script, initializationToken);
	}

	public ZestSimpleLoop(int stmtIndex, ZestRunner runner, ZestScript script,
			InitializationToken initializationToken)
			throws FileNotFoundException {
		super(stmtIndex);
		init(runner, script, initializationToken);
	}

	private void init(ZestRunner runner, ZestScript script, InitializationToken initializationToken)
			throws FileNotFoundException {
		this.runner = runner;
		this.script=script;
		this.inputFile = initializationToken.getInputFile();
		if (this.inputFile != null) {
			initializeTokenList();
		} else {
			this.tokens = initializationToken.getTokens();
		}
		this.loopIndex = initializationToken.getStartLoopIndex();
		this.tmpToken = tokens.get(loopIndex);
	}

	private void initializeTokenList() throws FileNotFoundException {
		tokens = new LinkedList<>();
		Scanner in = new Scanner(inputFile);
		while (in.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(in.nextLine());
			while (st.hasMoreTokens()) {
				tokens.add(new ZestLoopToken(st.nextToken()));
			}
		}
		in.close();
	}

	@Override
	public ZestStatement getLast() {
		return loopStatements.get(loopStatements.size() - 1);
	}

	@Override
	public ZestStatement getStatement(int index) {
		return loopStatements.get(index);
	}

	@Override
	public int getIndex(ZestStatement child) {
		boolean found = false;
		int i;
		for (i = 0; !found && i < loopStatements.size(); i++) {
			found = loopStatements.get(i).equals(child);
		}
		if (found) {
			return i - 1;
		} else {
			return -1;
		}
	}

	@Override
	public void move(int index, ZestStatement stmt) {
		int currentPosition=getIndex(stmt);
		loopStatements.remove(currentPosition);
		loopStatements.add(index, stmt);
	}

	@Override
	public ZestStatement getChildBefore(ZestStatement child) {
		int childIndex=getIndex(child);
		if(childIndex==0){
			return null;
		}
		return loopStatements.get(childIndex-1);
	}

	@Override
	public ZestResponse loop(ZestResponse response) {
		//TODO implement!
		return null;
	}

	@Override
	void setPrefix(String oldPrefix, String newPrefix)
			throws MalformedURLException {
		// Do nothing
	}

	@Override
	void setUpRefs(ZestScript script) {
		// do nothing
	}

	@Override
	public List<ZestTransformation> getTransformations() {
		return new LinkedList<>();//no transformation TODO return null?
	}

	@Override
	public ZestStatement deepCopy() {//copy of the current state
		ZestSimpleLoop copy=new ZestSimpleLoop(this.getIndex());
		copy.inputFile=this.inputFile;
		copy.loopIndex=this.loopIndex;
		List<ZestToken> copyTokens=new LinkedList<>();
		for(int i=0; i<tokens.size(); i++){
			copyTokens.add(this.tokens.get(i).deepCopy());
		}
		copy.tokens=copyTokens;
		List<ZestStatement> copyLoopStatements=new LinkedList<>();
		for(int i=0; i<this.loopStatements.size(); i++){
			copyLoopStatements.add(this.loopStatements.get(i).deepCopy());
		}
		copy.loopStatements=copyLoopStatements;
		copy.runner=this.runner;//not copied!!
		copy.script=this.script.deepCopy();
		copy.tmpResponse=this.tmpResponse.deepCopy();
		int indexOfTmpToken=copy.getIndexToken(this.tmpToken);
		copy.tmpToken=copy.tokens.get(indexOfTmpToken);
		return copy;
	}
	private int getIndexToken(ZestToken token){
		int i;
		boolean found=false;
		for(i=0;!found && i<this.tokens.size(); i++){
			found=this.tokens.get(i).equals(token);
		}
		if(found){
			return i-1;
		}
		else{
			return -1;
		}
	}
	@Override
	public Set<String> getTokens(String tokenStart, String tokenEnd) {
		return null;// do nothing
	}

	public class InitializationToken {
		private int startLoopIndex = 0;
		private List<ZestToken> tokens = null;
		private File inputFile = null;

		public InitializationToken(List<ZestToken> tokens) {
			this(0, tokens);
		}

		public InitializationToken(int startLoopIndex, List<ZestToken> tokens) {
			this.startLoopIndex = startLoopIndex;
			this.tokens = tokens;
		}

		public InitializationToken(File inputFile) {
			this(0, inputFile);
		}

		public InitializationToken(int startLoopIndex, File inputFile) {
			this.startLoopIndex = startLoopIndex;
			this.inputFile = inputFile;
		}

		public int getStartLoopIndex() {
			return startLoopIndex;
		}

		public List<ZestToken> getTokens() {
			return this.tokens;
		}

		public File getInputFile() {
			return this.inputFile;
		}
	}

}
