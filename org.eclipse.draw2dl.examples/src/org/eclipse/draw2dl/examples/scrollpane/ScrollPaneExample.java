/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.examples.scrollpane;

import org.eclipse.draw2dl.examples.AbstractExample;
import org.eclipse.draw2dl.examples.ExampleUtil;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.*;

/**
 * The scrollpane example shows how draw2dl uses getPreferredSize() to determine when
 * scrollbars should be displayed.  One window shows a view of a fixed-sized figure.  The
 * other shows a FlowLayout that wraps from left-to-right, and then top-to-bottom.
 * @author hudsonr
 */
public class ScrollPaneExample extends AbstractExample {

int count = 1;

public static void main(String[] args) {
	new ScrollPaneExample().run();
}

protected org.eclipse.draw2dl.IFigure getContents() {
	org.eclipse.draw2dl.Figure mainPanel = new org.eclipse.draw2dl.Figure();
	mainPanel.add(
		newScrollingFrame("Flow layouts",
			ExampleUtil.createToolbarLayout()));

	org.eclipse.draw2dl.Figure fixedSize = new Figure();
	fixedSize.setBorder(new LineBorder());
	fixedSize.setPreferredSize(100,100);
	mainPanel.add(
		newScrollingFrame("fixed 100x100",
			fixedSize));

	return mainPanel;
}

private InternalFrame newScrollingFrame(String title, IFigure contents){
	InternalFrame frame = new InternalFrame();
	frame.setLabel(title);
	org.eclipse.draw2dl.ScrollPane scrollpane = new ScrollPane();
	scrollpane.setBorder(new GroupBoxBorder("scrollpane"));
//	scrollpane.setScrollBarVisibility(scrollpane.ALWAYS);
	scrollpane.getViewport().setContentsTracksWidth(true);
	scrollpane.setContents(contents);
	frame.add(scrollpane);
	frame.setBounds(new Rectangle(count*20, count*20, 200,140));
	return frame;
}

}
