/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.model;

/**
 * @since 3.1
 */
public abstract class ModelElement extends Notifier {

private Container container;

protected int type;

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