/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
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

package org.eclipse.gef.examples.text.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @since 3.1
 */
public abstract class Container extends ModelElement {

	public static final String CHILDREN_PROPERTY = "children"; //$NON-NLS-1$
	public static final int TYPE_BULLETED_LIST = 1;
	public static final int TYPE_COMMENT = 2;
	public static final int TYPE_IMPORT_DECLARATIONS = 3;
	public static final int TYPE_PARAGRAPH = 4;
	public static final int TYPE_ROOT = 5;
	public static final int TYPE_INLINE = 6;
	public static final int TYPE_CODE = 7;

	private static final long serialVersionUID = 1;

	private final List<ModelElement> children = new ArrayList<>();
	private final Style style = new Style();

	protected Container(int type) {
		this.type = type;
	}

	public void add(ModelElement child) {
		add(child, -1);
	}

	public void add(ModelElement child, int index) {
		child.setParent(this);
		if (index == -1) {
			index = children.size();
		}
		children.add(index, child);
		firePropertyChange(CHILDREN_PROPERTY, null, child);
	}

	/**
	 * $TODO should this be reflexive?
	 *
	 * @since 3.1
	 * @param child
	 * @return true if this is a child or a transitive child of this container
	 */
	public boolean contains(ModelElement child) {
		do {
			if (child.getContainer() == this) {
				return true;
			}
			child = child.getContainer();
		} while (child != null);
		return false;
	}

	public List<ModelElement> getChildren() {
		return children;
	}

	public int getChildType() {
		return switch (getType()) {
		case TYPE_IMPORT_DECLARATIONS -> TextRun.TYPE_IMPORT;
		case TYPE_BULLETED_LIST -> TextRun.TYPE_BULLET;
		default -> 0;
		};
	}

	public Style getStyle() {
		return style;
	}

	public int remove(ModelElement child) {
		int index = children.indexOf(child);
		children.remove(child);
		child.setParent(null);
		firePropertyChange(CHILDREN_PROPERTY, child, null);
		return index;
	}

	public void removeAll(Collection<ModelElement> children) {
		if (children.removeAll(children)) {
			firePropertyChange(CHILDREN_PROPERTY, children, null);
		}
	}

	@Override
	public void setParent(Container container) {
		super.setParent(container);
		if (container == null) {
			getStyle().setParentStyle(null);
		} else {
			getStyle().setParentStyle(container.getStyle());
		}
	}

	/**
	 * @see org.eclipse.gef.examples.text.model.ModelElement#size()
	 */
	@Override
	public int size() {
		return getChildren().size();
	}

	abstract Container newContainer();

	public Container subdivideContainer(int offset) {
		Container result = newContainer();
		List<ModelElement> reparent = getChildren().subList(offset, getChildren().size());
		removeAll(reparent);
		result.getChildren().addAll(reparent);
		return result;
	}

}
