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
package org.eclipse.gef.examples.text.actions;

import org.eclipse.gef.examples.text.model.Style;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.IPartService;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class ResizeFontContributionItem extends StyleComboContributionItem {

	private static final String[] INIT_SIZES = new String[] { "8", "9", "10",
			"11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36",
			"48", "72" };

	public ResizeFontContributionItem(IPartService service) {
		super(service);
	}

	protected String[] getItems() {
		return INIT_SIZES;
	}

	protected String getProperty() {
		return Style.PROPERTY_FONT_SIZE;
	}

	protected void handleWidgetSelected(SelectionEvent e) {
		Integer fontSize = null;
		try {
			fontSize = new Integer(combo.getText());
		} catch (NumberFormatException nfe) {
		}
		if (fontSize != null
				&& !fontSize.equals(styleService.getStyle(getProperty())))
			// No refresh required
			styleService.setStyle(getProperty(), fontSize);
		else
			refresh();
	}

}
