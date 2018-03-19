/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The Class ZestLoop.
 *
 * @param <T> the generic type
 */
public abstract class ZestLoop<T> extends ZestStatement
        implements ZestContainer, Enumeration<ZestStatement> {

    /** contains all the statement inside the loop. */
    private List<ZestStatement> statements = new LinkedList<>();

    /** The variable name. */
    private String variableName = "";
    /** contains the snapshot of the current state of the loop. */
    private transient ZestLoopState<T> currentState;
    /** contains the index of the current statement considered. */
    private transient int stmtIndex = 0;

    private transient ZestRuntime runtime = null;

    /** Instantiates a new zest loop. */
    protected ZestLoop() {
        super();
    }

    protected ZestLoop(int index) {
        super(index);
    }

    public ZestLoop(String variableName) {
        this.variableName = variableName;
    }

    /**
     * Inits the Loop.
     *
     * @param set the initialization token set
     * @param statements the list of statements inside the loop
     */
    protected void init(ZestLoopTokenSet<T> set, List<ZestStatement> statements) {
        this.statements = statements;
        this.currentState = set.getFirstState();
    }
    /**
     * inits the loop refreshing the current state to the first considered state
     *
     * @param runtime the Zest runtime.
     */
    public void init(ZestRuntime runtime) {
        this.runtime = runtime;
        this.currentState = this.getSet().getFirstState();
    }
    /**
     * sets the current state to the new one (for subclasses).
     *
     * @param newSet the new sets the
     */
    protected void setSet(ZestLoopTokenSet<T> newSet) {
        this.setCurrentState(newSet.getFirstState());
    }

    /**
     * Sets the statements list.
     *
     * @param stmts the stmts list
     * @return the previous statement list
     */
    public List<ZestStatement> setStatements(List<ZestStatement> stmts) {
        List<ZestStatement> oldStatements = this.statements;
        this.statements = stmts;
        return oldStatements;
    }

    /**
     * Gets the statements.
     *
     * @return the statements list
     */
    public List<ZestStatement> getStatements() {
        return this.statements;
    }

    @Override
    public List<ZestStatement> getChildren() {
        return Collections.unmodifiableList(this.getStatements());
    }

    /**
     * increase the current state (ignoring all the statements which are still to be computed for
     * this loop: a new one starts).
     *
     * @param set the set of tokens to continue the loop.
     * @return the new state (of the following loop)
     */
    protected boolean loop(ZestLoopTokenSet<T> set) {
        return this.currentState.increase(set);
    }

    /**
     * ends the loop and set the state to the final value.
     *
     * @param set the set of tokens to end the loop.
     */
    protected void endLoop(ZestLoopTokenSet<T> set) {
        this.currentState.toLastState(set);
    }

    /**
     * adds a new statement inside the loop.
     *
     * @param stmt the new statement to add
     */
    public void addStatement(ZestStatement stmt) {
        this.add(this.statements.size(), stmt);
    }

    /**
     * Adds the statement in the specified index in the script.
     *
     * @param index the index at which the statement will be added
     * @param stmt the statement to add
     */
    public void add(int index, ZestStatement stmt) {
        ZestStatement prev = this;
        if (index == this.statements.size()) {
            // Add at the end
            this.statements.add(stmt);

        } else {
            this.statements.add(index, stmt);
        }
        if (index > 0) {
            prev = this.statements.get(index - 1);
        }
        // This will wire everything up
        stmt.insertAfter(prev);
    }

    /**
     * returns the current state of the loop.
     *
     * @return the current state
     */
    public ZestLoopState<T> getCurrentState() {
        return this.currentState;
    }

    /**
     * Sets the current state.
     *
     * @param newState the new current state
     */
    protected void setCurrentState(ZestLoopState<T> newState) {
        this.currentState = newState;
    }

    /**
     * return the current token considered inside the loop.
     *
     * @return the current token considered inside the loop
     */
    public T getCurrentToken() {
        return this.currentState.getCurrentToken();
    }
    /**
     * returns the current index (related to the current token) of the loop
     *
     * @return the current index (related to the current token)
     */
    public int getCurrentIndex() {
        return this.currentState.getCurrentIndex();
    }
    /**
     * returns the current statement index
     *
     * @return the index of the statement currently considered
     */
    public int getCurrentStatementIndex() {
        return this.stmtIndex;
    }

    /**
     * returns the set of the tokens in this loop.
     *
     * @return the set of the tokens in this loop
     */
    public abstract ZestLoopTokenSet<T> getSet();

    @Override
    public ZestStatement getLast() {
        if (statements == null || statements.isEmpty()) {
            return this;
        }
        return statements.get(statements.size() - 1);
    }

    @Override
    public ZestStatement getStatement(int index) {
        for (ZestStatement zr : this.statements) {
            if (zr.getIndex() == index) {
                return zr;
            }
            if (zr instanceof ZestContainer) {
                ZestStatement stmt = ((ZestContainer) zr).getStatement(index);
                if (stmt != null) {
                    return stmt;
                }
            }
        }
        return null;
    }

    @Override
    public int getIndex(ZestStatement child) {
        if (statements.contains(child)) {
            return statements.indexOf(child);
        } else {
            return -1;
        }
    }

    @Override
    public void move(int index, ZestStatement stmt) {
        if (this.statements.contains(stmt)) {
            this.statements.remove(stmt);
            this.statements.add(index, stmt);
        } else {
            throw new IllegalArgumentException("Not a direct child: " + stmt);
        }
    }

    @Override
    public boolean isSameSubclass(ZestElement ze) {
        return ze instanceof ZestLoop<?>;
    }

    @Override
    public ZestStatement getChildBefore(ZestStatement child) {
        if (this.statements.contains(child)) {
            int childIndex = this.statements.indexOf(child);
            if (childIndex > 1) {
                return this.statements.get(childIndex - 1);
            }
        }
        return null;
    }

    @Override
    public Set<String> getVariableNames() {
        Set<String> tokens = new HashSet<String>();
        tokens.add(this.getVariableName());
        for (ZestStatement stmt : this.statements) {
            if (stmt instanceof ZestContainer) {
                tokens.addAll(((ZestContainer) stmt).getVariableNames());

            } else if (stmt instanceof ZestAssignment) {
                tokens.add(((ZestAssignment) stmt).getVariableName());
            }
        }
        return tokens;
    }

    @Override
    public void setPrefix(String oldPrefix, String newPrefix) throws MalformedURLException {
        for (ZestStatement stmt : this.statements) {
            stmt.setPrefix(oldPrefix, newPrefix);
        }
    }

    @Override
    public abstract ZestLoop<T> deepCopy();

    @Override
    public boolean hasMoreElements() {
        boolean isLastLoop = isLastState();
        if (isLastLoop) {
            return false;
        }
        boolean isLastStmt = this.stmtIndex == statements.size();
        if (isLastStmt) {
            return false;
        }
        if (this.statements.get(stmtIndex) instanceof ZestControlLoopBreak) {
            return false;
        }
        return true;
    }
    /**
     * checks if the loop is on its last state
     *
     * @return true if the loop is over
     */
    public abstract boolean isLastState();
    /** increase the loop indexes */
    protected abstract void increase();
    /** ends the loop taking its current state to the last state */
    public abstract void toLastState();

    @Override
    public ZestStatement nextElement() {
        int currentStmt = stmtIndex;
        ++stmtIndex;
        if (stmtIndex == statements.size()) {
            this.increase();
            stmtIndex = 0;
        }
        ZestStatement newStatement = statements.get(currentStmt);
        if (newStatement instanceof ZestControlLoopBreak) {
            onControlBreak();
            return null;
        } else if (newStatement instanceof ZestControlLoopNext) {
            onControlNext();
            return statements.get(stmtIndex);
        }
        return statements.get(currentStmt);
    }
    /** act as a BREAK */
    public void onControlBreak() {
        toLastState();
        this.stmtIndex = statements.size();
    }
    /** act as a NEXT */
    public void onControlNext() {
        increase();
        this.stmtIndex = 0;
    }

    /**
     * Copy statements.
     *
     * @return the list of copied statements
     */
    public List<ZestStatement> copyStatements() {
        List<ZestStatement> copiedStatements = new LinkedList<>();
        if (this.getStatements() == null) {
            return copiedStatements;
        }

        for (ZestStatement stmt : getStatements()) {
            copiedStatements.add(stmt.deepCopy());
        }
        return copiedStatements;
    }

    /**
     * Returns the variable name.
     *
     * @return the variable name
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Sets the variable name.
     *
     * @param name the new variable name
     */
    public void setVariableName(String name) {
        this.variableName = name;
    }

    @Override
    public boolean isPassive() {
        return true;
    }

    @Override
    protected ZestStatement setPrev(ZestStatement prev) {
        for (ZestStatement statement : statements) {
            if (prev != null) {
                prev.setNext(statement);
            }
            prev = statement.setPrev(prev);
        }
        return prev;
    }

    protected ZestRuntime getRuntime() {
        return runtime;
    }
}
