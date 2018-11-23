/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		InputStream is = TestImages.class
				.getResourceAsStream("icons/bits8.gif");
		depth_8 = new Image(null, is);
		try {
			is.close();
		} catch (Exception e) {
		}

		is = TestImages.class.getResourceAsStream("icons/bits24.jpg");
		depth_24 = new Image(null, is);
		try {
			is.close();
		} catch (Exception e) {
		}
	}

}
