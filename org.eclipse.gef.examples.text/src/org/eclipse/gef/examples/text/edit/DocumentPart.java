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

package org.eclipse.gef.examples.text.edit;

import org.eclipse.gef.EditPart;

import org.eclipse.gef.examples.text.SelectionRange;
import org.eclipse.gef.examples.text.actions.StyleService;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.Style;
import org.eclipse.gef.examples.text.model.TextRun;
import org.eclipse.gef.examples.text.requests.CaretRequest;
import org.eclipse.gef.examples.text.requests.SearchResult;

/**
 * @since 3.1
 */
public class DocumentPart extends BlockTextPart implements TextStyleManager {

	public DocumentPart(Container model) {
		super(model);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy("Text Editing", new BlockEditPolicy()); //$NON-NLS-1$
	}

	@Override
	public <T> T getAdapter(final Class<T> key) {
		if (key == TextStyleManager.class) {
			return key.cast(this);
		}
		return super.getAdapter(key);
	}

	@Override
	public void getTextLocation(CaretRequest search, SearchResult result) {
		if (search.getType() == CaretRequest.DOCUMENT) {
			search.isInto = true;
			search.isForward = !search.isForward;
			search.isRecursive = true;
			search.setType(CaretRequest.COLUMN);
		}
		super.getTextLocation(search, result);
	}

	@Override
	public Object getStyleState(String styleID, SelectionRange range) {
		return StyleService.STATE_EDITABLE;
	}

	@Override
	public Object getStyleValue(String styleID, SelectionRange range) {
		switch (styleID) {
		case Style.PROPERTY_BOLD:
			for (EditPart ep : range.getLeafParts()) {
				TextRun run = (TextRun) ep.getModel();
				if (!run.getContainer().getStyle().isBold()) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		case Style.PROPERTY_FONT_SIZE: {
			int fontHeight = -1;
			for (EditPart editPart : range.getLeafParts()) {
				TextRun run = (TextRun) editPart.getModel();
				if (fontHeight == -1) {
					fontHeight = run.getContainer().getStyle().getFontHeight();
				} else if (fontHeight != run.getContainer().getStyle().getFontHeight()) {
					return StyleService.UNDEFINED;
				}
			}
			return Integer.valueOf(fontHeight);
		}
		case Style.PROPERTY_FONT: {
			String fontName = null;
			for (EditPart editPart : range.getLeafParts()) {
				TextRun run = (TextRun) editPart.getModel();
				if (fontName == null) {
					fontName = run.getContainer().getStyle().getFontFamily();
				} else if (!fontName.equals(run.getContainer().getStyle().getFontFamily())) {
					return StyleService.UNDEFINED;
				}
			}
			return fontName;
		}
		case Style.PROPERTY_ITALIC:
			for (EditPart editPart : range.getLeafParts()) {
				TextRun run = (TextRun) editPart.getModel();
				if (!run.getContainer().getStyle().isItalic()) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		case Style.PROPERTY_UNDERLINE:
			for (EditPart name : range.getLeafParts()) {
				TextRun run = (TextRun) name.getModel();
				if (!run.getContainer().getStyle().isUnderline()) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		case Style.PROPERTY_ALIGNMENT: {
			int alignment = 0;
			for (EditPart editPart : range.getLeafParts()) {
				TextRun run = (TextRun) editPart.getModel();
				Style style = run.getBlockContainer().getStyle();
				if (alignment == 0) {
					alignment = style.getAlignment();
				}
				if (!style.isSet(styleID) || style.getAlignment() != alignment) {
					return StyleService.UNDEFINED;
				}
			}
			return Integer.valueOf(alignment);
		}
		case Style.PROPERTY_ORIENTATION: {
			int orientation = 0;
			for (EditPart editPart : range.getLeafParts()) {
				TextRun run = (TextRun) editPart.getModel();
				Style style = run.getBlockContainer().getStyle();
				if (orientation == 0) {
					orientation = style.getOrientation();
				}
				if (!style.isSet(styleID) || style.getOrientation() != orientation) {
					return StyleService.UNDEFINED;
				}
			}
			return Integer.valueOf(orientation);
		}
		default:
			break;
		}
		return StyleService.UNDEFINED;
	}

}