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

package org.eclipse.gef.examples.text.figures;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.text.FlowPage;

public class CommentPage
	extends FlowPage
{

private static final Image BEGIN = new Image(null, CommentPage.class.getResourceAsStream("javadoc_begin.gif"));
private static final Image END = new Image(null, CommentPage.class.getResourceAsStream("javadoc_end.gif"));
private static final Insets INSETS = new Insets(9, 15, 12, 10);
private static final Color COMMENT_FG = ColorConstants.darkBlue;

public CommentPage() {
	setForegroundColor(COMMENT_FG);
}

public Insets getInsets() {
	return INSETS;
}

protected void paintFigure(Graphics g) {
	g.drawImage(BEGIN, bounds.x, bounds.y);
	g.drawImage(END, bounds.x + 6, bounds.bottom() - 10);
	g.setForegroundColor(ColorConstants.darkBlue);
	g.drawLine(bounds.x + 9, bounds.y + 11, bounds.x + 9, bounds.bottom() - 11);
	g.drawLine(bounds.x + 25, bounds.y + 5, bounds.right() - 5, bounds.y + 5);
	g.drawLine(bounds.x + 30, bounds.bottom() - 5, bounds.right() - 5, bounds.bottom() - 5);
	g.drawLine(bounds.right() - 5, bounds.y + 5, bounds.right() - 5, bounds.bottom() - 5);
}

}