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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.text.FlowPage;

public class CommentPage 
	extends FlowPage 
{
	
private static final Color COLOR = new Color(null, 255, 255, 170);

public CommentPage() {
	setBackgroundColor(COLOR);
}
	
public Insets getInsets() {
	return new Insets(7, 11, 7, 11);
}

protected void paintFigure(Graphics g) {
	g.fillRoundRectangle(getBounds().getCropped(new Insets(5)), 12, 12);
}

}