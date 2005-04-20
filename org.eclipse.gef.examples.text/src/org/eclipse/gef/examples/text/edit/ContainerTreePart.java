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

import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.gef.examples.text.actions.TextActionConstants;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.Style;

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
			if (style.isSet(TextActionConstants.STYLE_FONT_SIZE))
				label.append("<FONT SIZE>");
			if (style.isSet(TextActionConstants.STYLE_BOLD))
				label.append("<B>");
			if (style.isSet(TextActionConstants.STYLE_ITALIC))
				label.append("<I>");
			if (style.isSet(TextActionConstants.STYLE_UNDERLINE))
				label.append("<U>");
			if (style.isSet(TextActionConstants.STYLE_FONT_NAME))
				label.append("<FONT>");
			if (style.isSet(TextActionConstants.BLOCK_ALIGN_LEFT)
					|| style.isSet(TextActionConstants.BLOCK_ALIGN_RIGHT)
					|| style.isSet(TextActionConstants.BLOCK_ALIGN_CENTER))
				label.append("<ALIGN>");
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
