package org.eclipse.gef.ui.parts;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.*;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.ZoomListener;
import org.eclipse.draw2d.ZoomManager;

import org.eclipse.gef.ui.actions.GEFActionConstants;

/**
 * A ControlContribution that uses a {@link org.eclipse.swt.widgets.Combo} as its control
 * 
 * @author Eric Bordeau
 */
public class ZoomComboContributionItem 
	extends ContributionItem
	implements SelectionListener, ZoomListener
{

private Combo combo;
private String currentItem;
private String initString = "8888%"; //$NON-NLS-1$
private String[] items;
private ToolItem toolitem;
private ZoomManager zoomManager;

/**
 * Constructor for ComboToolItem.
 * @param id
 */
public ZoomComboContributionItem() {
	super(GEFActionConstants.ZOOM);
}

/**
 * Constructor for ComboToolItem.
 * @param items The Strings to display in the combo
 */
public ZoomComboContributionItem(String[] items) {
	super(GEFActionConstants.ZOOM);
	setItems(items);
}

/**
 * Constructor for ComboToolItem.
 * @param items The List of strings to display in the combo
 */
public ZoomComboContributionItem(List items) {
	super(GEFActionConstants.ZOOM);
	setItems(items);
}

/**
 * Computes the width required by control
 * @param control The control to compute width * @return int The width required */
protected int computeWidth(Control control) {
	return control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
}

/**
 * @see org.eclipse.jface.action.ControlContribution#createControl(Composite)
 */
protected Control createControl(Composite parent) {
	combo = new Combo(parent, SWT.DROP_DOWN);
	combo.addSelectionListener(this);
	
	// Initialize width of combo
	combo.setText(initString);
	toolitem.setWidth(computeWidth(combo));
	combo.removeAll();
	
	if (items != null) {
		setItems(items);
		if (currentItem != null)
			setCurrentItem(currentItem);
	}
	return combo;
}

/**
 * @see org.eclipse.jface.action.ContributionItem#dispose()
 */
public void dispose() {
	combo.removeSelectionListener(this);
	combo = null;
	zoomManager.removeZoomListener(this);
	zoomManager = null;
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
 * Returns the Combo
 * @return Combo The Combo */
public Combo getCombo() {
	return combo;
}

/**
 * Returns the zoomManager.
 * @return ZoomManager
 */
public ZoomManager getZoomManager() {
	return zoomManager;
}

/**
 * Sets the combo to display item
 * @param item The String to display in the Combo */
public void setCurrentItem(String item) {	
	currentItem = item;
	if (combo != null)
		combo.setText(currentItem);
}

/**
 * Sets the initString. This is the string used to initialize the size of the combo. The
 * combo's width is set to the width of this String. 
 * @param initString The initString to set
 */
public void setInitString(String initString) {
	this.initString = initString;
	
	//If the combo exists, update its width
	if (combo != null) {
		combo.setText(initString);
		toolitem.setWidth(computeWidth(combo));
		combo.removeAll();
	}	

}

/**
 * Sets the Combo to display items
 * @param items The Strings to display in the Combo */
public void setItems(String[] items) {
	this.items = items;
	
	if (combo != null)
		combo.setItems(items);
}

/**
 * Sets the Combo to display list
 * @param list The List of Strings to display in the Combo */
public void setItems(List list) {
	items = new String[list.size()];
	for (int i = 0; i < list.size(); i++)
		items[i] = (String)list.get(i);
		
	if (combo != null)
		combo.setItems(items);
}

/**
 * Sets the ZoomManager
 * @param zm The ZoomManager
 */
public void setZoomManager(ZoomManager zm) {
	if (zoomManager != null)
		zoomManager.removeZoomListener(this);

	zoomManager = zm;

	if (zoomManager != null)
		zoomManager.addZoomListener(this);
}

/**
 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
 */
public void widgetDefaultSelected(SelectionEvent event) {
	zoomManager.setZoom(combo.getText());
}

/**
 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
 */
public void widgetSelected(SelectionEvent event) {
	zoomManager.setZoom(combo.getText());
}

/**
 * @see org.eclipse.draw2d.ZoomListener#zoomChanged(double)
 */
public void zoomChanged(double zoom) {
	setCurrentItem(zoomManager.getZoomAsText());
}

}