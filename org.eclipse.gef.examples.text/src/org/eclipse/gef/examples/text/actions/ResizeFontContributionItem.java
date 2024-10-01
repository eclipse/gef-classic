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
package org.eclipse.gef.examples.text.actions;

import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.ui.IPartService;

import org.eclipse.gef.examples.text.model.Style;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class ResizeFontContributionItem extends StyleComboContributionItem {

	private static final String[] INIT_SIZES = { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
			"24", "26", "28", "36", "48", "72" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	public ResizeFontContributionItem(IPartService service) {
		super(service);
	}

	@Override
	protected String[] getItems() {
		return INIT_SIZES;
	}

	@Override
	protected String getProperty() {
		return Style.PROPERTY_FONT_SIZE;
	}

	@Override
	protected void handleWidgetSelected(SelectionEvent e) {
		Integer fontSize = null;
		try {
			fontSize = Integer.parseInt(combo.getText());
		} catch (NumberFormatException nfe) {
		}
		if (fontSize != null && !fontSize.equals(styleService.getStyle(getProperty()))) {
			// No refresh required
			styleService.setStyle(getProperty(), fontSize);
		} else {
			refresh();
		}
	}

}
