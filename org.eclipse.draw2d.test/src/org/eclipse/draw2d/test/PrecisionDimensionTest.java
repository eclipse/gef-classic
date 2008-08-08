/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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

import org.eclipse.draw2d.geometry.PrecisionDimension;

/**
 * JUnit Tests for PrecisionDimension.
 * 
 * @author Anthony Hunter
 */
public class PrecisionDimensionTest extends TestCase {

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=227977 
	 */
	public void testEquals() {
		PrecisionDimension p1 = new PrecisionDimension(0.1, 0.1);
		PrecisionDimension p2 = new PrecisionDimension(0.2, 0.2);
		assertFalse(p1.equals(p2));
	}
}
