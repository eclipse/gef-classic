package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Font;

/**
 * LabeledBorders have a text message somewhere on them. The Font for the text can be set.
 * LabeledBorders should not change their Insets when the label changes, therefore,
 * Figures using this Border should repaint() when updating the label, and revalidate()
 * when changing the Font.
 */
public interface LabeledBorder 
	extends Border
{

/**
 * Returns the label for this Border.
 * @return The label for this Border
 */
String getLabel();

/**
 * Sets the Font for the label.
 * @param f The Font to be set
 */
void setFont(Font f);

/**
 * Sets the text to be displayed as the label for this Border.
 * @param l The text
 */
void setLabel(String l);

}