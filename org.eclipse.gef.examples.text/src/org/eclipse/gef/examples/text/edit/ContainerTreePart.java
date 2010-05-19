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
package org.eclipse.gef.examples.text.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.Style;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @since 3.1
 */
public class ContainerTreePart extends ExampleTreePart {

public ContainerTreePart(Object model) {
	setModel(model);
}

protected List getModelChildren() {
	return ((Container)getModel()).getChildren();
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("children"))
		refreshChildren();
}

private Container getContainer() {
	return (Container)getModel();
}

protected void refreshChildren() {
	super.refreshChildren();
	if (getWidget() instanceof TreeItem) {
		TreeItem item = (TreeItem)getWidget();
		item.setExpanded(true);
	}
}

protected void refreshVisuals() {
	StringBuffer label = new StringBuffer();
	switch (getContainer().getType()) {
		case Container.TYPE_BULLETED_LIST:
			label.append("<bullet>");
			break;
		case Container.TYPE_COMMENT:
			label.append("<comment>");
			break;
		case Container.TYPE_IMPORT_DECLARATIONS:
			label.append("<import declarations>");
			break;

		case Container.TYPE_INLINE:
			Style style = getContainer().getStyle();
			if (style.isSet(Style.PROPERTY_FONT_SIZE))
				label.append("<FONT SIZE>");
			if (style.isSet(Style.PROPERTY_BOLD))
				label.append("<B>");
			if (style.isSet(Style.PROPERTY_ITALIC))
				label.append("<I>");
			if (style.isSet(Style.PROPERTY_UNDERLINE))
				label.append("<U>");
			if (style.isSet(Style.PROPERTY_FONT))
				label.append("<FONT>");
			break;
		case Container.TYPE_PARAGRAPH:
			label.append("Paragraph");
			break;
		default:
			label.append("Unknown container");
			break;
	}
	setWidgetText(label.toString());
}

}
