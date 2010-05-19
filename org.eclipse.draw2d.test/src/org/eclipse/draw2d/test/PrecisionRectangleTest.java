/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author sshaw
 * 
 */
public class PrecisionRectangleTest extends TestCase {

	public void testCrop() {
		Insets insets = new Insets(2, 2, 2, 2);

		PrecisionRectangle prect = new PrecisionRectangle(new Rectangle(100,
				100, 250, 250));
		PrecisionRectangle copy = prect.getPreciseCopy();
		prect.performTranslate(30, 30);
		prect.performScale(2f);
		prect.crop(insets);
		prect.performScale(1 / 2f);
		prect.performTranslate(-30, -30);

		assertTrue(!prect.equals(copy));
	}

	public void testCrop2() {
		Insets insets = new Insets(1, 1, -1, -1);

		PrecisionRectangle prect = new PrecisionRectangle(new Rectangle(0, 0,
				3, 3));
		PrecisionRectangle copy = prect.getPreciseCopy();
		prect.performTranslate(1, 1);
		prect.performScale(4f);
		prect.crop(insets);
		prect.performScale(1 / 4f);
		prect.performTranslate(-1, -1);

		assertTrue(!prect.equals(copy));
	}
}
