/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.text;

/**
 * This class is for INTERNAL use only.
 *
 * @since 3.1
 */
public class BidiInfo {

	/**
	 * Odd-sized array consisting of bidi levels, interleaved with the offsets at
	 * which levels change.
	 */
	public int[] levelInfo;

	/**
	 * Indicates if the ZWJ character needs to be prepended to text being rendered.
	 */
	public boolean leadingJoiner;

	/**
	 * Indicates if the ZRJ character needs to be appended to the text being
	 * rendered..
	 */
	public boolean trailingJoiner;

}
