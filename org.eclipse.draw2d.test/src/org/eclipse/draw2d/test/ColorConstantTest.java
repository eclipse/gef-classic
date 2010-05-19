/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
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

import org.eclipse.swt.widgets.Display;

public class ColorConstantTest extends TestCase {
	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test_ColorConstantInit() {
		final Boolean result[] = new Boolean[2];
		result[0] = Boolean.FALSE;
		result[1] = Boolean.FALSE;

		Thread testThread = new Thread() {
			public void run() {
				try {
					Class.forName("org.eclipse.draw2d.ColorConstants");
					result[0] = Boolean.TRUE;
				} catch (Error e) {
					result[0] = Boolean.FALSE;
				} catch (Exception ex) {
					result[0] = Boolean.FALSE;
				}

				result[1] = Boolean.TRUE;
			}
		};

		testThread.start();

		while (!result[1].booleanValue()) {
			Display.getDefault().readAndDispatch();
		}

		assertTrue(result[0].booleanValue());

	}
}
