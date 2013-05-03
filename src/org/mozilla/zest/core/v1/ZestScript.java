/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ZestScript extends ZestStatement implements ZestContainer {

	public static final String ZEST_URL = "https://developer.mozilla.org/en-US/docs/Zest";
	public static final String ABOUT = "This is a Zest script. For more details about Zest visit " + ZEST_URL;

	public enum Type {Targeted, Active, Passive };

	private String about = ABOUT;
	private int zestVersion = 1;
	private String generatedBy;
	private String author;
	private String title;
	private String description;
	private String prefix;
	private String type;
	private ZestTokens tokens = new ZestTokens();
	
	private List<ZestStatement> statements = new ArrayList<ZestStatement>();
	private List<ZestAuthentication> authentication = new ArrayList<ZestAuthentication>();
	private List<ZestStatement> commonTests = new ArrayList<ZestStatement>();
	
	public ZestScript () {
		super();
	}
	
	public ZestScript (String title, String description, String type) {
		this();
		this.title = title;
		this.description = description;
		this.setType(type);
	}
	
	public ZestScript (String title, String description, Type type) {
		this();
		this.title = title;
		this.description = description;
		this.setType(type);
	}
	
	public void setType(Type type) {
		this.type = type.name();
	}
	
	public void setType(String type) {
		try {
			Type.valueOf(type);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unsupported type: " + type);
		}
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public ZestScript deepCopy() {
		ZestScript script = new ZestScript();
		this.duplicateTo(script);
		return script;
	}
	
	public void duplicateTo(ZestScript script) {
		script.about = this.about;
		script.zestVersion = this.zestVersion;
		script.generatedBy = this.generatedBy;
		script.author = this.author;
		script.title = this.title;
		script.description = this.description;
		script.prefix = this.prefix;
		script.tokens = this.tokens.deepCopy();
		script.type = this.type;
		
		for (ZestStatement zr : this.getStatements()) {
			script.add(zr.deepCopy());
		}
		for (ZestAuthentication za : this.getAuthentication()) {
			script.addAuthentication((ZestAuthentication)za.deepCopy());
		}
		for (ZestStatement zr : this.getCommonTests()) {
			script.addCommonTest(zr.deepCopy());
		}
		// Correct references in the copy... 
		script.setUpRefs();
		script.setTokens(this.getTokens().deepCopy());
	}

	private void setUpRefs() {
		for (ZestStatement zr : this.getStatements()) {
			zr.setUpRefs(this);
		}
	}

	public void add(ZestStatement req) {
		this.add(this.statements.size(), req);
	}
	
	public void add(int index, ZestStatement req) {
		ZestStatement prev = this;
		if (index == this.statements.size()) {
			// Add at the end
			this.statements.add(req);
			
		} else {
			this.statements.add(index, req);
		}
		if (index > 0) {
			prev = this.statements.get(index-1);
		}
		// This will wire everything up
		req.insertAfter(prev);
		updateTokens(req);
	}
	
	public void move(int index, ZestStatement req) {
		this.remove(req);
		this.add(index, req);
	}
	
	public void remove(ZestStatement req) {
		this.statements.remove(req);
		req.remove();
	}
	
	public void removeStatement(int index) {
		this.remove(this.statements.get(index));
	}
	
	public ZestStatement getStatement (int index) {
		for (ZestStatement zr : this.getStatements()) {
			if (zr.getIndex() == index) {
				return zr;
			}
			if (zr instanceof ZestContainer) {
				ZestStatement stmt = ((ZestContainer)zr).getStatement(index);
				if (stmt != null) {
					return stmt;
				}
			}
		}

		return null;
	}
	
	public void addCommonTest(ZestStatement req) {
		this.addCommonTest(this.commonTests.size(), req);
	}
	
	public void addCommonTest(int index, ZestStatement req) {
		ZestStatement prev = this;
		if (index == this.commonTests.size()) {
			// Add at the end
			this.commonTests.add(req);
			
		} else {
			this.commonTests.add(index, req);
		}
		if (index > 0) {
			prev = this.commonTests.get(index-1);
		}
		// This will wire everything up
		req.insertAfter(prev);
	}
	
	public void moveCommonTest(int index, ZestStatement req) {
		this.removeCommonTest(req);
		this.addCommonTest(index, req);
	}
	
	public void removeCommonTest(ZestStatement req) {
		this.commonTests.remove(req);
		req.remove();
	}
	
	public void removeCommonTest(int index) {
		this.remove(this.commonTests.get(index));
	}
	
	public ZestStatement getCommonTest (int index) {
		for (ZestStatement zr : this.getCommonTests()) {
			if (zr.getIndex() == index) {
				return zr;
			}
			if (zr instanceof ZestContainer) {
				ZestStatement stmt = ((ZestContainer)zr).getStatement(index);
				if (stmt != null) {
					return stmt;
				}
			}
		}

		return null;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ZestStatement> getStatements() {
		return statements;
	}

	public void setStatements(List<ZestStatement> statements) {
		this.statements = statements;
		this.setUpRefs();
	}
	
	public void addAuthentication(ZestAuthentication auth) {
		this.authentication.add(auth);
	}

	public void removeAuthentication(ZestAuthentication auth) {
		this.authentication.remove(auth);
	}

	public List<ZestAuthentication> getAuthentication() {
		return authentication;
	}

	public void setAuthentication(List<ZestAuthentication> authentication) {
		this.authentication = authentication;
	}
	
	public List<ZestStatement> getCommonTests() {
		return commonTests;
	}

	public void setCommonTests(List<ZestStatement> statements) {
		this.commonTests = statements;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String newPrefix) throws MalformedURLException {
		this.setPrefix(this.prefix, newPrefix);
	}

	public void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
		for (ZestStatement stmt : this.statements) {
			stmt.setPrefix(oldPrefix, newPrefix);
		}
		this.prefix = newPrefix;
	}

	public ZestTokens getTokens() {
		return this.tokens;
	}

	public void setTokens(ZestTokens tokens) {
		this.tokens = tokens;
	}

	private void updateTokens(ZestStatement statement) {
		Set<String> allTokens = statement.getTokens(this.tokens.getTokenStart(), this.tokens.getTokenEnd());
		for (String str : allTokens) {
			// Will default if not present
			this.tokens.addToken(str);
		}
	}

	public int getZestVersion() {
		return zestVersion;
	}

	public void setZestVersion(int zestVersion) {
		if (zestVersion != 1) {
			throw new IllegalArgumentException("Version " + zestVersion + " not supported by this class");
		}
		this.zestVersion = zestVersion;
	}

	public String getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public Set<String> getTokens(String tokenStart, String tokenEnd) {
		Set<String> tokens = new HashSet<String>();
		for (ZestStatement stmt : this.statements) {
			tokens.addAll(stmt.getTokens(tokenStart, tokenEnd));
		}
		return tokens;
	}

	@Override
	void setUpRefs(ZestScript script) {
		this.setUpRefs();
	}

	@Override
	public List<ZestTransformation> getTransformations() {
		List<ZestTransformation> list = new ArrayList<ZestTransformation>();
		for (ZestStatement stmt : this.statements) {
			list.addAll(stmt.getTransformations());
		}
		return list;
	}

	@Override
	public int getIndex (ZestStatement child) {
		return this.statements.indexOf(child);
	}

	@Override
	public ZestStatement getLast() {
		return null;
	}

	@Override
	public ZestStatement getChildBefore(ZestStatement child) {
		if (this.statements.contains(child)) {
			int childIndex = this.statements.indexOf(child);
			if (childIndex > 1) {
				return this.statements.get(childIndex-1);
			}
		}
		return null;
	}
	
}
