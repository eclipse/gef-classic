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
import java.util.Collection;
import java.util.List;

/**
 * @since 3.1
 */
public class Container extends ModelElement {

public static final int TYPE_COMMENT = 1;
public static final int TYPE_IMPORT_DECLARATIONS = 2;
public static final int TYPE_ROOT = 3;
public static final int TYPE_BULLETED_LIST = 4;

private List children = new ArrayList();

public Container(int type) {
	this.type = type;
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

public int getChildType() {
	switch (getType()) {
		case TYPE_IMPORT_DECLARATIONS:
			return TextRun.TYPE_IMPORT;
		case TYPE_BULLETED_LIST:
			return TextRun.TYPE_BULLET;
		default:
			return 0;
	}
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

public void removeAll(Collection children) {
	if (children.removeAll(children))
		firePropertyChange("children", children, null);
}

/**
 * @see org.eclipse.gef.examples.text.model.ModelElement#size()
 */
int size() {
	return getChildren().size();
}

public Container subdivideContainer(int offset) {
	Container result = new Container(getType());
	List reparent = getChildren().subList(offset, getChildren().size());
	removeAll(reparent);
	result.getChildren().addAll(reparent);
	return result;
}

}