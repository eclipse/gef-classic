/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
	extends FlowBox
{

/** The offset in pixels **/
public int offset;

/** The length in pixels **/
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