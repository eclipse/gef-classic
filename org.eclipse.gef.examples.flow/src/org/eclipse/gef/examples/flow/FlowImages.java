/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.graphics.Image;

/**
 * @author hudsonr
 */
public class FlowImages {

	public static final Image GEAR;

	static {
		InputStream stream = FlowPlugin.class.getResourceAsStream("images/gear.gif");
		GEAR = new Image(null, stream);
		try {
			stream.close();
		} catch (IOException ioe) {
		}
	}

}
