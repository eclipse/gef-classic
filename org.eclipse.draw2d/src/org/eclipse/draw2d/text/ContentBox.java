/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
 * FlowBoxes that are leaf nodes.
 * 
 * @author Pratik Shah
 * @since 3.1
 */
public class ContentBox extends FlowBox
{
	
private int bidiLevel = -1;

/**
 * @return the Bidi level of this box, if one has been set; -1 otherwise
 * @see #setBidiLevel(int)
 */
public int getBidiLevel() {
	return bidiLevel;
}

/**
 * Returns <code>true</code> if the bidi level for this box is right-to-left.
 * @see org.eclipse.draw2d.text.FlowBox#isBidi()
 */
public boolean isBidi() {
	return bidiLevel % 2 == 1;
}

/**
 * Sets the Bidi level of this fragment.  It is used to rearrange fragments as defined
 * by the Unicode Bi-directional algorithm.  Valid values are -1 (meaning no Bidi level),
 * or any non-negative integer less than 62.
 * @param newLevel the new BidiLevel
 * @see #getBidiLevel()
 */
public void setBidiLevel(int newLevel) {
	bidiLevel = newLevel;
}

}