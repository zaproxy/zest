/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.zest.core.v1;

/**
 * The Class ZestClientFailException.
 */
public class ZestClientFailException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Client. */
	private ZestElement element = null;

	/**
	 * Instantiates a new zest client fail exception.
	 *
	 * @param client the client
	 * @param message the message
	 */
	public ZestClientFailException (ZestElement client, String message) {
		super(message);
		this.element = client;
	}

	/**
	 * Instantiates a new zest client fail exception.
	 *
	 * @param client the client
	 * @param message the message
	 */
	public ZestClientFailException (ZestElement client, Throwable cause) {
		super(cause);
		this.element = client;
	}

	/**
	 * Instantiates a new zest client fail exception.
	 *
	 * @param client the client
	 */
	public ZestClientFailException (ZestElement client) {
		super();
		this.element = client;
	}

	/**
	 * Gets the client.
	 *
	 * @return the client
	 */
	public ZestElement getElement() {
		return element;
	}
	
}
