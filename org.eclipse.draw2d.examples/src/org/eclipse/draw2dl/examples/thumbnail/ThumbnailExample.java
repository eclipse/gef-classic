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
package org.eclipse.draw2dl.examples.thumbnail;

import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.draw2dl.parts.Thumbnail;
import org.eclipse.draw2dl.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This example demonstrates an overview window
 * 
 * @author hudsonr
 */
public class ThumbnailExample {

	private static org.eclipse.draw2dl.Figure contents;
	private static Shell mainShell, overviewShell;
	private static Dimension offset = new Dimension();

	public static void main(String[] args) {
		Display display = new Display();

		mainShell = new Shell(display);
		mainShell.setText("Source Shell");
		mainShell.setLayout(new FillLayout());
		org.eclipse.draw2dl.FigureCanvas mainCanvas = new FigureCanvas(mainShell);
		mainCanvas.setContents(getContents());

		overviewShell = new Shell(mainShell, SWT.TITLE | SWT.RESIZE
				| SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
		overviewShell.setText("Overview Shell");
		overviewShell.setLayout(new FillLayout());
		org.eclipse.draw2dl.LightweightSystem overviewLWS = new LightweightSystem(overviewShell);
		overviewLWS.setContents(createThumbnail(getContents()));

		mainShell.setSize(600, 600);
		mainShell.open();
		overviewShell.setSize(200, 200);
		overviewShell.open();

		while (!mainShell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		mainShell.dispose();
		overviewShell.dispose();
	}

	protected static org.eclipse.draw2dl.Figure getContents() {
		if (contents == null)
			contents = createContents();
		return contents;
	}

	private static org.eclipse.draw2dl.Figure createContents() {
		org.eclipse.draw2dl.Figure contents = new org.eclipse.draw2dl.Figure();
		contents.setBorder(new LineBorder());
		contents.setLayoutManager(new XYLayout());
		final org.eclipse.draw2dl.Figure figure1 = new org.eclipse.draw2dl.RectangleFigure();
		figure1.setBackgroundColor(org.eclipse.draw2dl.ColorConstants.green);
		figure1.setBounds(new Rectangle(50, 50, 200, 200));
		figure1.addMouseListener(new org.eclipse.draw2dl.MouseListener.Stub() {
			public void mousePressed(org.eclipse.draw2dl.MouseEvent event) {
				offset.setWidth(event.x - figure1.getLocation().x());
				offset.setHeight(event.y - figure1.getLocation().y());
			}

			public void mouseReleased(org.eclipse.draw2dl.MouseEvent event) {
				offset.setWidth(0);
				offset.setHeight(0);
			}
		});
		figure1.addMouseMotionListener(new org.eclipse.draw2dl.MouseMotionListener.Stub() {
			public void mouseDragged(org.eclipse.draw2dl.MouseEvent event) {
				Rectangle rect = figure1.getBounds().getCopy();
				rect.setX(event.x - offset.width());
				rect.setY(event.y - offset.height());
				figure1.setBounds(rect);
			}
		});
		contents.add(figure1);
		final org.eclipse.draw2dl.Figure figure2 = new RectangleFigure();
		figure2.setBackgroundColor(ColorConstants.blue);
		figure2.setBounds(new Rectangle(350, 350, 150, 200));
		figure2.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(org.eclipse.draw2dl.MouseEvent event) {
				offset.setWidth(event.x - figure2.getLocation().x());
				offset.setHeight(event.y - figure2.getLocation().y());
			}

			public void mouseReleased(org.eclipse.draw2dl.MouseEvent event) {
				offset.setWidth(0);
				offset.setHeight(0);
			}
		});
		figure2.addMouseMotionListener(new MouseMotionListener.Stub() {
			public void mouseDragged(MouseEvent event) {
				Rectangle rect = figure2.getBounds().getCopy();
				rect.setX(event.x - offset.width());
				rect.setY(event.y - offset.height());
				figure2.setBounds(rect);
			}
		});
		contents.add(figure2);
		return contents;
	}

	protected static org.eclipse.draw2dl.Figure createThumbnail(Figure source) {
		Thumbnail thumbnail = new Thumbnail();
		thumbnail.setBorder(new GroupBoxBorder("Overview Figure"));
		thumbnail.setSource(source);
		return thumbnail;
	}

}
