package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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