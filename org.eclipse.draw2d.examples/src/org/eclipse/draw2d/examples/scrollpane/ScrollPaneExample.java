/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
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
package org.eclipse.draw2d.examples.scrollpane;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.examples.AbstractExample;
import org.eclipse.draw2d.examples.ExampleUtil;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The scrollpane example shows how draw2d uses getPreferredSize() to determine
 * when scrollbars should be displayed. One window shows a view of a fixed-sized
 * figure. The other shows a FlowLayout that wraps from left-to-right, and then
 * top-to-bottom.
 *
 * @author hudsonr
 */
public class ScrollPaneExample extends AbstractExample {

	int count = 1;

	public static void main(String[] args) {
		new ScrollPaneExample().run();
	}

	@Override
	protected IFigure createContents() {
		Figure mainPanel = new Figure();
		mainPanel.add(newScrollingFrame("Flow layouts", ExampleUtil.createToolbarLayout())); //$NON-NLS-1$

		Figure fixedSize = new Figure();
		fixedSize.setBorder(new LineBorder());
		fixedSize.setPreferredSize(100, 100);
		mainPanel.add(newScrollingFrame("fixed 100x100", fixedSize)); //$NON-NLS-1$

		return mainPanel;
	}

	private InternalFrame newScrollingFrame(String title, IFigure contents) {
		InternalFrame frame = new InternalFrame();
		frame.setLabel(title);
		ScrollPane scrollpane = new ScrollPane();
		scrollpane.setBorder(new GroupBoxBorder("scrollpane")); //$NON-NLS-1$
		//	scrollpane.setScrollBarVisibility(scrollpane.ALWAYS);
		scrollpane.getViewport().setContentsTracksWidth(true);
		scrollpane.setContents(contents);
		frame.add(scrollpane);
		frame.setBounds(new Rectangle(count * 20, count * 20, 200, 140));
		return frame;
	}

}
