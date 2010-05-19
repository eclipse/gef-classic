/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.actions;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * @since 3.1
 */
public class BooleanStyleAction extends Action {

	protected String property;
	protected StyleService service;
	private StyleListener styleListener = new StyleListener() {
		public void styleChanged(String styleID) {
			if (styleID == null || styleID.equals(getId()))
				refresh();
		}
	};

	public BooleanStyleAction(StyleService service, String styleID,
			String property) {
		setStyleService(service);
		setId(styleID);
		this.property = property;
		configureStyleAction(this);
	}

	protected boolean calculateEnabled() {
		return service.getStyleState(getId()) == StyleService.STATE_EDITABLE;
	}

	static void configureStyleAction(IAction a) {
		String styleID = a.getId();
		a.setActionDefinitionId(styleID);
		if (styleID.equals(TextActionConstants.STYLE_BOLD)) {
			a.setText("Bold");
			a.setImageDescriptor(InternalImages.DESC_BOLD);
		} else if (styleID.equals(TextActionConstants.STYLE_ITALIC)) {
			a.setText("Italics");
			a.setImageDescriptor(InternalImages.DESC_ITALIC);
		} else if (styleID.equals(TextActionConstants.STYLE_UNDERLINE)) {
			a.setText("Underline");
			a.setImageDescriptor(InternalImages.DESC_UNDERLINE);
		} else if (styleID.equals(TextActionConstants.BLOCK_ALIGN_CENTER)) {
			a.setText("Center");
			a.setImageDescriptor(InternalImages.DESC_BLOCK_ALIGN_CENTER);
		} else if (styleID.equals(TextActionConstants.BLOCK_ALIGN_LEFT)) {
			a.setText("Left");
			a.setImageDescriptor(InternalImages.DESC_BLOCK_ALIGN_LEFT);
		} else if (styleID.equals(TextActionConstants.BLOCK_ALIGN_RIGHT)) {
			a.setText("Right");
			a.setImageDescriptor(InternalImages.DESC_BLOCK_ALIGN_RIGHT);
		} else if (styleID.equals(TextActionConstants.BLOCK_LTR)) {
			a.setText("Left to Right");
			a.setImageDescriptor(InternalImages.DESC_BLOCK_LTR);
		} else if (styleID.equals(TextActionConstants.BLOCK_RTL)) {
			a.setText("Right to Left");
			a.setImageDescriptor(InternalImages.DESC_BLOCK_RTL);
		} else {
			throw new RuntimeException("The given style ID was not recognized"); //$NON-NLS-1$
		}
	}

	public void run() {
		service.setStyle(property, isChecked() ? Boolean.TRUE : Boolean.FALSE);
	}

	// should only be called once
	private void setStyleService(StyleService styleService) {
		Assert.isNotNull(styleService);
		service = styleService;
		// no need to remove this listener; it will be GCed when the editor's
		// closed
		service.addStyleListener(styleListener);
	}

	public void refresh() {
		setChecked(service.getStyle(property).equals(Boolean.TRUE));
		setEnabled(service.getStyleState(property).equals(
				StyleService.STATE_EDITABLE));
	}

}