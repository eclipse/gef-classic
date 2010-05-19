/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import java.util.Map;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.UpdateListener;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PaintDamageEraseTest extends TestCase implements UpdateListener {

	private FigureCanvas fc;
	protected IFigure contents;
	protected Shell shell;
	protected IFigure insideBox;
	protected Rectangle lastDamaged;
	protected Display d;
	protected IFigure container;
	private String errMsg;

	public void testPaintDamageErase() {
		doRelativeBoundsMixedMove2();
		doRelativeBoundsMixedMove();
		doRelativeBoundsPositiveMove();
		doRelativeBoundsNegativeMove();
		doAbsoluteBoundsMixedMove2();
		doAbsoluteBoundsMixedMove();
		doAbsoluteBoundsPositiveMove();
		doAbsoluteBoundsNegativeMove();
	}

	/**
	 * Tests a mixed move (x pos,y neg) with relative bounds.
	 * 
	 */
	public void doRelativeBoundsMixedMove2() {
		doIndividualSetup(true);

		doTestEraseBoxAfterMove(15, -15, "RelativeMixed2");

		doIndividualTearDown();
	}

	/**
	 * Tests a mixed move (x pos,y neg) with absolute bounds.
	 * 
	 */
	public void doAbsoluteBoundsMixedMove2() {
		doIndividualSetup(false);

		doTestEraseBoxAfterMove(15, -15, "AbsoluteMixed2");

		doIndividualTearDown();
	}

	/**
	 * Tests a mixed move (x neg,x pos) with relative bounds.
	 * 
	 */
	public void doRelativeBoundsMixedMove() {
		doIndividualSetup(true);

		doTestEraseBoxAfterMove(-15, 15, "RelativeMixed");

		doIndividualTearDown();
	}

	/**
	 * Tests a mixed move (x neg, y pos) with absolute bounds.
	 * 
	 */
	public void doAbsoluteBoundsMixedMove() {
		doIndividualSetup(false);

		doTestEraseBoxAfterMove(-15, 15, "AbsoluteMixed");

		doIndividualTearDown();
	}

	/**
	 * Tests a positive move with relative bounds.
	 * 
	 */
	public void doRelativeBoundsPositiveMove() {
		doIndividualSetup(true);

		doTestEraseBoxAfterMove(15, 15, "RelativePositive");

		doIndividualTearDown();
	}

	/**
	 * Tests a negative move with relative bounds.
	 * 
	 */
	public void doRelativeBoundsNegativeMove() {
		doIndividualSetup(true);

		doTestEraseBoxAfterMove(-15, -15, "RelativeNegative");

		doIndividualTearDown();
	}

	/**
	 * Tests a positive move with absolute bounds.
	 * 
	 */
	public void doAbsoluteBoundsPositiveMove() {
		doIndividualSetup(false);

		doTestEraseBoxAfterMove(15, 15, "AbsolutePositive");

		doIndividualTearDown();
	}

	/**
	 * Tests a negative move with absolute bounds.
	 * 
	 */
	public void doAbsoluteBoundsNegativeMove() {
		doIndividualSetup(false);

		doTestEraseBoxAfterMove(-15, -15, "AbsoluteNegative");

		doIndividualTearDown();
	}

	/**
	 * This method tests to see if the old box has been properly erased by
	 * moving the box by the given offsets and asserting that the damaged region
	 * is correct.
	 * 
	 * 
	 */
	public void doTestEraseBoxAfterMove(int xOffset, int yOffset, String msg) {
		insideBox.setBounds(insideBox.getBounds().getTranslated(xOffset,
				yOffset));

		container.getUpdateManager().performUpdate();

		assertNotNull(lastDamaged);

		int x, y, h, w;

		if (xOffset >= 0) {
			x = 25;
			w = xOffset + 25;
		} else {
			x = 25 + xOffset;
			w = Math.abs(xOffset) + 25;
		}

		if (yOffset >= 0) {
			y = 25;
			h = yOffset + 25;
		} else {
			y = 25 + yOffset;
			h = Math.abs(yOffset) + 25;
		}

		if (lastDamaged == null || x != lastDamaged.x || y != lastDamaged.y
				|| h != lastDamaged.height || w != lastDamaged.width) {
			errMsg += " " + msg;
		}
	}

	protected void doIndividualSetup(boolean useRelativeCoordinates) {
		if (useRelativeCoordinates) {
			container = new FigureWithRelativeCoords();
		} else {
			container = new RectangleFigure();
		}

		container.setBackgroundColor(ColorConstants.red);
		container.setBorder(new MarginBorder(15));

		insideBox = new RectangleFigure();

		container.add(insideBox);

		insideBox.setBackgroundColor(ColorConstants.green);

		if (useRelativeCoordinates) {
			insideBox.setBounds(new Rectangle(10, 10, 25, 25));
		} else {
			insideBox.setBounds(new Rectangle(25, 25, 25, 25));
		}

		fc.setContents(container);

		container.getUpdateManager().performUpdate();
		while (shell.getDisplay().readAndDispatch()) {
		}
		container.getUpdateManager().addUpdateListener(this);
	}

	protected void doIndividualTearDown() {
		container.getUpdateManager().removeUpdateListener(this);
		fc.setContents(new Figure());

		insideBox = null;
		lastDamaged = null;
		container = null;
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		errMsg = new String();
		d = Display.getDefault();
		shell = new Shell(d);

		String appName = getClass().getName();
		appName = appName.substring(appName.lastIndexOf('.') + 1);

		shell.setText(appName);
		shell.setLayout(new FillLayout());

		fc = new FigureCanvas(shell);

		fc.setSize(200, 200);

		shell.open();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		shell.dispose();

		if (!errMsg.equals("")) {
			throw new AssertionFailedError(errMsg);
		}
	}

	/**
	 * Constructor for PaintDamageEraseTest.
	 * 
	 * @param name
	 */
	public PaintDamageEraseTest(String name) {
		super(name);
	}

	public void notifyPainting(Rectangle damage, Map dirtyRegions) {
		lastDamaged = damage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.UpdateListener#notifyValidating()
	 */
	public void notifyValidating() {
		// nothing
	}

}

class FigureWithRelativeCoords extends RectangleFigure {
	protected boolean useLocalCoordinates() {
		return true;
	}
}
