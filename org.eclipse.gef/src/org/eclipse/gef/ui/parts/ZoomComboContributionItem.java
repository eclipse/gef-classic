package org.eclipse.gef.ui.parts;

import java.util.List;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.*;

/**
 * A ControlContribution that uses a {@link org.eclipse.swt.widgets.Combo} as its control.  
 * @author Eric Bordeau
 */
public class ZoomComboContributionItem 
	extends ControlContribution 
	implements SelectionListener
{

private Combo combo;
private String[] items;

/**
 * Constructor for ComboToolItem.
 * @param id
 */
public ZoomComboContributionItem(String id) {
	super(id);
}

/**
 * Constructor for ComboToolItem.
 * @param id
 */
public ZoomComboContributionItem(String id, String[] items) {
	super(id);
	setItems(items);
}

/**
 * Constructor for ComboToolItem.
 * @param id
 */
public ZoomComboContributionItem(String id, List items) {
	super(id);
	setItems(items);
}

/**
 * @see org.eclipse.jface.action.ControlContribution#createControl(org.eclipse.swt.widgets.Composite)
 */
protected Control createControl(Composite parent) {
	combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
	combo.setItems(items);
	combo.addSelectionListener(this);
	return combo;
}

/**
 * @see org.eclipse.jface.action.ContributionItem#dispose()
 */
public void dispose() {
	combo.removeSelectionListener(this);
	super.dispose();
}

public Combo getCombo() {
	return combo;
}

public void setItems(String[] items) {
	this.items = items;
}

public void setItems(List list) {
	items = new String[list.size()];
	for (int i=0; i<list.size(); i++)
		items[i] = (String)list.get(i);
}

/**
 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
 */
public void widgetDefaultSelected(SelectionEvent event) {}

/**
 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
 */
public void widgetSelected(SelectionEvent event) {
	
}

}
