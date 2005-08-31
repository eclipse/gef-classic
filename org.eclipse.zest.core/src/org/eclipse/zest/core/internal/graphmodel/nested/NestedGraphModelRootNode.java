/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphmodel.nested;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * The root model node.
 * 
 * @author ccallendar
 */
public class NestedGraphModelRootNode extends NestedGraphModelNode {

	private static final Image IMG_HOME = ImageDescriptor.createFromFile(NestedGraphModelRootNode.class, "/icons/home.gif").createImage();

	/**
	 * Creates a new root node with the text "Root" and the home icon.
	 * The data is set to be null.
	 * @param model the model
	 */
	public NestedGraphModelRootNode(NestedGraphModel model) {
		super(model, "Root", IMG_HOME, null);
	}

}
