/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
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
import org.eclipse.swt.widgets.Display;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ColorConstantTest extends Assert {

	@Test
	public void test_ColorConstantInit() {
		final Boolean result[] = new Boolean[2];
		result[0] = Boolean.FALSE;
		result[1] = Boolean.FALSE;

		Thread testThread = new Thread() {
			@Override
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
