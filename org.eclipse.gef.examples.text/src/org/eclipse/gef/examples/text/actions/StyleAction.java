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

package org.eclipse.gef.examples.text.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.Assert;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.ui.actions.GEFActionConstants;

/**
 * @since 3.1
 */
public class StyleAction 
	extends Action {

private StyleService service;
private StyleListener styleListener = new StyleListener() {
	public void styleChanged(String styleID) {
		if (styleID == null || styleID.equals(getId()))
			refresh();
	}
};
	
public StyleAction(StyleService service, String styleID) {
	this(service, styleID, IAction.AS_CHECK_BOX);
}

/**
 * @param part
 * @since 3.1
 */
public StyleAction(StyleService service, String styleID, int style) {
	setStyleService(service);
	setId(styleID);
	configureStyleAction(this);
}

protected boolean calculateEnabled() {
	return service.getStyleState(getId()) == StyleService.STATE_EDITABLE;
}

static void configureStyleAction(IAction a) {
	String styleID = a.getId();
	a.setActionDefinitionId(styleID);
	if (styleID.equals(GEFActionConstants.STYLE_BOLD)) {
		a.setText(GEFMessages.StyleBold_Tooltip);
		a.setImageDescriptor(InternalImages.DESC_BOLD);
	} else if (styleID.equals(GEFActionConstants.STYLE_ITALIC)) {
		a.setText(GEFMessages.StyleItalic_Tooltip);
		a.setImageDescriptor(InternalImages.DESC_ITALIC);
	} else if (styleID.equals(GEFActionConstants.STYLE_UNDERLINE)) {
		a.setText(GEFMessages.StyleUnderline_Tooltip);
		a.setImageDescriptor(InternalImages.DESC_UNDERLINE);
	} else if (styleID.equals(GEFActionConstants.BLOCK_ALIGN_CENTER)){
		a.setText(GEFMessages.BlockAlignCenter_Tooltip);
		a.setImageDescriptor(InternalImages.DESC_BLOCK_ALIGN_CENTER);
	} else if (styleID.equals(GEFActionConstants.BLOCK_ALIGN_LEFT)){
		a.setText(GEFMessages.BlockAlignLeft_Tooltip);
		a.setImageDescriptor(InternalImages.DESC_BLOCK_ALIGN_LEFT);
	} else if (styleID.equals(GEFActionConstants.BLOCK_ALIGN_RIGHT)){
		a.setText(GEFMessages.BlockAlignRight_Tooltip);
		a.setImageDescriptor(InternalImages.DESC_BLOCK_ALIGN_RIGHT);
	} else if (styleID.equals(GEFActionConstants.BLOCK_LTR)){
		a.setText(GEFMessages.BlockLeftToRight_Tooltip);
		a.setImageDescriptor(InternalImages.DESC_BLOCK_LTR);
	} else if (styleID.equals(GEFActionConstants.BLOCK_RTL)){
		a.setText(GEFMessages.BlockRightToLeft_Tooltip);
		a.setImageDescriptor(InternalImages.DESC_BLOCK_RTL);
	} else {
		throw new RuntimeException("The given style ID was not recognized"); //$NON-NLS-1$
	}
}
	
public void run() {
	service.setStyle(getId(), isChecked() ? Boolean.TRUE : Boolean.FALSE);
}

// should only be called once
private void setStyleService(StyleService styleService) {
	Assert.isNotNull(styleService);
	service = styleService;
	// no need to remove this listener; it will be GCed when the editor's closed
	service.addStyleListener(styleListener);
}

public void refresh() {
	setChecked(service.getStyle(getId()).equals(Boolean.TRUE));
	setEnabled(service.getStyleState(getId()).equals(StyleService.STATE_EDITABLE));
}

}