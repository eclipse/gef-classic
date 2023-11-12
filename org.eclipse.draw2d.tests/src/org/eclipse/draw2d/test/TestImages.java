/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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

import java.io.InputStream;

import org.eclipse.swt.graphics.Image;

public class TestImages {

	static final Image depth_8;
	static final Image depth_24;

	static {
		InputStream is = TestImages.class.getResourceAsStream("icons/bits8.gif"); //$NON-NLS-1$
		depth_8 = new Image(null, is);
		try {
			is.close();
		} catch (Exception e) {
		}

		is = TestImages.class.getResourceAsStream("icons/bits24.jpg"); //$NON-NLS-1$
		depth_24 = new Image(null, is);
		try {
			is.close();
		} catch (Exception e) {
		}
	}

}
