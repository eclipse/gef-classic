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

package org.eclipse.gef.examples.text.figures;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.graphics.Image;

/**
 * @since 3.1
 */
public final class Images {

	public static final Image IMPORTS = createImage("imports.gif"); //$NON-NLS-1$
	public static final Image IMPORT = createImage("importstatement.gif"); //$NON-NLS-1$
	public static final Image PUBLIC = createImage("public.gif"); //$NON-NLS-1$
	public static final Image PRIVATE = createImage("private.gif"); //$NON-NLS-1$
	public static final Image PROTECTED = createImage("protected.gif"); //$NON-NLS-1$

	private static Image createImage(String name) {
		try (InputStream stream = Images.class.getResourceAsStream(name)) {
			return new Image(null, stream);
		} catch (IOException exc) {
		}
		return null;
	}

	private Images() {
		throw new UnsupportedOperationException("Utility class shall not be instantiated!"); //$NON-NLS-1$
	}

}
