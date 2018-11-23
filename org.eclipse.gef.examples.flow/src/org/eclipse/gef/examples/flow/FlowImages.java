/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		InputStream stream = FlowPlugin.class
				.getResourceAsStream("images/gear.gif");
		GEAR = new Image(null, stream);
		try {
			stream.close();
		} catch (IOException ioe) {
		}
	}

}
