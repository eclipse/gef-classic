/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.gef.examples.text.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.examples.text.model.TextRun;

/**
 * @since 3.1
 */
public class TextRunTreePart extends ExampleTreePart {

public TextRunTreePart(Object model) {
	setModel(model);
}

public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("text"))
		refreshVisuals();
}

protected void refreshVisuals() {
	TextRun run = (TextRun)getModel();
	String s = run.getText();
	if (s.length() > 50)
		s = s.substring(0, 50) + "...";
	setWidgetText(s);
}

}
