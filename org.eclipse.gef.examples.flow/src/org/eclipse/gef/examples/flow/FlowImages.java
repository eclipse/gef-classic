/*******************************************************************************
 * Copyright (c) 2003, 2024 IBM Corporation and others.
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
package org.eclipse.gef.examples.flow;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author hudsonr
 */
public class FlowImages {

	public static final Image GEAR = ImageDescriptor.createFromFile(FlowPlugin.class, "images/gear.gif") //$NON-NLS-1$
			.createImage(true);

	private FlowImages() {
		throw new UnsupportedOperationException("Utility class shell not be instantiated!"); //$NON-NLS-1$
	}
}
