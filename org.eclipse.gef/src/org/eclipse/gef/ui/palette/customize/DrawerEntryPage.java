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
 * @author Pratik Shah
 */
public class DrawerEntryPage 
	extends DefaultEntryPage 
{
	
private Button b, pinOption;

public void createControl(Composite parent, PaletteEntry entry) {
	super.createControl(parent, entry);
	
	b = new Button(panel, SWT.CHECK);
	b.setFont(panel.getFont());
	b.setText(PaletteMessages.EXPAND_LABEL);
	b.setSelection(getDrawer().isInitiallyOpen());
	if (!isReadOnly()) {
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleExpandSelected(((Button)e.getSource()).getSelection());
			}
		});
	} else {
		b.setEnabled(false);
	}
	
	pinOption = new Button(panel, SWT.CHECK);
	pinOption.setFont(panel.getFont());
	pinOption.setText(PaletteMessages.DRAWER_PIN);
	GridData data = new GridData();
	data.horizontalIndent = 15;
	pinOption.setLayoutData(data);
	pinOption.setEnabled(b.getSelection() && b.isEnabled());
	pinOption.setSelection(getDrawer().isInitiallyPinned());
	if (!isReadOnly()) {
		pinOption.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handlePinSelected(((Button)e.getSource()).getSelection());
			}
		});
	}
}

protected PaletteDrawer getDrawer() {
	return (PaletteDrawer)entry;
}

protected void handleExpandSelected(boolean selection) {
	int status = selection ? PaletteDrawer.INITIAL_STATE_OPEN
	                       : PaletteDrawer.INITIAL_STATE_CLOSED;
	getDrawer().setInitialState(status);
	pinOption.setEnabled(selection);
	if (!selection) {
		pinOption.setSelection(false);
	}
}

protected void handlePinSelected(boolean selection) {
	int status = selection ? PaletteDrawer.INITIAL_STATUS_PINNED_OPEN
	                       : PaletteDrawer.INITIAL_STATE_OPEN;
	getDrawer().setInitialState(status);
}

}