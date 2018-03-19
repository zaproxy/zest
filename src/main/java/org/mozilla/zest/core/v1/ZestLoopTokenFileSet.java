/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * The Class ZestLoopTokenFileSet.<br>
 * This class does not override ZestLoopTokenStringSet because<br>
 * the tokens inside the converted set must not be transient
 */
public class ZestLoopTokenFileSet extends ZestElement implements ZestLoopTokenSet<String> {

    /** The path to file. */
    private String pathToFile = null;

    /** The converted set. */
    private transient ZestLoopTokenStringSet convertedSet = null;

    /**
     * Instantiates a new zest loop token file set.
     *
     * @param pathToFile the path to file
     * @throws FileNotFoundException the file not found exception
     */
    public ZestLoopTokenFileSet(String pathToFile) throws FileNotFoundException {
        super();
        this.pathToFile = pathToFile;
        this.convertedSet = this.getConvertedSet(new File(pathToFile));
    }

    /**
     * private method for initialization of the loop (TokenSet & first state).
     *
     * @param file the file
     * @return the zest loop token string set
     * @throws FileNotFoundException if the file does not exist
     */
    private ZestLoopTokenStringSet getConvertedSet(File file) throws FileNotFoundException {
        if (this.convertedSet == null) {
            try (Scanner in = new Scanner(file)) {
                ZestLoopTokenStringSet initializationSet = new ZestLoopTokenStringSet();
                String line;
                while (in.hasNextLine()) {
                    line = in.nextLine();
                    if (!line.startsWith("#")
                            && !line.isEmpty()) { // discards commented and empty line
                        initializationSet.addToken(line);
                    }
                }
                this.convertedSet = initializationSet;
            }
        }
        return convertedSet;
    }

    @Override
    public String getToken(int index) {
        return this.getConvertedSet().getToken(index);
    }

    public List<String> getTokens() {
        List<String> listOfTokens = getConvertedSet().getTokens();
        return Collections.unmodifiableList(listOfTokens);
    }

    @Override
    public int indexOf(String token) {
        return getConvertedSet().indexOf(token);
    }

    @Override
    public String getLastToken() {
        return getConvertedSet().getLastToken();
    }

    @Override
    public int size() {
        return this.getConvertedSet().size();
    }

    @Override
    public ZestLoopTokenFileSet deepCopy() {
        try {
            ZestLoopTokenFileSet copy = new ZestLoopTokenFileSet(pathToFile);
            copy.convertedSet = this.getConvertedSet().deepCopy();
            return copy;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public File getFile() {
        return new File(pathToFile);
    }

    public String getFilePath() {
        return this.pathToFile;
    }

    /**
     * Gets the converted set.
     *
     * @return the converted set
     */
    protected ZestLoopTokenStringSet getConvertedSet() {
        try {
            return this.getConvertedSet(new File(pathToFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this.convertedSet;
    }

    @Override
    public ZestLoopStateFile getFirstState() {
        ZestLoopStateFile stateFile = new ZestLoopStateFile(this);
        return stateFile;
    }
}
