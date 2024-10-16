/*******************************************************************************
 * Copyright (c) 2004, 2024 IBM Corporation and others.
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

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;

public final class TestImages {

	static final Image depth_8 = loadImage("icons/bits8.jpg"); //$NON-NLS-1$
	static final Image depth_24 = loadImage("icons/bits24.jpg"); //$NON-NLS-1$

	private static Image loadImage(final String imageName) {
		return ImageDescriptor.createFromFile(TestImages.class, imageName).createImage();
	}

	private TestImages() {
		throw new UnsupportedOperationException("Utility class shall not be instantiated!"); //$NON-NLS-1$
	}

}
