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
package gef.bugs;

import org.eclipse.draw2dl.ColorConstants;
import org.eclipse.draw2dl.FigureCanvas;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.examples.AbstractExample;
import org.eclipse.draw2dl.text.FlowPage;
import org.eclipse.draw2dl.text.SimpleTextLayout;
import org.eclipse.draw2dl.text.TextFlow;

/**
 * Problem scenario - computeSize does not include vertical scrollbar
 */
public class ComputeSizeAndScrolbars extends AbstractExample {

/**
 * @see AbstractExample#getContents()
 */
protected IFigure getContents() {
	FlowPage page = new FlowPage();
	page.setOpaque(true);
	page.setBackgroundColor(ColorConstants.white);
	TextFlow courier = new TextFlow("Courier Courier Courier");
	courier.setLayoutManager(new SimpleTextLayout(courier));
	TextFlow heading = new TextFlow("Heading Heading");
	courier.setFont(COURIER);
	heading.setFont(HEADING_1);
	page.add(courier);
	page.add(heading);
	return page;
}

public static void main(String[] args) {
	new ComputeSizeAndScrolbars().run();
}

protected void setFigureCanvas(FigureCanvas canvas) {
	super.setFigureCanvas(canvas);
	canvas.setVerticalScrollBarVisibility(FigureCanvas.ALWAYS);
	canvas.getViewport().setContentsTracksWidth(true);
}

}
