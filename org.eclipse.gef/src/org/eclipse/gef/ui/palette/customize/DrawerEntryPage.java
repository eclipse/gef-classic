package org.eclipse.gef.ui.palette.customize;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * The default entry page for drawers.
 * 
 * @author Pratik Shah
 */
public class DrawerEntryPage 
	extends DefaultEntryPage 
{
	
private Button openDrawerOption, pinDrawerOption;

/** * @see org.eclipse.gef.ui.palette.customize.EntryPage#createControl(Composite, PaletteEntry) */
public void createControl(Composite parent, PaletteEntry entry) {
	super.createControl(parent, entry);
	
	openDrawerOption = createOpenDrawerInitiallyOption((Composite)getControl());
	pinDrawerOption = createPinDrawerInitiallyOption((Composite)getControl());
}

/**
 * Creates the button that provides the option to pin a drawer open at start-up.
 * 
 * @param panel	The parent Composite
 * @return The button for the new option
 */
protected Button createOpenDrawerInitiallyOption(Composite panel) {
	Button b = new Button(panel, SWT.CHECK);
	b.setFont(panel.getFont());
	b.setText(PaletteMessages.EXPAND_DRAWER_AT_STARTUP_LABEL);
	b.setSelection(getDrawer().isInitiallyOpen());
	if (getPermission() >= getEntry().PERMISSION_LIMITED_MODIFICATION) {
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleOpenSelected(((Button)e.getSource()).getSelection());
			}
		});
	} else {
		b.setEnabled(false);
	}

	return b;
}

/**
 * Creates the button that provides the option to have a drawer open at start-up.
 * 
 * @param panel	The parent Composite
 * @return The button for the new option
 */
protected Button createPinDrawerInitiallyOption(Composite panel) {
	Button pinOption = new Button(panel, SWT.CHECK);
	pinOption.setFont(panel.getFont());
	pinOption.setText(PaletteMessages.DRAWER_PIN_AT_STARTUP);
	GridData data = new GridData();
	data.horizontalIndent = 15;
	pinOption.setLayoutData(data);
	pinOption.setEnabled(openDrawerOption.getSelection() && openDrawerOption.isEnabled());
	pinOption.setSelection(getDrawer().isInitiallyPinned());
	if (getPermission() >= getEntry().PERMISSION_LIMITED_MODIFICATION) {
		pinOption.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handlePinSelected(((Button)e.getSource()).getSelection());
			}
		});
	}
	return pinOption;
}

/**
 * Convenience method that provides access to the PaletteDrawer.
 *  * @return the entry as a PaletteDrawer */
protected PaletteDrawer getDrawer() {
	return (PaletteDrawer)getEntry();
}

/**
 * This method is invoked when the selection state of the option to open drawer at
 * start-up is toggled.
 * <p>
 * It sets the initial state of the drawer accordingly.
 *  * @param selection	<code>true</code> if that option is now selected */
protected void handleOpenSelected(boolean selection) {
	int status = selection ? PaletteDrawer.INITIAL_STATE_OPEN
	                       : PaletteDrawer.INITIAL_STATE_CLOSED;
	getDrawer().setInitialState(status);
	pinDrawerOption.setEnabled(selection);
	if (!selection) {
		pinDrawerOption.setSelection(false);
	}
}

/**
 * This method is invoked when the selection state of the option to pin a drawer open at
 * start-up is toggled.
 * <p>
 * It sets the initial state of the drawer accordingly.
 *  * @param selection	<code>true</code> if that option is now selected
 */
protected void handlePinSelected(boolean selection) {
	int status = selection ? PaletteDrawer.INITIAL_STATUS_PINNED_OPEN
	                       : PaletteDrawer.INITIAL_STATE_OPEN;
	getDrawer().setInitialState(status);
}

}