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

import org.eclipse.gef.examples.text.TextEditor;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public abstract class StyleComboContributionItem 
	extends ContributionItem
{

protected Combo combo;
protected ToolItem toolItem;
protected StyleService styleService;
protected IPartService partService;
protected StyleListener styleListener = new StyleListener() {
	public void styleChanged(String styleID) {
		if (styleID == null || styleID.equals(getProperty()))
			refresh();
	}
};
protected IPartListener2 partListener = new IPartListener2() {
	public void partActivated(IWorkbenchPartReference partRef) {
		IWorkbenchPart part = partRef.getPart(false);
		if (part instanceof TextEditor)
			setStyleService((StyleService)part.getAdapter(StyleService.class));
	}
	public void partBroughtToTop(IWorkbenchPartReference partRef) { }
	public void partClosed(IWorkbenchPartReference partRef) { }
	public void partDeactivated(IWorkbenchPartReference partRef) { }
	public void partOpened(IWorkbenchPartReference partRef) { }
	public void partHidden(IWorkbenchPartReference partRef) { }
	public void partVisible(IWorkbenchPartReference partRef) { }
	public void partInputChanged(IWorkbenchPartReference partRef) { }
};

public StyleComboContributionItem(IPartService service) {
	super();
	partService = service;
	partService.addPartListener(partListener);
}

/**
 * Creates and returns the combo for this contribution item
 * under the given parent composite.
 *
 * @param parent the parent composite
 * @return the new control
 */
protected Control createControl(Composite parent) {
	combo = new Combo(parent, SWT.DROP_DOWN);
	combo.addSelectionListener(new SelectionListener() {
		public void widgetDefaultSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}
		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}
	});
	combo.addFocusListener(new FocusListener() {
		public void focusGained(FocusEvent e) {
			// do nothing
		}
		public void focusLost(FocusEvent e) {
			refresh();
		}
	});
	
	// Initialize width of combo
	combo.setItems(getItems());
	toolItem.setWidth(combo.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	refresh();
	return combo;
}

/**
 * Removes the listeners from the StyleService and the IPartService.
 * @see org.eclipse.jface.action.IContributionItem#dispose()
 */
public void dispose() {
	if (styleService != null)
		styleService.removeStyleListener(styleListener);
	partService.removePartListener(partListener);
}

/**
 * The control item implementation of this <code>IContributionItem</code>
 * method calls the <code>createControl</code> framework method to
 * create a control under the given parent, and then creates
 * a new tool item to hold it.
 * 
 * @param parent The ToolBar to add the new control to
 * @param index Index
 */
public void fill(ToolBar parent, int index) {
	toolItem = new ToolItem(parent, SWT.SEPARATOR, index);
	Control control = createControl(parent);
	toolItem.setControl(control);	
}

protected int findIndexOf(String text) {
	for (int i = 0; i < getItems().length; i++) {
		if (getItems()[i].equalsIgnoreCase(text))
			return i;
	}
	return -1;
}

protected abstract String[] getItems();

protected abstract String getProperty();

protected abstract void handleWidgetSelected(SelectionEvent e);

protected void refresh() {
	if (combo == null)
		return;
	
	boolean enablement = true;
	if (styleService == null)
		enablement = false;
	else {
		if (!styleService.getStyleState(getProperty())
				.equals(StyleService.STATE_EDITABLE))
			// we want the combo disabled, but still want to update the value
			enablement = false;
		Object style = styleService.getStyle(getProperty());
		String value = style.toString();
		if (StyleService.UNDEFINED.equals(style))
			value = "";
		int index = findIndexOf(value);
		if (index >= 0)
			combo.select(index);
		else
			combo.setText(value);
	}
	combo.setEnabled(enablement);
}

protected void setStyleService(StyleService service) {
	if (styleService == service)
		return;
	if (styleService != null)
		styleService.removeStyleListener(styleListener);
	styleService = service;
	if (styleService != null)
		styleService.addStyleListener(styleListener);
	refresh();
}

}
