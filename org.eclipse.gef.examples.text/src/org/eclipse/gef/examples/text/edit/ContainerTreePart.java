/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.edit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.examples.text.model.Container;

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

protected void refreshVisuals() {
	String label;
	switch (getContainer().getType()) {
		case Container.TYPE_BULLETED_LIST:
			label = "bullet";
			break;
		case Container.TYPE_COMMENT:
			label = "comment";
			break;
		case Container.TYPE_IMPORT_DECLARATIONS:
			label = "import declarations";
			break;

		default:
			label = "unknown container";
			break;
	}
	setWidgetText(label);
}

}