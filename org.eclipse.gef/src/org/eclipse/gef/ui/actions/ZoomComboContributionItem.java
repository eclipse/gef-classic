/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.actions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.*;

import org.eclipse.draw2d.FigureUtilities;

import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;


/**
 * A ControlContribution that uses a {@link org.eclipse.swt.widgets.Combo} as its control
 * 
 * @author Eric Bordeau
 */
public class ZoomComboContributionItem 
	extends ContributionItem
	implements ZoomListener
{

private Combo combo;
private String[] initStrings;
private ToolItem toolitem;
private ZoomManager zoomManager;
private IPartService service;
private IPartListener partListener;

/**
 * Constructor for ComboToolItem.
 * @param partService used to add a PartListener
 */
public ZoomComboContributionItem(IPartService partService) {
	this(partService, "8888%");//$NON-NLS-1$
}

/**
 * Constructor for ComboToolItem.
 * @param partService used to add a PartListener
 * @param initString the initial string displayed in the combo
 */
public ZoomComboContributionItem(IPartService partService, String initString) {
	this(partService, new String[] {initString});
}

/**
 * Constructor for ComboToolItem.
 * @param partService used to add a PartListener
 * @param initStrings the initial string displayed in the combo
 */
public ZoomComboContributionItem(IPartService partService, String[] initStrings) {
	super(GEFActionConstants.ZOOM_TOOLBAR_WIDGET);
	this.initStrings = initStrings;
	service = partService;
	Assert.isNotNull(partService);
	partService.addPartListener(partListener = new IPartListener() {
		public void partActivated(IWorkbenchPart part) {
			setZoomManager((ZoomManager) part.getAdapter(ZoomManager.class));
		}
		public void partBroughtToTop(IWorkbenchPart p) { }
		public void partClosed(IWorkbenchPart p) { }
		public void partDeactivated(IWorkbenchPart p) { }
		public void partOpened(IWorkbenchPart p) { }
	});
}

void refresh() {
	if (combo == null || combo.isDisposed())
		return;
	//$TODO GTK workaround
	try {
		if (zoomManager == null) {
			combo.setEnabled(false);
			combo.removeAll();
		} else {
			combo.setItems(getZoomManager().getZoomLevelsAsText());
			String zoom = getZoomManager().getZoomAsText();
			int index = combo.indexOf(zoom);
			if (index != -1)
				combo.select(index);
			else
				combo.setText(zoom);
			combo.setEnabled(true);
		}
	} catch (SWTException exception) {
		if (!SWT.getPlatform().equals("gtk")) //$NON-NLS-1$
			throw exception;
	}
}

/**
 * Computes the width required by control
 * @param control The control to compute width
 * @return int The width required */
protected int computeWidth(Control control) {
	int width = control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
	// $TODO: Windows workaround - Fixed in Eclipse 3.0 
	// Combo is not wide enough to show all text - add enough space for another character
	if (SWT.getPlatform().equals("win32"))
		width += FigureUtilities.getTextWidth("8", control.getFont());
	return width;
}

/**
 * @see org.eclipse.jface.action.ControlContribution#createControl(Composite)
 */
protected Control createControl(Composite parent) {
	combo = new Combo(parent, SWT.DROP_DOWN);
	combo.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}
		public void widgetDefaultSelected(SelectionEvent e) {
			handleWidgetDefaultSelected(e);
		}
	});
	combo.addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			dispose();
		}
	});
	
	// Initialize width of combo
	combo.setItems(initStrings);
	toolitem.setWidth(computeWidth(combo));
	refresh();
	return combo;
}

/**
 * @see org.eclipse.jface.action.ContributionItem#dispose()
 */
public void dispose() {
	if (partListener == null)
		return;
	service.removePartListener(partListener);
	if (zoomManager != null) {
		zoomManager.removeZoomListener(this);
		zoomManager = null;
	}
	combo = null;
	partListener = null;
}

/**
 * The control item implementation of this <code>IContributionItem</code>
 * method calls the <code>createControl</code> framework method.
 * Subclasses must implement <code>createControl</code> rather than
 * overriding this method.
 * 
 * @param parent The parent of the control to fill
 */
public final void fill(Composite parent) {
	createControl(parent);
}

/**
 * The control item implementation of this <code>IContributionItem</code>
 * method throws an exception since controls cannot be added to menus.
 * 
 * @param parent The menu
 * @param index Menu index
 */
public final void fill(Menu parent, int index) {
	Assert.isTrue(false, "Can't add a control to a menu");//$NON-NLS-1$
}

/**
 * The control item implementation of this <code>IContributionItem</code>
 * method calls the <code>createControl</code> framework method to
 * create a control under the given parent, and then creates
 * a new tool item to hold it.
 * Subclasses must implement <code>createControl</code> rather than
 * overriding this method.
 * 
 * @param parent The ToolBar to add the new control to
 * @param index Index
 */
public void fill(ToolBar parent, int index) {
	toolitem = new ToolItem(parent, SWT.SEPARATOR, index);
	Control control = createControl(parent);
	toolitem.setControl(control);	
}

/**
 * Returns the zoomManager.
 * @return ZoomManager
 */
public ZoomManager getZoomManager() {
	return zoomManager;
}

/**
 * Sets the ZoomManager
 * @param zm The ZoomManager
 */
public void setZoomManager(ZoomManager zm) {
	if (zoomManager == zm)
		return;
	if (zoomManager != null)
		zoomManager.removeZoomListener(this);

	zoomManager = zm;
	refresh();

	if (zoomManager != null)
		zoomManager.addZoomListener(this);
}

/**
 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
 */
private void handleWidgetDefaultSelected(SelectionEvent event) {
	if (zoomManager != null)
		zoomManager.setZoomAsText(combo.getText());
}

/**
 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
 */
private void handleWidgetSelected(SelectionEvent event) {
	if (zoomManager != null)
		zoomManager.setZoomAsText(combo.getText());
}

/**
 * @see ZoomListener#zoomChanged(double)
 */
public void zoomChanged(double zoom) {
	refresh();
}

}