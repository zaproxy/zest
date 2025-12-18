/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package org.zaproxy.zest.impl.jackson;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import java.io.Serial;

public final class HtmlSafeCharacterEscapes extends CharacterEscapes {
    @Serial private static final long serialVersionUID = 1L;

    private static final int[] HTML_SAFE_ESCAPES;

    private static final HtmlSafeCharacterEscapes sInstance = new HtmlSafeCharacterEscapes();

    static {
        HTML_SAFE_ESCAPES = CharacterEscapes.standardAsciiEscapesForJSON();
        HTML_SAFE_ESCAPES['<'] = CharacterEscapes.ESCAPE_STANDARD;
        HTML_SAFE_ESCAPES['>'] = CharacterEscapes.ESCAPE_STANDARD;
        HTML_SAFE_ESCAPES['&'] = CharacterEscapes.ESCAPE_STANDARD;
        HTML_SAFE_ESCAPES['='] = CharacterEscapes.ESCAPE_STANDARD;
        HTML_SAFE_ESCAPES['\''] = CharacterEscapes.ESCAPE_STANDARD;
    }

    private HtmlSafeCharacterEscapes() {}

    public static HtmlSafeCharacterEscapes instance() {
        return sInstance;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return HTML_SAFE_ESCAPES;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        return null;
    }
}
