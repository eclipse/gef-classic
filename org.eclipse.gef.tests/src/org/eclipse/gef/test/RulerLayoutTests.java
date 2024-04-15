/*******************************************************************************
 * Copyright (c) 2024 Patrick Ziegler and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.internal.ui.rulers.RulerFigure;
import org.eclipse.gef.internal.ui.rulers.RulerLayout;
import org.eclipse.gef.rulers.RulerProvider;

import org.junit.Before;
import org.junit.Test;

public class RulerLayoutTests {
	private static final Rectangle EMPTY_RECTANGLE = new Rectangle(0, 0, 0, 0);
	private RulerLayout rulerLayout;
	private Figure figure;
	private Figure dummy;
	private Figure container;
	private int constraint;

	@Before
	public void setUp() {
		constraint = 42;
		figure = new Figure();
		dummy = new Figure();
		container = new RulerFigure(true, RulerProvider.UNIT_CENTIMETERS);
		container.add(figure, (Object) constraint);
		container.add(dummy);
		rulerLayout = (RulerLayout) container.getLayoutManager();
	}

	/**
	 * Use Case: Constraint must be of type int -> no error
	 */
	@Test
	public void testConstraint() {
		int c = rulerLayout.getConstraint(figure);
		assertEquals(constraint, c);
	}

	/**
	 * Use Case: Constraint must be of type int -> error
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstraint() {
		container.add(figure, new Object());
	}

	/**
	 * Use Case: No constraint specified for dummy figure -> skip
	 */
	@Test
	public void testLayoutMissingConstraint() {
		assertEquals(dummy.getBounds(), EMPTY_RECTANGLE);
		rulerLayout.layout(container);
		assertEquals(dummy.getBounds(), EMPTY_RECTANGLE);
	}

	/**
	 * Use Case: Constraint describes the position of the figure
	 */
	@Test
	public void testLayout() {
		assertEquals(figure.getBounds(), EMPTY_RECTANGLE);
		rulerLayout.layout(container);
		assertNotEquals(figure.getBounds(), EMPTY_RECTANGLE);
	}
}
