/*******************************************************************************
 * Copyright (c) 2000, 2024 IBM Corporation and others.
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
package org.eclipse.draw2d.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.eclipse.draw2d.Container;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.junit.Before;
import org.junit.Test;

public class XYLayoutTest {
	protected static final Rectangle EMPTY_RECTANGLE = new Rectangle(0, 0, 0, 0);
	protected XYLayout layout;
	protected RectangleFigure figure;
	protected RectangleFigure dummy;
	protected Container contents;
	protected Rectangle constraint;

	@Before
	public void setUp() {
		constraint = new Rectangle(0, 0, 100, -1);
		figure = new RectangleFigure();
		figure.setPreferredSize(100, 150);
		dummy = new RectangleFigure();
		layout = new XYLayout();
		contents = new Container(layout);
		contents.add(figure, constraint);
		contents.add(dummy);
	}

	@Test
	public void testPreferredSize() {
		Dimension d = contents.getPreferredSize();

		assertEquals(100, d.width);
		assertEquals(150, d.height);
	}

	/**
	 * Use Case: Constraint must be of type {@link Rectangle} -> no error
	 */
	@Test
	public void testConstraint() {
		Rectangle r = (Rectangle) layout.getConstraint(figure);
		assertEquals(constraint, r);
	}

	/**
	 * Use Case: Constraint must be of type {@link Rectangle} -> error
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstraint() {
		contents.add(figure, (Object) 1);
	}

	/**
	 * Use Case: No constraint specified for dummy figure -> skip
	 */
	@Test
	public void testLayoutMissingConstraint() {
		assertEquals(dummy.getBounds(), EMPTY_RECTANGLE);
		layout.layout(contents);
		assertEquals(dummy.getBounds(), EMPTY_RECTANGLE);
	}

	/**
	 * Use Case: Constraint describes the position of the figure
	 */
	@Test
	public void testLayout() {
		assertEquals(figure.getBounds(), EMPTY_RECTANGLE);
		layout.layout(contents);
		assertNotEquals(figure.getBounds(), EMPTY_RECTANGLE);
	}
}
