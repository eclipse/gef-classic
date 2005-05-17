/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.model;

/**
 * @since 3.1
 */
public abstract class ModelElement extends Notifier {

private static final long serialVersionUID = 1;
private Container container;
protected int type;

public Block getBlockContainer() {
	Container parent = getContainer();
	while (parent != null && !(parent instanceof Block))
		parent = parent.getContainer();
	return (Block)parent;
}

/**
 * @return Returns the container.
 */
public Container getContainer() {
	return container;
}

public int getType() {
	return type;
}

/**
 * @since 3.1
 * @param container
 */
public void setParent(Container container) {
	this.container = container;
}

public void setType(int type) {
	this.type = type;
}

abstract int size();

}
