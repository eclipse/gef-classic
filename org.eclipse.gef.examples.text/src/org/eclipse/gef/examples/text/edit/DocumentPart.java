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

import java.util.Iterator;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.actions.StyleService;
import org.eclipse.gef.examples.text.model.Style;
import org.eclipse.gef.examples.text.model.TextRun;

/**
 * @since 3.1
 */
public class DocumentPart
	extends BlockTextualPart
	implements TextStyleManager
{

public DocumentPart(Object model) {
	super(model);
}

protected void createEditPolicies() {
	installEditPolicy("Text Editing", new BlockEditPolicy());
}

public Object getAdapter(Class key) {
	if (key == TextStyleManager.class)
		return this;
	return super.getAdapter(key);
}

public TextLocation getLocation(Point absolute) {
	return null;
}

public TextLocation getNextLocation(int movement, TextLocation current, Rectangle caret) {
	return null;
}

public Object getStyleState(String styleID, SelectionRange range) {
	return StyleService.STATE_EDITABLE;
}

public Object getStyleValue(String styleID, SelectionRange range) {
	if (styleID.equals(Style.PROPERTY_BOLD)) {
		for (Iterator iter = range.getLeafParts().iterator(); iter.hasNext();) {
			TextRun run = (TextRun)((TextualEditPart)iter.next()).getModel();
			if (!run.getContainer().getStyle().isBold())
				return Boolean.FALSE;
		}
		return Boolean.TRUE;
	} else if (styleID.equals(Style.PROPERTY_FONT_SIZE)) {
		int fontHeight = -1;
		for (Iterator iter = range.getLeafParts().iterator(); iter.hasNext();) {
			TextRun run = (TextRun)((TextualEditPart)iter.next()).getModel();
			if (fontHeight == -1)
				fontHeight = run.getContainer().getStyle().getFontHeight();
			else if (fontHeight != run.getContainer().getStyle().getFontHeight())
				return StyleService.UNDEFINED;
		}
		return new Integer(fontHeight);
	} else if (styleID.equals(Style.PROPERTY_FONT)) {
		String fontName = null;
		for (Iterator iter = range.getLeafParts().iterator(); iter.hasNext();) {
			TextRun run = (TextRun)((TextualEditPart)iter.next()).getModel();
			if (fontName == null)
				fontName = run.getContainer().getStyle().getFontFamily();
			else if (!fontName.equals(run.getContainer().getStyle().getFontFamily()))
				return StyleService.UNDEFINED;
		}
		return fontName;
	} else if (styleID.equals(Style.PROPERTY_ITALIC)) {
		for (Iterator iter = range.getLeafParts().iterator(); iter.hasNext();) {
			TextRun run = (TextRun)((TextualEditPart)iter.next()).getModel();
			if (!run.getContainer().getStyle().isItalic())
				return Boolean.FALSE;
		}
		return Boolean.TRUE;
	} else if (styleID.equals(Style.PROPERTY_UNDERLINE)) {
		for (Iterator iter = range.getLeafParts().iterator(); iter.hasNext();) {
			TextRun run = (TextRun)((TextualEditPart)iter.next()).getModel();
			if (!run.getContainer().getStyle().isUnderline())
				return Boolean.FALSE;
		}
		return Boolean.TRUE;
	} else if (Style.PROPERTY_ALIGNMENT.equals(styleID)) {
		int alignment = 0;
		for (Iterator iter = range.getLeafParts().iterator(); iter.hasNext();) {
			TextRun run = (TextRun)((TextualEditPart)iter.next()).getModel();
			Style style = run.getBlockContainer().getStyle();
			if (alignment == 0)
				alignment = style.getAlignment();
			if (!style.isSet(styleID) || style.getAlignment() != alignment)
				return StyleService.UNDEFINED;
		}
		return new Integer(alignment);
	}
	return StyleService.UNDEFINED;
}

}