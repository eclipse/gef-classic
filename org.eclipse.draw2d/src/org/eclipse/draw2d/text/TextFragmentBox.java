/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

/**
 * A Geometric object for representing a TextFragment region on a line of Text. 
 */
public class TextFragmentBox
	extends ContentBox
{

/** The offset in characters **/
public int offset;

/** The length in characters **/
public int length;

private int ascent;
boolean truncated;

/**
 * Creates a new TextFragmentBox
 */
public TextFragmentBox() { }

/**
 * Returns the ascent of this TextFragmentBox
 * @return the ascent
 */
public int getAscent() {
	return ascent;
}

/**
 * Returns <code>true</code> if the bidi level is odd.  Right to left fragments should be
 * queried and rendered with the RLO control character inserted in front.
 * @return <code>true</code> if right-to-left
 * @since 3.1
 */
public boolean isRightToLeft() {
	// -1 % 2 == -1
	return bidiLevel % 2 == 1;
}

/**
 * Sets the ascent of this TextFragmentBox to the given value
 * @param a the ascent
 */
public void setAscent(int a) {
	ascent = a;
}

/**
 * Sets the height of this TextFragmentBox to the given value
 * @param h the height
 */
public void setHeight(int h) {
	height = h;
}

/**
 * Sets the width of this TextFragmentBox to the given value
 * @param w the width
 */
public void setWidth (int w) {
	width = w;
}

}