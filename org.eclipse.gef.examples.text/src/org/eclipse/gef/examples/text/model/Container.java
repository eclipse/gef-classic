/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 3.1
 */
public class Container extends ModelElement {

private List children = new ArrayList();

/**
 * @since 3.1
 */
public Container() {
	super();
}

public void add(ModelElement child) {
	add(child, -1);
}

public void add(ModelElement child, int index) {
	child.setParent(this);
	if (index == -1) index = children.size();
	children.add(index, child);
	firePropertyChange("children", null, child);
}

/**
 * $TODO should this be reflexive?
 * @since 3.1
 * @param child
 * @return
 */
public boolean contains(ModelElement child) {
	do {
		if (child.getContainer() == this) return true;
		child = child.getContainer();
	} while (child != null);
	return false;
}

public List getChildren() {
	return children;
}

public int remove(ModelElement child) {
	int index = children.indexOf(child);
	children.remove(child);
	firePropertyChange("children", child, null);
	return index;
}

/**
 * @see org.eclipse.gef.examples.text.model.ModelElement#size()
 */
int size() {
	return getChildren().size();
}

}