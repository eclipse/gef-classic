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
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * Provides for a frame-like border which contains a 
 * title bar for holding the title of a Figure.
 */
public class FrameBorder
	extends CompoundBorder
	implements LabeledBorder
{

protected static final SchemeBorder.Scheme
	SCHEME_FRAME = new SchemeBorder.Scheme(
		new Color[] {ColorConstants.button, ColorConstants.buttonLightest, ColorConstants.button},
		new Color[] {ColorConstants.buttonDarkest, ColorConstants.buttonDarker, ColorConstants.button}
	);

{createBorders();}

/**
 * Constructs a FrameBorder with its label set to
 * the name of the {@link TitleBarBorder TitleBarBorder}
 * class.
 * 
 * @since 2.0
 */
public FrameBorder(){}

/**
 * Constructs a FrameBorder with the title set to 
 * the passed String.
 *
 * @param label  Label or title of the frame.
 * @since 2.0
 */
public FrameBorder(String label){
	setLabel(label);
}

/**
 * Creates the necessary borders for this FrameBorder. The
 * inner border is a {@link TitleBarBorder TitleBarBorder}. The
 * outer border is a {@link SchemeBorder SchemeBorder}.
 * 
 * @since 2.0
 */
protected void createBorders(){
	inner = new TitleBarBorder();
	outer = new SchemeBorder(SCHEME_FRAME);
}

/**
 * Returns the inner border of this FrameBorder, which
 * contains the label for the FrameBorder.
 *
 * @return  The border holding the label.
 * @since 2.0
 */
protected LabeledBorder getLabeledBorder() {
	return (LabeledBorder) inner;
}

public String getLabel(){
	return getLabeledBorder().getLabel();
}

public void setLabel(String label){
	getLabeledBorder().setLabel(label);
}

public void setFont(Font font){
	getLabeledBorder().setFont(font);
}

}