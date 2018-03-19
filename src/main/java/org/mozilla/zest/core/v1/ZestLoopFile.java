/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/** This class represent a loop through a list of strings given in input through a file. */
public class ZestLoopFile extends ZestLoop<String> {

    private ZestLoopTokenFileSet set = null;

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
     * @param index the index of the statement
     * @throws IOException
     * @throws FileNotFoundException
     */
    private ZestLoopFile(int index) throws FileNotFoundException, IOException {
        super(index);
        this.set =
                new ZestLoopTokenFileSet(
                        File.createTempFile("emptyfile", ".txt").getAbsolutePath());
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Instantiates a new zest loop file.
     *
     * @param file the file containing the loop values
     * @throws FileNotFoundException the file not found exception
     */
    private ZestLoopFile(File file) throws FileNotFoundException {
        super();
        this.set = new ZestLoopTokenFileSet(file.getAbsolutePath());
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Instantiates a new zest loop file.
     *
     * @param pathToFile the path to the file which contains the loop values
     * @throws FileNotFoundException the file not found exception
     */
    public ZestLoopFile(String pathToFile) throws FileNotFoundException {
        super();
        this.set = new ZestLoopTokenFileSet(pathToFile);
        super.init(set, new LinkedList<ZestStatement>());
    }

    /**
     * Instantiates a new zest loop file.
     *
     * @param index the index of the statement
     * @param pathToFile the path to the file which contains the loop values
     * @throws FileNotFoundException the file not found exception
     */
    public ZestLoopFile(int index, String pathToFile) throws FileNotFoundException {
        super(index);
        this.set = new ZestLoopTokenFileSet(pathToFile);
        super.init(set, new LinkedList<ZestStatement>());
    }

    @Override
    public ZestLoopFile deepCopy() {
        ZestLoopFile copy;
        try {
            copy = new ZestLoopFile(this.getIndex());
            copy.setVariableName(this.getVariableName());
            copy.setCurrentState(this.getCurrentState().deepCopy());
            copy.setStatements(this.copyStatements());
            copy.setSet(this.getSet().deepCopy());
            copy.setEnabled(this.isEnabled());
            return copy;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns the file of this loop.
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
        return this.set;
    }

    @Override
    public void setSet(ZestLoopTokenSet<String> set) {
        if (set instanceof ZestLoopTokenFileSet) {
            try {
                ZestLoopTokenFileSet fileSet = (ZestLoopTokenFileSet) set;
                this.set = new ZestLoopTokenFileSet(fileSet.getFile().getAbsolutePath());
                super.init(set, getStatements());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException(
                    "Invelid set. It must be instance of " + ZestLoopTokenFileSet.class.getName());
        }
    }

    @Override
    public boolean isLastState() {
        return super.getCurrentState().isLastState(getSet());
    }

    @Override
    public void increase() {
        super.getCurrentState().increase(getSet());
    }

    @Override
    public void toLastState() {
        getCurrentState().toLastState(getSet());
    }

    @Override
    public String getCurrentToken() {
        if (super.getCurrentToken() == null) {
            super.init(getSet(), getStatements());
        }
        return super.getCurrentToken();
    }

    public boolean loop() {
        return super.loop(getSet());
    }

    public void endLoop() {
        super.endLoop(getSet());
    }
}
