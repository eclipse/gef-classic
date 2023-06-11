/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.text.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.ModelElement;
import org.eclipse.gef.examples.text.model.Style;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @since 3.1
 */
public class ContainerTreePart extends ExampleTreePart {

	public ContainerTreePart(Container model) {
		setModel(model);
	}

	@Override
	protected List<ModelElement> getModelChildren() {
		return getModel().getChildren();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("children")) //$NON-NLS-1$
			refreshChildren();
	}

	@Override
	public Container getModel() {
		return (Container) super.getModel();
	}

	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		if (getWidget() instanceof TreeItem item) {
			item.setExpanded(true);
		}
	}

	@Override
	protected void refreshVisuals() {
		StringBuffer label = new StringBuffer();
		switch (getModel().getType()) {
		case Container.TYPE_BULLETED_LIST:
			label.append("<bullet>"); //$NON-NLS-1$
			break;
		case Container.TYPE_COMMENT:
			label.append("<comment>"); //$NON-NLS-1$
			break;
		case Container.TYPE_IMPORT_DECLARATIONS:
			label.append("<import declarations>"); //$NON-NLS-1$
			break;

		case Container.TYPE_INLINE:
			Style style = getModel().getStyle();
			if (style.isSet(Style.PROPERTY_FONT_SIZE))
				label.append("<FONT SIZE>"); //$NON-NLS-1$
			if (style.isSet(Style.PROPERTY_BOLD))
				label.append("<B>"); //$NON-NLS-1$
			if (style.isSet(Style.PROPERTY_ITALIC))
				label.append("<I>"); //$NON-NLS-1$
			if (style.isSet(Style.PROPERTY_UNDERLINE))
				label.append("<U>"); //$NON-NLS-1$
			if (style.isSet(Style.PROPERTY_FONT))
				label.append("<FONT>"); //$NON-NLS-1$
			break;
		case Container.TYPE_PARAGRAPH:
			label.append("Paragraph"); //$NON-NLS-1$
			break;
		default:
			label.append("Unknown container"); //$NON-NLS-1$
			break;
		}
		setWidgetText(label.toString());
	}

}
