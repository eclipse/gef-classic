/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.editor;

import org.eclipse.gef.dnd.SimpleObjectTransfer;

/**
 * This Transfer expects a java.util.List as the object being transferred.
 * 
 * @author Pratik Shah
 * @since 3.1
 */
public class OutlineToDiagramTransfer 
	extends SimpleObjectTransfer
{

public static final String TYPE_NAME = "Outline To Diagram Transfer"; //$NON-NLS-1$
private static OutlineToDiagramTransfer TRANSFER = null;
private static final int TYPE_ID = registerType(TYPE_NAME);

private OutlineToDiagramTransfer() {
}

public static OutlineToDiagramTransfer getInstance() {
	if (TRANSFER == null)
		TRANSFER = new OutlineToDiagramTransfer();
	return TRANSFER;
}

protected int[] getTypeIds() {
	return new int[] {TYPE_ID};
}

protected String[] getTypeNames() {
	return new String[] {TYPE_NAME};
}

}
