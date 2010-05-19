/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.test;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.AbstractFlowBorder;
import org.eclipse.draw2d.text.TextFragmentBox;

/**
 * @since 3.1
 */
public class AbstractTextTest extends BaseTestCase {

	static class TestBorder extends AbstractFlowBorder {
		private Insets insets;

		TestBorder(Insets insets) {
			this.insets = insets;
		}

		public Insets getInsets(IFigure figure) {
			return insets;
		}
	}

	public Rectangle expected = new Rectangle();

	protected void assertFragmentLocation(TextFragmentBox box) {
		assertEquals("Fragment location error", box.getX(), expected.x);
		assertEquals("Fragment location error",
				box.getBaseline() - box.getAscent(), expected.y);
		assertEquals("Fragment location error",
				box.getBaseline() + box.getDescent(), expected.bottom());
		assertEquals("Fragment location error", box.getWidth() + box.getX(),
				expected.right());
	}

}
